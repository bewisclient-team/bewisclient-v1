package bewis09.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.List;

public class CoordinateHud extends LineHudElement {
    public CoordinateHud() {
        super(7,7,Horizontal.RIGHT,Vertical.TOP,90, List.of(Text.of("X: "+ (MinecraftClient.getInstance().cameraEntity != null ? MinecraftClient.getInstance().cameraEntity.getX() : 0)),Text.of("Y: "+ (MinecraftClient.getInstance().cameraEntity != null ? MinecraftClient.getInstance().cameraEntity.getY() : 0)),Text.of("Z: "+ (MinecraftClient.getInstance().cameraEntity != null ? MinecraftClient.getInstance().cameraEntity.getZ() : 0))),false);
    }

    @Override
    public List<Text> getTexts() {
        assert MinecraftClient.getInstance().cameraEntity != null;
        return List.of(Text.of("X: "+ (int)MinecraftClient.getInstance().cameraEntity.getX()),Text.of("Y: "+ (int)MinecraftClient.getInstance().cameraEntity.getY()),Text.of("Z: "+ (int)MinecraftClient.getInstance().cameraEntity.getZ()));
    }

    @Override
    public String getId() {
        return "COORDINATES";
    }

    @Override
    public float getDefSize() {
        return 0.7f;
    }
}
