package net.EpicFight_Improve.Network;

import net.EpicFight_Improve.EpicFight_Improve;
import net.EpicFight_Improve.Network.packet.BasePacket;
import net.EpicFight_Improve.Network.packet.PerfectDodgePacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import java.util.function.Function;
public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(EpicFight_Improve.MODID, "main"),
            () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals
    );
    private static int index;
    public static synchronized void register() {
        register(PerfectDodgePacket.class, PerfectDodgePacket::decode);
    }
    private static <MSG extends BasePacket> void register(final Class<MSG> packet, Function<FriendlyByteBuf, MSG> decoder) {
        INSTANCE.messageBuilder(packet, index++).encoder(BasePacket::encode).decoder(decoder).consumer(BasePacket::handle).add();
    }
}