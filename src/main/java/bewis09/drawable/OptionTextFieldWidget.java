package bewis09.drawable;

import bewis09.util.OptionWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class OptionTextFieldWidget extends TextFieldWidget implements OptionWidget {

    public final int xRightOffset;
    public final int yOffset;

    public OptionTextFieldWidget(int x, int y, int width, int height, Object message, int xRightOffset, int yOffset, Listener listener) {
        super(MinecraftClient.getInstance().textRenderer, x, y, width, height, Text.of(message.toString()));
        this.xRightOffset = xRightOffset;
        this.yOffset = yOffset;
        this.visible=true;
        this.setText(message.toString());
        this.setChangedListener(listener::action);
    }

    public interface Listener {
        void action(String str);
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
