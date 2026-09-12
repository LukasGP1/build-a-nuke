package de.lulkas_.buildanuke.data;

import com.mojang.serialization.Codec;
import de.lulkas_.buildanuke.BuildANuke;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;

import java.util.function.UnaryOperator;

public class ModDataComponents {
    public static final DataComponentType<Double> ENRICHMENT_PROPORTION = registerDataComponentType("enrichment_proportion",
            builder -> builder.persistent(Codec.DOUBLE).networkSynchronized(ByteBufCodecs.DOUBLE));

    private static <T>DataComponentType<T> registerDataComponentType(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, BuildANuke.id(name), builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void register() {
        BuildANuke.LOGGER.info("Registered Data Components for " + BuildANuke.MOD_ID);
    }
}
