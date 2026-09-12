package de.lulkas_.buildanuke.datagen;

import de.lulkas_.buildanuke.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends FabricTagsProvider.BlockTagsProvider {
    public ModBlockTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.getResourceKey(ModBlocks.URANIUM_RICH_STONE))
                .add(ModBlocks.getResourceKey(ModBlocks.URANIUM_RICH_DEEPSLATE))
                .add(ModBlocks.getResourceKey(ModBlocks.URANIUM_RICH_GRANITE))
                .add(ModBlocks.getResourceKey(ModBlocks.URANIUM_RICH_ANDESITE))
                .add(ModBlocks.getResourceKey(ModBlocks.URANIUM_RICH_DIORITE));
    }
}
