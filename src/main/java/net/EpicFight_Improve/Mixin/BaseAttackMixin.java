package net.EpicFight_Improve.Mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.skill.BasicAttack;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;
import java.util.UUID;
@Mixin(value = BasicAttack.class, remap = false)
public abstract class BaseAttackMixin {
    @Final
    @Shadow
    private static final UUID EVENT_UUID = UUID.fromString("a42e0198-fdbc-11eb-9a03-0242ac130003");
    @Inject(at = @At("HEAD"), method = "onInitiate")
    public void InjectOnInitiate(SkillContainer container, CallbackInfo ci) {
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.DEALT_DAMAGE_EVENT_ATTACK, EVENT_UUID, (event) ->
            event.getTarget().getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).ifPresent(entityPatch -> {
            if (entityPatch instanceof LivingEntityPatch<?> livingEntityPatch) {
                if (livingEntityPatch.getEntityState().knockDown()) {
                    event.getDamageSource().setStunType(StunType.NONE);
                }
            }
        }));
    }
    @Inject(at = @At("HEAD"), method = "onRemoved")
    public void InjectOnRemoved(SkillContainer container,CallbackInfo ci) {
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.DEALT_DAMAGE_EVENT_ATTACK, EVENT_UUID);
    }
}
