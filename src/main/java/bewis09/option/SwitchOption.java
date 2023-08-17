package bewis09.option;

import bewis09.drawable.OptionButtonWidget;
import bewis09.util.TextHelper;
import bewis09.util.FileReader;
import bewis09.util.OptionWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class SwitchOption extends TextOption {

    private final String id;
    private final List<String> entries;
    private List<OptionWidget> buttons;

    public SwitchOption(Text text, String id, List<String> entries) {
        super(text, false);
        this.id = id;
        this.entries = entries;
        reloadButtons();
    }

    public void reloadButtons() {
        final int[] index = {entries.contains(FileReader.getSwitch(id, "97453")) ? entries.indexOf(FileReader.getSwitch(id, entries.get(0))) : 0};
        buttons = new ArrayList<>();
        buttons.add(new OptionButtonWidget(0, 0, 100, 20, TextHelper.getText(entries.get(index[0])), button -> {
            index[0] = (index[0] +1)%entries.size();
            FileReader.setByFirst("Switch",id,entries.get(index[0]));
        },102,2){
            @Override
            public Text getMessage() {
                return TextHelper.getText(entries.get(index[0]));
            }
        });
    }

    @Override
    public List<OptionWidget> getButtons() {
        return buttons;
    }
}
