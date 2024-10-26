package net.EpicFight_Improve.Mixin;

import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataKey;
import yesman.epicfight.skill.SkillDataKeys;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.skill.guard.ParryingSkill;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;
@Mixin(value = {ParryingSkill.class}, remap = false)
public abstract class ParryMixin extends GuardSkill {
    public ParryMixin(Builder builder) {
        super(builder);
    }
    @Inject(method = "createActiveGuardBuilder", at = @At("RETURN"))
    private static void InjectAdvancedGuardMotion(CallbackInfoReturnable<Builder> info) {
        Builder builder = info.getReturnValue();
        builder.addAdvancedGuardMotion(CapabilityItem.WeaponCategories.GREATSWORD, (item, playerpatch) -> new StaticAnimation[]{Animations.GREATSWORD_GUARD_HIT});
        builder.addGuardMotion(CapabilityItem.WeaponCategories.GREATSWORD,(item, playerpatch) -> Animations.GREATSWORD_GUARD_HIT);
    }
    @Inject(at = @At("HEAD"), method = "onInitiate", cancellable = true)
    public void InjectOnInitiate(SkillContainer container, CallbackInfo ci) {
        ci.cancel();
        super.onInitiate(container);
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.SERVER_ITEM_USE_EVENT, EVENT_UUID, (event) -> {
            CapabilityItem itemCapability = event.getPlayerPatch().getHoldingItemCapability(InteractionHand.MAIN_HAND);
            if (this.isHoldingWeaponAvailable(event.getPlayerPatch(), itemCapability, BlockType.GUARD) && this.isExecutableState(event.getPlayerPatch())) {
                event.getPlayerPatch().getOriginal().startUsingItem(InteractionHand.MAIN_HAND);
            }
            int lastActive = (Integer) container.getDataManager().getDataValue((SkillDataKey<?>) SkillDataKeys.LAST_ACTIVE.get());
            if (event.getPlayerPatch().getOriginal().tickCount - lastActive > 0) {
                container.getDataManager().setData(SkillDataKeys.LAST_ACTIVE.get(), event.getPlayerPatch().getOriginal().tickCount);
            }
        });
    }
}




