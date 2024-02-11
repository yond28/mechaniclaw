package com.yond.mechaniclaw.item.custom;

import com.google.common.collect.ImmutableMap;
import com.yond.mechaniclaw.armor.MechaniClawArmorRenderer;
import com.yond.mechaniclaw.armor.MechaniClawItemRenderer;
import com.yond.mechaniclaw.item.MechaniClawArmorMaterials;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class MechaniClawArmorItem extends ArmorItem implements GeoItem {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final Map<ArmorMaterial, MobEffectInstance> MATERIAL_TO_EFFECT_MAP = (new ImmutableMap.Builder<ArmorMaterial, MobEffectInstance>())
            .put(MechaniClawArmorMaterials.MECHANICLAW, new MobEffectInstance(MobEffects.DIG_SPEED, 200, 1, false, false, false))
            .build();

    public static final String KEY_LORE = "mechaniclaw.info.lore";

    public static final RawAnimation JUMP_LOOP = RawAnimation.begin().thenPlayAndHold("move.jump");

    public MechaniClawArmorItem(ArmorMaterial material, ArmorItem.Type slot, Item.Properties settings) {
        super(material, slot, settings);
    }

    // Create our armor model/renderer for forge and return it
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GeoArmorRenderer<?> armorRenderer;
            private GeoItemRenderer<?> itemRenderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.armorRenderer == null)
                    this.armorRenderer = new MechaniClawArmorRenderer();

                // This prepares our GeoArmorRenderer for the current render frame.
                // These parameters may be null however, so we don't do anything further with them
                this.armorRenderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);

                return this.armorRenderer;
            }

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                /* The renderer for the ITEM not armor :) */
                if (this.itemRenderer == null)
                    this.itemRenderer = new MechaniClawItemRenderer();

                return this.itemRenderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "playerAnimation", 5, this::reactToPlayerAnimation));
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        //Not working ??? So we used old deprecated onArmorTick which is working ??
    }

    @SuppressWarnings("removal")
    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide()) {
            if (isWearingMechanicalClaw(player)) {
                evaluateArmorEffects(player);
            }
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    /* ===================
        Custom code here
       =================== */
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        if (pStack.hasTag()) {
            pTooltipComponents.add(Component.translatable(KEY_LORE).withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.ITALIC));
        }

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }


    /**
     * handle claws animation reacting to player actions (idle/jump/crouch...)
     *
     * @param state AnimationState of armor
     * @return state of animation
     */
    private PlayState reactToPlayerAnimation(AnimationState<MechaniClawArmorItem> state) {
        Entity livingEntity = state.getData(DataTickets.ENTITY);

        if (!(livingEntity instanceof Player player)) {
            return PlayState.STOP;
        }
        //Reset transition speed
        state.getController().setOverrideEasingType(null);
        // Priority TOP TO BOTTOM
        // DEATH
        if (player.getHealth() <= 0 || player.isSleeping()) {
            return state.setAndContinue(DefaultAnimations.SIT);
        }
        // SWIMMING MOVE
        if (player.isInWater() && player.isSwimming()) {
            return state.setAndContinue(DefaultAnimations.SWIM);
        }
        // CROUCH
        if (!player.isInWater() && player.isCrouching()) {
            return state.setAndContinue(DefaultAnimations.SNEAK);
        }
        // JUMP
        if (!player.isInWater() && !player.onGround()) {
            //Set transition speed to out expo back for a quick transition
            state.getController().setOverrideEasingType(EasingType.EASE_OUT_BACK);
            return state.setAndContinue(JUMP_LOOP);
        }
        // SPRINT
        if (player.isSprinting()) {
            return state.setAndContinue(DefaultAnimations.RUN);
        }

        // default idle
        return state.setAndContinue(DefaultAnimations.IDLE);
    }

    private boolean isWearingMechanicalClaw(Player player) {
        ItemStack breastplateSlot = player.getItemBySlot(EquipmentSlot.CHEST);
        if (!breastplateSlot.isEmpty()) {
            ArmorItem breastplate = (ArmorItem) breastplateSlot.getItem();
            return breastplate.getMaterial() == getMaterial();
        }

        return false;
    }

    private void evaluateArmorEffects(Player player) {
        for (Map.Entry<ArmorMaterial, MobEffectInstance> entry : MATERIAL_TO_EFFECT_MAP.entrySet()) {
            MobEffectInstance mapStatusEffect = entry.getValue();

            boolean hasPlayerEffect = player.hasEffect(mapStatusEffect.getEffect());

            if (!hasPlayerEffect) {
                player.addEffect(new MobEffectInstance(mapStatusEffect));
            }
        }
    }
}
