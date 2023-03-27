package bewis09.api;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.util.HashMap;
import java.util.Map;

public class ApiLoader {
    public static Map<ModContainer, ConfigScreenFactory<?>> getConfigScreens() {
        Map<ModContainer, ConfigScreenFactory<?>> map = new HashMap<>();
        FabricLoader.getInstance().getEntrypointContainers("modmenu", ModMenuApi.class).forEach(modMenuApiEntrypointContainer -> {
            map.put(modMenuApiEntrypointContainer.getProvider(),modMenuApiEntrypointContainer.getEntrypoint().getModConfigScreenFactory());
        });
        return map;
    }
}
