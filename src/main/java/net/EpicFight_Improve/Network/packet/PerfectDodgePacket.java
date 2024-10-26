package net.EpicFight_Improve.Network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import yesman.epicfight.particle.EpicFightParticles;
public record PerfectDodgePacket(int id) implements BasePacket {
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(id);
    }
    public static PerfectDodgePacket decode(FriendlyByteBuf buf){
        return new PerfectDodgePacket(buf.readInt());
    }
    @Override
    public void execute(Player player) {
        if(Minecraft.getInstance().player != null && Minecraft.getInstance().level != null){
            Entity entity = Minecraft.getInstance().level.getEntity(id);
            if(entity != null){
                Minecraft.getInstance().level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0.0, 0.0);
            }
        }
    }
}
