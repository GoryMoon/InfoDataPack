package se.gory_moon.idp.client.jei;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IStackHelper;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.advanced.IRecipeManagerPlugin;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.plugins.jei.info.IngredientInfoRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InfoRecipeManager implements IRecipeManagerPlugin {

    private final ImmutableList<ResourceLocation> uuids = ImmutableList.of(VanillaRecipeCategoryUid.INFORMATION);
    private final IStackHelper stackHelper;
    @Nullable
    private IIngredientManager ingredientManager;
    private List<IngredientInfoRecipe> recipes = ImmutableList.of();

    public InfoRecipeManager(IStackHelper stackHelper) {
        this.stackHelper = stackHelper;
    }

    @Override
    public <V> List<ResourceLocation> getRecipeCategoryUids(IFocus<V> iFocus) {
        return uuids;
    }

    @SuppressWarnings({ "unchecked", "ConstantConditions" })
    @Override
    public <T, V> List<T> getRecipes(IRecipeCategory<T> recipeCategory, IFocus<V> focus) {
        if (recipeCategory.getUid() != VanillaRecipeCategoryUid.INFORMATION)
            return Collections.emptyList();

        ITypedIngredient<V> ingredient = focus.getTypedValue();
        IIngredientHelper<V> helper = ingredientManager.getIngredientHelper(ingredient.getType());

        if (helper.getIngredientType() != VanillaTypes.ITEM)
            return Collections.emptyList();

        List<IngredientInfoRecipe> allRecipes = new ArrayList<>();
        for (IngredientInfoRecipe recipe : recipes) {
            for (ITypedIngredient<?> i : recipe.getIngredients()) {
                if (i.getIngredient() instanceof ItemStack stack &&stackHelper.isEquivalent((ItemStack) ingredient.getIngredient(), stack, UidContext.Ingredient)) {
                    allRecipes.add(recipe);
                    break;
                }
            }

        }
        return (List<T>) allRecipes;
    }

    @Override
    public <T> List<T> getRecipes(IRecipeCategory<T> iRecipeCategory) {
        if (iRecipeCategory.getUid() != VanillaRecipeCategoryUid.INFORMATION)
            return Collections.emptyList();
        return getRecipes();
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getRecipes() {
        return (List<T>) recipes;
    }

    public void setRecipes(List<IngredientInfoRecipe> recipes) {
        this.recipes = ImmutableList.copyOf(recipes);
    }

    public void setIngredientManager(IIngredientManager ingredientManager) {
        this.ingredientManager = ingredientManager;
    }
}
