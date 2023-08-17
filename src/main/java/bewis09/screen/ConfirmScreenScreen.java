package bewis09.screen;

import bewis09.util.FileReader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.List;

public class ConfirmScreenScreen extends AbstractOptionScreen {

    private final Screen parent;
    private final Screen confirm;
    private final String shouldString;
    private final List<OrderedText> texts;

    public ConfirmScreenScreen(Screen parent, Screen confirm, Text text, String shouldString) {
        this.parent = parent;
        this.confirm = confirm;
        this.shouldString = shouldString;
        texts = MinecraftClient.getInstance().textRenderer.wrapLines(text,190);
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(parent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        drawBackground(context);
        int y = height/2-13*texts.size();
        int o = 0;
        for (OrderedText text : texts) {
            context.drawCenteredTextWithShadow(textRenderer,text,width/2,y+o,-1);
            o+=13;
        }
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    protected void init() {
        if(FileReader.getBoolean(shouldString))
            MinecraftClient.getInstance().setScreen(confirm);
        addDrawableChild(ButtonWidget.builder(ScreenTexts.BACK, button -> close()).dimensions(width/2-102,height/2+10,100,20).build());
        addDrawableChild(ButtonWidget.builder(ScreenTexts.PROCEED, button -> {
            FileReader.setByFirst("Boolean",shouldString,true);
            MinecraftClient.getInstance().setScreen(confirm);
        }).dimensions(width/2+2,height/2+10,100,20).build());
    }

    @Override
    public int maxHeight() {
        return 0;
    }
}
