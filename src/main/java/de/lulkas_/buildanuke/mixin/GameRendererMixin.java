package de.lulkas_.buildanuke.mixin;

import de.lulkas_.buildanuke.rendering.EnricherEffectRenderingPipeline;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
	@Inject(at = @At("RETURN"), method = "close")
	private void onGameRenderClose(CallbackInfo info) {
		EnricherEffectRenderingPipeline.close();
	}
}