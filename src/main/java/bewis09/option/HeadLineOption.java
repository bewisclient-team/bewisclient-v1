package bewis09.option;

import net.minecraft.text.Text;

public class HeadLineOption extends TextOption {

    boolean open = false;

    public HeadLineOption(Text text) {
        super(text, true);
    }

    public Boolean isOpen() {
        return open;
    }

    public void toggleOpen() {
        open = !open;
    }
}
