package se.gory_moon.idp.common.base;

import com.google.common.collect.ImmutableList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import se.gory_moon.idp.common.ItemPredicate;

import java.util.List;
import java.util.stream.IntStream;

public  class BaseData {
    private final List<ItemPredicate> itemPredicates;
    private final List<Component> text;

    /**
     * Constructs a DataObject.
     *
     * @param itemPredicates The ResourceLocations to match against
     * @param text              The string to add the item matching the condition
     */
    public BaseData(List<ItemPredicate> itemPredicates, List<Component> text) {
        this.itemPredicates = itemPredicates;
        this.text = text;
    }

    /**
     * Constructs a DataObject from a packet.
     *
     * @param buffer A buffer with the packet data
     */
    public BaseData(FriendlyByteBuf buffer) {
        int length = buffer.readInt();
        itemPredicates = IntStream.range(0, length)
                .mapToObj(i -> new ItemPredicate(buffer))
                .collect(ImmutableList.toImmutableList());

        length = buffer.readInt();
        text = IntStream.range(0, length)
                .mapToObj(i -> buffer.readComponent())
                .collect(ImmutableList.toImmutableList());
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(itemPredicates.size());
        itemPredicates.forEach(predicate -> predicate.write(buffer));

        buffer.writeInt(text.size());
        text.forEach(buffer::writeComponent);
    }

    public final void apply(@NotNull ItemStack item, List<Component> toolTips) {
        if (this.itemPredicates.stream().anyMatch(predicate -> predicate.test(item)))
            toolTips.addAll(this.text);
    }

    public List<ItemPredicate> getItemPredicates() {
        return itemPredicates;
    }

    public List<Component> getText() {
        return text;
    }
}
