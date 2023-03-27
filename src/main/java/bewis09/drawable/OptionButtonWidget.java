package bewis09.drawable;

import bewis09.util.OptionWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class OptionButtonWidget extends ButtonWidget implements OptionWidget {
    public final int xMiddleOffset;
    public final int yOffset;

    public OptionButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress, int xMiddleOffset, int yOffset) {
        super(x, y, width, height, message, onPress, textSupplier -> (MutableText) Text.of(""));
        this.xMiddleOffset = xMiddleOffset;
        this.yOffset = yOffset;
        this.visible=true;
    }

    @Override
    public int getXOffset() {
        return xMiddleOffset;
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
