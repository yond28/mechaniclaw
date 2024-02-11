package com.yond.mechaniclaw.item;

import com.yond.mechaniclaw.MechaniClaw;
import com.yond.mechaniclaw.item.custom.MechaniClawArmorItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public class MechaniClawItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MechaniClaw.MOD_ID);

    public static final RegistryObject<Item> MECHANICLAW_CHESTPLATE = ITEMS.register("mechaniclaw_chestplate",
            () -> new MechaniClawArmorItem(MechaniClawArmorMaterials.MECHANICLAW, ArmorItem.Type.CHESTPLATE, new Item.Properties()));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
