package com.rld.createtfa.common.registration;

import com.rld.createtfa.CreateDecoMod;
import com.rld.createtfa.common.api.CDTags;
import com.rld.createtfa.common.api.Coins;
import com.rld.createtfa.common.items.CoinStackItem;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.function.Function;

public class ItemRegistry {
  public static ItemEntry<Item> ANDESITE_SHEET;
  public static ItemEntry<Item> ZINC_SHEET;
  public static ItemEntry<Item> NETHERITE_NUGGET;
  public static ItemEntry<Item> INDUSTRIAL_IRON_NUGGET;
  public static ItemEntry<Item> INDUSTRIAL_IRON_INGOT;
  public static ItemEntry<Item> INDUSTRIAL_IRON_SHEET;

  public static HashMap<String, Function<String, Item>> METAL_TYPES = new HashMap<>();
  public static HashMap<String, Function<String, Item>> COIN_METALS = new HashMap<>();

  public static HashMap<String, ItemEntry<Item>> COINS = new HashMap<>();
  public static HashMap<String, ItemEntry<CoinStackItem>> COINSTACKS = new HashMap<>();

  public static void init () {
    CreateDecoMod.LOGGER.info("Registering items for " + CreateDecoMod.NAME);
    CreateDecoMod.REGISTRATE.defaultCreativeTab("props_tab");

    registerSheets();
    registerNuggets();
    registerIngots();

    METAL_TYPES.put("Andesite", (str) -> AllItems.ANDESITE_ALLOY.get());
    METAL_TYPES.put("Zinc", (str) -> AllItems.ZINC_INGOT.get());
    METAL_TYPES.put("Copper", (str) -> Items.COPPER_INGOT);
    METAL_TYPES.put("Brass", (str) -> AllItems.BRASS_INGOT.get());
    METAL_TYPES.put("Iron", (str) -> Items.IRON_INGOT);
    METAL_TYPES.put("Industrial Iron", (str) -> INDUSTRIAL_IRON_INGOT.get());

    COIN_METALS.put("Zinc", (str) -> AllItems.ZINC_INGOT.get());
    COIN_METALS.put("Copper", (str) -> Items.COPPER_INGOT);
    COIN_METALS.put("Brass", (str) -> AllItems.BRASS_INGOT.get());
    COIN_METALS.put("Iron", (str) -> Items.IRON_INGOT);
    COIN_METALS.put("Industrial Iron", (str) -> INDUSTRIAL_IRON_INGOT.get());
    COIN_METALS.put("Gold", (str) -> Items.GOLD_INGOT);
    COIN_METALS.put("Netherite", (str) -> Items.NETHERITE_INGOT);

    COIN_METALS.forEach(ItemRegistry::registerCoins);
  }

  private static void registerSheets () {
    ANDESITE_SHEET = CreateDecoMod.REGISTRATE.item("andesite_sheet", Item::new)
        .tag(CDTags.of("andesite", "plates").tag)
        .tag(AllTags.AllItemTags.PLATES.tag)
        .lang("Andesite Alloy Sheet")
        .register();

    ZINC_SHEET = CreateDecoMod.REGISTRATE.item("zinc_sheet", Item::new)
      .tag(CDTags.of("zinc", "plates").tag)
      .tag(AllTags.AllItemTags.PLATES.tag)
      .lang("Zinc Sheet")
      .register();

    INDUSTRIAL_IRON_SHEET = CreateDecoMod.REGISTRATE.item("industrial_iron_sheet", Item::new)
      .tag(CDTags.of("industrial_iron", "plates").tag)
      .lang("Industrial Iron Sheet")
      .tag(AllTags.AllItemTags.PLATES.tag)
      .register();
  }

  private static void registerNuggets () {
    NETHERITE_NUGGET = CreateDecoMod.REGISTRATE.item("netherite_nugget", Item::new)
      .properties(Item.Properties::fireResistant)
      .tag(CDTags.of("netherite", "nuggets").tag)
      .lang("Netherite Nugget")
      .recipe((ctx, prov) -> prov.storage(ctx, RecipeCategory.MISC, () -> Items.NETHERITE_INGOT))
      .register();

    INDUSTRIAL_IRON_NUGGET = CreateDecoMod.REGISTRATE.item("industrial_iron_nugget", Item::new)
      .tag(CDTags.of("industrial_iron", "nuggets").tag)
      .lang("Industrial Iron Nugget")
      .recipe((ctx, prov) -> prov.storage(ctx, RecipeCategory.MISC, INDUSTRIAL_IRON_INGOT))
      .register();
  }

  private static void registerIngots () {
    INDUSTRIAL_IRON_INGOT = CreateDecoMod.REGISTRATE.item("industrial_iron_ingot", Item::new)
      .tag(CDTags.of("industrial_iron", "ingots").tag)
      .lang("Industrial Iron Ingot")
      .recipe((ctx, prov) -> prov.storage(ctx, RecipeCategory.MISC, AllBlocks.INDUSTRIAL_IRON_BLOCK))
      .register();
  }

  private static void registerCoins (String metal, Function<String, Item> getter) {
    if (metal.equals("Andesite")) return;
    COINS.put(metal, Coins.buildCoinItem(CreateDecoMod.REGISTRATE,
      () -> COINSTACKS.get(metal).get(), metal)
      .register()
    );
    COINSTACKS.put(metal, Coins.buildCoinStackItem(CreateDecoMod.REGISTRATE, () -> COINS.get(metal).get(), metal).register());
  }
}
