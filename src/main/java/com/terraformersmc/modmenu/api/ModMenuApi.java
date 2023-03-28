package com.terraformersmc.modmenu.api;

public interface ModMenuApi {
    default ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (screen) -> null;
    }
    
        static Text createModsButtonText() {
        return Text.empty();
    }
}
