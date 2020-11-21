package se.gory_moon.idp.common.base;

import com.google.common.collect.ImmutableList;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class BaseInfoMessage<T extends BaseData> {
    protected final List<T> dataList;

    public BaseInfoMessage(List<T> dataList) {
        this.dataList = dataList;
    }

    public BaseInfoMessage(PacketBuffer buffer, Function<PacketBuffer, T> createData) {
        ImmutableList.Builder<T> builder = ImmutableList.builder();

        int length = buffer.readInt();
        for (int i = 0; i < length; i++) {
            builder.add(createData.apply(buffer));
        }
        dataList = builder.build();
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeInt(dataList.size());
        dataList.forEach(data -> data.write(buffer));
    }

    public abstract void handle(Supplier<NetworkEvent.Context> ctx);
}
