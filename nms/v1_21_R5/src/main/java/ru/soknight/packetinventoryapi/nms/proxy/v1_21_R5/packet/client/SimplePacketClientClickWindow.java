package ru.soknight.packetinventoryapi.nms.proxy.v1_21_R5.packet.client;

import com.comphenix.protocol.events.PacketContainer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.soknight.packetinventoryapi.packet.client.PacketClientClickWindow;
import ru.soknight.packetinventoryapi.packet.client.WrappedClientPacket;

public class SimplePacketClientClickWindow extends WrappedClientPacket implements PacketClientClickWindow {

    public SimplePacketClientClickWindow(PacketContainer container, Player player) {
        super(container, player);
    }

    @Override
    public int getWindowID() {
        return handle.getIntegers().read(0);
    }

    @Override
    public int getSlot() {
        return handle.getShorts().read(0);
    }

    @Override
    public int getButtonID() {
        return handle.getBytes().read(0);
    }

    @Override
    public int getModeID() {
        return handle.getEnumModifier(InventoryClickType.class, 4).read(0).ordinal();
    }

    @Override
    public ItemStack getClickedItem() {
        return null; // Packet no longer provides a real ItemStack instance.
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public enum InventoryClickType {
        PICKUP(0),
        QUICK_MOVE(1),
        SWAP(2),
        CLONE(3),
        THROW(4),
        QUICK_CRAFT(5),
        PICKUP_ALL(6);

        private final int id;
    }

    // Helper record
    public record ChangedSlot(int index, ItemStack item) { }

}
