package se.gory_moon.idp.common.tooltip;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import se.gory_moon.idp.client.ClientTooltipManager;
import se.gory_moon.idp.common.base.BaseInfoMessage;

import java.util.List;
import java.util.function.Supplier;

public class TooltipInfoMessage extends BaseInfoMessage<TooltipData> {

    public TooltipInfoMessage(List<TooltipData> tooltipDataList) {
        super(tooltipDataList);
    }

    public TooltipInfoMessage(PacketBuffer buffer) {
        super(buffer, TooltipData::new);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientTooltipManager.INSTANCE.updateData(dataList);
        });
        ctx.get().setPacketHandled(true);
    }
}
