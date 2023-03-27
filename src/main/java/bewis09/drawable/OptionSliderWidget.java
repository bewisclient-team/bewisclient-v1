package bewis09.drawable;

import bewis09.util.OptionWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public abstract class OptionSliderWidget extends SliderWidget implements OptionWidget {

    private final int xOffset;
    private final int yOffset;

    public OptionSliderWidget(int x, int y, int width, int height, Text text, double value, int xOffset, int yOffset) {
        super(x, y, width, height, text, value);
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    @Override
    public int getXOffset() {
        return xOffset;
    }

    @Override
    public int getYOffset() {
        return yOffset;
    }

    @Override
    protected abstract void updateMessage();

    @Override
    public void setX(int x) {
        super.setX(x);
    }

    @Override
    public void setY(int y) {
        super.setY(y);
    }

    @Override
    protected abstract void applyValue();

    @Override
    public int getX() {
        return super.getX();
    }

    @Override
    public int getY() {
        return super.getY();
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
