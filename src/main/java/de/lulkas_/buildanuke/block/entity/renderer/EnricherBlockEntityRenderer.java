package de.lulkas_.buildanuke.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import de.lulkas_.buildanuke.block.entity.custom.EnricherBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class EnricherBlockEntityRenderer implements BlockEntityRenderer<EnricherBlockEntity, EnricherBlockEntityRenderState> {
    private final ItemModelResolver itemModelResolver;

    public EnricherBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public EnricherBlockEntityRenderState createRenderState() {
        return new EnricherBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(EnricherBlockEntity blockEntity, EnricherBlockEntityRenderState state, float partialTicks,
                                   Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.level = blockEntity.getLevel();
        state.rotation = (blockEntity.getLevel().getGameTime() + partialTicks * 0.5f) % 360f;

        itemModelResolver.updateForTopItem(state.enrichmentSlotItemStackRenderState, blockEntity.getItem(EnricherBlockEntity.ENRICHMENT_SLOT),
                ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
        itemModelResolver.updateForTopItem(state.supplySlotItemStackRenderState, blockEntity.getItem(EnricherBlockEntity.SUPPLY_SLOT),
                ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
    }

    @Override
    public void submit(EnricherBlockEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();

        poseStack.translate(0.25f, 1.15f, 0.5f);
        poseStack.scale(0.2f, 0.2f, 0.2f);
        poseStack.mulPose(Axis.YP.rotationDegrees(state.rotation));
        state.enrichmentSlotItemStackRenderState.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);

        poseStack.mulPose(Axis.YP.rotationDegrees(-state.rotation));
        poseStack.scale(1f / 0.2f, 1f / 0.2f, 1f / 0.2f);
        poseStack.translate(0.5f, 0f, 0f);
        poseStack.scale(0.2f, 0.2f, 0.2f);
        poseStack.mulPose(Axis.YP.rotationDegrees(state.rotation));
        state.supplySlotItemStackRenderState.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);

        poseStack.popPose();
    }


}
