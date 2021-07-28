package ru.soknight.packetinventoryapi.menu.item.page.element.filler;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import ru.soknight.packetinventoryapi.item.update.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.menu.item.DisplayableMenuItem;
import ru.soknight.packetinventoryapi.menu.item.WrappedItemStack;
import ru.soknight.packetinventoryapi.menu.item.page.element.renderer.SlotItemRenderer;
import ru.soknight.packetinventoryapi.placeholder.container.list.ListContainer;
import ru.soknight.packetinventoryapi.placeholder.container.string.StringContainer;
import ru.soknight.packetinventoryapi.placeholder.element.ElementPlaceholderReplacer;
import ru.soknight.packetinventoryapi.placeholder.element.LiteElementPlaceholderReplacer;
import ru.soknight.packetinventoryapi.menu.item.regular.RegularMenuItem;
import ru.soknight.packetinventoryapi.util.Validate;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class AbstractPageContentFiller<I extends DisplayableMenuItem> implements PageContentFiller<I> {

    protected final SlotItemRenderer slotItemRenderer;
    protected final boolean replaceWithEmptyItems;
    protected final List<ElementPlaceholderReplacer> placeholderReplacers;

    protected AbstractPageContentFiller(SlotItemRenderer slotItemRenderer, boolean replaceWithEmptyItems) {
        this.slotItemRenderer = slotItemRenderer;
        this.replaceWithEmptyItems = replaceWithEmptyItems;
        this.placeholderReplacers = new ArrayList<>();
    }

    @Override
    public void fillPageContent(Player viewer, I menuItem, int startIndex, ContentUpdateRequest<?, ?> contentUpdateRequest) {
        if(slotItemRenderer == null)
            return;

        for(int pageIndex = 0; pageIndex < 90; pageIndex++) {
            int totalIndex = startIndex + pageIndex;

            RegularMenuItem<?, ?> item;
            if(menuItem.isRegular())
                item = menuItem.asRegularItem();
            else if(menuItem.isStateable())
                item = menuItem.asStateableItem().getItemFor(viewer, -1, pageIndex, totalIndex);
            else
                throw new IllegalArgumentException("'menuItem' has unexprected displayable menu item type");

            if(item == null)
                return;

            int[] slots = item.getSlots();
            if(slots == null || slots.length == 0)
                return;

            if(pageIndex >= slots.length)
                break;

            int slot = slots[pageIndex];

            ItemStack itemStack = slotItemRenderer.renderItem(viewer, item, slot, pageIndex, totalIndex);
            if(itemStack == null || (!replaceWithEmptyItems && itemStack.getType() == Material.AIR))
                continue;

            replacePlaceholders(viewer, itemStack, slot, pageIndex, totalIndex);
            contentUpdateRequest.set(itemStack, slot, true);
        }
    }

    protected String replacePlaceholders(Player viewer, String original, int slot, int pageIndex, int totalIndex) {
        if(placeholderReplacers.isEmpty())
            return original;

        if(original == null || original.isEmpty())
            return original;

        StringContainer wrapper = StringContainer.wrap(original);
        for(ElementPlaceholderReplacer replacer : placeholderReplacers)
            replacer.replace(viewer, wrapper, slot, pageIndex, totalIndex);

        return wrapper.getString();
    }

    protected List<String> replacePlaceholders(Player viewer, List<String> original, int slot, int pageIndex, int totalIndex) {
        if(placeholderReplacers.isEmpty())
            return original;

        if(original == null || original.isEmpty())
            return original;

        ListContainer wrapper = ListContainer.wrap(original);
        for(ElementPlaceholderReplacer replacer : placeholderReplacers)
            replacer.replace(viewer, wrapper, slot, pageIndex, totalIndex);

        return wrapper.getList();
    }

    @SuppressWarnings("deprecation")
    protected void replacePlaceholders(Player viewer, ItemStack item, int slot, int pageIndex, int totalIndex) {
        if(item == null || !item.hasItemMeta())
            return;

        ItemMeta itemMeta = item.getItemMeta();

        if(itemMeta.hasDisplayName()) {
            String displayName = itemMeta.getDisplayName();
            itemMeta.setDisplayName(replacePlaceholders(viewer, displayName, slot, pageIndex, totalIndex));
        }

        if(itemMeta.hasLore()) {
            List<String> lore = itemMeta.getLore();
            itemMeta.setLore(replacePlaceholders(viewer, lore, slot, pageIndex, totalIndex));
        }

        if(item instanceof WrappedItemStack) {
            WrappedItemStack wrapper = (WrappedItemStack) item;
            String playerHead = wrapper.getVanillaItem().getPlayerHead();
            if(playerHead != null && !playerHead.isEmpty() && itemMeta instanceof SkullMeta) {
                SkullMeta skullMeta = (SkullMeta) itemMeta;
                OfflinePlayer owningPlayer = Bukkit.getOfflinePlayer(replacePlaceholders(viewer, playerHead, slot, pageIndex, totalIndex));
                skullMeta.setOwningPlayer(owningPlayer);
            }
        }

        item.setItemMeta(itemMeta);
    }

    @Override
    public PageContentFiller<I> appendReplacerFirst(@NotNull LiteElementPlaceholderReplacer replacer) {
        return appendReplacerFirst((ElementPlaceholderReplacer) replacer);
    }

    @Override
    public PageContentFiller<I> appendReplacerFirst(@NotNull ElementPlaceholderReplacer replacer) {
        Validate.notNull(replacer, "replacer");
        this.placeholderReplacers.add(0, replacer);
        return this;
    }

    @Override
    public PageContentFiller<I> appendReplacer(@NotNull LiteElementPlaceholderReplacer replacer) {
        return appendReplacer((ElementPlaceholderReplacer) replacer);
    }

    @Override
    public PageContentFiller<I> appendReplacer(@NotNull ElementPlaceholderReplacer replacer) {
        Validate.notNull(replacer, "replacer");
        this.placeholderReplacers.add(replacer);
        return this;
    }

    @Override
    public PageContentFiller<I> removeReplacer(@NotNull ElementPlaceholderReplacer replacer) {
        Validate.notNull(replacer, "replacer");
        this.placeholderReplacers.remove(replacer);
        return this;
    }

}
