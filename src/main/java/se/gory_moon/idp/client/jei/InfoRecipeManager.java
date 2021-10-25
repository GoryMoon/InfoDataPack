package se.gory_moon.idp.client.jei;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.helpers.IStackHelper;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.advanced.IRecipeManagerPlugin;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.gui.Focus;
import mezz.jei.plugins.jei.info.IngredientInfoRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InfoRecipeManager implements IRecipeManagerPlugin {

    private final ImmutableList<ResourceLocation> uuids = ImmutableList.of(VanillaRecipeCategoryUid.INFORMATION);
    private final IStackHelper stackHelper;
    private List<IngredientInfoRecipe<ItemStack>> recipes = ImmutableList.of();

    public InfoRecipeManager(IStackHelper stackHelper) {
        this.stackHelper = stackHelper;
    }

    @Override
    public <V> List<ResourceLocation> getRecipeCategoryUids(IFocus<V> iFocus) {
        return uuids;
    }

    @SuppressWarnings({ "unchecked", "NullableProblems" })
    @Override
    public <T, V> List<T> getRecipes(IRecipeCategory<T> iRecipeCategory, IFocus<V> iFocus) {
        if (iRecipeCategory.getUid() != VanillaRecipeCategoryUid.INFORMATION) return Collections.emptyList();
        IFocus<V> focus = Focus.check(iFocus);
        if (!(focus.getValue() instanceof ItemStack)) return Collections.emptyList();

        ItemStack ingredient = (ItemStack) focus.getValue();
        List<IngredientInfoRecipe<ItemStack>> allRecipes = new ArrayList<>();
        for (IngredientInfoRecipe<ItemStack> recipe : recipes) {
            for (ItemStack stack : recipe.getIngredients()) {
                if (stackHelper.isEquivalent(ingredient, stack, UidContext.Ingredient)) {
                    allRecipes.add(recipe);
                    break;
                }
            }

        }
        return (List<T>) allRecipes;
    }

    @Override
    public <T> List<T> getRecipes(IRecipeCategory<T> iRecipeCategory) {
        if (iRecipeCategory.getUid() != VanillaRecipeCategoryUid.INFORMATION) return Collections.emptyList();
        return getRecipes();
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getRecipes() {
        return (List<T>) recipes;
    }

    public void setRecipes(List<IngredientInfoRecipe<ItemStack>> recipes) {
        this.recipes = ImmutableList.copyOf(recipes);
    }
}
