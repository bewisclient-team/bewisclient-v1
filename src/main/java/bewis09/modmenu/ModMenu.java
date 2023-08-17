package bewis09.modmenu;

import bewis09.api.ApiLoader;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import net.fabricmc.loader.api.ModContainer;

import java.util.Map;

public class ModMenu {

    public static Map<ModContainer, ? extends ConfigScreenFactory<?>> screenMap;

    public static void load() {
        screenMap = ApiLoader.getConfigScreens();
    }


}
