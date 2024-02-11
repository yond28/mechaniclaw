package com.yond.mechaniclaw.armor;

import com.yond.mechaniclaw.MechaniClaw;
import com.yond.mechaniclaw.item.custom.MechaniClawArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class MechaniClawItemRenderer extends GeoItemRenderer<MechaniClawArmorItem> {
    public MechaniClawItemRenderer() {
        super(new DefaultedItemGeoModel<MechaniClawArmorItem>(new ResourceLocation(MechaniClaw.MOD_ID, "mechaniclaw_item"))
                .withAltTexture(new ResourceLocation(MechaniClaw.MOD_ID, "mechaniclaw_armor")));
    }
}
