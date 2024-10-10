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
    private final CompoundTag excludeNbt;

    private final NbtPredicate predicate;
    private NbtPredicate excludePredicate = null;

    public ItemPredicate(ResourceLocation item, @Nullable CompoundTag nbt, @Nullable CompoundTag excludeNbt) {
        this.item = item;
        this.nbt = nbt;
        this.excludeNbt = excludeNbt;
        this.predicate = nbt != null ? new NbtPredicate(nbt) : NbtPredicate.ANY;
    }

    public ItemPredicate(FriendlyByteBuf buffer) {
        item = buffer.readResourceLocation();
        nbt = buffer.readNbt();
        excludeNbt = buffer.readNbt();
        this.predicate = nbt != null ? new NbtPredicate(nbt) : NbtPredicate.ANY;
        this.excludePredicate = excludeNbt != null ? new NbtPredicate(excludeNbt) : null;
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(item);
        buffer.writeNbt(nbt);
        buffer.writeNbt(excludeNbt);
    }

    public boolean test(@Nullable ItemStack input) {
        if (input == null)
            return false;
        ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(input.getItem());
        return item.equals(registryName) &&
                predicate.matches(input.getShareTag()) &&
                (excludePredicate == null || !excludePredicate.matches(input.getShareTag()));
    }

    public ItemStack getItemStack() {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", item.toString());
        tag.putByte("Count", (byte) 1);
        if (nbt != null)
            tag.put("tag", nbt.copy());

        return ItemStack.of(tag);
    }
}
