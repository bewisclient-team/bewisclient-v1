package bewis09.screen;

import bewis09.option.HeadLineOption;
import bewis09.option.Option;
import bewis09.util.OptionWidget;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class OptionListScreen extends Screen {

    Identifier identifier = new Identifier("minecraft","textures/block/calcite.png");

    private final List<Option> list;
    private int scroll;

    protected OptionListScreen(List<Option> list) {
        super(Text.of(""));
        this.list = list;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        setButtonPos();
        RenderSystem.setShaderTexture(0,identifier);
        drawTexture(matrices,width/2-200,0,0,0,400,height,32,32);
        fill(matrices,width/2-200,0,width/2+200,height,0xAA000000);
        int i = -1;
        HeadLineOption currentOption = null;
        for (Option o : list) {
            if(!(o instanceof HeadLineOption e)) {
                assert currentOption != null;
                if (currentOption.isOpen()) {
                    i++;
                    o.render(matrices, width / 2 - 190, i * 28 + scroll + 2, mouseX, mouseY);
                }
            } else {
                i++;
                o.render(matrices, width / 2 - 190, i * 28 + scroll + 2, mouseX, mouseY);
                currentOption = e;
            }
        }
        super.render(matrices, mouseX, mouseY, delta);
    }

    public void setButtonPos() {
        int s = -1;
        HeadLineOption currentOption = null;
        for (Option option : list) {
            if (!(option instanceof HeadLineOption e)) {
                assert currentOption != null;
                if (currentOption.isOpen()) {
                    s++;
                    for (OptionWidget widget : option.getButtons()) {
                        widget.setX(width / 2 + widget.getXOffset());
                        widget.setY(s * 28 + scroll + widget.getYOffset() + 2);
                        widget.setVisible(true);
                    }
                } else {
                    for (OptionWidget widget : option.getButtons()) {
                        widget.setVisible(false);
                    }
                }
            } else {
                s++;
                for (OptionWidget widget : option.getButtons()) {
                    widget.setX(width / 2 + widget.getXOffset());
                    widget.setY(s * 28 + scroll + widget.getYOffset() + 2);
                }
                currentOption = e;
            }
        }
    }

    @Override
    protected void init() {
        for (Option option : list) {
            for (OptionWidget widgetList : option.getButtons()) {
                this.addDrawableChild((ClickableWidget) widgetList);
            }
        }
        mouseScrolled(0,0,0);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        scroll += amount*10;
        scroll = Math.max(scroll,-(getLength()*28-height));
        scroll = Math.min(scroll,0);
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    public int getLength() {
        int s = 0;
        HeadLineOption currentOption = null;
        for (Option o : list) {
            if (currentOption != null && currentOption.isOpen() && !(o instanceof HeadLineOption)) {
                s++;
            } else if (o instanceof HeadLineOption e) {
                s++;
                currentOption = e;
            }
        }
        return s;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Option o : list) {
            if(o.isHovered() && o instanceof HeadLineOption headLineOption) {
                headLineOption.toggleOpen();
                mouseScrolled(0,0,0);
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
