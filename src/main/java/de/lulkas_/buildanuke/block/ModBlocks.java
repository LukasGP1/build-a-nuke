package de.lulkas_.buildanuke.block;

import de.lulkas_.buildanuke.BuildANuke;
import de.lulkas_.buildanuke.block.custom.EnricherBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ModBlocks {
    public static final List<Block> blocksForCreativeModeTab = new ArrayList<>();
    public static final List<Block> blocksWithTrivialCubeModel = new ArrayList<>();
    public static final List<Block> blocksWhichDropThemselves = new ArrayList<>();
    public static final List<ItemLike> uraniumRichBlocks = new ArrayList<>();

    public static final Block URANIUM_RICH_STONE = registerUraniumRichVariant(Blocks.STONE);
    public static final Block URANIUM_RICH_DEEPSLATE = registerUraniumRichVariant(Blocks.DEEPSLATE);
    public static final Block URANIUM_RICH_GRANITE = registerUraniumRichVariant(Blocks.GRANITE);
    public static final Block URANIUM_RICH_ANDESITE = registerUraniumRichVariant(Blocks.ANDESITE);
    public static final Block URANIUM_RICH_DIORITE = registerUraniumRichVariant(Blocks.DIORITE);
    public static final Block ENRICHER = registerBlock("enricher",
            List.of(BlockRegistrationFeature.CREATIVE_MODE_TAB, BlockRegistrationFeature.DROP_SELF),
            BlockBehaviour.Properties.of().strength(3f),
            EnricherBlock::new
    );

    private static Block registerUraniumRichVariant(Block block) {
        Block registeredBlock = registerBlock(
                "uranium_rich_" + BuiltInRegistries.BLOCK.getResourceKey(block).get().identifier().getPath(),
                List.of(BlockRegistrationFeature.CREATIVE_MODE_TAB, BlockRegistrationFeature.TRIVIAL_CUBE_MODEL, BlockRegistrationFeature.DROP_SELF),
                BlockBehaviour.Properties.ofLegacyCopy(block).lightLevel(state -> 7)
        );
        uraniumRichBlocks.add(registeredBlock);
        return registeredBlock;
    }

    private static Block registerBlock(String name, List<BlockRegistrationFeature> registrationFeatures,
                                       BlockBehaviour.Properties properties) {
        return registerBlock(name, registrationFeatures, properties, Block::new);
    }

    private static Block registerBlock(String name, List<BlockRegistrationFeature> registrationFeatures,
                                       BlockBehaviour.Properties properties, Function<BlockBehaviour.Properties, Block> function) {
        return registerBlock(name, registrationFeatures, properties, function, new Item.Properties());
    }

    private static Block registerBlock(String name, List<BlockRegistrationFeature> registrationFeatures, BlockBehaviour.Properties properties,
                                       Function<BlockBehaviour.Properties, Block> function, Item.Properties blockItemProperties) {
        Block toRegister = function.apply(properties.setId(ResourceKey.create(Registries.BLOCK, BuildANuke.id(name))));
        for(BlockRegistrationFeature feature : registrationFeatures) feature.apply.accept(toRegister);
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

    private enum BlockRegistrationFeature {
        CREATIVE_MODE_TAB(blocksForCreativeModeTab::add),
        TRIVIAL_CUBE_MODEL(blocksWithTrivialCubeModel::add),
        DROP_SELF(blocksWhichDropThemselves::add);

        public final Consumer<Block> apply;

        BlockRegistrationFeature(Consumer<Block> apply) {
            this.apply = apply;
        }
    }

    public static ResourceKey<Block> getResourceKey(Block block) {
        return BuiltInRegistries.BLOCK.getResourceKey(block).get();
    }

    public static void register() {
        BuildANuke.LOGGER.info("Registered Blocks for " + BuildANuke.MOD_ID);
    }
}
