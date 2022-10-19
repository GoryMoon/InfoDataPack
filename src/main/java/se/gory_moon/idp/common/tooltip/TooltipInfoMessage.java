package se.gory_moon.idp.common.tooltip;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import se.gory_moon.idp.client.ClientTooltipManager;
import se.gory_moon.idp.common.base.BaseData;
import se.gory_moon.idp.common.base.BaseInfoMessage;

import java.util.List;
import java.util.function.Supplier;

public class TooltipInfoMessage extends BaseInfoMessage {

    public TooltipInfoMessage(List<BaseData> tooltipDataList) {
        super(tooltipDataList);
    }

    public TooltipInfoMessage(FriendlyByteBuf buffer) {
        super(buffer, BaseData::new);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        //noinspection ConstantConditions
        ClientTooltipManager.INSTANCE.updateData(dataList);
    }
}
