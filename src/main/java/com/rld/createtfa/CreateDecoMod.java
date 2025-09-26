package com.rld.createtfa;

import com.rld.createtfa.common.registration.BlockRegistry;
import com.rld.createtfa.common.registration.ItemRegistry;
import com.rld.createtfa.common.data.CreateDecoPressingRecipeGen;
import com.rld.createtfa.common.registration.CreateTFARegistrate;
import com.rld.createtfa.config.CreateTFAConfig;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

@Mod(CreateDecoMod.MOD_ID)
@EventBusSubscriber
public class CreateDecoMod {
  public static final String MOD_ID = "createtfa";
  public static final String NAME = "That Factory Aesthetic";
  public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

  public static final CreateTFARegistrate REGISTRATE = CreateTFARegistrate.create(MOD_ID).skipErrors(true);

  public CreateDecoMod(ModContainer container) {
    IEventBus bus = container.getEventBus();
    container.registerConfig(ModConfig.Type.COMMON, CreateTFAConfig.SPEC);
    CreativeTabs.register(bus);
    CreateDecoMod.REGISTRATE.registerEventListeners(bus);
    init();
  }

  public static void init() {
    ItemRegistry.init();
    BlockRegistry.init();
  }

  @SubscribeEvent
  public static void gatherDataEvent(GatherDataEvent gde) {
    DataGenerator gen = gde.getGenerator();
    PackOutput pOut = gen.getPackOutput();
    CompletableFuture<HolderLookup.Provider> prov = gde.getLookupProvider();
    gen.addProvider(true, new DataProvider() {
      @Override public CompletableFuture<?> run(CachedOutput output) {
        return new CreateDecoPressingRecipeGen(pOut, prov).run(output);
      }

      @Override @NotNull public String getName() {
        return "Factory Aesthetic Pressing";
      }
    });
  }

  public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
