package bewis09.screen;

import bewis09.main.Main;
import bewis09.util.Macro;
import bewis09.util.TextHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringHelper;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MacroScreen extends Screen {

    private final Screen parent;

    List<Macro> macros = getMacros();

    public static final Identifier id = new Identifier("bewisclient","gui/macro_icons.png");

    double scroll = 0;

    TextFieldWidget widget;

    ButtonWidget button;

    Macro currentKey = null;

    protected MacroScreen(Screen parent) {
        super(TextHelper.getText("macros"));

        this.parent = parent;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackgroundTexture(context);
        int u = -1;
        context.enableScissor(0,32,width,height-32);
        context.fill(0,32,width,height-32,0x76000000);
        for (Macro macro : macros) {
            u++;
            context.fill(width/2-200, (int) (34+(u*26)-scroll),width/2+200, (int) (58+(u*26)-scroll),0xFF000000);
            context.drawBorder(width/2-200, (int) (34+(u*26)-scroll),400,24,-1);
            context.drawText(textRenderer,macro.name(),width/2-196, (int) (42+(u*26)-scroll),-1,true);
        }
        super.render(context, mouseX, mouseY, delta);
        context.disableScissor();
        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 16777215);
        widget.render(context, mouseX, mouseY, delta);
        button.render(context, mouseX, mouseY, delta);
        context.fillGradient(RenderLayer.getGuiOverlay(), 0, 32, width, 32 + 4, -16777216, 0, 0);
        context.fillGradient(RenderLayer.getGuiOverlay(), 0, height-32 - 4, width, height-32, 0, -16777216, 0);
    }

    public static List<Macro> getMacros() {
        try {
            Scanner scanner = new Scanner(new File(FabricLoader.getInstance().getGameDir()+"\\bewisclient\\macros.bof"));
            List<Macro> macros = new ArrayList<>();
            while (scanner.hasNextLine()) {
                List<String> commands = new ArrayList<>();
                String key = "key.keyboard.unknown";
                String name = scanner.nextLine();
                File file = new File(FabricLoader.getInstance().getGameDir()+"\\bewisclient\\macros\\"+name+".txt");
                File file1 = new File(FabricLoader.getInstance().getGameDir()+"\\bewisclient\\macro_key\\"+name+".txt");
                if(file.exists()) {
                    Scanner macroScanner = new Scanner(file);
                    while (macroScanner.hasNextLine()) {
                        commands.add(macroScanner.nextLine());
                    }
                    macroScanner.close();
                }
                if(file1.exists()) {
                    Scanner macroScanner = new Scanner(file1);
                    if (macroScanner.hasNextLine()) {
                        key = macroScanner.nextLine();
                    }
                    macroScanner.close();
                }
                InputUtil.Key key1;
                try {
                    key1 = InputUtil.fromTranslationKey(key);
                } catch (Exception e) {
                    key1 = InputUtil.UNKNOWN_KEY;
                }
                macros.add(new Macro(name,commands,key1));
            }
            return macros;
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        }
    }

    @Override
    protected void init() {
        updateScroll(0);
        int u = -1;
        for (Macro macro : macros) {
            u++;
            if (MinecraftClient.getInstance().world != null) {
                addDrawableChild(new TexturedButtonWidget(width / 2 + 138, (int) (36 + (u * 26) - scroll), 20, 20, 60, 0, 20, id, 80, 40, button -> {
                    for (String st : macro.commands()) {
                        sendMessage(st,true);
                    }
                }));
            }
            addDrawableChild(new TexturedButtonWidget(width / 2+158, (int) (36 + (u * 26) - scroll), 20, 20, 0, 0, 20, id,80,40, button -> MinecraftClient.getInstance().setScreen(new MacroConfigScreen(macro,this))));
            addDrawableChild(new TexturedButtonWidget(width / 2+178, (int) (36 + (u * 26) - scroll), 20, 20, 20, 0, 20, id,80,40, button -> {
                macros.remove(macro);
                mouseScrolled(0,0,0);
                updateMacroFile();
                File file = new File(FabricLoader.getInstance().getGameDir()+"\\bewisclient\\macros\\"+macro.name()+".txt");
                file.delete();
                File file1 = new File(FabricLoader.getInstance().getGameDir()+"\\bewisclient\\macro_key\\"+macro.name()+".txt");
                file1.delete();
            }));
            addDrawableChild(ButtonWidget.builder(macro.key().getLocalizedText(), button -> {
                currentKey = macro;
                button.setMessage(Text.literal("> ").setStyle(Style.EMPTY.withColor(0xFFFF00)).append(Text.literal(macro.key().getLocalizedText().getString()).setStyle(Style.EMPTY.withUnderline(true).withColor(-1))).append(Text.literal(" <").setStyle(Style.EMPTY.withColor(0xFFFF00))));
            }).dimensions(width / 2 + (MinecraftClient.getInstance().world == null ? 78 : 58), (int) (36 + (u * 26) - scroll), 80, 20).build());
        }
        widget = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, width/2-100,height-26,149,20, Text.translatable(""));
        button = ButtonWidget.builder(TextHelper.getText("add"), button -> {
            macros.add(new Macro(widget.getText(),new ArrayList<>(),InputUtil.UNKNOWN_KEY));
            updateMacroFile();
            clearAndInit();
        }).dimensions(width/2+51,height-26,49,20).build();
        addDrawableChild(widget);
        addDrawableChild(button);
    }

    public static String normalize(String chatText) {
        return StringHelper.truncateChat(StringUtils.normalizeSpace(chatText.trim()));
    }

    public static void sendMessage(String chatText, boolean addToHistory) {
        chatText = normalize(chatText);
        assert MinecraftClient.getInstance() != null;
        assert MinecraftClient.getInstance().player != null;
        if (!chatText.isEmpty()) {
            if (addToHistory) {
                MinecraftClient.getInstance().inGameHud.getChatHud().addToMessageHistory(chatText);
            }

            if (chatText.startsWith("/")) {
                MinecraftClient.getInstance().player.networkHandler.sendChatCommand(chatText.substring(1));
            } else {
                MinecraftClient.getInstance().player.networkHandler.sendChatMessage(chatText);
            }

        }
    }

    @Override
    public void close() {
    }

    public void updateMacroFile() {
        File file = new File(FabricLoader.getInstance().getGameDir()+"\\bewisclient\\macros.bof");
        try {
            try (PrintWriter writer = new PrintWriter(file)) {
                for (Macro macro : macros) {
                    writer.println(macro.name());
                }
                writer.flush();
            }
        } catch (FileNotFoundException e) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
                try (PrintWriter writer = new PrintWriter(file)) {
                    for (Macro macro : macros) {
                        writer.println(macro.name());
                    }
                    writer.flush();
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        Main.macros = new ArrayList<>(macros);
    }

    public void updateMacroKeyFile(Macro m) {
        File file = new File(FabricLoader.getInstance().getGameDir()+"\\bewisclient\\macro_key\\"+m.name()+".txt");
        try {
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println(m.key().getTranslationKey());
                writer.flush();
            }
        } catch (FileNotFoundException e) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
                try (PrintWriter writer = new PrintWriter(file)) {
                    writer.println(m.key().getTranslationKey());
                    writer.flush();
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        Main.macros = new ArrayList<>(macros);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        updateScroll(amount);
        clearAndInit();
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    public void updateScroll(double amount) {
        scroll-=amount*10;
        scroll=Math.min(macros.size()*26+2-height+64,scroll);
        scroll= Math.max(0,scroll);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (currentKey != null) {
            Macro macro;
            if (keyCode != GLFW.GLFW_KEY_ESCAPE) {
                macro = new Macro(currentKey.name(), currentKey.commands(), InputUtil.fromKeyCode(keyCode, scanCode));
            } else {
                macro = new Macro(currentKey.name(), currentKey.commands(), InputUtil.UNKNOWN_KEY);
            }
            if (macros.contains(currentKey))
                macros.set(macros.indexOf(currentKey), macro);
            updateMacroKeyFile(macro);
            currentKey = null;
            clearAndInit();
        } else {
            if(keyCode == GLFW.GLFW_KEY_ESCAPE)
                MinecraftClient.getInstance().setScreen(parent);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
