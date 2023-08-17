package bewis09.screen;

import bewis09.main.Main;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.List;

public class NotInWorldScreen extends Screen {

    List<OrderedText> infoText;
    Screen parent;

    public NotInWorldScreen(Screen parent) {
        super(Text.empty());
        this.parent = parent;
        infoText = MinecraftClient.getInstance().textRenderer.wrapLines(Text.translatable("bewis.option.no_world", Text.translatable(Main.settings.getBoundKeyTranslationKey()).getString()),200);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        int z = -1;
        for (OrderedText o : infoText) {
            z++;
            context.drawCenteredTextWithShadow(textRenderer,o,width/2,height/3+z*10,-1);
        }
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(parent);
    }

    @Override
    protected void init() {
        addDrawableChild(ButtonWidget.builder(ScreenTexts.BACK, button -> MinecraftClient.getInstance().setScreen(parent)).dimensions(width/2-50,height/3*2,100,20).build());
    }
}
