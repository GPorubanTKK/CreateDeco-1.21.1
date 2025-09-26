package com.rld.createtfa.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class CreateTFAConfig {
    public static final CreateTFAConfig CONFIG;
    public static final ModConfigSpec SPEC;

    public final ModConfigSpec.ConfigValue<Boolean> craftableCoins;

    private CreateTFAConfig(ModConfigSpec.Builder builder) {
        builder.comment("Allows coins to be created by pressing an associated nugget");
        craftableCoins = builder.define("craftable_coins", false);
    }

    static {
        Pair<CreateTFAConfig, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(CreateTFAConfig::new);

        CONFIG = pair.getLeft();
        SPEC = pair.getRight();
    }
}
