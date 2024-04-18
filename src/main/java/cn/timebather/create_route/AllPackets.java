package cn.timebather.create_route;


import cn.timebather.create_route.content.station.screen.board_port.BoardPortScreenEditPacket;
import cn.timebather.create_route.content.train.packets.*;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public enum AllPackets {
    BOARD_PORT_EDIT(BoardPortScreenEditPacket.class, BoardPortScreenEditPacket::new, NetworkDirection.PLAY_TO_SERVER),
    DEVICE_INTERACTION_PACKET(DeviceInteractionResultPacket.class, DeviceInteractionResultPacket::new, NetworkDirection.PLAY_TO_CLIENT),
    TRAIN_DEVICE_PEER_PACKET_CLIENT_BOUND(ClientBoundDevicePeerPacket.class, ClientBoundDevicePeerPacket::new,NetworkDirection.PLAY_TO_CLIENT),
    TRAIN_DEVICE_PEER_PACKET_SERVER_BOUND(ServerBoundDevicePeerPacket.class, ServerBoundDevicePeerPacket::new,NetworkDirection.PLAY_TO_SERVER),
    DEVICE_CONFIGURE_REQUEST(DeviceConfigureRequestPacket.class,DeviceConfigureRequestPacket::new,NetworkDirection.PLAY_TO_SERVER),
    DEVICE_CONFIGURE_RESPONSE(DeviceConfigureResponsePacket.class,DeviceConfigureResponsePacket::new,NetworkDirection.LOGIN_TO_CLIENT)
    ;


    public static final ResourceLocation CHANNEL_NAME = CreateRoute.asResource("packets");
    public static final int NETWORK_VERSION = 1;
    public static final String NETWORK_VERSION_STR = String.valueOf(NETWORK_VERSION);
    private static SimpleChannel channel;

    private PacketType<?> packetType;

    <T extends SimplePacketBase> AllPackets(Class<T> type, Function<FriendlyByteBuf, T> factory,
                                            NetworkDirection direction) {
        packetType = new PacketType<>(type, factory, direction);
    }

    public static void registerPackets() {
        channel = NetworkRegistry.ChannelBuilder.named(CHANNEL_NAME)
                .serverAcceptedVersions(NETWORK_VERSION_STR::equals)
                .clientAcceptedVersions(NETWORK_VERSION_STR::equals)
                .networkProtocolVersion(() -> NETWORK_VERSION_STR)
                .simpleChannel();

        for (AllPackets packet : values())
            packet.packetType.register();
    }

    public static SimpleChannel getChannel() {
        return channel;
    }

    public static void sendToNear(Level world, BlockPos pos, int range, Object message) {
        getChannel().send(
                PacketDistributor.NEAR.with(PacketDistributor.TargetPoint.p(pos.getX(), pos.getY(), pos.getZ(), range, world.dimension())),
                message);
    }

    private static class PacketType<T extends SimplePacketBase> {
        private static int index = 0;

        private BiConsumer<T, FriendlyByteBuf> encoder;
        private Function<FriendlyByteBuf, T> decoder;
        private BiConsumer<T, Supplier<NetworkEvent.Context>> handler;
        private Class<T> type;
        private NetworkDirection direction;

        private PacketType(Class<T> type, Function<FriendlyByteBuf, T> factory, NetworkDirection direction) {
            encoder = T::write;
            decoder = factory;
            handler = (packet, contextSupplier) -> {
                NetworkEvent.Context context = contextSupplier.get();
                if (packet.handle(context)) {
                    context.setPacketHandled(true);
                }
            };
            this.type = type;
            this.direction = direction;
        }

        private void register() {
            getChannel().messageBuilder(type, index++, direction)
                    .encoder(encoder)
                    .decoder(decoder)
                    .consumerNetworkThread(handler)
                    .add();
        }
    }
}

