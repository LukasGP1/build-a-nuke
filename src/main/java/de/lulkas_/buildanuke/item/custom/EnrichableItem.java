package de.lulkas_.buildanuke.item.custom;

import de.lulkas_.buildanuke.data.ModDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.Locale;
import java.util.function.Consumer;

public class EnrichableItem extends Item {
    public final double defaultEnrichmentProportion;

    public EnrichableItem(Properties properties, double defaultEnrichmentProportion) {
        super(properties);
        this.defaultEnrichmentProportion = defaultEnrichmentProportion;
    }

    @Override
    public void onCraftedPostProcess(ItemStack itemStack, Level level) {
        itemStack.set(ModDataComponents.ENRICHMENT_PROPORTION, defaultEnrichmentProportion);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        if(itemStack.has(ModDataComponents.ENRICHMENT_PROPORTION)) {
            builder.accept(Component.literal(String.format(Locale.US, "%.1f", itemStack.get(ModDataComponents.ENRICHMENT_PROPORTION) * 100) + "% enriched"));
        }

        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
    }
}
