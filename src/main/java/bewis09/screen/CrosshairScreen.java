package bewis09.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;

import java.util.HashMap;
import java.util.Map;

public class CrosshairScreen extends AbstractOptionScreen {

    private final Screen parent;

    Map<String, CrosshairData> data = new HashMap<>();

    public CrosshairScreen(Screen parent) {
        this.parent = parent;
        data.put("Default",new CrosshairData(Shape.RECTANGLE,0,0,0,1,0,0));
        data.put("Better",new CrosshairData(Shape.RECTANGLE,0,0,0,1,0,0));
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(parent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        drawBackground(context);
        int i = -1;
        for (Map.Entry<String, CrosshairData> entry : data.entrySet()) {
            i++;
            context.fill(width/2-150,i*24+2,width/2+150,i*24+22,0xAA000000);
            context.drawTextWithShadow(textRenderer,entry.getKey(),width/2-145,i*24+8,-1);
        }
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public int maxHeight() {
        return 0;
    }

    public record CrosshairData(Shape shape, double spacing, double spacingDifferent, double spacingTime, double size, double sizeDifferent, double sizeTime) { }

    public enum Shape {
        RECTANGLE, CIRCLE, CROSS
    }
}
