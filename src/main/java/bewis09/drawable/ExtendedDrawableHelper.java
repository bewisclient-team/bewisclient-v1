package bewis09.drawable;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class ExtendedDrawableHelper extends DrawableHelper {

    static Identifier widgets = new Identifier("bewisclient","gui/screen/widgets.png");
    static Identifier render = new Identifier("bewisclient","gui/screen/render.png");
    static Identifier mods = new Identifier("bewisclient","gui/screen/mods.png");

    public static void fillRound(MatrixStack matrixStack, int x, int y, int width, int height, int cornerWidth, int color, int edgeColor) {
        x*=10;
        y*=10;
        width*=10;
        height*=10;
        cornerWidth*=10;
        matrixStack.push();
        matrixStack.scale(0.1f,0.1f,0.1f);

        //corner

        fill(matrixStack,x+cornerWidth,y,x+width-cornerWidth,y+height,edgeColor);
        fill(matrixStack,x,y+cornerWidth,x+cornerWidth,y+height-cornerWidth, edgeColor);
        fill(matrixStack,x+width-cornerWidth,y+cornerWidth,x+width,y+height-cornerWidth, edgeColor);

        for (int i = 0; i < cornerWidth; i++) {
            fill(matrixStack,x+cornerWidth-i-1,y+cornerWidth-(int)(Math.cos(Math.asin(i/(float)cornerWidth))*cornerWidth),x+cornerWidth-i,y+cornerWidth,edgeColor);
            fill(matrixStack,x+cornerWidth-i-1,y+height-cornerWidth,x+cornerWidth-i,y+height+(int)(Math.cos(Math.asin(i/(float)cornerWidth))*cornerWidth)-cornerWidth,edgeColor);
            int x1 = x + width - cornerWidth + i;
            int x2 = x + width - cornerWidth + i + 1;
            fill(matrixStack, x1,y+cornerWidth-(int)(Math.cos(Math.asin(i/(float)cornerWidth))*cornerWidth), x2,y+cornerWidth,edgeColor);
            fill(matrixStack, x1,y+height-cornerWidth,x2,y+height+(int)(Math.cos(Math.asin(i/(float)cornerWidth))*cornerWidth)-cornerWidth,edgeColor);
        }

        //fill

        x+=20;
        y+=20;
        width-=40;
        height-=40;
        cornerWidth-=20;

        fill(matrixStack,x+cornerWidth,y,x+width-cornerWidth,y+height,color);
        fill(matrixStack,x,y+cornerWidth,x+cornerWidth,y+height-cornerWidth, color);
        fill(matrixStack,x+width-cornerWidth,y+cornerWidth,x+width,y+height-cornerWidth, color);

        for (int i = 0; i < cornerWidth; i++) {
            fill(matrixStack,x+cornerWidth-i-1,y+cornerWidth-(int)(Math.cos(Math.asin(i/(float)cornerWidth))*cornerWidth),x+cornerWidth-i,y+cornerWidth,color);
            fill(matrixStack,x+cornerWidth-i-1,y+height-cornerWidth,x+cornerWidth-i,y+height+(int)(Math.cos(Math.asin(i/(float)cornerWidth))*cornerWidth)-cornerWidth,color);
            int x1 = x + width - cornerWidth + i;
            int x2 = x + width - cornerWidth + i + 1;
            fill(matrixStack, x1,y+cornerWidth-(int)(Math.cos(Math.asin(i/(float)cornerWidth))*cornerWidth), x2,y+cornerWidth,color);
            fill(matrixStack, x1,y+height-cornerWidth,x2,y+height+(int)(Math.cos(Math.asin(i/(float)cornerWidth))*cornerWidth)-cornerWidth,color);
        }
        matrixStack.pop();
    }

    public static void drawBewisOptionOverlay(MatrixStack matrixStack, int width, int height, double size1, double size2, double size3, double size4, float roundMulti) {
        width*=roundMulti;
        height*=roundMulti;
        matrixStack.push();
        matrixStack.scale(1f/roundMulti,1f/roundMulti,1f/roundMulti);
        int size = (int) (Math.min(width,height)*0.7f/2);
        int sizebefore = size;
        size*=size1;
        for (int i = 0; i < size; i++) {
            fill(matrixStack, (int) (width/2-i-(roundMulti*5)), (int) ((int) (height/2-Math.cos(Math.asin(i/(float)(size)))*size)-(roundMulti*5)), (int) (width/2-i+1-(roundMulti*5)), (int) (height/2-(roundMulti*5)), 0x64646464);
        }
        size = sizebefore;
        size*=size2;
        for (int i = 0; i < size; i++) {
            fill(matrixStack, (int) (width/2-i-(roundMulti*5)), (int) (height /2+(roundMulti*5)), (int) (width/2-i+1-(roundMulti*5)), (int) ((height/2+Math.cos(Math.asin(i/(float)(size)))*size)+(roundMulti*5)), 0x64646464);
        }
        size = sizebefore;
        size*=size3;
        for (int i = 0; i < size; i++) {
            fill(matrixStack, (int) (width/2+i+(roundMulti*5)), (int) ((int) (height/2-Math.cos(Math.asin(i/(float)(size)))*size)-(roundMulti*5)), (int) (width/2+i+1+(roundMulti*5)), (int) (height/2-(roundMulti*5)), 0x64646464);
        }
        size = sizebefore;
        size*=size4;
        for (int i = 0; i < size; i++) {
            fill(matrixStack, (int) (width/2+i+(roundMulti*5)), (int) (height/2+(roundMulti*5)), (int) (width/2+i+1+(roundMulti*5)), (int) ((int) (height/2+Math.cos(Math.asin(i/(float)(size)))*size)+(roundMulti*5)), 0x64646464);
        }
        size = (int) (sizebefore*0.95f);
        sizebefore = size;
        size*=size1;
        for (int i = 0; i < size; i++) {
            fill(matrixStack, (int) (width/2-i-(roundMulti*5)), (int) ((int) (height/2-Math.cos(Math.asin(i/(float)(size)))*size)-(roundMulti*5)), (int) (width/2-i+1-(roundMulti*5)), (int) (height/2-(roundMulti*5)), 0x25AAAAAA);
        }
        size = sizebefore;
        size*=size2;
        for (int i = 0; i < size; i++) {
            fill(matrixStack, (int) (width/2-i-(roundMulti*5)), (int) (height /2+(roundMulti*5)), (int) (width/2-i+1-(roundMulti*5)), (int) ((height/2+Math.cos(Math.asin(i/(float)(size)))*size)+(roundMulti*5)), 0x25AAAAAA);
        }
        size = sizebefore;
        size*=size3;
        for (int i = 0; i < size; i++) {
            fill(matrixStack, (int) (width/2+i+(roundMulti*5)), (int) ((int) (height/2-Math.cos(Math.asin(i/(float)(size)))*size)-(roundMulti*5)), (int) (width/2+i+1+(roundMulti*5)), (int) (height/2-(roundMulti*5)), 0x25AAAAAA);
        }
        size = sizebefore;
        size*=size4;
        for (int i = 0; i < size; i++) {
            fill(matrixStack, (int) (width/2+i+(roundMulti*5)), (int) (height/2+(roundMulti*5)), (int) (width/2+i+1+(roundMulti*5)), (int) ((int) (height/2+Math.cos(Math.asin(i/(float)(size)))*size)+(roundMulti*5)), 0x25AAAAAA);
        }
        size=sizebefore;
        size /= roundMulti*2;
        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture(0,widgets);
        drawTexture(matrixStack, (int) Math.round(width/2f-size*(7/5f*roundMulti)*size1),(int) (height/2f-size*(7/5f*roundMulti)*size1),0,0,(int) (size*(6/5f*roundMulti)*size1),(int) (size*(6/5f*roundMulti)*size1),(int) (size*(6/5f*roundMulti)*size1),(int) (size*(6/5f*roundMulti)*size1));
        RenderSystem.setShaderTexture(0,render);
        drawTexture(matrixStack, (int) (Math.round(width/2f+size*(7/5f*roundMulti)*size3)-((int) (size*(6/5f*roundMulti)*size3))),(int) (height/2f-size*(7/5f*roundMulti)*size3),0,0,(int) (size*(6/5f*roundMulti)*size3),(int) (size*(6/5f*roundMulti)*size3),(int) (size*(6/5f*roundMulti)*size3),(int) (size*(6/5f*roundMulti)*size3));
        RenderSystem.setShaderTexture(0,mods);
        drawTexture(matrixStack, (int) Math.round(width/2f-size*(7/5f*roundMulti)*size2),(int) (height/2f+size*(1/5f*roundMulti)*size2),0,0,(int) (size*(6/5f*roundMulti)*size2),(int) (size*(6/5f*roundMulti)*size2),(int) (size*(6/5f*roundMulti)*size2),(int) (size*(6/5f*roundMulti)*size2));
        RenderSystem.disableBlend();
        matrixStack.pop();
    }
}
