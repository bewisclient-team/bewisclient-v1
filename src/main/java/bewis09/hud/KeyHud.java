package bewis09.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.math.MatrixStack;

public class KeyHud extends HudElement {
    public KeyHud() {
        super(5,5,Horizontal.LEFT,Vertical.TOP,64,42);
    }

    @Override
    public String getId() {
        return "KEY";
    }

    @Override
    public void paint(MatrixStack stack) {
        stack.push();
        stack.scale(getSize(),getSize(),getSize());
        GameOptions gameOptions = MinecraftClient.getInstance().options;
        fill(stack,getX()+22,getY(),getX()+42,getY()+20,gameOptions.forwardKey.isPressed() ? 0x967a7a7a : 0x960a0a0a);
        fill(stack,getX(),getY()+22,getX()+20,getY()+42,gameOptions.leftKey.isPressed() ? 0x967a7a7a : 0x960a0a0a);
        fill(stack,getX()+22,getY()+22,getX()+42,getY()+42,gameOptions.backKey.isPressed() ? 0x967a7a7a : 0x960a0a0a);
        fill(stack,getX()+44,getY()+22,getX()+64,getY()+42,gameOptions.rightKey.isPressed() ? 0x967a7a7a : 0x960a0a0a);
        stack.pop();
        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        drawCenteredTextWithShadow(stack, renderer, "W", (getX()+32), (getY()+4+3), -1);
        drawCenteredTextWithShadow(stack, renderer, "A", (getX()+10), (getY()+26+3), -1);
        drawCenteredTextWithShadow(stack, renderer, "S", (getX()+32), (getY()+26+3), -1);
        drawCenteredTextWithShadow(stack, renderer, "D", (getX()+54), (getY()+26+3), -1);
    }

    @Override
    public float getSize() {
        return 1f;
    }
}