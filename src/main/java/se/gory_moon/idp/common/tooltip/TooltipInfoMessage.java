package se.gory_moon.idp.common.tooltip;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import se.gory_moon.idp.client.ClientTooltipManager;
import se.gory_moon.idp.common.base.BaseData;
import se.gory_moon.idp.common.base.BaseInfoMessage;

import java.util.List;
import java.util.function.Supplier;

public class TooltipInfoMessage extends BaseInfoMessage {

    public TooltipInfoMessage(List<BaseData> tooltipDataList) {
        super(tooltipDataList);
    }

    public TooltipInfoMessage(PacketBuffer buffer) {
        super(buffer, BaseData::new);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        //noinspection CodeBlock2Expr
        ctx.get().enqueueWork(() -> {
            ClientTooltipManager.INSTANCE.updateData(dataList);
        });
        ctx.get().setPacketHandled(true);
    }
}
