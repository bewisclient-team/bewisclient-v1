package bewis09.hud;

import bewis09.drawable.OptionButtonWidget;
import bewis09.drawable.OptionSliderWidget;
import bewis09.mixin.BossBarHudMixin;
import bewis09.option.BooleanOption;
import bewis09.option.Option;
import bewis09.option.SliderOption;
import bewis09.util.TextHelper;
import bewis09.screen.WidgetScreen;
import bewis09.util.FileReader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public abstract class HudElement {
    private int x;
    private int y;
    Horizontal horizontal;
    Vertical vertical;
    private final int width;
    private final int height;
    private boolean visible = isNormalVisible();
    public double alpha;
    private final double size = getDefSize();

    public HudElement(int x, int y, Horizontal horizontal, Vertical vertical, int width, int height) {
        this.x = FileReader.getX(getId())==-4269?x:FileReader.getX(getId());
        this.y = FileReader.getY(getId())==-4269?y:FileReader.getY(getId());
        this.horizontal = FileReader.getHorizontal(getId())==null?horizontal:FileReader.getHorizontal(getId());
        this.vertical = FileReader.getVertical(getId())==null?vertical:FileReader.getVertical(getId());
        this.width = width;
        this.height = height;
        this.alpha = FileReader.getByFirstIntFirst("Double","Alpha_"+getId(), (float) 0x96 / 0xFF);
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

    public int getColor() {
        return getColorWithColor(0x0a0a0a);
    }

    public int getColorWithColor(int color) {
        return ((int)(alpha*0xFF))*0x1000000+color;
    }

    public abstract void paint(DrawContext context);
    
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

    public final float getSize() {
        return sizeCustom() ? (float) size : getDefSize();
    };

    public boolean sizeCustom() {
        return false;
    }

    public int getY() {
        int screenHeight = (int) (MinecraftClient.getInstance().getWindow().getScaledHeight()*(1/getSize()));
        return vertical==Vertical.TOP?y+(MinecraftClient.getInstance().currentScreen instanceof WidgetScreen || horizontal != Horizontal.CENTER ? 0 : (((BossBarHudMixin)MinecraftClient.getInstance().inGameHud.getBossBarHud()).getBossBars().size()*19)):vertical==Vertical.BOTTOM? screenHeight-getHeight()-y:screenHeight/2-getHeight()/2+y;
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

    public List<Option> getOptions() {
        ArrayList<Option> list = new ArrayList<>();
        list.add(new SliderOption(TextHelper.getText("alpha"),"Alpha_"+getId(),1, (float) 0x96 / 0xFF,0,2){
            @Override
            public void clickExtra(double value) {
                alpha = value;
                FileReader.setByFirst("Boolean","DefaultAlpha_"+getId(),false);
                ((BooleanOption)list.get(1)).bl = false;
                ((OptionButtonWidget)list.get(1).getButtons().get(0)).setMessage(Text.of("FALSE"));
            }
        });
        list.add(new BooleanOption(TextHelper.getText("default_alpha"),"DefaultAlpha_"+getId()){
            @Override
            public void clickExtra(boolean enabled) {
                if(enabled) {
                    alpha = 0x96/(float)0xFF;
                    FileReader.setByFirst("Double","Alpha_"+getId(),0x96/(float)0xFF);
                    ((OptionSliderWidget)list.get(0).getButtons().get(0)).setValue(alpha);
                    (this).bl = true;
                    ((OptionButtonWidget)this.getButtons().get(0)).setMessage(Text.of("TRUE"));
                    FileReader.setByFirst("Boolean", "DefaultAlpha_"+getId(), bl);
                }
            }
        });
        return list;
    }

    public abstract float getDefSize();

    public enum Horizontal {
        LEFT, RIGHT, CENTER
    }

    public enum Vertical {
        TOP, BOTTOM, CENTER
    }
}
