package ru.soknight.packetinventoryapi.menu.item;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.soknight.packetinventoryapi.nms.vanilla.VanillaItem;

@Getter
public final class WrappedItemStack extends ItemStack {

    private final VanillaItem<?, ?> vanillaItem;
    private final ItemStack delegate;

    public WrappedItemStack(@NotNull Material type, @NotNull VanillaItem<?, ?> vanillaItem) {
        super(type);
        this.vanillaItem = vanillaItem;
        this.delegate = super.clone();
    }

    public WrappedItemStack(@NotNull ItemStack itemStack, @NotNull VanillaItem<?, ?> vanillaItem) {
        super(itemStack.clone());
        this.vanillaItem = vanillaItem;
        this.delegate = itemStack.clone();
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public @NotNull WrappedItemStack clone() {
        return new WrappedItemStack(delegate.clone(), vanillaItem);
    }
}
