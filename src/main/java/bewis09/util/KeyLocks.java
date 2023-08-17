package bewis09.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;

import java.util.Map;

public class KeyLocks {
    public static Map<KeyBinding,Boolean> getLockKeys() {
        KeyBinding[] keyBindings = MinecraftClient.getInstance().options.allKeys;
        return Util.withMethod(keyBindings, KeyBinding::isPressed);
    }

    public static void lockKeys(Map<KeyBinding,Boolean> map) {

    }

    public static void unlockKeys() {

    }
}
