package net.EpicFight_Improve.Mixin;

import net.EpicFight_Improve.Network.PacketHandler;
import net.EpicFight_Improve.Network.PacketSend;
import net.EpicFight_Improve.Network.packet.PerfectDodgePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.passive.PassiveSkill;
import yesman.epicfight.skill.passive.TechnicianSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;
import java.util.UUID;
@Mixin(value = {TechnicianSkill.class}, remap = false)
public class DodgeTechnicianMixin extends PassiveSkill {
    @Final
    @Shadow
    private static final UUID EVENT_UUID = UUID.fromString("99e5c782-fdaf-11eb-9a03-0242ac130003");
    public DodgeTechnicianMixin(Skill.Builder<? extends Skill> builder) {
        super(builder);
    }
    @Inject(at = @At("HEAD"), method = "onInitiate", cancellable = true)
    public void InjectOnInitiate(SkillContainer container, CallbackInfo ci) {
        ci.cancel();
        super.onInitiate(container);
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.DODGE_SUCCESS_EVENT, EVENT_UUID, (event) -> {
            ServerPlayerPatch ServerPlayer = event.getPlayerPatch();
            PlayerPatch<?> playerPatch = container.getExecuter();
            Player player = event.getPlayerPatch().getOriginal();
            float consumption = playerPatch.getModifiedStaminaConsume(playerPatch.getSkill(SkillSlots.DODGE).getSkill().getConsumption());
            if(player.level() instanceof ServerLevel){
                PacketSend.sendToAll(PacketHandler.INSTANCE, new PerfectDodgePacket(player.getId()));
            }
            ServerPlayer.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP,-0.5F, 1F);
            playerPatch.setStamina(playerPatch.getStamina() + consumption);
        });
    }
}