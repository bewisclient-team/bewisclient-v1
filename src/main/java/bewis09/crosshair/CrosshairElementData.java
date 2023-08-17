package bewis09.crosshair;

import bewis09.option.Option;
import net.minecraft.text.Text;

import java.util.List;

public record CrosshairElementData(Text text, List<Option> list, Painter painter) {
    public interface Painter {

    }
}
