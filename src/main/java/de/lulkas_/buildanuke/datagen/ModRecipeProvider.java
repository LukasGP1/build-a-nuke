package de.lulkas_.buildanuke.datagen;

import de.lulkas_.buildanuke.block.ModBlocks;
import de.lulkas_.buildanuke.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.crafting.CookingBookCategory;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        return new RecipeProvider(registries, output) {
            @Override
            public void buildRecipes() {
                oreSmelting(ModBlocks.uraniumRichBlocks, RecipeCategory.MISC, CookingBookCategory.BLOCKS,
                        ModItems.URANIUM_NUGGET, 0.25f, 200, "uranium_nugget");
                oreBlasting(ModBlocks.uraniumRichBlocks, RecipeCategory.MISC, CookingBookCategory.BLOCKS,
                        ModItems.URANIUM_NUGGET, 0.25f, 100, "uranium_nugget");

                nineBlockStorageRecipes(RecipeCategory.MISC, ModItems.URANIUM_NUGGET, RecipeCategory.MISC, ModItems.URANIUM);
            }
        };
    }

    @Override
    public String getName() {
        return "Build a Nuke Recipes";
    }
}
