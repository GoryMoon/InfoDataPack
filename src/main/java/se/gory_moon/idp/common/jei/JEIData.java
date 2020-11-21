package se.gory_moon.idp.common.jei;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import se.gory_moon.idp.common.base.BaseData;

import java.util.List;

public class JEIData extends BaseData {

    public JEIData(List<ResourceLocation> resourceLocations, List<ITextComponent> text) {
        super(resourceLocations, text);
    }

    public JEIData(PacketBuffer buffer) {
        super(buffer);
    }
}
