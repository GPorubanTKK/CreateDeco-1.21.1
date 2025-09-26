package com.rld.createtfa.common.data;

import com.rld.createtfa.common.registration.ItemRegistry;
import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.PressingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import static com.rld.createtfa.CreateDecoMod.MOD_ID;

public class CreateDecoPressingRecipeGen extends PressingRecipeGen {
    public CreateDecoPressingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, MOD_ID);
    }

    GeneratedRecipe INDUSTRIAL_IRON_PLATE = create("industrial_iron_plate_from_ingot",
        () -> ItemRegistry.INDUSTRIAL_IRON_INGOT,
        b -> b.output(ItemRegistry.INDUSTRIAL_IRON_SHEET)
    );
    GeneratedRecipe ZINC_PLATE = create("zinc_plate_from_ingot",
        () -> AllItems.ZINC_INGOT,
        b -> b.output(ItemRegistry.ZINC_SHEET)
    );

    GeneratedRecipe ANDESITE_PLATE = create("andesite_plate_from_ingot",
        () -> AllItems.ANDESITE_ALLOY,
        b -> b.output(ItemRegistry.ANDESITE_SHEET)
    );

    private HashMap<ItemLike, String> coinMaterials = new HashMap<>() {{
        put(AllItems.BRASS_NUGGET, "Brass");
        put(AllItems.ZINC_NUGGET, "Zinc");
        put(AllItems.COPPER_NUGGET, "Copper");
        put(Items.IRON_NUGGET, "Iron");
        put(Items.GOLD_NUGGET, "Gold");
        put(ItemRegistry.NETHERITE_NUGGET, "Netherite");
        put(ItemRegistry.INDUSTRIAL_IRON_NUGGET, "Industrial Iron");
    }};

    {
        coinMaterials.forEach((nugget, str) -> create(MOD_ID, () -> nugget, o -> o.output(ItemRegistry.COINS.get(str))));
    }
}
