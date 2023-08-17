package bewis09.option;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class SimpleOption extends Option {

    double h = 0;
    public boolean isHovered = false;
    public Text tooltip = null;

    @Override
    public void render(DrawContext context, int x, int y, int mouseX, int mouseY, int width) {
        isHovered = false;
        boolean b = mouseX < x + width & mouseX > x;
        if(b &&mouseY>y&&mouseY<y+24) {
            h+=10;
            isHovered = true;
            if(getTooltip()!=null) {
                assert MinecraftClient.getInstance().currentScreen != null;
                context.drawTooltip(MinecraftClient.getInstance().textRenderer, getTooltip(),mouseX,mouseY);
            }
        }
        h*=1-(1/21f);
        context.fill(x,y,x+width,y+24, getColor(h));
        if(b &&mouseY>y&&mouseY<y+24 && getTooltip()!=null) {
            assert MinecraftClient.getInstance().currentScreen != null;
            context.drawTooltip(MinecraftClient.getInstance().textRenderer, getTooltip(),mouseX,mouseY);
        }
    }

    public int getColor(double h) {
        return (int)(90 + h/(1.5)) << 24;
    }

    @Override
    public boolean isHovered() {
        return isHovered;
    }

    @SuppressWarnings("all")
    public SimpleOption withTooltip(String key) {
        tooltip = Text.translatable("bewis.option.tooltip."+key);
        return this;
    }

    public Text getTooltip() {
        return tooltip;
    }
}
