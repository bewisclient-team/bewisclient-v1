package bewis09.option;

import bewis09.drawable.ExtendedDrawableHelper;
import bewis09.util.OptionWidget;
import net.minecraft.client.util.math.MatrixStack;

import java.util.List;

public abstract class Option extends ExtendedDrawableHelper {

    public abstract void render(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY);
    public List<OptionWidget> getButtons() {return List.of();}

    public abstract boolean isHovered();
}
