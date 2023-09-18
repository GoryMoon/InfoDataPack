package se.gory_moon.idp.client.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.gory_moon.idp.InfoDataPack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    private static final Logger LOGGER = LogManager.getLogger(JEIPlugin.class);

    @Nullable
    public static Map<List<ItemStack>, List<Component>> infoData;
    @Nullable
    private static IRecipeRegistration recipeRegistration;

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(InfoDataPack.MODID, "jei");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        recipeRegistration = registration;

        // Add data if available
        if (infoData != null) {
            addRecipes(infoData);
        }
    }

    public static void addInfoRecipes(Map<List<ItemStack>, List<Component>> infoData) {
        JEIPlugin.infoData = infoData;
    }

    private void addRecipes(Map<List<ItemStack>, List<Component>> infoData) {
        LOGGER.debug("Adding ingredient info");

        infoData.forEach((key, value) -> {
            if (recipeRegistration != null)
                recipeRegistration.addIngredientInfo(key, VanillaTypes.ITEM_STACK, value.toArray(Component[]::new));
        });
    }
}
