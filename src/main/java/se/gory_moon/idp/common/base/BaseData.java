package se.gory_moon.idp.common.base;

import com.google.common.collect.ImmutableList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.stream.IntStream;

public  class BaseData {
    private final List<ResourceLocation> resourceLocations;
    private final List<Component> text;

    /**
     * Constructs a DataObject.
     *
     * @param resourceLocations The ResourceLocations to match against
     * @param text              The string to add the item matching the condition
     */
    public BaseData(List<ResourceLocation> resourceLocations, List<Component> text) {
        this.resourceLocations = resourceLocations;
        this.text = text;
    }

    /**
     * Constructs a DataObject from a packet.
     *
     * @param buffer A buffer with the packet data
     */
    public BaseData(FriendlyByteBuf buffer) {
        int length = buffer.readInt();
        resourceLocations = IntStream.range(0, length)
                .mapToObj(i -> buffer.readResourceLocation())
                .collect(ImmutableList.toImmutableList());

        length = buffer.readInt();
        text = IntStream.range(0, length)
                .mapToObj(i -> buffer.readComponent())
                .collect(ImmutableList.toImmutableList());
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(resourceLocations.size());
        resourceLocations.forEach(buffer::writeResourceLocation);

        buffer.writeInt(text.size());
        text.forEach(buffer::writeComponent);
    }

    public final void apply(ResourceLocation item, List<Component> toolTips) {
        if (this.resourceLocations.stream().anyMatch(item::equals))
            toolTips.addAll(this.text);
    }

    public List<ResourceLocation> getResourceLocations() {
        return resourceLocations;
    }

    public List<Component> getText() {
        return text;
    }
}
