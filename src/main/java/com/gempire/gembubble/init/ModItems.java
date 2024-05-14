package com.gempire.gembubble.init;

import com.gempire.Gempire;
import com.gempire.gembubble.item.BubbleWandItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Gempire.MODID);

    public static final RegistryObject<Item> BUBBLE_WAND = ITEMS.register("bubble_wand", () ->
            new BubbleWandItem(new Item.Properties().stacksTo(1))
    );


}
