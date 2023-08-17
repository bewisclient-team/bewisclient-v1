package bewis09.option;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class TextOption extends SimpleOption {

    public final Text text;
    private final boolean centered;

    public TextOption(Text text, boolean centered) {
        this.text = text;
        this.centered = centered;
    }

    @Override
    public void render(DrawContext context, int x, int y, int mouseX, int mouseY, int width) {
        super.render(context, x, y, mouseX, mouseY, width);
        if(centered) {
            context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, text, x+width/2, y+8, -1);
        } else {
            context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, text.asOrderedText(), x + 5, y + 8, -1);
        }
    }

    @Override
    public int getColor(double h) {
        return super.getColor(h);
    }
}
