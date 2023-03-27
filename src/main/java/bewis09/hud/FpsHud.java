package bewis09.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.List;

public class FpsHud extends LineHudElement {
    public FpsHud() {
        super(7, new CoordinateHud().getY()+new CoordinateHud().getHeight()+2,Horizontal.RIGHT,Vertical.TOP,70,List.of(Text.of("")),true);
    }

    @Override
    public String getId() {
        return "FPS";
    }

    @Override
    public float getSize() {
        return 0.7f;
    }

    @Override
    public List<Text> getTexts() {
        return List.of(Text.of(MinecraftClient.getInstance().fpsDebugString.split(" ")[0]+" FPS"));
    }
}
