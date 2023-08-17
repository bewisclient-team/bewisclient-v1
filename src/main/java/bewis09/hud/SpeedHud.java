package bewis09.hud;

import bewis09.util.MathUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;

import java.util.List;

public class SpeedHud extends LineHudElement {
    public SpeedHud() {
        super(7,new SleepTimerHud().getY()+new SleepTimerHud().getHeight()+2, Horizontal.RIGHT, Vertical.TOP, 70, List.of(Text.of("")), true);
    }

    @Override
    public String getId() {
        return "SPEED";
    }

    @Override
    public float getDefSize() {
        return 0.7f;
    }

    @Override
    public List<Text> getTexts() {
        assert MinecraftClient.getInstance().cameraEntity != null;
        return List.of(Text.of(MathUtil.withAfterComma(understVehicle(MinecraftClient.getInstance().cameraEntity).getVelocity().horizontalLength() * 20, 2) +" m/s"));
    }

    public Entity understVehicle(Entity entity) {
        Entity entity1 = entity;
        while (entity1.getVehicle()!=null) {
            entity1=entity1.getVehicle();
        }
        return entity1;
    }
}
