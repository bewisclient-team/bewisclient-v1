package bewis09.option;

import bewis09.drawable.OptionTextFieldWidget;
import bewis09.main.Main;
import bewis09.util.FileReader;
import bewis09.util.OptionWidget;
import net.minecraft.text.Text;

import java.util.List;

public class TextFieldOption extends TextOption {

    private final String name;
    List<OptionWidget> widgets;

    public TextFieldOption(Text text, String name, String defaultV) {
        super(text, false);
        this.name = name;
        try {
            widgets = List.of(new OptionTextFieldWidget(0, 0, 100, 20, FileReader.getByFirst("Text", name)[0], 102, 2, this::onChange));
        } catch (Exception exception) {
            widgets = List.of(new OptionTextFieldWidget(0, 0, 100, 20, defaultV, 102, 2, this::onChange));
        }
    }

    @Override
    public List<OptionWidget> getButtons() {
        return widgets;
    }

    public void onChange(String text) {
        FileReader.setByFirst("Text",name,text);
        Main.setOptionBackground();
    }
}
