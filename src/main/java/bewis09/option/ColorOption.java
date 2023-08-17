package bewis09.option;

import bewis09.drawable.OptionButtonWidget;
import bewis09.util.TextHelper;
import bewis09.screen.WidgetConfigScreen;
import bewis09.util.FileReader;
import bewis09.util.OptionWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

public class ColorOption extends TextOption {

    public List<OptionWidget> optionWidgets;

    public ColorOption(Text text, String str) {
        super(text, false);
        optionWidgets = List.of(new OptionButtonWidget(0, 0, 100, 20, () -> Text.of(Integer.toString(getColor(str), 16)), button -> {
            Screen screen = MinecraftClient.getInstance().currentScreen;
            MinecraftClient.getInstance().setScreen(new WidgetConfigScreen(List.of(
                    new SliderOption(TextHelper.getText("red"), "red_" + str, 255, 0),
                    new SliderOption(TextHelper.getText("green"), "green_" + str, 255, 0),
                    new SliderOption(TextHelper.getText("blue"), "blue_" + str, 255, 0),
                    new ColorDisplayOption(() -> getColor(str))), screen, str));
        }, 102, 2));
    }

    @Override
    public List<OptionWidget> getButtons() {
        return optionWidgets;
    }

    public static int getColor(String str) {
        return (((int)(FileReader.getByFirstIntFirst("Double","red_"+str,0)*255))<<16)+(((int)(FileReader.getByFirstIntFirst("Double","green_"+str,0)*255))<<8)+((int)(FileReader.getByFirstIntFirst("Double","blue_"+str,0)*255));
    }
}
