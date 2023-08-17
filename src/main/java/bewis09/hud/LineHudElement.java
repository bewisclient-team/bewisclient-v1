package bewis09.hud;

import bewis09.screen.WidgetScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.List;

public abstract class LineHudElement extends HudElement {
    private final boolean centered;

    public LineHudElement(int x, int y, Horizontal horizontal, Vertical vertical, int width, List<Text> texts, boolean centered) {
        super(x, y, horizontal, vertical, width, texts.size()*11+3);
        this.centered = centered;
    }

    public abstract List<Text> getTexts();

    @Override
    public boolean sizeCustom() {
        return true;
    }

    @Override
    public void paint(DrawContext context) {
        if(getTexts().size() != 0 || MinecraftClient.getInstance().currentScreen instanceof WidgetScreen) {
            context.getMatrices().push();
            context.getMatrices().scale(getSize(), getSize(), getSize());
            context.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), getColor());
            int i = -1;
            for (Text text : getTexts()) {
                i++;
                if (centered) {
                    context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, text, getX() + getWidth() / 2, getY() + i * 11 + 3, -1);
                } else {
                    context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, text, getX() + 4, getY() + i * 11 + 3, -1);
                }
            }
            context.getMatrices().pop();
        }
    }
}
