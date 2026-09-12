package de.lulkas_.buildanuke.block.custom;

import com.mojang.serialization.MapCodec;
import de.lulkas_.buildanuke.block.entity.ModBlockEntities;
import de.lulkas_.buildanuke.block.entity.custom.EnricherBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class EnricherBlock extends BaseEntityBlock {
    public static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 13, 14);
    private static MapCodec<EnricherBlock> CODEC = simpleCodec(EnricherBlock::new);

    public EnricherBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new EnricherBlockEntity(worldPosition, blockState);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity,
                              ItemStack destroyedWith) {
        if(level.getBlockEntity(pos) instanceof EnricherBlockEntity enricherBlockEntity) {
            enricherBlockEntity.drops();
            level.updateNeighbourForOutputSignal(pos, this);
        }
        super.playerDestroy(level, player, pos, state, blockEntity, destroyedWith);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player,
                                          InteractionHand hand, BlockHitResult hitResult) {
        if(level.getBlockEntity(pos) instanceof EnricherBlockEntity enricherBlockEntity) {
            if(enricherBlockEntity.isEmpty() && !itemStack.isEmpty()) {
                enricherBlockEntity.setItem(EnricherBlockEntity.ENRICHMENT_SLOT, itemStack);
                itemStack.shrink(itemStack.getCount());
                level.playSound(player, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 2f);
            } else if(!enricherBlockEntity.isEmpty()) {
                ItemStack stackInEnricher = enricherBlockEntity.getItem(EnricherBlockEntity.ENRICHMENT_SLOT).copy();
                enricherBlockEntity.clearContent();
                if(!player.getInventory().add(stackInEnricher)) {
                    player.drop(stackInEnricher, false);
                }
                level.playSound(player, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 2f);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return level.isClientSide() ? null : createTickerHelper(type, ModBlockEntities.ENRICHER_BE, EnricherBlockEntity::tick);
    }
}
