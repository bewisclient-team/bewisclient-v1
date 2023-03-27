package bewis09.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class StartAnimationScreen extends Screen {

    double tick = 0;

    public static Identifier grass = new Identifier("bewisclient","gui/startanimation/grass.png");
    public static Identifier pig = new Identifier("bewisclient","gui/startanimation/pig.png");
    public static Identifier logo = new Identifier("bewisclient","gui/logo_long.png");
    public static Identifier background = new Identifier("textures/block/green_wool.png");

    public StartAnimationScreen() {
        super(Text.of(""));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        for (int i = 31; i < 40; i++) {
            if(Math.min(300,Math.max((i+1)*100-30*tick,0))!=300) {
                RenderSystem.setShaderTexture(0, grass);
                drawTexture(matrices, width / 10 * (i - 31) + width / 10-width/18, (int) (height - width / 10 - Math.min(300, Math.max((i + 1) * 100 - 30 * tick, 0)) - width / 20), (width / 9), (int) (width / 9), 0, 0, 160, 144, 160, 144);
            }
        }
        for (int i = 21; i < 31; i++) {
            if(Math.min(300,Math.max((i+1)*100-30*tick,0))!=300) {
                RenderSystem.setShaderTexture(0, grass);
                drawTexture(matrices, width / 10 * (i - 21) + width / 20-width/18, (int) (height - width / 10 - Math.min(300, Math.max((i + 1) * 100 - 30 * tick, 0)) - width / 40), (width / 9), (int) (width / 9), 0, 0, 160, 144, 160, 144);
            }
        }
        for (int i = 10; i < 21; i++) {
            if (Math.min(300, Math.max((i + 1) * 100 - 30 * tick, 0)) != 300) {
                RenderSystem.setShaderTexture(0, grass);
                drawTexture(matrices, width / 10 * (i-10)-width/18, (int) (height - width / 10 - Math.min(300, Math.max((i + 1) * 100 - 30 * tick, 0))), (width / 9), (int) (width / 9), 0, 0, 160, 144, 160, 144);
            }
        }
        for (int i = 0; i < 10; i++) {
            if(Math.min(300,Math.max((i+1)*100-30*tick,0))!=300) {
                RenderSystem.setShaderTexture(0, grass);
                drawTexture(matrices, width / 10 * (i) + width / 20-width/18, (int) (height - width / 10 - Math.min(300, Math.max((i + 1) * 100 - 30 * tick, 0)) + width / 40), (width / 9), (int) (width / 9), 0, 0, 160, 144, 160, 144);
            }
        }
        if(tick>120) {
            RenderSystem.setShaderTexture(0,pig);
            RenderSystem.setShaderColor(1F, 1F, 1F, (float) Math.min(1,(tick-120)/10f));
            RenderSystem.enableBlend();
            drawTexture(matrices,width/10,height-width/5,width/8, width/8,0,0, 350,324, 350,324);
        }
        if(tick>140) {
            RenderSystem.setShaderTexture(0,logo);
            RenderSystem.setShaderColor(1F, 1F, 1F, (float) Math.min(1,(tick-140)/20f));
            RenderSystem.enableBlend();
            drawTexture(matrices,width/6,(int) (height/2-height*(1/3f)+(1/5.5)),(int) (width*(2/3f)), (int) (width*(2/3f)*(1/5.5)),0,0, 954,172,954,172);
        }
        RenderSystem.setShaderColor(1,1,1,1);
    }

    @Override
    public void tick() {
        assert client != null;
        if(client.getOverlay()==null)
            tick+=1.8;
    }

    @Override
    public void renderBackgroundTexture(MatrixStack matrices) {
        RenderSystem.setShaderTexture(0, background);
        RenderSystem.setShaderColor(0.25F, 0.25F, 0.25F, 1.0F);
        drawTexture(matrices, 0, 0, 0, 0.0F, 0.0F, this.width, this.height, 32, 32);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
