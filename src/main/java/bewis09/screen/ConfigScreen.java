package bewis09.screen;

import bewis09.option.HeadLineOption;
import bewis09.option.Option;
import bewis09.util.OptionWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.MutableText;

import java.util.ArrayList;
import java.util.List;

public class ConfigScreen extends AbstractOptionScreen {

    private final List<Option> optionList;
    private final Screen parent;

    public ConfigScreen(List<Option> optionList, MutableText text, Screen parent) {
        this.optionList = new ArrayList<>(optionList);
        this.parent = parent;
        this.optionList.add(0,new HeadLineOption(text));
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(parent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        drawBackground(context);
        int z = 0;
        for (Option option : optionList) {
            option.render(context,width/2-144, (int) (4+z*28-scroll),mouseX,mouseY,288);
            z++;
        }
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        boolean b = super.mouseScrolled(mouseX, mouseY, amount);
        clearAndInit();
        return b;
    }

    @Override
    public int maxHeight() {
        return optionList.size()*28+4;
    }

    @Override
    protected void init() {
        correctScroll();
        int z = 0;
        for (Option o : optionList) {
            for (OptionWidget optionWidget : o.getButtons()) {
                addDrawableChild((ClickableWidget) optionWidget);
                optionWidget.setX(width/2+144 - optionWidget.getXOffset());
                optionWidget.setY((int) (4+z*28+optionWidget.getYOffset()-scroll));
            }
            z++;
        }
    }
}
