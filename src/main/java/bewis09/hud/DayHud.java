package bewis09.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.List;

public class DayHud extends LineHudElement {
    public DayHud() {
        super(7,new FpsHud().getY()+new FpsHud().getHeight()+2,Horizontal.RIGHT,Vertical.TOP,70, List.of(Text.of("")),true);
    }

    @Override
    public String getId() {
        return "DAY";
    }

    @Override
    public List<Text> getTexts() {
        assert MinecraftClient.getInstance().world != null;
        return List.of(Text.of("Day "+ ((int)MinecraftClient.getInstance().world.getTimeOfDay()/24000)));
    }

    @Override
    public float getDefSize() {
        return 0.7f;
    }
}
