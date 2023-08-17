package bewis09.option;

import bewis09.drawable.OptionButtonWidget;
import bewis09.util.OptionWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import java.awt.event.ActionListener;
import java.util.List;

public class ButtonClickOption extends TextOption {

    List<OptionWidget> optionWidgets;

    public ButtonClickOption(Text text, ActionListener action) {
        super(text, false);
        optionWidgets = List.of(new OptionButtonWidget(0, 0, 100, 20, ScreenTexts.PROCEED, button -> action.actionPerformed(null),102,2));
    }

    @Override
    public List<OptionWidget> getButtons() {
        return optionWidgets;
    }
}
