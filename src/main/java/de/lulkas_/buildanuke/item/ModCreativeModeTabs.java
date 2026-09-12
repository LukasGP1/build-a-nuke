package de.lulkas_.buildanuke.item;

import de.lulkas_.buildanuke.BuildANuke;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTabs {
    public static final CreativeModeTab BUILD_A_NUKE_TAB = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            BuildANuke.id("build_a_nuke_items"), FabricCreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.URANIUM))
                    .title(Component.translatable("creativemodetab.build-a-nuke.build_a_nuke_items"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.URANIUM);
                        output.accept(ModItems.URANIUM_NUGGET);
                    })
                    .build()
    );

    public static void register() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(output -> {
            output.accept(ModItems.URANIUM);
            output.accept(ModItems.URANIUM_NUGGET);
        });

        BuildANuke.LOGGER.info("Registered Creative Mode Tabs for " + BuildANuke.MOD_ID);
    }
}
