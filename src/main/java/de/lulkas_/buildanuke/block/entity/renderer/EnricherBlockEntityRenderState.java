package de.lulkas_.buildanuke.block.entity.renderer;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.level.Level;

public class EnricherBlockEntityRenderState extends BlockEntityRenderState {
    public Level level;

    public float rotation;
    final ItemStackRenderState enrichmentSlotItemStackRenderState = new ItemStackRenderState();
    final ItemStackRenderState supplySlotItemStackRenderState = new ItemStackRenderState();
}
