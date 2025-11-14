package ru.soknight.packetinventoryapi.nms.proxy.v1_21_R5.packet.server;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.world.inventory.MenuType;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.packet.server.PacketServerOpenWindow;
import ru.soknight.packetinventoryapi.packet.server.WrappedServerPacket;

public class SimplePacketServerOpenWindow extends WrappedServerPacket implements PacketServerOpenWindow {

    private int containerId = -1;
    private MenuType<?> menuType = null;
    private Component title = null;

    public SimplePacketServerOpenWindow() {
        super(PacketServerOpenWindow.PACKET_TYPE);
    }

    @Override
    public void send(Player player) {
        if (!player.isOnline()) {
            return;
        }

        ClientboundOpenScreenPacket packet = new ClientboundOpenScreenPacket(containerId, menuType, title);
        ((CraftPlayer) player).getHandle().connection.send(packet);
    }

    @Override
    public PacketServerOpenWindow windowID(int value) {
        containerId = value;
        return this;
    }

    @Override
    public PacketServerOpenWindow windowType(int value) {
        menuType = adaptWindowType(value);
        return this;
    }

    @Override
    public PacketServerOpenWindow windowTitle(BaseComponent value) {
        BungeeComponentSerializer serializer = BungeeComponentSerializer.get();
        net.kyori.adventure.text.Component kyoriComponent = serializer.deserialize(new BaseComponent[]{value});
        title = PaperAdventure.asVanilla(kyoriComponent);

        return this;
    }

    private MenuType<?> adaptWindowType(int index) {
        return switch (index) {
            case 0 -> MenuType.GENERIC_9x1;
            case 1 -> MenuType.GENERIC_9x2;
            case 2 -> MenuType.GENERIC_9x3;
            case 3 -> MenuType.GENERIC_9x4;
            case 4 -> MenuType.GENERIC_9x5;
            case 5 -> MenuType.GENERIC_9x6;
            case 6 -> MenuType.GENERIC_3x3;
            case 7 -> MenuType.CRAFTER_3x3;
            case 8 -> MenuType.ANVIL;
            case 9 -> MenuType.BEACON;
            case 10 -> MenuType.BLAST_FURNACE;
            case 11 -> MenuType.BREWING_STAND;
            case 12 -> MenuType.CRAFTING;
            case 13 -> MenuType.ENCHANTMENT;
            case 14 -> MenuType.FURNACE;
            case 15 -> MenuType.GRINDSTONE;
            case 16 -> MenuType.HOPPER;
            case 17 -> MenuType.LECTERN;
            case 18 -> MenuType.LOOM;
            case 19 -> MenuType.MERCHANT;
            case 20 -> MenuType.SHULKER_BOX;
            case 21 -> MenuType.SMITHING;
            case 22 -> MenuType.SMOKER;
            case 23 -> MenuType.CARTOGRAPHY_TABLE;
            case 24 -> MenuType.STONECUTTER;

            default -> throw new IllegalArgumentException("Unknown window type index: " + index);
        };
    }
}
