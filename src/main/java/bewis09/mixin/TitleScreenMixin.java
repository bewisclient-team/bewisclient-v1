package bewis09.mixin;

import bewis09.drawable.OptionScreenClickableWidget;
import bewis09.main.Main;
import bewis09.main.TitleStaticInitializer;
import bewis09.screen.OptionScreen;
import bewis09.util.FileReader;
import bewis09.util.PublicOptionSaver;
import bewis09.util.TextHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    @Shadow private long backgroundFadeStart;
    @Shadow @Final private boolean doBackgroundFade;
    Identifier identifier = new Identifier("bewisclient","gui/bewisclient_config_button.png");

    double p = 1;

    boolean z = PublicOptionSaver.values.getOrDefault("TitleButton", String.valueOf(false)).equals("true");
    private double j = 1;

    protected TitleScreenMixin(Text title) {
        super(title);
        TitleStaticInitializer.register();
    }

    @Inject(method = "init",at=@At("HEAD"))
    private void init(CallbackInfo ci) {
        boolean enabled = FileReader.getBoolean("fullbright");
        MinecraftClient.getInstance().options.getGamma().setValue(enabled ? FileReader.getByFirstIntFirst("Double","fullbrightvalue",0.1)*15 : 1);
        TexturedButtonWidget t = addDrawableChild(new TexturedButtonWidget(this.width / 2 + 104, (int) (this.height / 4 + 48 + 24), 20, 20, 0, 0,20, identifier,20,40, button -> {
            assert this.client != null;
            this.client.setScreen(new OptionScreen());
            PublicOptionSaver.write("TitleButton", String.valueOf(true));
            z=true;
        }));
        if(OptionScreenClickableWidget.newVersion)
            t.setTooltip(Tooltip.of(TextHelper.getText("new_update")));
        if(Main.updateInfo!=null && Main.updateInfo.equals("HF") && !(PublicOptionSaver.values.getOrDefault("hotfix"+"_"+ FabricLoader.getInstance().getModContainer("bewisclient").get().getMetadata().getVersion(),"false").equals("true"))) {
            MinecraftClient.getInstance().setScreen(new ConfirmScreen(b -> {
                if (b) {
                    Util.getOperatingSystem().open("https://modrinth.com/mod/bewisclient");
                } else {
                    PublicOptionSaver.write("hotfix"+"_"+ FabricLoader.getInstance().getModContainer("bewisclient").get().getMetadata().getVersion(),"true");
                    MinecraftClient.getInstance().setScreen(TitleScreenMixin.this);
                }
            }, TextHelper.getText("hotfix"), TextHelper.getText("hotfix_info"), TextHelper.getText("download"), ScreenTexts.CONTINUE));
        }
    }

    @Inject(method = "render",at=@At("RETURN"))
    private void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        float f = this.doBackgroundFade ? (float)(Util.getMeasuringTimeMs() - this.backgroundFadeStart) / 1000.0F : 1.0F;
        context.setShaderColor(1.0F, 1.0F, 1.0F, this.doBackgroundFade ? MathHelper.clamp(f - 1.0F, 0.0F, 1.0F) : 1.0F);
        RenderSystem.enableBlend();
        if (!z) {
            int i = (int) (p <= 4 ? p : 8 - p);
            context.fill((this.width / 2 + 104) - i, (this.height / 4 + 48 + 24) - i, (this.width / 2 + 104) + i, (this.height / 4 + 48 + 24), 0xFFFFFFFF);
            context.fill((this.width / 2 + 104) - i, (this.height / 4 + 48 + 24) - i, (this.width / 2 + 104), (this.height / 4 + 48 + 24) + i, 0xFFFFFFFF);
            context.fill((this.width / 2 + 104) - i, (this.height / 4 + 48 + 24) + 20, (this.width / 2 + 104) + i, (this.height / 4 + 48 + 24) + 20 + i, 0xFFFFFFFF);
            context.fill((this.width / 2 + 104) - i, (this.height / 4 + 48 + 24) + 20 - i, (this.width / 2 + 104), (this.height / 4 + 48 + 24) + 20 + i, 0xFFFFFFFF);
            context.fill((this.width / 2 + 104) + 20, (this.height / 4 + 48 + 24) - i, (this.width / 2 + 104) + 20 + i, (this.height / 4 + 48 + 24) + i, 0xFFFFFFFF);
            context.fill((this.width / 2 + 104) + 20 - i, (this.height / 4 + 48 + 24) - i, (this.width / 2 + 104) + 20 + i, (this.height / 4 + 48 + 24), 0xFFFFFFFF);
            context.fill((this.width / 2 + 104) + 20, (this.height / 4 + 48 + 24) + 20 - i, (this.width / 2 + 104) + 20 + i, (this.height / 4 + 48 + 24) + 20 + i, 0xFFFFFFFF);
            context.fill((this.width / 2 + 104) + 20 - i, (this.height / 4 + 48 + 24) + 20, (this.width / 2 + 104) + 20 + i, (this.height / 4 + 48 + 24) + 20 + i, 0xFFFFFFFF);
        }
        if (OptionScreenClickableWidget.newVersion) {
            int g = (int) (j <= 4 ? j : 8 - j);
            context.fill((this.width / 2 + 104) + 18,(this.height / 4 + 48 + 8)+g+16,(this.width / 2 + 104) + 22,(this.height / 4 + 48 + 8) + 12+g+16,-1);
            context.fill((this.width / 2 + 104) + 18,(this.height / 4 + 48 + 24)+g+16,(this.width / 2 + 104) + 22,(this.height / 4 + 48 + 8) + 20+g+16,-1);
            context.drawBorder((this.width / 2 + 104) + 18,(this.height / 4 + 48 + 8)+g+16,4,12,0xFFDDBB77);
            context.drawBorder((this.width / 2 + 104) + 18,(this.height / 4 + 48 + 24)+g+16,4,4,0xFFDDBB77);
        }
        RenderSystem.disableBlend();
        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Inject(method = "tick",at=@At("RETURN"))
    private void tick(CallbackInfo ci) {
        if (!z) {
            p = (p + 0.2 - 1) % 7 + 1;
        }
        if (OptionScreenClickableWidget.newVersion) {
            j = (j + 0.2 - 1) % 7 + 1;
        }
    }
}
