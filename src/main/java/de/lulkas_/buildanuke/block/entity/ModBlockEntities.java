package de.lulkas_.buildanuke.block.entity;

import de.lulkas_.buildanuke.BuildANuke;
import de.lulkas_.buildanuke.block.ModBlocks;
import de.lulkas_.buildanuke.block.entity.custom.EnricherBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static final BlockEntityType<EnricherBlockEntity> ENRICHER_BE = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            BuildANuke.id("enricher_be"),
            FabricBlockEntityTypeBuilder.create(EnricherBlockEntity::new, ModBlocks.ENRICHER).build()
    );

    public static void register() {
        BuildANuke.LOGGER.info("Registered Block Entities for " + BuildANuke.MOD_ID);
    }
}
