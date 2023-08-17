package bewis09.screen;

import bewis09.drawable.OptionScreenClickableWidget;
import bewis09.option.*;
import bewis09.util.FileReader;
import bewis09.util.TextHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static bewis09.util.TextHelper.getText;

public class OptionScreen extends AbstractOptionScreen {

    List<Option> blockhitList = List.of(
            new BooleanOption(getText("customblockhit"),"customblockhit"),
            new BooleanOption(getText("blockhitrgb"),"blockhitrgb"),
            new SliderOption(getText("blockhitalpha"),"blockhitalpha",1,0.4,0),
            new ColorOption(getText("blockhitcolor"),"blockhitcolor"),
            new SliderOption(getText("hitrgbspeed"),"hitrgbspeed",500,0.2,10,0)
    );

    List<Option> informationList = List.of(
            new BooleanOption(getText("helditeminfo"), "helditeminfo"),
            new SliderOption(getText("maxinfolength"), "maxinfolength", 10, 0.55, 1, 0),
            new BooleanOption(getText("cleaner_debug"),"cleaner_debug"),
            new BooleanOption(getText("debug_light_code"),"debug_light_code"),
            new SliderOption(getText("scoreboardsize"),"scoreboardsize",1.5,0.5,0.5),
            new BooleanOption(getText("noscoreboardnumbers"),"noscoreboardnumbers"),
            new BooleanOption(getText("shulkerbox_tooltip"),"shulkerbox_tooltip")
    );

    List<Option> visibilityList = List.of(
            new BooleanOption(getText("lava"), "lava"),
            new BooleanOption(getText("water"), "water"),
            new BooleanOption(getText("nether"), "nether"),
            new BooleanOption(getText("powder_snow"), "powder_snow"),
            new SliderOption(getText("lava_view"), "lava_view", 1, 0.5f),
            new BooleanOption(getText("disable_pumpkin_overlay"), "disable_pumpkin_overlay"),
            new BooleanOption(getText("show_pumpkin_icon"), "show_pumpkin_icon", false)
    );

    List<Option> fullbrightList = List.of(
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
            new BooleanOption(getText("night_vision"),"night_vision")
    );

    List<Option> otherList = List.of(
            new BooleanOption(getText("instant_zoom"), "instant_zoom"),
            new BooleanOption(getText("hard_camera"),"hard_camera"),
            new SliderOption(getText("fire_height"), "fire_height", 1, 1),
            new TextFieldOption(getText("backgroundtexture"),"backgroundtexture",""),
            new BooleanOption(getText("should_reload_color"),"should_reload_color"),
            new ColorOption(getText("reload_color"),"reloadcolor")
    );

    OptionScreenClickableWidget[] optionScreenClickableWidgets = {
            new OptionScreenClickableWidget(getText("widgets"), WidgetScreen::new,new Identifier("bewisclient","gui/icons/widgets.png"),true),
            new OptionScreenClickableWidget(getText("cosmetics"),CosmeticsScreen::new,new Identifier("bewisclient","gui/icons/cosmetics.png"),true),
            new OptionScreenClickableWidget(getText("mods"),ModScreen::new,new Identifier("bewisclient","gui/icons/mods.png"),false),
            new OptionScreenClickableWidget(getText("macros"),MacroScreen::new,new Identifier("bewisclient","gui/icons/macros.png"),false),
            new OptionScreenClickableWidget(getText("blockhit"), parent -> new ConfigScreen(blockhitList,getText("blockhit"),parent), new Identifier("bewisclient","gui/icons/blockhit.png"), false),
            new OptionScreenClickableWidget(getText("visibility"),parent -> new ConfigScreen(visibilityList,getText("visibility"),parent),new Identifier("bewisclient","gui/icons/visibility.png"),false),
            new OptionScreenClickableWidget(getText("fullbright"),parent -> new ConfigScreen(fullbrightList,getText("fullbright"),parent),new Identifier("bewisclient","gui/icons/fullbright.png"),false),
            new OptionScreenClickableWidget(getText("information"),parent -> new ConfigScreen(informationList,getText("information"),parent),new Identifier("bewisclient","gui/icons/information.png"),false),
            new OptionScreenClickableWidget(getText("other"),parent -> new ConfigScreen(otherList,getText("other"),parent),new Identifier("bewisclient","gui/icons/others.png"),false),
            new OptionScreenClickableWidget(getText("news"), parent -> new LoadingScreen(() -> setNewsScreen(parent)),new Identifier("bewisclient","gui/icons/news.png"),false),
            new OptionScreenClickableWidget(getText("profiles"), ProfilesScreen::new,new Identifier("bewisclient","gui/icons/profiles.png"),false),
            new OptionScreenClickableWidget(getText("crosshair"), parent -> new ConfirmScreenScreen(parent,new LegacyCrosshairScreen(parent), TextHelper.getText("beta_text"), "beta_crosshair"),new Identifier("bewisclient","gui/icons/beta.png"),false),
            new OptionScreenClickableWidget(getText("contact"), ContactScreen::new,new Identifier("bewisclient","gui/icons/feedback.png"),false)
    };

    public static void setNewsScreen(Screen parent) {
        MinecraftClient.getInstance().setScreen(new NewsScreen(parent));
    }

    @Override
    public int maxHeight() {
        return (((optionScreenClickableWidgets.length-1)/4+1)*73)+8;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        drawBackground(context);
        for (int i = 0; i < optionScreenClickableWidgets.length; i++) {
            optionScreenClickableWidgets[i].render(context,width,i,scroll,mouseX,mouseY);
        }
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        AtomicInteger i = new AtomicInteger();
        Arrays.stream(optionScreenClickableWidgets).forEach(optionScreenClickableWidget -> {
            optionScreenClickableWidget.clickAll((int) mouseX, (int) mouseY,width,i.get(), (int) scroll);
            i.getAndIncrement();
        });
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void init() {
        correctScroll();
    }
}
