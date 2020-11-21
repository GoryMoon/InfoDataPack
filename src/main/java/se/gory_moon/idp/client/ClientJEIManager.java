package se.gory_moon.idp.client;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.CharacterManager;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextProcessing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.client.gui.GuiUtils;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import se.gory_moon.idp.client.jei.JEIPlugin;
import se.gory_moon.idp.common.jei.JEIData;

import java.util.List;
import java.util.Map;

public class ClientJEIManager {

    public static ClientJEIManager INSTANCE;
    private final boolean jeiLoaded;

    private List<JEIData> dataList = ImmutableList.of();
    private ImmutableMap<List<ItemStack>, List<String>> itemInfoMap;

    public static void init(FMLClientSetupEvent event) {
        INSTANCE = new ClientJEIManager();
        MinecraftForge.EVENT_BUS.register(INSTANCE);
    }

    public ClientJEIManager() {
        jeiLoaded = ModList.get().isLoaded("jei");
    }

    public void updateData(List<JEIData> data) {
        dataList = data;
        itemInfoMap = null;
        if (jeiLoaded) {
            JEIPlugin.addInfoRecipes(getInfoData());
        }
    }

    public Map<List<ItemStack>, List<String>> getInfoData() {
        if (itemInfoMap == null) {
            ImmutableMap.Builder<List<ItemStack>, List<String>> itemInfoMapBuilder = ImmutableMap.builder();

            for (JEIData data : dataList) {
                ImmutableList.Builder<ItemStack> itemListBuilder = ImmutableList.builder();
                for (ResourceLocation resourceLocation : data.getResourceLocations()) {
                    Item value = ForgeRegistries.ITEMS.getValue(resourceLocation);
                    if (value != null) {
                        itemListBuilder.add(new ItemStack(value));
                    }
                }

                ImmutableList<String> textList = data.getText().stream().map(ITextComponent::getString).collect(ImmutableList.toImmutableList());
                itemInfoMapBuilder.put(itemListBuilder.build(), textList);
            }
            itemInfoMap = itemInfoMapBuilder.build();
        }
        return itemInfoMap;
    }
}
