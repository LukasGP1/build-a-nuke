package de.lulkas_.buildanuke.rendering;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import de.lulkas_.buildanuke.BuildANuke;
import de.lulkas_.buildanuke.block.entity.custom.EnricherBlockEntity;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.StagedVertexBuffer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

public class EnricherEffectRenderingPipeline {
    public static final float OFFSET_X = 0.75f;
    public static final float OFFSET_Y = 1.25f;
    public static final float OFFSET_Z = 0.5f;
    public static final float BOX_SPEED = 0.005f;
    public static final float BOX_SIZE = 0.025f;
    public static final int BOX_COUNT = 10;
    public static final Color BOX_COLOR = new Color(0, 255, 0, 255);

    public static List<BlockPos> positions = new ArrayList<>();

    private static final Vector4f COLOR_MODULATOR = new Vector4f(1f, 1f, 1f, 1f);
    private static final Vector3f MODEL_OFFSET = new Vector3f();
    private static final Matrix4f TEXTURE_MATRIX = new Matrix4f();
    private static final StagedVertexBuffer stagedBuffer = new StagedVertexBuffer(() -> "Waypoints Buffer", RenderType.SMALL_BUFFER_SIZE);

    private float totalTime = 0;

    public static void close() {
        stagedBuffer.close();
    }

    public void extract(LevelExtractionContext context) {
        totalTime += context.deltaTracker().getGameTimeDeltaTicks();
        List<Integer> indicesToRemove = new ArrayList<>();
        for(int i = 0; i < positions.size(); i++) {
            if(context.level().getBlockEntity(positions.get(i)) instanceof EnricherBlockEntity enricherBlockEntity) {
                if(enricherBlockEntity.isEnriching()) continue;
            }
            indicesToRemove.add(i);
        }
        for(Integer i : indicesToRemove) {
            try {
                positions.remove(i.intValue());
            } catch (IndexOutOfBoundsException e) {

            }
        }
    }

    public void render(LevelRenderContext context) {
        RenderPipeline renderPipeline = ModRenderingPipelines.ENRICHER_EFFECT;
        VertexFormat formatBinding = renderPipeline.getVertexFormatBinding(0);

        assert formatBinding != null;

        PrimitiveTopology primitive = renderPipeline.getPrimitiveTopology();
        StagedVertexBuffer.Draw draw = stagedBuffer.appendDraw(formatBinding, primitive, primitive == PrimitiveTopology.QUADS ? RenderSystem.getProjectionType().vertexSorting() : null);

        for(BlockPos pos : positions) renderEffect(context, draw, pos);

        stagedBuffer.upload();

        StagedVertexBuffer.ExecuteInfo info = stagedBuffer.getExecuteInfo(draw);

        if (info != null) {
            draw(Minecraft.getInstance(), info, renderPipeline);
        }

        stagedBuffer.endFrame();
    }

    private void renderEffect(LevelRenderContext context, StagedVertexBuffer.Draw draw, BlockPos pos) {
        PoseStack matrices = context.poseStack();
        Vec3 camera = context.levelState().cameraRenderState.pos;

        matrices.pushPose();
        matrices.translate(-camera.x, -camera.y, -camera.z);

        final var builder = stagedBuffer.getVertexBuilder(draw);

        float baseX = pos.getX() + OFFSET_X;
        float y = pos.getY() + OFFSET_Y;
        float z = pos.getZ() + OFFSET_Z;
        for(int i = 0; i < BOX_COUNT; i++) {
            float x = baseX + (2 * i * BOX_SIZE - totalTime * BOX_SPEED) % (2 * BOX_SIZE * BOX_COUNT);
            renderFilledBox(matrices.last().pose(), builder, x, y, z, x + BOX_SIZE, y + BOX_SIZE, z + BOX_SIZE,
                    BOX_COLOR.getRed() / 255f, BOX_COLOR.getGreen() / 255f, BOX_COLOR.getBlue() / 255f, BOX_COLOR.getAlpha() / 255f);
        }

        matrices.popPose();
    }

    private void renderFilledBox(Matrix4fc positionMatrix, VertexConsumer buffer, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, float red, float green, float blue, float alpha) {
        // Front Face
        buffer.addVertex(positionMatrix, minX, minY, maxZ).setColor(red, green, blue, alpha);
        buffer.addVertex(positionMatrix, maxX, minY, maxZ).setColor(red, green, blue, alpha);
        buffer.addVertex(positionMatrix, maxX, maxY, maxZ).setColor(red, green, blue, alpha);
        buffer.addVertex(positionMatrix, minX, maxY, maxZ).setColor(red, green, blue, alpha);

        // Back face
        buffer.addVertex(positionMatrix, maxX, minY, minZ).setColor(red, green, blue, alpha);
        buffer.addVertex(positionMatrix, minX, minY, minZ).setColor(red, green, blue, alpha);
        buffer.addVertex(positionMatrix, minX, maxY, minZ).setColor(red, green, blue, alpha);
        buffer.addVertex(positionMatrix, maxX, maxY, minZ).setColor(red, green, blue, alpha);

        // Left face
        buffer.addVertex(positionMatrix, minX, minY, minZ).setColor(red, green, blue, alpha);
        buffer.addVertex(positionMatrix, minX, minY, maxZ).setColor(red, green, blue, alpha);
        buffer.addVertex(positionMatrix, minX, maxY, maxZ).setColor(red, green, blue, alpha);
        buffer.addVertex(positionMatrix, minX, maxY, minZ).setColor(red, green, blue, alpha);

        // Right face
        buffer.addVertex(positionMatrix, maxX, minY, maxZ).setColor(red, green, blue, alpha);
        buffer.addVertex(positionMatrix, maxX, minY, minZ).setColor(red, green, blue, alpha);
        buffer.addVertex(positionMatrix, maxX, maxY, minZ).setColor(red, green, blue, alpha);
        buffer.addVertex(positionMatrix, maxX, maxY, maxZ).setColor(red, green, blue, alpha);

        // Top face
        buffer.addVertex(positionMatrix, minX, maxY, maxZ).setColor(red, green, blue, alpha);
        buffer.addVertex(positionMatrix, maxX, maxY, maxZ).setColor(red, green, blue, alpha);
        buffer.addVertex(positionMatrix, maxX, maxY, minZ).setColor(red, green, blue, alpha);
        buffer.addVertex(positionMatrix, minX, maxY, minZ).setColor(red, green, blue, alpha);

        // Bottom face
        buffer.addVertex(positionMatrix, minX, minY, minZ).setColor(red, green, blue, alpha);
        buffer.addVertex(positionMatrix, maxX, minY, minZ).setColor(red, green, blue, alpha);
        buffer.addVertex(positionMatrix, maxX, minY, maxZ).setColor(red, green, blue, alpha);
        buffer.addVertex(positionMatrix, minX, minY, maxZ).setColor(red, green, blue, alpha);
    }

    private static void draw(Minecraft client, StagedVertexBuffer.ExecuteInfo info, RenderPipeline pipeline) {
        GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms()
                .writeTransform(RenderSystem.getModelViewMatrixCopy(), COLOR_MODULATOR, MODEL_OFFSET, TEXTURE_MATRIX);

        RenderTarget mainTarget = client.gameRenderer.mainRenderTarget();
        GpuTextureView colorTexture = mainTarget.getColorTextureView();

        assert colorTexture != null;

        try (RenderPass renderPass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(() -> BuildANuke.MOD_ID + " example render pipeline rendering", colorTexture, Optional.empty(), mainTarget.getDepthTextureView(), OptionalDouble.empty())) {
            renderPass.setPipeline(pipeline);

            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.setUniform("DynamicTransforms", dynamicTransforms);

            renderPass.setVertexBuffer(0, info.vertexBuffer().slice());
            renderPass.setIndexBuffer(info.indexBuffer(), info.indexType());

            renderPass.drawIndexed(info.indexCount(), 1, info.firstIndex(), info.baseVertex(), 0);
        }
    }
}
