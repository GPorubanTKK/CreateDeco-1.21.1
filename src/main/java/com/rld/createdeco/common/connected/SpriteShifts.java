package com.rld.createdeco.common.connected;

import com.rld.createdeco.CreateDecoMod;
import com.rld.createdeco.common.ItemRegistry;
import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import net.createmod.catnip.data.Couple;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

import java.util.HashMap;
import java.util.Locale;

public class SpriteShifts {
  public static final HashMap<String, CTSpriteShiftEntry> METAL_WINDOWS     = new HashMap<>();
  public static final HashMap<String, CTSpriteShiftEntry> SHEET_METAL_SIDES = new HashMap<>();
  public static final HashMap<String, CTSpriteShiftEntry> CATWALK_TOPS      = new HashMap<>();

  public static final HashMap<DyeColor, Couple<CTSpriteShiftEntry>> VAULT_TOP = new HashMap<>();
  public static final HashMap<DyeColor, Couple<CTSpriteShiftEntry>> VAULT_FRONT = new HashMap<>();
  public static final HashMap<DyeColor, Couple<CTSpriteShiftEntry>> VAULT_SIDE = new HashMap<>();
  public static final HashMap<DyeColor, Couple<CTSpriteShiftEntry>> VAULT_BOTTOM = new HashMap<>();

  static {
    populateMaps();
  }

  private static Couple<CTSpriteShiftEntry> vault (DyeColor color, String face) {
    //final String prefixed = "block/vault/vault_" + name;
    final String prefixed = "block/palettes/shipping_containers/" + color + "/vault_" + face;
    return Couple.createWithContext(medium -> CTSpriteShifter.getCT(
      AllCTTypes.RECTANGLE, CreateDecoMod.id(prefixed + "_small"),
      CreateDecoMod.id(medium ? prefixed + "_medium" : prefixed + "_large")
    ));
  }

  private static void populateMaps () {
    for (DyeColor color : DyeColor.values()) {
      VAULT_TOP   .put(color, vault(color, "top"));
      VAULT_BOTTOM.put(color, vault(color, "bottom"));
      VAULT_FRONT .put(color, vault(color, "front"));
      VAULT_SIDE  .put(color, vault(color, "side"));
    }

    for (String metal : ItemRegistry.METAL_TYPES.keySet()) {
      String path = "block/palettes/sheet_metal/" + metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_sheet_metal";
      ResourceLocation blockTexture     = ResourceLocation.fromNamespaceAndPath(CreateDecoMod.MOD_ID, path);
      ResourceLocation connectedTexture = ResourceLocation.fromNamespaceAndPath(CreateDecoMod.MOD_ID, path + "_connected");
      SHEET_METAL_SIDES.put(metal, CTSpriteShifter.getCT(AllCTTypes.VERTICAL, blockTexture, connectedTexture));

      path = "block/palettes/catwalks/" + metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_catwalk";
      blockTexture     = ResourceLocation.fromNamespaceAndPath(CreateDecoMod.MOD_ID, path);
      connectedTexture = ResourceLocation.fromNamespaceAndPath(CreateDecoMod.MOD_ID, path + "_connected");
      CATWALK_TOPS.put(metal, CTSpriteShifter.getCT(AllCTTypes.OMNIDIRECTIONAL, blockTexture, connectedTexture));

      path = "block/palettes/windows/" + metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_window";
      blockTexture     = ResourceLocation.fromNamespaceAndPath(CreateDecoMod.MOD_ID, path);
      connectedTexture = ResourceLocation.fromNamespaceAndPath(CreateDecoMod.MOD_ID, path + "_connected");
      METAL_WINDOWS.put(metal, CTSpriteShifter.getCT(AllCTTypes.VERTICAL, blockTexture, connectedTexture));
    }
  }
}
