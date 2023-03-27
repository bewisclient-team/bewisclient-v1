package bewis09.option;

import bewis09.drawable.OptionButtonWidget;
import bewis09.util.FileReader;
import bewis09.util.OptionWidget;
import net.minecraft.text.Text;

import java.util.List;

public class BooleanOption extends TextOption {

    boolean bl;

    List<OptionWidget> buttonWidgets;

    public BooleanOption(Text text, String name) {
        this(text,name,false);
    }

    public BooleanOption(Text text, String name, boolean defaultV) {
        super(text, false);
        bl = FileReader.getBoolean(name,defaultV);
        buttonWidgets = List.of(new OptionButtonWidget(0, 0, 100, 20, Text.of("FALSE"), (a) -> {
            bl = !bl;
            FileReader.setByFirst("Boolean", name, bl);
            clickExtra(bl);
        }, 88, 2));
    }

    public void clickExtra(boolean enabled) {}

    @Override
    public List<OptionWidget> getButtons() {
        ((OptionButtonWidget)buttonWidgets.get(0)).setMessage(Text.of(String.valueOf(bl).toUpperCase()));
        return buttonWidgets;
    }
}
