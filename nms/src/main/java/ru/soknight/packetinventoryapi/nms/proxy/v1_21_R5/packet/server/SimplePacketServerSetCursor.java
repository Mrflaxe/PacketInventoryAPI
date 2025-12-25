package ru.soknight.packetinventoryapi.nms.proxy.v1_21_R5.packet.server;

import com.comphenix.protocol.events.PacketContainer;
import net.minecraft.network.protocol.game.ClientboundSetCursorItemPacket;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.soknight.packetinventoryapi.packet.server.PacketServerSetCursor;
import ru.soknight.packetinventoryapi.packet.server.WrappedServerPacket;

public class SimplePacketServerSetCursor extends WrappedServerPacket implements PacketServerSetCursor {

    public SimplePacketServerSetCursor() {
        super(PacketServerSetCursor.PACKET_TYPE);
    }

    public SimplePacketServerSetCursor(PacketContainer container) {
        super(container);
    }

    public void send(Player player, ItemStack itemStack) {
        ClientboundSetCursorItemPacket setCursorItemPacket = new ClientboundSetCursorItemPacket(CraftItemStack.unwrap(itemStack));
        CraftPlayer craftPlayer = (CraftPlayer) player;
        craftPlayer.getHandle().connection.send(setCursorItemPacket);
    }
}
