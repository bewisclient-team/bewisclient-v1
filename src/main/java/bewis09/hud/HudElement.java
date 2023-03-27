package bewis09.hud;

import bewis09.util.FileReader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

public abstract class HudElement extends DrawableHelper {
    private int x;
    private int y;
    Horizontal horizontal;
    Vertical vertical;
    private final int width;
    private final int height;
    private boolean visible = isNormalVisible();

    public HudElement(int x, int y, Horizontal horizontal, Vertical vertical, int width, int height) {
        this.x = FileReader.getX(getId())==-4269?x:FileReader.getX(getId());
        this.y = FileReader.getY(getId())==-4269?y:FileReader.getY(getId());
        this.horizontal = FileReader.getHorizontal(getId())==null?horizontal:FileReader.getHorizontal(getId());
        this.vertical = FileReader.getVertical(getId())==null?vertical:FileReader.getVertical(getId());
        this.width = width;
        this.height = height;
    }

    public abstract String getId();

    public void setX(int x) {
        this.x=x;
     }

    public void setY(int y) {
        this.y=y;
    }

    public int getNormalX() {
        return x;
    }

    public int getNormalY() {
        return y;
    }

    public Horizontal getHorizontal() {
        return horizontal;
    }

    public Vertical getVertical() {
        return vertical;
    }

    public abstract void paint(MatrixStack matrixStack);
    
    public void setHorizontal(Horizontal horizontal) {
        this.horizontal=horizontal;
    }

    public void setVertical(Vertical vertical) {
        this.vertical=vertical;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public abstract float getSize();

    public int getY() {
        int screenHeight = (int) (MinecraftClient.getInstance().getWindow().getScaledHeight()*(1/getSize()));
        return vertical==Vertical.TOP?y:vertical==Vertical.BOTTOM? screenHeight-getHeight()-y:screenHeight/2-getHeight()/2+y;
    }

    public int getX() {
        int screenWidth = (int) (MinecraftClient.getInstance().getWindow().getScaledWidth()*(1/getSize()));
        return horizontal==Horizontal.LEFT?x:horizontal==Horizontal.RIGHT? screenWidth-getWidth()-x:screenWidth/2-getWidth()/2+x;
    }

    private boolean isNormalVisible() {
        try {
            return Boolean.parseBoolean(FileReader.getByFirst("Widget", getId())[4]);
        } catch (Exception e) {
            return true;
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public enum Horizontal {
        LEFT, RIGHT, CENTER
    }

    public enum Vertical {
        TOP, BOTTOM, CENTER
    }
}
