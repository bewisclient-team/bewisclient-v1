package bewis09.option;

import net.minecraft.client.gui.DrawContext;

import java.util.function.IntSupplier;

public class ColorDisplayOption extends Option {

    private final IntSupplier supplier;

    public ColorDisplayOption(IntSupplier supplier) {
        this.supplier = supplier;
    }

    @Override
    public void render(DrawContext context, int x, int y, int mouseX, int mouseY, int width) {
        context.fill(x,y,width+x,y+24,0xFF000000+supplier.getAsInt());
    }

    @Override
    public boolean isHovered() {
        return false;
    }
}
