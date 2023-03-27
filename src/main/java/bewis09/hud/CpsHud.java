package bewis09.hud;

import bewis09.main.Main;
import net.minecraft.text.Text;

import java.util.List;

public class CpsHud extends LineHudElement {
    public CpsHud() {
        super(7, new DayHud().getHeight()+new DayHud().getY()+2, Horizontal.RIGHT, Vertical.TOP, 70, List.of(Text.of("")),true);
    }

    @Override
    public List<Text> getTexts() {
        return List.of(Text.of(Main.lCount()+" | "+Main.rCount()+" CPS"));
    }

    @Override
    public String getId() {
        return "CPS";
    }

    @Override
    public float getSize() {
        return 0.7f;
    }
}
