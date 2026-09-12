package de.lulkas_.buildanuke.block.entity.custom;

import de.lulkas_.buildanuke.block.entity.ModBlockEntities;
import de.lulkas_.buildanuke.rendering.EnricherEffectRenderingPipeline;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public class EnricherBlockEntity extends BlockEntity implements Container {
    public static final int ENRICHMENT_SLOT = 0;
    public static final int SUPPLY_SLOT = 1;
    public NonNullList<ItemStack> inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);

    public EnricherBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.ENRICHER_BE, worldPosition, blockState);
    }

    // LOGIC

    private boolean isEnriching = true;

    public boolean isEnriching() {
        return isEnriching;
    }

    public static void tick(final Level level, final BlockPos pos, final BlockState state, final EnricherBlockEntity entity) {
        entity.tick();
    }

    private void tick() {
        if(isEnriching) {
            addPositionToEffectList();
        }
    }

    private void addPositionToEffectList() {
        boolean needToAdd = true;
        for(BlockPos pos : EnricherEffectRenderingPipeline.positions) {
            if (arePositionsEqual(pos, this.worldPosition)) {
                needToAdd = false;
                break;
            }
        }
        if(needToAdd) EnricherEffectRenderingPipeline.positions.add(worldPosition);
    }

    private boolean arePositionsEqual(BlockPos pos1, BlockPos pos2) {
        return pos1.getX() == pos2.getX() && pos1.getY() == pos2.getY() && pos1.getZ() == pos2.getZ();
    }

    // DATA SAVING AND LOADING

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, inventory);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        ContainerHelper.loadAllItems(input, inventory);
    }

    // CONTAINER METHODS

    public void drops() {
        Containers.dropContents(this.level, this.worldPosition, this.inventory);
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack stack : inventory) {
            if(!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return slot >= 0 && slot < getContainerSize() ? inventory.get(slot) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        return slot >= 0 && slot < getContainerSize() ? inventory.get(slot).split(count) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return removeItem(slot, getMaxStackSize());
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        setChanged();
        inventory.set(0, itemStack.copy());
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        for(int i = 0; i < getContainerSize(); i++) {
            removeItemNoUpdate(i);
        }
    }

    // BLOCK ENTITY SYNC

    @Override
    public void setChanged() {
        super.setChanged();
        if(!level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }
}
