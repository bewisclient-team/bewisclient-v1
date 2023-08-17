package bewis09.drawable;

import bewis09.util.OptionWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.function.Supplier;

public class OptionButtonWidget extends ButtonWidget implements OptionWidget {
    private final Supplier<Text> text;
    public final int xRightOffset;
    public final int yOffset;

    public OptionButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress, int xRightOffset, int yOffset) {
        super(x, y, width, height, message, onPress, textSupplier -> (MutableText) Text.of(""));
        this.text = () -> message;
        this.xRightOffset = xRightOffset;
        this.yOffset = yOffset;
        this.visible=true;
    }

    public OptionButtonWidget(int x, int y, int width, int height, Supplier<Text> text, PressAction onPress, int xRightOffset, int yOffset) {
        super(x, y, width, height, text.get(), onPress, textSupplier -> (MutableText) Text.of(""));
        this.text = text;
        this.xRightOffset = xRightOffset;
        this.yOffset = yOffset;
        this.visible=true;
    }

    @Override
    public Text getMessage() {
        return text.get();
    }

    @Override
    public int getXOffset() {
        return xRightOffset;
    }

    @Override
    public int getYOffset() {
        return yOffset;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public int getX() {
        return super.getX();
    }

    @Override
    public int getY() {
        return super.getY();
    }
}
