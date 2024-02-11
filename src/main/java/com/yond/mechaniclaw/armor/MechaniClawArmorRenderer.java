package com.yond.mechaniclaw.armor;

import com.yond.mechaniclaw.MechaniClaw;
import com.yond.mechaniclaw.item.custom.MechaniClawArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class MechaniClawArmorRenderer extends GeoArmorRenderer<MechaniClawArmorItem> {

    public MechaniClawArmorRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(MechaniClaw.MOD_ID, "mechaniclaw_armor")));
        
    }
}
