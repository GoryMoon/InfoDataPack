package se.gory_moon.idp.mixins;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.management.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import se.gory_moon.idp.InfoDataPack;

@Mixin(PlayerList.class)
public class MixinPlayerList {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/network/NetworkHooks;sendMCRegistryPackets(Lnet/minecraft/network/NetworkManager;Ljava/lang/String;)V", remap = false), method = "placeNewPlayer(Lnet/minecraft/network/NetworkManager;Lnet/minecraft/entity/player/ServerPlayerEntity;)V")
    private void OnPlaceNewPlayer(NetworkManager manager, ServerPlayerEntity player, CallbackInfo ci) {
        InfoDataPack.checkAndSendData(player);
    }
}
