package net.EpicFight_Improve;

import net.EpicFight_Improve.Network.PacketHandler;
import net.minecraftforge.fml.common.Mod;
@Mod("epicfight_improve")
public class EpicFight_Improve {
    public static final String MODID = "epicfight_improve";
    public EpicFight_Improve() {
        PacketHandler.register();
    }
}