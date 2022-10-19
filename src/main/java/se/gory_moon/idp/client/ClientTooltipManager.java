package se.gory_moon.idp.client;

import com.google.common.collect.ImmutableList;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import se.gory_moon.idp.common.base.BaseData;

import javax.annotation.Nullable;
import java.util.List;

public class ClientTooltipManager {

    @Nullable
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
    public void onLeaveWorld(LevelEvent.Unload event) {
        dataList = ImmutableList.of();
    }

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        for (BaseData data : dataList) {
            ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(event.getItemStack().getItem());
            if (registryName != null) {
                data.apply(registryName, event.getToolTip());
            }
        }
    }
}
