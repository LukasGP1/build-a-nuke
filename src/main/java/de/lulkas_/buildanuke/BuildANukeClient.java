package de.lulkas_.buildanuke;

import de.lulkas_.buildanuke.block.entity.ModBlockEntities;
import de.lulkas_.buildanuke.block.entity.renderer.EnricherBlockEntityRenderer;
import de.lulkas_.buildanuke.rendering.EnricherEffectRenderingPipeline;
import de.lulkas_.buildanuke.rendering.ModRenderingPipelines;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class BuildANukeClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModRenderingPipelines.register();
        BlockEntityRenderers.register(ModBlockEntities.ENRICHER_BE, EnricherBlockEntityRenderer::new);
    }
}
