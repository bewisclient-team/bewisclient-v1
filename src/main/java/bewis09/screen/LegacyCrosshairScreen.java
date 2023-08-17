package bewis09.screen;

import bewis09.option.BooleanOption;
import bewis09.option.ColorOption;
import bewis09.option.Option;
import bewis09.util.FileReader;
import bewis09.util.OptionWidget;
import bewis09.util.TextHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.SliderWidget;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class LegacyCrosshairScreen extends AbstractOptionScreen {
    private final Screen parent;

    public boolean isClicked = false;

    public Option option = new ColorOption(TextHelper.getText("color"),"crosshairColor");

    public Option option2 = new BooleanOption(TextHelper.getText("custom_crosshair"),"custom_crosshair");

    public static int p = 9;

    public static int s = (int) (((float) FileReader.getByFirstIntFirst("Double","ScaleCrosshair",1/6f))*24+1);

    public static int[][] pixels = new int[p][p];

     static {
        if(new File(FabricLoader.getInstance().getGameDir() + "\\bewisclient\\crosshair.bof").exists()) {
            try (FileInputStream stream = new FileInputStream(FabricLoader.getInstance().getGameDir() + "\\bewisclient\\crosshair.bof")) {
                byte[] bytes = stream.readAllBytes();
                p = (int) Math.sqrt(bytes.length/3f);
                pixels = new int[p][p];
                for (int i = 0; i < p; i++) {
                    for (int j = 0; j < p; j++) {
                        int r = (bytes[(i*p+j)*3]+256)%256;
                        int g = (bytes[(i*p+j)*3+1]+256)%256;
                        int b = (bytes[(i*p+j)*3+2]+256)%256;
                        pixels[i][j] = r*0x10000+g*0x100+b;
                    }
                }
            } catch (Exception ignored) {}
        }
    }

    protected LegacyCrosshairScreen(Screen parent) {
        this.parent = parent;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        boolean b = super.mouseScrolled(mouseX, mouseY, amount);
        clearAndInit();
        return b;
    }

    public void savePixels() {
        try (FileOutputStream outputStream = new FileOutputStream(FabricLoader.getInstance().getGameDir() + "\\bewisclient\\crosshair.bof")) {
            new File(FabricLoader.getInstance().getGameDir() + "\\bewisclient\\crosshair.bof").createNewFile();
            byte[] b = new byte[p*3*p];
            int z = 0;
            for (int i = 0; i < p; i++) {
                for (int j = 0; j < p; j++) {
                    b[z] = (byte) (pixels[i][j]>>16);
                    b[z+1] = (byte) ((pixels[i][j]-(b[z]*0x10000))>>8);
                    b[z+2] = (byte) (pixels[i][j] - b[z] * 0x10000 - b[z + 1] * 0x100);
                    z+=3;
                }
            }
            outputStream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        drawBackground(context);
        context.fill(width/2-101,9-getScroll(),width/2-101+202,9+202-getScroll(),0xFF000000);
        context.drawBorder(width/2-101,9-getScroll(),202,202,-1);
        for (int d = 0; d < p; d++) {
            for (int j = 0; j < p; j++) {
                int i = pixels[d][j];
                context.fill((int) (width / 2 - 100 + (d * (200f / p))), (int) (10+ (j * (200f / p))-getScroll()), (int) (width / 2 - 100 + ((d + 1) * (200f / p))), (int) (10 + ((j+1) * (200f / p))-getScroll()), 0xFF000000+i);
            }
        }
        if(isClicked &&
                (int)(width / 2f - 100) < mouseX &&
                (10-getScroll()) < mouseY &&
                (int)(width / 2f - 100 + ((p + 1) * (200f / p))) > mouseX &&
                (int)(10 + ((p+1) * (200f / p))-getScroll()) > mouseY) {
            try {
                pixels[(int) ((mouseX - (width / 2f - 100)) / 200f * p)][(int) ((mouseY - 10f + getScroll()) / 200 * p)] = 0xFF000000 + ColorOption.getColor("crosshairColor");
                savePixels();
            } catch (Exception ignored) {}
        }
        option.render(context,width/2-101,234-getScroll(),mouseX,mouseY,202);
        option2.render(context,width/2-101,260-getScroll(),mouseX,mouseY,202);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    protected void init() {
        correctScroll();
        this.addDrawableChild(new SliderWidget(width/2-101,213-getScroll(),99,20, TextHelper.getText("resolution").append(": ").append(String.valueOf(p)), p / 25f){
            @Override
            protected void updateMessage() {
                this.setMessage(TextHelper.getText("resolution").append(": ").append(String.valueOf((int) (value*24+1))));
            }

            @Override
            protected void applyValue() {
                pixels = new int[(int) (value*24+1)][(int) (value*99+1)];
                p = (int) (value*24+1);
            }
        });
        this.addDrawableChild(new SliderWidget(width/2+2,213-getScroll(),99,20, TextHelper.getText("scale").append(": ").append(String.valueOf(s)),(s-1)/24f){
            @Override
            protected void updateMessage() {
                this.setMessage(TextHelper.getText("scale").append(": ").append(String.valueOf((int) (value*24+1))));
            }

            @Override
            protected void applyValue() {
                s = (int) (value*24+1);
                FileReader.setByFirst("Double","ScaleCrosshair",value);
            }
        });
        for (OptionWidget widget : option.getButtons()) {
            this.addDrawableChild((Element & Drawable & Selectable) widget);
            widget.setY(234-getScroll()+ widget.getYOffset());
            widget.setX(width/2+101-widget.getXOffset());
        }
        for (OptionWidget widget : option2.getButtons()) {
            this.addDrawableChild((Element & Drawable & Selectable) widget);
            widget.setY(260-getScroll()+ widget.getYOffset());
            widget.setX(width/2+101-widget.getXOffset());
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean bl = super.mouseClicked(mouseX, mouseY, button);
        if (button==0 && !bl)
            isClicked = true;
        return bl;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button==0)
            isClicked = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(parent);
    }

    @Override
    public int maxHeight() {
        return 260;
    }
}
