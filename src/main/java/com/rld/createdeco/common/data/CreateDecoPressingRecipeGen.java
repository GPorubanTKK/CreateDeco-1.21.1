package com.rld.createdeco.common.data;

import com.rld.createdeco.CreateDecoMod;
import com.rld.createdeco.common.ItemRegistry;
import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.PressingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class CreateDecoPressingRecipeGen extends PressingRecipeGen {
    public CreateDecoPressingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, CreateDecoMod.MOD_ID);
    }

    GeneratedRecipe INDUSTRIAL_IRON_PLATE = create("industrial_iron_plate_from_ingot",
        () -> ItemRegistry.INDUSTRIAL_IRON_INGOT,
        b -> b.output(ItemRegistry.INDUSTRIAL_IRON_SHEET)
    );
    GeneratedRecipe ZINC_PLATE = create("zinc_plate_from_ingot",
        () -> AllItems.ZINC_INGOT,
        b -> b.output(ItemRegistry.ZINC_SHEET)
    );
    GeneratedRecipe NETHERITE_PLATE = create("netherite_plate_from_ingot",
        () -> Items.NETHERITE_INGOT,
        b -> b.output(ItemRegistry.NETHERITE_SHEET)
    );

    GeneratedRecipe ANDESITE_PLATE = create("andesite_plate_from_ingot",
        () -> AllItems.ANDESITE_ALLOY,
        b -> b.output(ItemRegistry.ANDESITE_SHEET)
    );
}
