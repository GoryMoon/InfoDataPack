package se.gory_moon.idp.client.jei;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.registration.IAdvancedRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.plugins.jei.info.IngredientInfoRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.gory_moon.idp.InfoDataPack;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    private static final Logger LOGGER = LogManager.getLogger(JEIPlugin.class);
    private static InfoRecipeManager recipeManager;

    private List<IngredientInfoRecipe<ItemStack>> dummyRecipes;

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(InfoDataPack.MODID, "jei");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        //ClientJEIManager.INSTANCE.getInfoData().forEach((key, value) -> registration.addIngredientInfo(key, VanillaTypes.ITEM, value.toArray(new String[0])));

        dummyRecipes = IngredientInfoRecipe.create(Collections.singletonList(new ItemStack(Items.BARRIER)), VanillaTypes.ITEM, "Dummy text, plz ignore ;)");
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        IRecipeManager recipeManager = jeiRuntime.getRecipeManager();
        dummyRecipes.forEach(recipe -> recipeManager.hideRecipe(recipe, VanillaRecipeCategoryUid.INFORMATION));
    }

    @Override
    public void registerAdvanced(IAdvancedRegistration registration) {
        recipeManager = new InfoRecipeManager(registration.getJeiHelpers().getStackHelper());
        registration.addRecipeManagerPlugin(recipeManager);
    }

    public static void addInfoRecipes(Map<List<ItemStack>, List<String>> infoData) {
        LOGGER.debug("Adding ingredient info");
        ImmutableList.Builder<IngredientInfoRecipe<ItemStack>> builder = ImmutableList.builder();
        infoData.forEach((key, value) -> {
            builder.addAll(IngredientInfoRecipe.create(key, VanillaTypes.ITEM, value.toArray(new String[0])));
        });
        recipeManager.setRecipes(builder.build());
    }
}
