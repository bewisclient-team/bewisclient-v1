package bewis09.screen;

import bewis09.drawable.ExtendedDrawableHelper;
import bewis09.option.*;
import bewis09.util.FileReader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.List;

public class LegacyOptionScreen extends Screen {

    double size1 = 0;
    double size2 = 0;
    double size3 = 0;
    double size4 = 0;

    public static List<Option> getOptions() {
        return List.of(
                new HeadLineOption(getText("visibility")),
                new BooleanOption(getText("lava"), "lava"),
                new BooleanOption(getText("water"), "water"),
                new BooleanOption(getText("nether"), "nether"),
                new BooleanOption(getText("powder_snow"), "powder_snow"),
                new BooleanOption(getText("nofog"), "nofog"),
                new BooleanOption(getText("sky_fog"), "sky_fog"),
                new SliderOption(getText("lava_view"), "lava_view", 1, 0.5f),
                new HeadLineOption(getText("other")),
                new SliderOption(getText("fire_height"), "fire_height", 1, 1),
                new BooleanOption(getText("shulkerbox_tooltip"),"shulkerbox_tooltip"),
                new TextFieldOption(getText("backgroundtexture"),"backgroundtexture",""),
                new HeadLineOption(getText("pumpkin_overlay")),
                new BooleanOption(getText("disable_pumpkin_overlay"), "disable_pumpkin_overlay"),
                new BooleanOption(getText("show_pumpkin_icon"), "show_pumpkin_icon", true),
                new HeadLineOption(getText("helditeminfo")),
                new BooleanOption(getText("helditeminfo"), "helditeminfo", true),
                new SliderOption(getText("maxinfolength"), "maxinfolength", 10, 0.55, 1, 0),
                new HeadLineOption(getText("fullbright")),
                new BooleanOption(getText("fullbright"),"fullbright"){
                    @Override
                    public void clickExtra(boolean enabled) {
                        MinecraftClient.getInstance().options.getGamma().setValue(enabled ? FileReader.getByFirstIntFirst("Double","fullbrightvalue",0.1)*10 : 1);
                    }
                },
                new SliderOption(getText("fullbrightvalue"), "fullbrightvalue", 10, 0.1, 0, 2){
                    @Override
                    public void clickExtra(double value) {
                        MinecraftClient.getInstance().options.getGamma().setValue(value * 10);
                    }
                },
                new BooleanOption(getText("night_vision"),"night_vision"),
                new HeadLineOption(getText("customblockhit")),
                new BooleanOption(getText("customblockhit"),"customblockhit"),
                new BooleanOption(getText("blockhitrgb"),"blockhitrgb"),
                new SliderOption(getText("blockhitalpha"),"blockhitalpha",1,0.4,0),
                new TextFieldOption(getText("blockhitcolor"),"blockhitcolor","000000"),
                new SliderOption(getText("hitrgbspeed"),"hitrgbspeed",500,0.2,10,0)
        );
    }

    public static Text getText(String option) {
        return Text.translatable("bewis.option." + option);
    }

    public LegacyOptionScreen() {
        super(Text.of("Bewisclient"));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        double roundMulti = FileReader.getByFirstIntFirst("Double", "option_size", 0.6f);
        String tooltip = "";
        int size = (int) (Math.min(width, height) * 0.7f / 2);
        if (mouseX < width / 2 && mouseY < height / 2 && Math.cos(Math.asin(Math.abs(mouseX - width / 2f) / (size * size1))) * size * size1 > height / 2f - mouseY) {
            size1 += 0.1;
            tooltip = "Widgets";
        } else if (mouseX > width / 2 && mouseY < height / 2 && Math.cos(Math.asin(Math.abs(mouseX - width / 2f) / (size * size3))) * size * size3 > height / 2f - mouseY) {
            size3 += 0.1;
            tooltip = "Render";
        } else if (mouseX < width / 2 && mouseY > height / 2 && Math.cos(Math.asin(Math.abs(mouseX - width / 2f) / (size * size2))) * size * size2 > Math.abs(height / 2f - mouseY)) {
            size2 += 0.1;
            tooltip = "Mods";
        } else if (mouseX > width / 2 && mouseY > height / 2 && Math.cos(Math.asin(Math.abs(mouseX - width / 2f) / (size * size4))) * size * size4 > Math.abs(height / 2f - mouseY)) {
            size4 += 0.1;
        }
        size1 = ((size1 - 1) * 0.5f + 1);
        size2 = ((size2 - 1) * 0.5f + 1);
        size3 = ((size3 - 1) * 0.5f + 1);
        size4 = ((size4 - 1) * 0.5f + 1);
        ExtendedDrawableHelper.drawBewisOptionOverlay(matrices, width, height, size1, size2, size3, size4, (float) getValue((float) roundMulti,1,5));
        if (!tooltip.equals("")) renderTooltip(matrices, Text.of(tooltip), mouseX, mouseY);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    protected void init() {

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int size = (int) (Math.min(width, height) * 0.7f / 2);
        assert this.client != null;
        if (mouseX < width / 2f && mouseY < height / 2f && Math.cos(Math.asin(Math.abs(mouseX - width / 2f) / (size * size1))) * size * size1 > height / 2f - mouseY) {
            this.client.setScreen(new WidgetScreen());
        }
        if (mouseX > width / 2f && mouseY < height / 2f && Math.cos(Math.asin(Math.abs(mouseX - width / 2f) / (size * size3))) * size * size3 > height / 2f - mouseY) {
            this.client.setScreen(new OptionListScreen(getOptions()));
        }
        if (mouseX < width / 2f) {
            System.out.println(1);
            if (mouseY > height / 2f) {
                System.out.println(2);
                if (Math.cos(Math.asin(Math.abs(mouseX - width / 2f) / (size * size2))) * size * size2 > mouseY - height / 2f) {
                    System.out.println(3);
                    this.client.setScreen(new ModScreen(this));
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    double getValue(double value, int min, int max) {
        return value * (max - min) + min;
    }
}