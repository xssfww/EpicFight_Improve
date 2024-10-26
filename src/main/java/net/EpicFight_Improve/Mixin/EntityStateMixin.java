package net.EpicFight_Improve.Mixin;

import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
@Mixin(value = EntityState.class, remap = false)
public abstract class EntityStateMixin {
    @Shadow
    public abstract boolean knockDown();
    @Inject(method = "attackResult", at = @At("HEAD"), cancellable = true)
    public void Inject(DamageSource damagesource, CallbackInfoReturnable<AttackResult.ResultType> cir){
        if(damagesource instanceof EpicFightDamageSource epicFightDamageSource){
            epicFightDamageSource.getHurtItem().getCapability(EpicFightCapabilities.CAPABILITY_ITEM).ifPresent(capabilityItem -> {
                if(this.knockDown()){
                    cir.setReturnValue(AttackResult.ResultType.SUCCESS);
                }
            });
        }
    }
}
