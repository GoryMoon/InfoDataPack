package se.gory_moon.idp.common;

import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import se.gory_moon.idp.InfoDataPack;
import se.gory_moon.idp.common.jei.JEIInfoMessage;
import se.gory_moon.idp.common.tooltip.TooltipInfoMessage;

public class IDPNetwork {

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(InfoDataPack.MODID, "main"),
            () -> InfoDataPack.PROTOCOL_VERSION,
            InfoDataPack.PROTOCOL_VERSION::equals,
            InfoDataPack.PROTOCOL_VERSION::equals
    );

    public static void register() {

        CHANNEL.messageBuilder(TooltipInfoMessage.class, 0, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(TooltipInfoMessage::encode)
                .decoder(TooltipInfoMessage::new)
                .consumer(TooltipInfoMessage::handle)
                .add();

        CHANNEL.messageBuilder(JEIInfoMessage.class, 1, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(JEIInfoMessage::encode)
                .decoder(JEIInfoMessage::new)
                .consumer(JEIInfoMessage::handle)
                .add();
    }

    public static <MSG> void sendToPlayer(Player player, MSG message) {
        IDPNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), message);
    }

}
