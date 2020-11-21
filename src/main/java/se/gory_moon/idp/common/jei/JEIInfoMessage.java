package se.gory_moon.idp.common.jei;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import se.gory_moon.idp.client.ClientJEIManager;
import se.gory_moon.idp.common.base.BaseInfoMessage;

import java.util.List;
import java.util.function.Supplier;

public class JEIInfoMessage extends BaseInfoMessage<JEIData> {

    public JEIInfoMessage(List<JEIData> tooltipDataList) {
        super(tooltipDataList);
    }

    public JEIInfoMessage(PacketBuffer buffer) {
        super(buffer, JEIData::new);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientJEIManager.INSTANCE.updateData(dataList);
        });
        ctx.get().setPacketHandled(true);
    }
}
