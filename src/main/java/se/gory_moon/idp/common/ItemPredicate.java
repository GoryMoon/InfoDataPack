package se.gory_moon.idp.common;

import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class ItemPredicate {
    private final ResourceLocation item;
    private final CompoundTag nbt;
    private final NbtPredicate predicate;

    public ItemPredicate(ResourceLocation item, @Nullable CompoundTag nbt) {
        this.item = item;
        this.nbt = nbt;
        this.predicate = nbt != null ? new NbtPredicate(nbt): NbtPredicate.ANY;
    }

    public ItemPredicate(FriendlyByteBuf buffer) {
        item = buffer.readResourceLocation();
        nbt = buffer.readNbt();
        this.predicate = nbt != null ? new NbtPredicate(nbt): NbtPredicate.ANY;
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(item);
        buffer.writeNbt(nbt);
    }

    public boolean test(@Nullable ItemStack input)
    {
        if (input == null)
            return false;
        ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(input.getItem());
        return item.equals(registryName) && predicate.matches(input.getShareTag());
    }

    public ItemStack getItemStack() {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", item.toString());
        tag.putByte("Count", (byte)1);
        if (nbt != null)
            tag.put("tag", nbt.copy());

        return ItemStack.of(tag);
    }
}
