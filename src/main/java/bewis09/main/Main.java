package bewis09.main;

import bewis09.hat.Hat;
import bewis09.mixin.DrawableHelperMixin;
import bewis09.modmenu.ModMenu;
import bewis09.screen.MacroScreen;
import bewis09.screen.OptionScreen;
import bewis09.update.UpdateChecker;
import bewis09.util.ExtraInfo;
import bewis09.util.FileReader;
import bewis09.util.Macro;
import bewis09.wings.Wing;
import bewis09.wings.WingFeatureRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import javax.swing.Timer;
import java.util.*;

public class Main implements ClientModInitializer {

    // 5158 Lines of Code

    public static List<Long> rightList = new ArrayList<>();
    public static List<Long> leftList = new ArrayList<>();
    public static boolean isZoomed= false;
    public static boolean isSmooth = false;
    static String EMPTY = "";
    public static int rgbValue = 0;
    public static java.util.Timer timer = new java.util.Timer();
    public static KeyBinding ElytraSwitcher;
    public static KeyBinding settings;

    public static String updateInfo;

    public static Map<InputUtil.Key,Boolean> pressMap = new HashMap<>();

    public static List<Macro> macros = MacroScreen.getMacros();

    @Override
    public void onInitializeClient() {

        settings = KeyBindingHelper.registerKeyBinding(new KeyBinding("bewis.option.open", GLFW.GLFW_KEY_RIGHT_SHIFT, "bewisclient.keybinding"));
        KeyBinding keyBinding2 = KeyBindingHelper.registerKeyBinding(new KeyBinding("bewis.zoom", GLFW.GLFW_KEY_C, "bewisclient.keybinding"));
        KeyBinding keyBinding3 = KeyBindingHelper.registerKeyBinding(new KeyBinding("bewis.gamma", GLFW.GLFW_KEY_G, "bewisclient.keybinding"));
        KeyBinding keyBinding4 = KeyBindingHelper.registerKeyBinding(new KeyBinding("bewis.night_vision", GLFW.GLFW_KEY_H, "bewisclient.keybinding"));
        KeyBinding keyBinding5 = KeyBindingHelper.registerKeyBinding(new KeyBinding("bewis.gamma_up", GLFW.GLFW_KEY_UP, "bewisclient.keybinding"));
        KeyBinding keyBinding6 = KeyBindingHelper.registerKeyBinding(new KeyBinding("bewis.gamma_down", GLFW.GLFW_KEY_DOWN, "bewisclient.keybinding"));

        ElytraSwitcher = KeyBindingHelper.registerKeyBinding(new KeyBinding("bewis.elytra_switcher", GLFW.GLFW_KEY_R, "bewisclient.keybinding"));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (settings.wasPressed()) {
                MinecraftClient.getInstance().setScreen(new OptionScreen());
            }
            while (keyBinding3.wasPressed()) {
                if (FileReader.getByFirstIntFirst("Double", "fullbrightvalue", 0.1f) <= 0.1) {
                    FileReader.setByFirst("Double", "fullbrightvalue", 1);
                } else {
                    FileReader.setByFirst("Double", "fullbrightvalue", 0.1);
                }
                FileReader.setByFirst("Boolean", "fullbright", true);
                printGammaMessage(FileReader.getByFirstIntFirst("Double", "fullbrightvalue", 0.1f));
                MinecraftClient.getInstance().options.getGamma().setValue(FileReader.getBoolean("fullbright") ? FileReader.getByFirstIntFirst("Double", "fullbrightvalue", 0.1) * 10 : 1);
            }
            while (keyBinding4.wasPressed()) {
                FileReader.setByFirst("Boolean", "night_vision", !FileReader.getBoolean("night_vision"));
                assert MinecraftClient.getInstance().player != null;
                MinecraftClient.getInstance().player.sendMessage(Text.translatable("bewis.option.night_vision." + (FileReader.getBoolean("night_vision") ? "enabled" : "disabled")).setStyle(
                        Style.EMPTY.withColor(FileReader.getBoolean("night_vision") ? 0xFFFF00 : 0xFF0000)
                ), true);
            }
            while (keyBinding5.wasPressed()) {
                FileReader.setByFirst("Boolean", "fullbright", true);
                FileReader.setByFirst("Double", "fullbrightvalue", ((double) (int) ((Math.min(1, FileReader.getByFirstIntFirst("Double", "fullbrightvalue", 0.1f) + 0.025f)) * 1000)) / 1000f);
                printGammaMessage(FileReader.getByFirstIntFirst("Double", "fullbrightvalue", 0.1f));
                MinecraftClient.getInstance().options.getGamma().setValue(FileReader.getBoolean("fullbright") ? FileReader.getByFirstIntFirst("Double", "fullbrightvalue", 0.1) * 10 : 1);
            }
            while (keyBinding6.wasPressed()) {
                FileReader.setByFirst("Boolean", "fullbright", true);
                FileReader.setByFirst("Double", "fullbrightvalue", ((double) Math.round((Math.max(0, FileReader.getByFirstIntFirst("Double", "fullbrightvalue", 0.1f) - 0.025f)) * 1000)) / 1000f);
                printGammaMessage(FileReader.getByFirstIntFirst("Double", "fullbrightvalue", 0.1f));
                MinecraftClient.getInstance().options.getGamma().setValue(FileReader.getBoolean("fullbright") ? FileReader.getByFirstIntFirst("Double", "fullbrightvalue", 0.1) * 10 : 1);
            }
            for (Macro macro : macros) {
                if (macro.key() != InputUtil.UNKNOWN_KEY && InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), macro.key().getCode()) && MinecraftClient.getInstance().world != null && MinecraftClient.getInstance().player != null) {
                    if (!pressMap.getOrDefault(macro.key(), false)) {
                        for (String st : macro.commands()) {
                            MacroScreen.sendMessage(st, true);
                        }
                        pressMap.remove(macro.key());
                        pressMap.put(macro.key(), true);
                    }
                } else {
                    pressMap.remove(macro.key());
                    pressMap.put(macro.key(), false);
                }
            }
            if (isZoomed) {
                if (!keyBinding2.isPressed()) {
                    isZoomed = false;
                    if (!FileReader.getBoolean("hard_camera")) {
                        MinecraftClient.getInstance().options.smoothCameraEnabled = isSmooth;
                    }
                } else if (!FileReader.getBoolean("hard_camera")) {
                    MinecraftClient.getInstance().options.smoothCameraEnabled = true;
                }
            } else if (keyBinding2.isPressed()) {
                isZoomed = true;
                if (!FileReader.getBoolean("hard_camera")) {
                    isSmooth = MinecraftClient.getInstance().options.smoothCameraEnabled;
                }
            }
        });
        FileReader.init();
        ExtraInfo.register(new ExtraInfo(state -> ExtraInfo.withText(state, RedstoneWireBlock.POWER)), Blocks.REDSTONE_WIRE);
        ExtraInfo.register(new ExtraInfo(state -> ExtraInfo.withText(state, SculkSensorBlock.POWER)), Blocks.SCULK_SENSOR);
        ExtraInfo.register(new ExtraInfo(state -> ExtraInfo.withText(state, NoteBlock.NOTE)), Blocks.NOTE_BLOCK);
        ExtraInfo.register(new ExtraInfo(state -> ExtraInfo.withText(state, HopperBlock.ENABLED)), Blocks.HOPPER);
        ExtraInfo.register(new ExtraInfo(state -> ExtraInfo.withText(state, TntBlock.UNSTABLE)), Blocks.TNT);
        ExtraInfo.register(new ExtraInfo(state -> ExtraInfo.withText(state, TripwireBlock.ATTACHED)), Blocks.TRIPWIRE_HOOK);
        ExtraInfo.register(new ExtraInfo(state -> ExtraInfo.withText(state, TripwireBlock.ATTACHED)), Blocks.TRIPWIRE);
        ExtraInfo.register(new ExtraInfo(state -> ExtraInfo.withText(state, SculkShriekerBlock.CAN_SUMMON)), Blocks.SCULK_SHRIEKER);
        setOptionBackground();
        rgb();
        ModMenu.load();
        wing();
        try {
            Hat.current_hat=(Hat.HATS[(int) FileReader.getByFirstIntFirst("Cosmetics", "Hat", 0)]);
        } catch (Exception ignored){}
        try {
            Wing.current_wing=(Wing.WINGS[(int) FileReader.getByFirstIntFirst("Cosmetics", "Wing", 0)]);
        } catch (Exception ignored){}

        Main.updateInfo = UpdateChecker.hasNewUpdate();
    }

    public static void printGammaMessage(double gamma) {
        assert MinecraftClient.getInstance().player != null;
        MinecraftClient.getInstance().player.sendMessage(Text.translatable("options.gamma")
                .setStyle(Style.EMPTY.withColor((0xFF00 + (((int) (gamma * 0xFF))))<<8))
                .append(": ").append(gamma * 1000f + "%"),true);
    }

    public static void rgb() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                rgb();
                rgbValue = (rgbValue+1)%60;

            }
        },(int) getValue(FileReader.getByFirstIntFirst("Double","hitrgbspeed",0.2),10,500));
    }

    public static void wing() {
        new Timer(50, e -> {
            WingFeatureRenderer.wing_animation_duration=(WingFeatureRenderer.wing_animation_duration+1)%60;
        }).start();
    }

    public static int rCount() {
        int t = 0;
        for (Long l : new ArrayList<>(rightList)) {
            if(System.currentTimeMillis()-l>1000) t++;
        }
        if (t > 0) {
            rightList.subList(0, t).clear();
        }
        return rightList.size();
    }

    public static void setOptionBackground() {
        try {
            if(FileReader.getByFirst("Text", "backgroundtexture").length==0 || FileReader.getByFirst("Text", "backgroundtexture")[0].equals(EMPTY)) {
                DrawableHelperMixin.setOPTIONS_BACKGROUND_TEXTURE(new Identifier("textures/gui/options_background.png"));
            } else if(FileReader.getByFirst("Text", "backgroundtexture")[0].contains("/") || FileReader.getByFirst("Text", "backgroundtexture")[0].contains(":")) {
                DrawableHelperMixin.setOPTIONS_BACKGROUND_TEXTURE(new Identifier(FileReader.getByFirst("Text", "backgroundtexture")[0]));
            } else {
                DrawableHelperMixin.setOPTIONS_BACKGROUND_TEXTURE(new Identifier("textures/block/" + FileReader.getByFirst("Text", "backgroundtexture")[0]+ ".png"));
            }
        } catch (Exception ignored){}
    }

    public static int lCount() {
        int t = 0;
        for (Long l : new ArrayList<>(leftList)) {
            if(System.currentTimeMillis()-l>1000) t++;
        }
        if (t > 0) {
            leftList.subList(0, t).clear();
        }
        return leftList.size();
    }

    static double getValue(double value, int min, int max) {
        return value * (max - min) + min;
    }
}
