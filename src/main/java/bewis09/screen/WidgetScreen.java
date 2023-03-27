package bewis09.screen;

import bewis09.hud.HudElement;
import bewis09.hud.Huds;
import bewis09.util.FileReader;
import bewis09.util.InGameHudImplementer;
import bewis09.util.MathUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class WidgetScreen extends Screen {

    boolean mousePressed = false;
    int mouseStartX = -1;
    int mouseStartY = -1;
    int eleX = -1;
    int eleY = -1;
    HudElement element = null;

    protected WidgetScreen() {
        super(Text.of("Widgets"));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        ((InGameHudImplementer) MinecraftClient.getInstance().inGameHud).renderWidgets(matrices, true);
        for (HudElement element : Huds.getList()) {
            RenderSystem.setShaderTexture(0,new Identifier("bewisclient",element.isVisible() ? "gui/screen/visible.png" : "gui/screen/invisible.png"));
            drawTexture(matrices, (int) (element.getX()*element.getSize()),(int)(element.getY()*element.getSize()), 8,8,0,0,8,8,8,8);
        }
        int outsideOffset = (int) Math.round(FileReader.getByFirstIntFirst("Double","outsideOffset",5));
        if(mousePressed) {
            if(element!=null) {
                int o1 = outsideOffset;
                double sizeMulti = (1/element.getSize());
                outsideOffset *= sizeMulti;
                element.setX((int) Math.min(
                        element.getHorizontal()== HudElement.Horizontal.CENTER?
                                width*sizeMulti/2f-element.getWidth()/2f-outsideOffset:
                                (width * sizeMulti - element.getWidth())-outsideOffset,
                        Math.max(element.getHorizontal()== HudElement.Horizontal.CENTER?
                                (-width*sizeMulti)/2+element.getWidth()/2f+outsideOffset:
                                        outsideOffset,
                                (int) (eleX + (element.getHorizontal()== HudElement.Horizontal.RIGHT?-1:1)*(mouseX - mouseStartX)*sizeMulti))));
                element.setY((int) Math.min(element.getVertical()== HudElement.Vertical.CENTER?(-element.getHeight()/2f)+height*sizeMulti/2-outsideOffset:(height*sizeMulti-element.getHeight())-outsideOffset, Math.max(element.getVertical()== HudElement.Vertical.CENTER?(-(height*sizeMulti)/2+element.getHeight()/2f)+outsideOffset:outsideOffset,(int) (eleY + (element.getVertical()== HudElement.Vertical.BOTTOM?-1:1)*(mouseY - mouseStartY)*sizeMulti))));

                element.setX((int) MathUtil.inRangeThen(element.getNormalX(), (element.getHorizontal() == HudElement.Horizontal.CENTER) ? 0 : ((width*sizeMulti-element.getWidth()) / 2f),5));
                element.setY((int) MathUtil.inRangeThen(element.getNormalY(), (element.getVertical() == HudElement.Vertical.CENTER) ? 0 : ((height*sizeMulti-element.getHeight()) / 2f),5));
                element.paint(matrices);
                for (HudElement element : Huds.getList()) {
                    RenderSystem.setShaderTexture(0,new Identifier("bewisclient",element.isVisible() ? "gui/screen/visible.png" : "gui/screen/invisible.png"));
                    drawTexture(matrices, (int) (element.getX()*element.getSize()),(int)(element.getY()*element.getSize()), 8,8,0,0,8,8,8,8);
                }
                outsideOffset=o1;
            }
        } else {
            for (HudElement element : Huds.getList()) {
                if (mouseX > element.getX() * element.getSize() && mouseX < (element.getX() + element.getWidth()) * element.getSize()) {
                    if (mouseY > element.getY() * element.getSize() && mouseY < (element.getY() + element.getHeight()) * element.getSize()) {
                        renderTooltip(matrices,Text.of(element.getId().toLowerCase().replaceFirst(".",String.valueOf(element.getId().charAt(0)).toUpperCase())),mouseX,mouseY);
                        break;
                    }
                }
            }
        }
        drawVerticalLine(matrices,outsideOffset-1,outsideOffset-2,height-outsideOffset+1,-1);
        drawVerticalLine(matrices,width-outsideOffset,outsideOffset-1,height-outsideOffset,-1);
        drawHorizontalLine(matrices,outsideOffset,width-outsideOffset,outsideOffset-1,-1);
        drawHorizontalLine(matrices,outsideOffset,width-outsideOffset,height-outsideOffset,-1);
        drawHorizontalLine(matrices,outsideOffset,width-outsideOffset,height/3,0xAA555555);
        drawHorizontalLine(matrices,outsideOffset,width-outsideOffset,height/3*2,0xAA555555);
        drawVerticalLine(matrices,width/3,outsideOffset,height-outsideOffset,0xAA555555);
        drawVerticalLine(matrices,width/3*2,outsideOffset,height-outsideOffset,0xAA555555);
        drawHorizontalLine(matrices,outsideOffset,width-outsideOffset,height/2,0xAACC5555);
        drawVerticalLine(matrices,width/2,outsideOffset,height-outsideOffset,0xAACC5555);
        super.render(matrices, mouseX, mouseY, delta);
        fill(matrices,width/2-50,height-27,width/2+50,height-10,0x77777777);
        drawCenteredTextWithShadow(matrices,textRenderer,"OutsideOffset: "+outsideOffset,width/2,height-22,-1);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        block: {
            if (button == 0) {
                mousePressed = true;
                mouseStartX = (int) mouseX;
                mouseStartY = (int) mouseY;
                boolean bl = false;
                for (HudElement element : Huds.getList()) {
                    if(mouseStartX > element.getX() * element.getSize() && mouseStartX < (element.getX() + 8) * element.getSize())
                        if (mouseStartY > element.getY() * element.getSize() && mouseStartY < (element.getY() + 8) * element.getSize()) {
                            element.setVisible(!element.isVisible());
                            FileReader.setByFirst("Widget",element.getId(),element.getNormalX(),element.getNormalY(),element.getHorizontal(),element.getVertical(),element.isVisible());
                            this.element = null;
                            break block;
                        }
                    if (mouseStartX > element.getX() * element.getSize() && mouseStartX < (element.getX() + element.getWidth()) * element.getSize())
                        if (mouseStartY > element.getY() * element.getSize() && mouseStartY < (element.getY() + element.getHeight()) * element.getSize()) {
                            this.element = element;
                            bl = true;
                            break;
                        }
                }
                if (!bl) {
                    element = null;
                    break block;
                }
                eleX = element.getNormalX();
                eleY = element.getNormalY();
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if(button==0&&element!=null) {
            mousePressed=false;
            if(element.getX()+element.getWidth()/2f<width/3f*(1/element.getSize())) {
                if(element.getHorizontal()== HudElement.Horizontal.RIGHT) {
                    element.setX((int) (width*(1/element.getSize())-element.getNormalX()-element.getWidth()));
                } else if(element.getHorizontal()== HudElement.Horizontal.CENTER) {
                    element.setX((int) ((width*(1/element.getSize()))/2+element.getNormalX()-element.getWidth()/2));
                }
                element.setHorizontal(HudElement.Horizontal.LEFT);
            } else if(element.getX()+element.getWidth()/2f<width/3f*2*(1/element.getSize())) {
                if(element.getHorizontal()== HudElement.Horizontal.RIGHT) {
                    element.setX((int) (element.getNormalX() -(width * (1 / element.getSize()) / 2) + element.getWidth() / 2)*-1);
                } else if(element.getHorizontal()== HudElement.Horizontal.LEFT) {
                    element.setX((int) ((width*(1/element.getSize())/2)-element.getNormalX()-(element.getWidth()/2))*-1);
                }
                element.setHorizontal(HudElement.Horizontal.CENTER);
            } else if(element.getX()+element.getWidth()/2f>=width/3f*2*(1/element.getSize())) {
                if(element.getHorizontal()== HudElement.Horizontal.LEFT) {
                    element.setX((int) (width*(1/element.getSize())-element.getNormalX()-element.getWidth()));
                } else if(element.getHorizontal()== HudElement.Horizontal.CENTER) {
                    element.setX((int) (width*(1/element.getSize())/2-element.getNormalX()-element.getWidth()/2));
                }
                element.setHorizontal(HudElement.Horizontal.RIGHT);
            }
            if(element.getY()+element.getHeight()/2f<height/3f*(1/element.getSize())) {
                if(element.getVertical()== HudElement.Vertical.BOTTOM) {
                    element.setY((int) (height*(1/element.getSize())-element.getNormalY()-element.getHeight()));
                } else if(element.getVertical()== HudElement.Vertical.CENTER) {
                    element.setY((int) ((height*(1/element.getSize()))/2+element.getNormalY()-element.getHeight()/2));
                }
                element.setVertical(HudElement.Vertical.TOP);
            } else if(element.getY()+element.getHeight()/2f<height/3f*2*(1/element.getSize())) {
                if(element.getVertical()== HudElement.Vertical.BOTTOM) {
                    element.setY((int) (element.getNormalY() -(height * (1 / element.getSize()) / 2) + element.getHeight() / 2)*-1);
                } else if(element.getVertical()== HudElement.Vertical.TOP) {
                    element.setY((int) ((height*(1/element.getSize())/2)-element.getNormalY()-(element.getHeight()/2))*-1);
                }
                element.setVertical(HudElement.Vertical.CENTER);
            } else if(element.getY()+element.getHeight()/2f>=height/3f*2*(1/element.getSize())) {
                if (element.getVertical() == HudElement.Vertical.TOP) {
                    element.setY((int) (height * (1 / element.getSize()) - element.getNormalY() - element.getHeight()));
                } else if (element.getVertical() == HudElement.Vertical.CENTER) {
                    element.setY((int) (height * (1 / element.getSize()) / 2 - element.getNormalY() - element.getHeight() / 2));
                }
                element.setVertical(HudElement.Vertical.BOTTOM);
            }
            FileReader.setByFirst("Widget",element.getId(),element.getNormalX(),element.getNormalY(),element.getHorizontal(),element.getVertical(),element.isVisible());
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        FileReader.setByFirst("Double","outsideOffset", Math.min(20,Math.max(0,FileReader.getByFirstIntFirst("Double","outsideOffset",5)+(amount))));
        return super.mouseScrolled(mouseX, mouseY, amount);
    }
}
