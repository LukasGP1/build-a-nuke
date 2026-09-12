package de.lulkas_.buildanuke.rendering;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import de.lulkas_.buildanuke.BuildANuke;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.renderer.RenderPipelines;

public class ModRenderingPipelines {
    public static final EnricherEffectRenderingPipeline ENRICHER_EFFECT_RENDERING_PIPELINE = new EnricherEffectRenderingPipeline();
    public static final RenderPipeline ENRICHER_EFFECT = RenderPipelines.register(RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
            .withLocation(BuildANuke.id("pipeline/debug_filled_box_through_walls"))
            .build()
    );

    public static void register() {
        LevelExtractionEvents.END_EXTRACTION.register(ENRICHER_EFFECT_RENDERING_PIPELINE::extract);
        LevelRenderEvents.AFTER_SOLID_FEATURES.register(ENRICHER_EFFECT_RENDERING_PIPELINE::render);

        BuildANuke.LOGGER.info("Registered Rendering Pipelines for " + BuildANuke.MOD_ID);
    }
}
