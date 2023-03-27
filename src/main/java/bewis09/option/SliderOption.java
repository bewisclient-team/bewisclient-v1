package bewis09.option;

import bewis09.drawable.OptionSliderWidget;
import bewis09.util.FileReader;
import bewis09.util.OptionWidget;
import net.minecraft.text.Text;

import java.util.List;

public class SliderOption extends TextOption {
    private final String name;
    private final int max;
    private final double defaultValue;
    private final int min;
    private final int intmulti;

    List<OptionWidget> optionWidgets;

    public SliderOption(Text text, String name, int max, double defaultValue) {
        this(text,name,max,defaultValue,0);
    }

    public SliderOption(Text text, String name, int max, double defaultValue, int min) {
        this(text,name,max,defaultValue, min,1);
    }

    public SliderOption(Text text, String name, int max, double defaultValue, int min, int intmulti) {
        super(text, false);
        this.name = name;
        this.max = max;
        this.defaultValue = defaultValue;
        this.min = min;
        this.intmulti = intmulti;
        this.optionWidgets = List.of(new OptionSliderWidget(0, 0, 100, 20, getText(FileReader.getByFirstIntFirst("Double", name, defaultValue)), FileReader.getByFirstIntFirst("Double", name, defaultValue), 88, 2) {
            @Override
            protected void updateMessage() {
                setMessage(getText(value));
            }

            @Override
            protected void applyValue() {
                FileReader.setByFirst("Double", name, value);
                clickExtra(value);
            }
        });
    }

    public void clickExtra(double value) {}

    @Override
    public List<OptionWidget> getButtons() {
        return optionWidgets;
    }

    Text getText(double value) {
        return Text.translatable("bewis.option.value").append(": ").append(String.valueOf(Math.round(getValue(value)*(Math.pow(10,intmulti)))/Math.pow(10,intmulti)));
    }

    double getValue(double value) {
        return value*(max-min)+min;
    }
}
