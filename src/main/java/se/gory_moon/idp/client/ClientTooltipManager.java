package se.gory_moon.idp.client;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import se.gory_moon.idp.common.base.BaseData;

import java.util.List;

public class ClientTooltipManager {

    public static ClientTooltipManager INSTANCE;
    protected List<BaseData> dataList = ImmutableList.of();

    public static void init(FMLClientSetupEvent event) {
        INSTANCE = new ClientTooltipManager();
        MinecraftForge.EVENT_BUS.register(INSTANCE);
    }

    public void updateData(List<BaseData> data) {
        dataList = data;
    }

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        for (BaseData data : dataList) {
            ResourceLocation registryName = event.getItemStack().getItem().getRegistryName();
            if (registryName != null) {
                data.apply(registryName, event.getToolTip());
            }
        }
    }
}
