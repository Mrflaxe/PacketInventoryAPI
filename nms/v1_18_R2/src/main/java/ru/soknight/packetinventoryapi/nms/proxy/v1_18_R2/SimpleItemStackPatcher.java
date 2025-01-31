package ru.soknight.packetinventoryapi.nms.proxy.v1_18_R2;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Vex;
import org.bukkit.inventory.meta.ItemMeta;
import ru.soknight.packetinventoryapi.nms.ItemStackPatcher;
import ru.soknight.packetinventoryapi.util.ReflectionHelper;
import ru.soknight.packetinventoryapi.util.Validate;

public final class SimpleItemStackPatcher implements ItemStackPatcher {

    @Override
    public void setDisplayName(ItemMeta itemMeta, TextComponent component) {
        setDisplayName(itemMeta, componentToJson(component));
    }

    @Override
    public void setDisplayName(ItemMeta itemMeta, String rawJson) {
        Validate.notNull(itemMeta, "itemMeta");

        try {
            ReflectionHelper.findFieldAndPutValue(itemMeta, "displayName", rawJson);
        } catch (Exception ignored) {
        }
    }

    private String componentToJson(TextComponent component) {
        return component != null ? ComponentSerializer.toString(component) : null;
    }

}
