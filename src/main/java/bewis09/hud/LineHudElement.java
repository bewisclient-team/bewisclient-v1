package bewis09.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
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
    public void paint(MatrixStack stack) {
        stack.push();
        stack.scale(getSize(),getSize(),getSize());
        fill(stack, getX(), getY(), getX()+getWidth(), getY()+getHeight(),0x960a0a0a);
        int i = -1;
        for (Text text : getTexts()) {
            i++;
            if(centered) {
                drawCenteredTextWithShadow(stack, MinecraftClient.getInstance().textRenderer, text, getX()+getWidth()/2, getY()+i*11+3,-1);
            } else {
                drawTextWithShadow(stack, MinecraftClient.getInstance().textRenderer, text, getX()+4, getY()+i*11+3,-1);
            }
        }
        stack.pop();
    }
}
