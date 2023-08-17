package bewis09.screen;

import bewis09.option.HeadLineOption;
import bewis09.option.Option;
import bewis09.util.OptionWidget;
import com.google.common.util.concurrent.AtomicDouble;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OptionListScreen extends Screen {
    private final List<Map.Entry<HeadLineOption,Option[]>> headLineOptionMap;

    int currentSelect = 0;

    private final List<AtomicDouble> extendList = new ArrayList<>();
    private final List<Double> extendLastList = new ArrayList<>();
    private double scroll = 0;
    private boolean hasFinal = false;
    private final Screen parent;
    private long lastTick;

    public OptionListScreen(List<Option> optionList, Screen parent) {
        super(Text.empty());
        this.parent = parent;
        List<Map.Entry<HeadLineOption,Option[]>> list = new ArrayList<>();
        List<Option> options = new ArrayList<>();
        HeadLineOption headLineOption = null;
        for (Option option : optionList) {
            if(option instanceof HeadLineOption h) {
                if(!options.isEmpty() && headLineOption != null) {
                    list.add(new AbstractMap.SimpleEntry<>(headLineOption,options.toArray(new Option[]{})));
                }
                options = new ArrayList<>();
                headLineOption = h;
            } else {
                options.add(option);
            }
        }
        if(!options.isEmpty() && headLineOption != null) {
            list.add(new AbstractMap.SimpleEntry<>(headLineOption,options.toArray(new Option[]{})));
        }
        headLineOptionMap = list;
        for (int i = 0; i < headLineOptionMap.size(); i++) {
            extendList.add(new AtomicDouble(0));
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        long s = System.currentTimeMillis();
        context.fill(139,0,width,height,0x66000000);
        context.drawVerticalLine(139,0,height,-1);
        int u = headLineOptionMap.size();
        int z = -1;
        assert this.client != null;
        if(this.client.currentScreen!=this)
            lastTick=s;
        if (!hasFinal) {
            extendLastList.clear();
            lastTick = s;
            for (AtomicDouble a : extendList) {
                extendLastList.add(a.get());
            }
            for (Map.Entry<HeadLineOption, Option[]> ignored : headLineOptionMap) {
                z++;
                extendList.get(z).set(extendList.get(z).get() * 0.66f);
                boolean bl = currentSelect != z;
                if (mouseX < 103 + extendList.get(z).get() && mouseY > height / 2 - u * 12 + 2 + 24 * z && mouseY < height / 2 - u * 12 + 2 + 24 * z + 20) {
                    extendList.get(z).set(extendList.get(z).get() + 10);
                    currentSelect = z;
                    if (bl) {
                        clearAndInit();
                    }
                }
            }
            hasFinal = true;
        }
        z = -1;
        for (Map.Entry<HeadLineOption, Option[]> entry : headLineOptionMap) {
            z++;
            double d = extendLastList.get(z)+((extendList.get(z).get()-extendLastList.get(z))*((s-lastTick)/50f));
            context.fill(0,height/2-u*12+2+24*z, (int) (100+d),height/2-u*12+2+24*z+20,0xFF000000);
            context.drawTextWithShadow(textRenderer,entry.getKey().text,5,height/2-u*12+2+24*z+7,-1);
            context.fill((int) (100+d),height/2-u*12+2+24*z, (int) (103+d),height/2-u*12+2+24*z+20,currentSelect==z?0xFF00FF00:-1);
        }
        if(currentSelect!=-1) {
            int i = -1;
            Option[] options = headLineOptionMap.get(currentSelect).getValue();
            for (Option o : options) {
                i++;
                o.render(context, 140, (int) (i * 28 + scroll + 2), mouseX, mouseY, width-140);
            }
        }
        setButtonPos();
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        assert client != null;
        client.setScreen(parent);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        scroll += amount*10;
        scroll = Math.max(scroll,-(getLength()*28-height));
        scroll = Math.min(scroll,0);
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public void tick() {
        hasFinal = false;
    }

    public int getLength() {
        return headLineOptionMap.get(currentSelect).getValue().length;
    }

    @Override
    protected void init() {
        if(currentSelect!=-1) {
            for (Option option : headLineOptionMap.get(currentSelect).getValue()) {
                for (OptionWidget widgetList : option.getButtons()) {
                    this.addDrawableChild((ClickableWidget) widgetList);
                }
            }
            mouseScrolled(0, 0, 0);
        }
    }

    public void setButtonPos() {
        int s = -1;
        if(currentSelect!=-1) {
            for (Option option : headLineOptionMap.get(currentSelect).getValue()) {
                s++;
                for (OptionWidget widget : option.getButtons()) {
                    widget.setX(width-widget.getXOffset());
                    widget.setY((int) (s * 28 + scroll + widget.getYOffset() + 2));
                    widget.setVisible(true);
                }
            }
        }
    }
}
