package bewis09.screen;

import bewis09.option.HeadLineOption;
import bewis09.option.Option;
import bewis09.util.OptionWidget;
import bewis09.util.TextHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class WidgetConfigScreen extends Screen {
    private final Screen parent;
    private final List<Option> optionList;

    public WidgetConfigScreen(List<Option> optionList, Screen parent, String ID) {
        super(Text.empty());
        this.parent = parent;
        this.optionList = new ArrayList<>(optionList);
        this.optionList.add(0,new HeadLineOption(((MutableText) TextHelper.getText("configure")).append(": ").append(ID.toLowerCase().replaceFirst(".", String.valueOf(ID.toUpperCase().charAt(0))))));
    }

    @Override
    public void close() {
        assert this.client != null;
        this.client.setScreen(parent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        renderBackground(context);

        if (!(parent instanceof ConfigScreen) && !(parent instanceof LegacyCrosshairScreen))
            parent.render(context, mouseX, mouseY, delta);

        int z = optionList.size()*28+4;
        context.fill(width/4-4,height/2-z/2,width/2+width/4+4,height/2+z/2,0x45AAAAAA);
        context.drawBorder(width/4-4-1,height/2-z/2-1,width/2+2+8,z+2,0xFFFFFFFF);
        int u = -1;
        for (Option option : optionList) {
            u++;
            option.render(context,width/4,u*28+height/2-z/2+4,mouseX,mouseY,width/2);
        }
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    protected void init() {
        assert client != null;
        parent.init(client,width,height);
        int p = 0;
        int z = optionList.size()*28+4;
        for (Option o : optionList) {
            for (OptionWidget widget : o.getButtons()) {
                widget.setX(width/2+width/4 - widget.getXOffset());
                widget.setY(p * 28 + widget.getYOffset() + height/2-z/2+4);
                widget.setVisible(true);
                addDrawableChild((ClickableWidget)widget);
            }
            p++;
        }
    }
}
