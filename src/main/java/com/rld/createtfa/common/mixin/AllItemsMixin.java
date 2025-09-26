package com.rld.createtfa.common.mixin;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AllItems.class)
public class AllItemsMixin {
    @Redirect(method = "<clinit>", at = @At(
        value = "INVOKE",
        target = "Lcom/simibubi/create/AllItems;taggedIngredient(Ljava/lang/String;[Lnet/minecraft/tags/TagKey;)Lcom/tterrag/registrate/util/entry/ItemEntry;",
        ordinal = 4
    )) private static ItemEntry<Item> mixin(String name, TagKey<Item>[] tags) {
        TagKey<Item>[] allTags = new TagKey[tags.length + 1];
        allTags[0] = AllTags.commonItemTag("ingots/andesite");
        System.arraycopy(tags, 0, allTags, 1, tags.length);
        return taggedIngredient(name, allTags);
    }

    @SafeVarargs
    @Shadow private static ItemEntry<Item> taggedIngredient(String name, TagKey<Item>... tags) {
        return null;
    }
}
