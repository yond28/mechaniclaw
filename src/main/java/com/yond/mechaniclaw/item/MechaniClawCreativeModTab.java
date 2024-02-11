package com.yond.mechaniclaw.item;

import com.yond.mechaniclaw.MechaniClaw;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MechaniClawCreativeModTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MechaniClaw.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MECHANICLAW_TAB = CREATIVE_MODE_TABS.register("mechaniclaw",
            () -> CreativeModeTab.builder()
                    .icon(Items.COPPER_INGOT::getDefaultInstance)
                    .title(Component.translatable("creativetab.mechaniclaw"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(MechaniClawItems.MECHANICLAW_CHESTPLATE.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
