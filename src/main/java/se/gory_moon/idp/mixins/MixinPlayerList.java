package se.gory_moon.idp.mixins;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.Connection;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import se.gory_moon.idp.InfoDataPack;

@Mixin(PlayerList.class)
public class MixinPlayerList {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraftforge/network/NetworkHooks;sendMCRegistryPackets(Lnet/minecraft/network/Connection;Ljava/lang/String;)V", remap = false), method = "placeNewPlayer")
    private void OnPlaceNewPlayer(Connection manager, ServerPlayer player, CallbackInfo ci) {
        InfoDataPack.checkAndSendData(player);
    }
}
