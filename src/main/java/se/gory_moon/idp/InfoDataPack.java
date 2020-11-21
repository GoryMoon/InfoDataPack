package se.gory_moon.idp;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.gory_moon.idp.client.ClientJEIManager;
import se.gory_moon.idp.client.ClientTooltipManager;
import se.gory_moon.idp.common.IDPNetwork;
import se.gory_moon.idp.common.jei.JEIInfoMessage;
import se.gory_moon.idp.common.jei.JEIManager;
import se.gory_moon.idp.common.tooltip.TooltipInfoMessage;
import se.gory_moon.idp.common.tooltip.TooltipManager;

import java.util.UUID;

@Mod(InfoDataPack.MODID)
public class InfoDataPack {
    public static final String MODID = "infodatapack";
    public static final String PROTOCOL_VERSION = "1";

    private static final Logger LOGGER = LogManager.getLogger(InfoDataPack.class);

    private static TooltipManager TOOLTIP_INSTANCE;
    private static JEIManager JEI_INSTANCE;

    public InfoDataPack() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::setup);
        modBus.addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        IDPNetwork.register();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ClientTooltipManager.init(event);
        ClientJEIManager.init(event);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.CLIENT)
            return;
        if (event.phase == TickEvent.Phase.START)
            return;

        checkAndSendData(event.player);
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        checkAndSendData(event.getPlayer());
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        checkAndSendData(event.getPlayer());
    }

    private void checkAndSendData(PlayerEntity player) {
        UUID uuid = player.getGameProfile().getId();
        String playerName = player.getDisplayName().getString();
        if (TOOLTIP_INSTANCE.isDirty(uuid)) {
            LOGGER.debug("Sending tooltip data to player {}", playerName);
            IDPNetwork.sendToPlayer(player, new TooltipInfoMessage(TOOLTIP_INSTANCE.getData()));
            TOOLTIP_INSTANCE.clearDirty(uuid);
        }

        if (JEI_INSTANCE.isDirty(uuid)) {
            LOGGER.debug("Sending jei data to player {}", playerName);
            IDPNetwork.sendToPlayer(player, new JEIInfoMessage(JEI_INSTANCE.getData()));
            JEI_INSTANCE.clearDirty(uuid);
        }
    }

    @SubscribeEvent
    public void onResourceReload(AddReloadListenerEvent event) {
        TOOLTIP_INSTANCE = new TooltipManager();
        event.addListener(TOOLTIP_INSTANCE);

        JEI_INSTANCE = new JEIManager();
        event.addListener(JEI_INSTANCE);
    }
}
