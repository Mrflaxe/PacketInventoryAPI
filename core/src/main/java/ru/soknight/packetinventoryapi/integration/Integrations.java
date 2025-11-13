package ru.soknight.packetinventoryapi.integration;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public final class Integrations {

    private static final String ITEMS_ADDER_PLUGIN_NAME = "ItemsAdder";

    public static boolean availableItemsAdder() {
        return isPluginEnabled(ITEMS_ADDER_PLUGIN_NAME);
    }

    private static boolean isPluginEnabled(@NotNull String pluginName) {
        return Bukkit.getPluginManager().isPluginEnabled(pluginName);
    }

}
