package bewis09.option;

import bewis09.util.OptionWidget;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

public abstract class Option {

    public abstract void render(DrawContext context, int x, int y, int mouseX, int mouseY, int width);
    public List<OptionWidget> getButtons() {return List.of();}

    public abstract boolean isHovered();
}
