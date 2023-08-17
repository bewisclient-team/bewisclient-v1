package bewis09.util;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class TextHelper {
    public static MutableText getText(String option) {
        return Text.translatable("bewis.option." + option);
    }
}
