package bewis09.screen;

import bewis09.hud.Huds;
import bewis09.main.Main;
import bewis09.util.FileReader;
import bewis09.util.PublicOptionSaver;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.IOException;

public class ProfilesScreen extends AbstractOptionScreen {

    private final Screen parent;

    public static final Identifier identifier = new Identifier("bewisclient","gui/macro_icons.png");

    public ProfilesScreen(Screen parent) {
        this.parent = parent;
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(parent);
    }

    public void drawBackground(DrawContext context) {
        context.enableScissor(0,0,width/2-150,height);
        renderBackgroundTexture(context);
        context.disableScissor();
        context.enableScissor(width/2+150,0,width,height);
        renderBackgroundTexture(context);
        context.disableScissor();
        assert client != null;
        if(client.world == null) {
            context.setShaderColor(0.125F, 0.125F, 0.125F, 1.0F);
            context.drawTexture(Screen.OPTIONS_BACKGROUND_TEXTURE, width / 2 - 150, (int) -scroll, (float) 0, (float) 0, 300, (int) (height+scroll), 32, 32);
            context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        } else {
            context.fill(width/2-150,0,width/2+150,height,0x88000000);
        }
        fillGradient(context,width/2-150,0,width/2-146,height-26,1,0xFF000000,0);
        fillGradient(context,width/2+146,0,width/2+150,height-26,1,0,0xFF000000);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        drawBackground(context);
        int i = -1;
        context.enableScissor(0,0,width,height-24);
        for (FileReader.Profile profile : FileReader.profiles) {
            i++;
            context.fill(width/2-140,2+26*i-getScroll(),width/2+140,26+26*i-getScroll(),PublicOptionSaver.values.getOrDefault("Profile","default").equals(profile.s()) ? 0xFF005500 : 0xFF000000);
            context.drawBorder(width/2-140,2+26*i-getScroll(),280,24,-1);
            context.drawTextWithShadow(textRenderer,profile.s().replace(".bof",""),width/2-134,2+26*i-getScroll()+8,-1);
        }
        context.disableScissor();
        context.enableScissor(width/2-154,height-26,width/2+154,height);
        context.setShaderColor(0.25F, 0.25F, 0.25F, 1.0F);
        context.drawTexture(OPTIONS_BACKGROUND_TEXTURE,0,0,0,0,width,height,32,32);
        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        context.disableScissor();
        context.fillGradient(width/2-150,height-30,width/2+150,height-26,0,0xFF000000);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    protected void init() {
        correctScroll();
        int i = -1;
        for (FileReader.Profile profile : FileReader.profiles) {
            i++;
            this.addDrawableChild(new TexturedButtonWidget(width / 2 + 118, 4 + 26 * i - getScroll(), 20, 20,60, 0, 20, identifier,80,40, button -> {
                FileReader.currentProfile = profile;
                PublicOptionSaver.write("Profile",profile.s());
                Main.setOptionBackground();
                Huds.reload();
                MinecraftClient.getInstance().setScreen(new ProfilesScreen(new OptionScreen()));
            }));
            if(!profile.s().equals("default.bof")) {
                this.addDrawableChild(new TexturedButtonWidget(width / 2 + 98, 4 + 26 * i - getScroll(), 20, 20, 20, 0, 20, identifier, 80, 40, button -> {
                    File file = new File(FileReader.folder+"\\"+profile.s());
                    file.delete();
                    FileReader.load();
                    clearAndInit();
                }));
            }
        }
        TextFieldWidget widget = this.addDrawableChild(new TextFieldWidget(textRenderer,width/2-146,height-23,192,20, Text.empty()));
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> {
            File file = new File(FileReader.folder+"\\"+widget.getText()+".bof");
            try {
                file.createNewFile();
            } catch (IOException ignored) {}
            FileReader.load();
            clearAndInit();
        }).dimensions(width/2+48,height-23,100,20).build());
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        clearAndInit();
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public int maxHeight() {
        return 0;
    }
}
