package de.lulkas_.buildanuke;

import de.lulkas_.buildanuke.datagen.ModBlockLootTableProvider;
import de.lulkas_.buildanuke.datagen.ModBlockTagsProvider;
import de.lulkas_.buildanuke.datagen.ModModelProvider;
import de.lulkas_.buildanuke.datagen.ModRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class BuildANukeDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		var pack = fabricDataGenerator.createPack();

		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModBlockTagsProvider::new);
		pack.addProvider(ModBlockLootTableProvider::new);
		pack.addProvider(ModRecipeProvider::new);
	}
}
