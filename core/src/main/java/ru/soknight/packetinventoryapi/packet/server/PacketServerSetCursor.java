package ru.soknight.packetinventoryapi.packet.server;

import com.comphenix.protocol.PacketType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.soknight.packetinventoryapi.nms.ImplementedAs;

@ImplementedAs("SimplePacketServerSetCursor")
public interface PacketServerSetCursor extends ServerPacket{

    PacketType PACKET_TYPE = PacketType.Play.Server.SET_CURSOR_ITEM;

    void send(Player player, ItemStack itemStack);
}
