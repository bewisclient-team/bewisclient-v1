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
import net.minecraft.text.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicReference;

public class MacroConfigScreen extends Screen {

    double scroll = 0;

    private final Macro macro;
    private final Screen parent;

    TextFieldWidget widget;

    ButtonWidget button;

    protected MacroConfigScreen(Macro macro, Screen parent) {
        super(TextHelper.getText("configure_macro").append(Text.of(": "+macro.name())));
        this.macro = macro;
        this.parent = parent;
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(parent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackgroundTexture(context);
        int u = -1;
        context.enableScissor(0,32,width,height-32);
        context.fill(0,32,width,height-32,0x76000000);
        for (String ignored : macro.commands()) {
            u++;
            context.fill(width/2-200, (int) (34+(u*26)-scroll),width/2+200, (int) (58+(u*26)-scroll),0xFF000000);
            context.drawBorder(width/2-200, (int) (34+(u*26)-scroll),400,24,-1);
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

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        updateScroll(amount);
        clearAndInit();
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    public void updateScroll(double amount) {
        scroll-=amount*10;
        scroll=Math.min(macro.commands().size()*26+2-height+64,scroll);
        scroll= Math.max(0,scroll);
    }

    @Override
    protected void init() {
        updateScroll(0);
        int u = -1;
        for (String str : macro.commands()) {
            AtomicReference<String> stringAtomicReference = new AtomicReference<>(str);
            u++;
            addDrawableChild(new TexturedButtonWidget(width / 2+178, (int) (36 + (u * 26) - scroll), 20, 20, 20, 0, 20, MacroScreen.id,80,40, button -> {
                macro.commands().remove(str);
                mouseScrolled(0,0,0);
                updateMacroFile();
            }));
            int finalU = u;
            addDrawableChild(new TexturedButtonWidget(width / 2+158, (int) (36 + (u * 26) - scroll), 20, 10, 40, 0, 10, MacroScreen.id,80,40, button -> {
                macro.commands().remove(stringAtomicReference.get());
                macro.commands().add(Math.max(0,finalU -1),stringAtomicReference.get());
                updateMacroFile();
                mouseScrolled(0,0,0);
            }));
            addDrawableChild(new TexturedButtonWidget(width / 2+158, (int) (46 + (u * 26) - scroll), 20, 10, 40, 20, 10, MacroScreen.id,80,40, button -> {
                macro.commands().remove(stringAtomicReference.get());
                macro.commands().add(Math.min(macro.commands().size(),finalU +1),stringAtomicReference.get());
                updateMacroFile();
                mouseScrolled(0,0,0);
            }));
            TextFieldWidget fieldWidget = addDrawableChild(new TextFieldWidget(textRenderer,width / 2-198, (int) (36 + (u * 26) - scroll), 354, 20,Text.of("")));
            fieldWidget.setMaxLength(Integer.MAX_VALUE);
            fieldWidget.setText(str);
            fieldWidget.setChangedListener(s -> {
                macro.commands().set(finalU,s);
                stringAtomicReference.set(s);
                updateMacroFile();
            });
        }
        widget = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, width/2-100,height-26,149,20, Text.translatable(""));
        button = ButtonWidget.builder(TextHelper.getText("add"), button -> {
            macro.commands().add(widget.getText());
            updateMacroFile();
            clearAndInit();
        }).dimensions(width/2+51,height-26,49,20).build();
        widget.setMaxLength(Integer.MAX_VALUE);
        addDrawableChild(widget);
        addDrawableChild(button);
    }

    public void updateMacroFile() {
        File file = new File(FabricLoader.getInstance().getGameDir()+"\\bewisclient\\macros\\"+macro.name()+".txt");
        try {
            try (PrintWriter writer = new PrintWriter(file)) {
                for (String str : macro.commands()) {
                    writer.println(str);
                }
                writer.flush();
            }
        } catch (FileNotFoundException e) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
                try (PrintWriter writer = new PrintWriter(file)) {
                    for (String str : macro.commands()) {
                        writer.println(str);
                    }
                    writer.flush();
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        Main.macros = MacroScreen.getMacros();
    }
}
