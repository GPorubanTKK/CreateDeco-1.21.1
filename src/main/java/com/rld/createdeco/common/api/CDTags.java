package com.rld.createdeco.common.api;

import com.rld.createdeco.CreateDecoMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Locale;

public class CDTags {
  private enum Material {
    andesite, iron, gold, copper, zinc, brass, industrial_iron, netherite;
  }

  private enum Form {
    nuggets, ingots, plates, blocks;
  }

  private static final HashMap<Material, HashMap<Form, LoaderTag<Item>>> tags = new HashMap<>();

  public static final TagKey<Item> PLACARD = TagKey.create(
    Registries.ITEM,
    ResourceLocation.fromNamespaceAndPath(CreateDecoMod.MOD_ID, "placards")
  );

  public static final LoaderTag<Item> GLASS_ITEM = LoaderTag.same(Registries.ITEM, "glass_blocks/colorless");

  static {
    init();
  }

  private static void init () {
    for (Material m : Material.values()) {
      tags.put(m, new HashMap<>());
      for (Form f : Form.values()) {
        tags.get(m).put(f, item(m.toString(),f.toString()));
      }
    }
  }

  public static LoaderTag<Item> of(String material, String form) {
    Material m = Material.valueOf(material.toLowerCase(Locale.ROOT).replaceAll(" ", "_"));
    Form f = Form.valueOf(form.toLowerCase(Locale.ROOT));
    if (tags.containsKey(m) && tags.get(m).containsKey(f))
      return tags.get(m).get(f);
    // else
    return tags.get(Material.iron).get(Form.ingots); // basically mod recipes in a nutshell
  }

  public static final LoaderTag<Block>
    STORAGE         = block("storage_blocks"),
    INDUSTRIAL_IRON_BLOCK = block("storage_blocks", "industrial_iron");

  private static LoaderTag<Item> item (String type, String name) {
    String fabric = name + "/" + type;
    return LoaderTag.standard(Registries.ITEM, fabric);
  }

  private static LoaderTag<Block> block (String type, String name) {
    String fabric = name + "/" + type;
    return LoaderTag.standard(Registries.BLOCK, fabric);
  }

  private static LoaderTag<Block> block (String type) {
    return LoaderTag.same(Registries.BLOCK, type);
  }
}
