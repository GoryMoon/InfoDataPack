package se.gory_moon.idp.client.jei;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.registration.IAdvancedRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.plugins.jei.info.IngredientInfoRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.gory_moon.idp.InfoDataPack;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    private static final Logger LOGGER = LogManager.getLogger(JEIPlugin.class);

    @Nullable
    private static InfoRecipeManager recipeManager;
    @Nullable
    private List<IngredientInfoRecipe> dummyRecipes;
    @Nullable
    public static List<IngredientInfoRecipe> previousRecipes;
    @Nullable
    public static Map<List<ItemStack>, List<Component>> preInfoData;
    @Nullable
    private static IIngredientManager ingredientManager;

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(InfoDataPack.MODID, "jei");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ingredientManager = registration.getIngredientManager();
        tryToSetData();
        dummyRecipes = IngredientInfoRecipe.create(ingredientManager, Collections.singletonList(new ItemStack(Items.BARRIER)), VanillaTypes.ITEM, new TextComponent("Dummy text, plz ignore ;)"));
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        IRecipeManager recipeManager = jeiRuntime.getRecipeManager();
        if (dummyRecipes != null)
            dummyRecipes.forEach(recipe -> recipeManager.hideRecipe(recipe, VanillaRecipeCategoryUid.INFORMATION));
    }

    @Override
    public void registerAdvanced(IAdvancedRegistration registration) {
        recipeManager = new InfoRecipeManager(registration.getJeiHelpers().getStackHelper());
        tryToSetData();
        registration.addRecipeManagerPlugin(recipeManager);
    }

    private void tryToSetData() {
        if (recipeManager != null && ingredientManager != null)
            recipeManager.setIngredientManager(ingredientManager);

        // If we have gotten the data before creating the manager use this
        if (previousRecipes != null && recipeManager != null)
            recipeManager.setRecipes(previousRecipes);

        if (preInfoData != null && ingredientManager != null) {
            addRecipes(preInfoData);
            preInfoData = null;
        }
    }

    public static void addInfoRecipes(Map<List<ItemStack>, List<Component>> infoData) {
        if (ingredientManager == null) {
            preInfoData = infoData;
            return;
        }
        addRecipes(infoData);
    }

    @SuppressWarnings("ConstantConditions")
    private static void addRecipes(Map<List<ItemStack>, List<Component>> infoData) {
        LOGGER.debug("Adding ingredient info");
        ImmutableList.Builder<IngredientInfoRecipe> builder = ImmutableList.builder();
        infoData.forEach((key, value) -> {
            builder.addAll(IngredientInfoRecipe.create(ingredientManager, key, VanillaTypes.ITEM, value.toArray(Component[]::new)));
        });
        previousRecipes = builder.build();
        if (recipeManager != null)
            recipeManager.setRecipes(previousRecipes);
    }
}
