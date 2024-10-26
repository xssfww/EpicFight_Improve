package net.EpicFight_Improve.Network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;
public interface BasePacket {
    void encode(FriendlyByteBuf var1);
    default void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> this.execute(context.get().getSender()));
    }
    void execute(Player var1);
}