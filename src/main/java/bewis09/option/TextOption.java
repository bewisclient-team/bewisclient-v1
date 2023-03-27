package bewis09.option;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class TextOption extends SimpleOption {

    public final Text text;
    private final boolean centered;

    public TextOption(Text text, boolean centered) {
        this.text = text;
        this.centered = centered;
    }

    @Override
    public void render(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY) {
        super.render(matrixStack, x, y, mouseX, mouseY);
        if(centered) {
            drawCenteredTextWithShadow(matrixStack, MinecraftClient.getInstance().textRenderer, text, x+190, y+8, -1);
        } else {
            drawTextWithShadow(matrixStack, MinecraftClient.getInstance().textRenderer, text.asOrderedText(), x + 5, y + 8, -1);
        }
    }

    @Override
    public int getColor(double h) {
        if(centered) {
            return ((int)(90 + h/(1.5)) << 24) + 0x999999;
        }
        return super.getColor(h);
    }
}
