package de.lulkas_.buildanuke.item;

import de.lulkas_.buildanuke.BuildANuke;
import de.lulkas_.buildanuke.item.custom.EnrichableItem;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class ModItems {
    public static final Item URANIUM = registerItem("uranium", properties -> new EnrichableItem(properties, 0.007));
    public static final Item URANIUM_NUGGET = registerItem("uranium_nugget", Item::new);

    private static Item registerItem(String name, Function<Item.Properties, Item> function) {
        return Registry.register(
                BuiltInRegistries.ITEM, BuildANuke.id(name),
                function.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, BuildANuke.id(name))))
        );
    }

    public static ResourceKey<Item> getResourceKey(Item item) {
        return BuiltInRegistries.ITEM.getResourceKey(item).get();
    }

    public static void register() {
        BuildANuke.LOGGER.info("Registered items for " + BuildANuke.MOD_ID);
    }
}
