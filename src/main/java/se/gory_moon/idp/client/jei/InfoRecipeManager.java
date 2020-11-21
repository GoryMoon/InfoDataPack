package se.gory_moon.idp.client.jei;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.advanced.IRecipeManagerPlugin;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.plugins.jei.info.IngredientInfoRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.List;

public class InfoRecipeManager implements IRecipeManagerPlugin {

    private final ImmutableList<ResourceLocation> uuids = ImmutableList.of(VanillaRecipeCategoryUid.INFORMATION);
    private List<IngredientInfoRecipe<ItemStack>> recipes = ImmutableList.of();

    @Override
    public <V> List<ResourceLocation> getRecipeCategoryUids(IFocus<V> iFocus) {
        return uuids;
    }

    @Override
    public <T, V> List<T> getRecipes(IRecipeCategory<T> iRecipeCategory, IFocus<V> iFocus) {
        if (iRecipeCategory.getUid() != VanillaRecipeCategoryUid.INFORMATION) return Collections.emptyList();
        return getRecipes();
    }

    @Override
    public <T> List<T> getRecipes(IRecipeCategory<T> iRecipeCategory) {
        if (iRecipeCategory.getUid() != VanillaRecipeCategoryUid.INFORMATION) return Collections.emptyList();
        return getRecipes();
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> getRecipes() {
        return (List<T>) recipes;
    }

    public void setRecipes(List<IngredientInfoRecipe<ItemStack>> recipes) {
        this.recipes = ImmutableList.copyOf(recipes);
    }
}
