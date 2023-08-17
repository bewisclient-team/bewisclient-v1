package bewis09.mixin;

import bewis09.drawable.OptionScreenClickableWidget;
import bewis09.screen.OptionScreen;
import bewis09.util.PublicOptionSaver;
import bewis09.util.TextHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public class InGameScreenMixin extends Screen {

    @Shadow @Final private boolean showMenu;
    Identifier identifier = new Identifier("bewisclient","gui/bewisclient_config_button.png");

    double p = 1;
    double j = 1;

    boolean z = PublicOptionSaver.values.getOrDefault("InGameButton", String.valueOf(false)).equals("true");

    protected InGameScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init",at=@At("HEAD"))
    private void init(CallbackInfo ci) {
        if(showMenu) {
            TexturedButtonWidget t = addDrawableChild(new TexturedButtonWidget(this.width / 2 + 104, (int) (this.height / 4 + 48 + 8), 20, 20, 0, 0,20, identifier,20,40, button -> {
                assert this.client != null;
                this.client.setScreen(new OptionScreen());
                PublicOptionSaver.write("InGameButton", String.valueOf(true));
                z=true;
            }));
            if(OptionScreenClickableWidget.newVersion)
                t.setTooltip(Tooltip.of(TextHelper.getText("new_update")));
        }
    }

    @Inject(method = "render",at=@At("RETURN"))
    private void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!z && showMenu) {
            int i = (int) (p <= 4 ? p : 8 - p);
            context.fill((this.width / 2 + 104) - i, (this.height / 4 + 48 + 8) - i, (this.width / 2 + 104) + i, (this.height / 4 + 48 + 8), 0xFFFFFFFF);
            context.fill((this.width / 2 + 104) - i, (this.height / 4 + 48 + 8) - i, (this.width / 2 + 104), (this.height / 4 + 48 + 8) + i, 0xFFFFFFFF);
            context.fill((this.width / 2 + 104) - i, (this.height / 4 + 48 + 8) + 20, (this.width / 2 + 104) + i, (this.height / 4 + 48 + 8) + 20 + i, 0xFFFFFFFF);
            context.fill((this.width / 2 + 104) - i, (this.height / 4 + 48 + 8) + 20 - i, (this.width / 2 + 104), (this.height / 4 + 48 + 8) + 20 + i, 0xFFFFFFFF);
            context.fill((this.width / 2 + 104) + 20, (this.height / 4 + 48 + 8) - i, (this.width / 2 + 104) + 20 + i, (this.height / 4 + 48 + 8) + i, 0xFFFFFFFF);
            context.fill((this.width / 2 + 104) + 20 - i, (this.height / 4 + 48 + 8) - i, (this.width / 2 + 104) + 20 + i, (this.height / 4 + 48 + 8), 0xFFFFFFFF);
            context.fill((this.width / 2 + 104) + 20, (this.height / 4 + 48 + 8) + 20 - i, (this.width / 2 + 104) + 20 + i, (this.height / 4 + 48 + 8) + 20 + i, 0xFFFFFFFF);
            context.fill((this.width / 2 + 104) + 20 - i, (this.height / 4 + 48 + 8) + 20, (this.width / 2 + 104) + 20 + i, (this.height / 4 + 48 + 8) + 20 + i, 0xFFFFFFFF);
        }
        if (OptionScreenClickableWidget.newVersion && showMenu) {
            int g = (int) (j <= 4 ? j : 8 - j);
            context.fill((this.width / 2 + 104) + 18,(this.height / 4 + 48 + 8)+g,(this.width / 2 + 104) + 22,(this.height / 4 + 48 + 8) + 12+g,-1);
            context.fill((this.width / 2 + 104) + 18,(this.height / 4 + 48 + 24)+g,(this.width / 2 + 104) + 22,(this.height / 4 + 48 + 8) + 20+g,-1);
            context.drawBorder((this.width / 2 + 104) + 18,(this.height / 4 + 48 + 8)+g,4,12,0xFFDDBB77);
            context.drawBorder((this.width / 2 + 104) + 18,(this.height / 4 + 48 + 24)+g,4,4,0xFFDDBB77);
        }
    }

    @Inject(method = "tick",at=@At("RETURN"))
    private void tick(CallbackInfo ci) {
        if (showMenu) {
            if (!z) {
                p = (p + 0.2 - 1) % 7 + 1;
            }
            if (OptionScreenClickableWidget.newVersion) {
                j = (j + 0.2 - 1) % 7 + 1;
            }
        }
    }
}
