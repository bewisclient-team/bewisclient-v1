package bewis09.screen;

import bewis09.mixin.DrawContextMixin;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import org.joml.Matrix4f;

public abstract class AbstractOptionScreen extends Screen {

    double scroll = 0;

    public AbstractOptionScreen() {
        super(Text.empty());
    }

    public void drawBackground(DrawContext context) {
        context.enableScissor(0,0,width/2-150,height);
        renderBackgroundTexture(context);
        context.disableScissor();
        context.enableScissor(width/2+150,0,width,height);
        renderBackgroundTexture(context);
        context.disableScissor();
        assert client != null;
        if(client.world == null) {
            context.setShaderColor(0.125F, 0.125F, 0.125F, 1.0F);
            context.drawTexture(Screen.OPTIONS_BACKGROUND_TEXTURE, width / 2 - 150, (int) -scroll, (float) 0, (float) 0, 300, (int) (height+scroll), 32, 32);
            context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        } else {
            context.fill(width/2-150,0,width/2+150,height,0x88000000);
        }
        fillGradient(context,width/2-150,0,width/2-146,height,1,0xFF000000,0);
        fillGradient(context,width/2+146,0,width/2+150,height,1,0,0xFF000000);
    }

    public void fillGradient(DrawContext context, int startX, int startY, int endX, int endY, int z, int colorStart, int colorEnd) {
        float f = (float) ColorHelper.Argb.getAlpha(colorStart) / 255.0F;
        float g = (float) ColorHelper.Argb.getRed(colorStart) / 255.0F;
        float h = (float) ColorHelper.Argb.getGreen(colorStart) / 255.0F;
        float i = (float) ColorHelper.Argb.getBlue(colorStart) / 255.0F;
        float j = (float) ColorHelper.Argb.getAlpha(colorEnd) / 255.0F;
        float k = (float) ColorHelper.Argb.getRed(colorEnd) / 255.0F;
        float l = (float) ColorHelper.Argb.getGreen(colorEnd) / 255.0F;
        float m = (float) ColorHelper.Argb.getBlue(colorEnd) / 255.0F;
        VertexConsumer vertexConsumer = ((DrawContextMixin)context).getVertexConsumers().getBuffer(RenderLayer.getGui());
        Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
        vertexConsumer.vertex(matrix4f, (float)startX, (float)startY, (float)z).color(g, h, i, f).next();
        vertexConsumer.vertex(matrix4f, (float)startX, (float)endY, (float)z).color(g, h, i, f).next();
        vertexConsumer.vertex(matrix4f, (float)endX, (float)endY, (float)z).color(k, l, m, j).next();
        vertexConsumer.vertex(matrix4f, (float)endX, (float)startY, (float)z).color(k, l, m, j).next();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        scroll-=amount*15;
        correctScroll();
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    public void correctScroll() {
        scroll=Math.min(maxHeight()-height,scroll);
        scroll=Math.max(0,scroll);
    }

    public abstract int maxHeight();

    public int getScroll() {
        return (int) scroll;
    }
}
