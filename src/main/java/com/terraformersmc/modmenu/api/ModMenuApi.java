package com.terraformersmc.modmenu.api;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.Map;
import java.util.function.Consumer;

public interface ModMenuApi {
    default ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (screen) -> null;
    }

    static Screen createModsScreen(Screen previous) {
        return null;
    }

    static Text createModsButtonText() {
        if (FabricLoader.getInstance().getModContainer("modmenu").isPresent()) {
            return ApiScreenHelper.getText();
        } else {
            return Text.of("");
        }
    }

    default Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        return ImmutableMap.of();
    }

    default void attachModpackBadges(Consumer<String> consumer) {
    }
}
