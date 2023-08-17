package com.terraformersmc.modmenu.api;

import com.terraformersmc.modmenu.ModMenu;
import net.minecraft.text.Text;

public interface ApiScreenHelper {
    static Text getText() {
        return ModMenu.createModsButtonText(true);
    }
}
