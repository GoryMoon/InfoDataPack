package se.gory_moon.idp.common.tooltip;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import se.gory_moon.idp.common.base.BaseData;

import java.util.List;

public class TooltipData extends BaseData {

    public TooltipData(List<ResourceLocation> resourceLocations, List<ITextComponent> text) {
        super(resourceLocations, text);
    }

    public TooltipData(PacketBuffer buffer) {
        super(buffer);
    }
}
