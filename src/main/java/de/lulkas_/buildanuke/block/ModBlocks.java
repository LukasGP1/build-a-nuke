package de.lulkas_.buildanuke.block;

import de.lulkas_.buildanuke.BuildANuke;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ModBlocks {
    public static final List<Block> blocksForCreativeModeTab = new ArrayList<>();
    public static final List<Block> blocksWithTrivialCubeModel = new ArrayList<>();

    public static final Block URANIUM_RICH_STONE = registerUraniumRichVariant(Blocks.STONE);
    public static final Block URANIUM_RICH_DEEPSLATE = registerUraniumRichVariant(Blocks.DEEPSLATE);
    public static final Block URANIUM_RICH_GRANITE = registerUraniumRichVariant(Blocks.GRANITE);
    public static final Block URANIUM_RICH_ANDESITE = registerUraniumRichVariant(Blocks.ANDESITE);
    public static final Block URANIUM_RICH_DIORITE = registerUraniumRichVariant(Blocks.DIORITE);

    private static Block registerUraniumRichVariant(Block block) {
        return registerBlock(
                "uranium_rich_" + BuiltInRegistries.BLOCK.getResourceKey(block).get().identifier().getPath(),
                true, true,
                BlockBehaviour.Properties.ofLegacyCopy(block).lightLevel(state -> 7)
        );
    }

    private static Block registerBlock(String name, boolean addToCreativeModeTab, boolean useTrivialCubeModel,
                                       BlockBehaviour.Properties properties) {
        return registerBlock(name, addToCreativeModeTab, useTrivialCubeModel, properties, Block::new);
    }

    private static Block registerBlock(String name, boolean addToCreativeModeTab, boolean useTrivialCubeModel,
                                       BlockBehaviour.Properties properties, Function<BlockBehaviour.Properties, Block> function) {
        return registerBlock(name, addToCreativeModeTab, useTrivialCubeModel, properties, function, new Item.Properties());
    }

    private static Block registerBlock(String name, boolean addToCreativeModeTab, boolean useTrivialCubeModel, BlockBehaviour.Properties properties,
                                       Function<BlockBehaviour.Properties, Block> function, Item.Properties blockItemProperties) {
        Block toRegister = function.apply(properties.setId(ResourceKey.create(Registries.BLOCK, BuildANuke.id(name))));
        if(addToCreativeModeTab) blocksForCreativeModeTab.add(toRegister);
        if(useTrivialCubeModel) blocksWithTrivialCubeModel.add(toRegister);
        registerBlockItem(name, toRegister, blockItemProperties);
        return Registry.register(BuiltInRegistries.BLOCK, BuildANuke.id(name), toRegister);
    }

    private static void registerBlockItem(String name, Block block, Item.Properties properties) {
        Registry.register(BuiltInRegistries.ITEM, BuildANuke.id(name), new BlockItem(block, properties
                        .useBlockDescriptionPrefix()
                        .setId(ResourceKey.create(Registries.ITEM, BuildANuke.id(name)))
                )
        );
    }

    public static void register() {
        BuildANuke.LOGGER.info("Registered Blocks for " + BuildANuke.MOD_ID);
    }
}
