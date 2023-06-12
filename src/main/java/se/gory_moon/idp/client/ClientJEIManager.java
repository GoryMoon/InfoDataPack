package se.gory_moon.idp.client;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import se.gory_moon.idp.client.jei.JEIPlugin;
import se.gory_moon.idp.common.ItemPredicate;
import se.gory_moon.idp.common.base.BaseData;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class ClientJEIManager {

    @Nullable
    public static ClientJEIManager INSTANCE;
    private final boolean jeiLoaded;

    private List<BaseData> dataList = ImmutableList.of();
    @Nullable
    private ImmutableMap<List<ItemStack>, List<Component>> itemInfoMap;

    public static void init(FMLClientSetupEvent event) {
        INSTANCE = new ClientJEIManager();
        MinecraftForge.EVENT_BUS.register(INSTANCE);
    }

    public ClientJEIManager() {
        jeiLoaded = ModList.get().isLoaded("jei");
    }

    public void updateData(List<BaseData> data) {
        dataList = data;
        itemInfoMap = null;
        if (jeiLoaded) {
            JEIPlugin.addInfoRecipes(getInfoData());
        }
    }

    public Map<List<ItemStack>, List<Component>> getInfoData() {
        if (itemInfoMap == null) {
            ImmutableMap.Builder<List<ItemStack>, List<Component>> itemInfoMapBuilder = ImmutableMap.builder();

            for (BaseData data : dataList) {
                ImmutableList.Builder<ItemStack> itemListBuilder = ImmutableList.builder();
                for (ItemPredicate itemPredicate : data.getItemPredicates()) {
                    ItemStack value = itemPredicate.getItemStack();
                    if (value != ItemStack.EMPTY) {
                        itemListBuilder.add(value);
                    }
                }

                itemInfoMapBuilder.put(itemListBuilder.build(), data.getText());
            }
            itemInfoMap = itemInfoMapBuilder.build();
        }
        return itemInfoMap;
    }
}
