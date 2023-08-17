package bewis09.mixin;

import bewis09.option.ColorOption;
import bewis09.util.FileReader;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.resource.ResourceReload;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.IntSupplier;

@Mixin(SplashOverlay.class)
public abstract class SplashOverlayMixin extends Overlay {
    @Shadow private float progress;

    @Shadow @Final
    static Identifier LOGO;

    @Shadow private long reloadCompleteTime;

    @Shadow protected abstract void renderProgressBar(DrawContext drawContext, int minX, int minY, int maxX, int maxY, float opacity);

    @Shadow @Final private ResourceReload reload;

    @Shadow @Final private MinecraftClient client;

    @Shadow @Final private Consumer<Optional<Throwable>> exceptionHandler;

    @Shadow @Final private boolean reloading;

    IntSupplier SUPP = () -> (int) ColorOption.getColor("reloadcolor");

    @Shadow
    private static int withAlpha(int color, int alpha) {
        return 0;
    }

    @Shadow private long reloadStartTime;

    @Shadow @Final private static IntSupplier BRAND_ARGB;

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        float h;
        int k;
        float g;
        int i = context.getScaledWindowWidth();
        int j = context.getScaledWindowHeight();
        long l = Util.getMeasuringTimeMs();
        if (this.reloading && this.reloadStartTime == -1L) {
            this.reloadStartTime = l;
        }
        float f = this.reloadCompleteTime > -1L ? (float) (l - this.reloadCompleteTime) / 1000.0f : -1.0f;
        float f2 = g = this.reloadStartTime > -1L ? (float) (l - this.reloadStartTime) / 500.0f : -1.0f;
        if (f >= 1.0f) {
            if (this.client.currentScreen != null) {
                this.client.currentScreen.render(context, 0, 0, delta);
            }
            k = MathHelper.ceil((1.0f - MathHelper.clamp(f - 1.0f, 0.0f, 1.0f)) * 255.0f);
            context.fill(RenderLayer.getGuiOverlay(), 0, 0, i, j, withAlpha(getBRAND().getAsInt(), k));
            h = 1.0f - MathHelper.clamp(f - 1.0f, 0.0f, 1.0f);
        } else if (this.reloading) {
            if (this.client.currentScreen != null && g < 1.0f) {
                this.client.currentScreen.render(context, mouseX, mouseY, delta);
            }
            k = MathHelper.ceil(MathHelper.clamp((double) g, 0.15, 1.0) * 255.0);
            context.fill(RenderLayer.getGuiOverlay(), 0, 0, i, j, withAlpha(getBRAND().getAsInt(), k));
            h = MathHelper.clamp(g, 0.0f, 1.0f);
        } else {
            k = getBRAND().getAsInt();
            float m = (float) (k >> 16 & 0xFF) / 255.0f;
            float n = (float) (k >> 8 & 0xFF) / 255.0f;
            float o = (float) (k & 0xFF) / 255.0f;
            GlStateManager._clearColor(m, n, o, 1.0f);
            GlStateManager._clear(16384, MinecraftClient.IS_SYSTEM_MAC);
            h = 1.0f;
        }
        k = (int) ((double) context.getScaledWindowWidth() * 0.5);
        int p = (int) ((double) context.getScaledWindowHeight() * 0.5);
        double d = Math.min((double) context.getScaledWindowWidth() * 0.75, (double) context.getScaledWindowHeight()) * 0.25;
        int q = (int) (d * 0.5);
        double e = d * 4.0;
        int r = (int) (e * 0.5);
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(770, 1);
        context.setShaderColor(1.0f, 1.0f, 1.0f, h);
        context.drawTexture(LOGO, k - r, p - q, r, (int) d, -0.0625f, 0.0f, 120, 60, 120, 120);
        context.drawTexture(LOGO, k, p - q, r, (int) d, 0.0625f, 60.0f, 120, 60, 120, 120);
        context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        int s = (int) ((double) context.getScaledWindowHeight() * 0.8325);
        float t = this.reload.getProgress();
        this.progress = MathHelper.clamp(this.progress * 0.95f + t * 0.050000012f, 0.0f, 1.0f);
        if (f < 1.0f) {
            this.renderProgressBar(context, i / 2 - r, s - 5, i / 2 + r, s + 5, 1.0f - MathHelper.clamp(f, 0.0f, 1.0f));
        }
        if (f >= 2.0f) {
            this.client.setOverlay(null);
        }
        if (this.reloadCompleteTime == -1L && this.reload.isComplete() && (!this.reloading || g >= 2.0f)) {
            try {
                this.reload.throwException();
                this.exceptionHandler.accept(Optional.empty());
            } catch (Throwable throwable) {
                this.exceptionHandler.accept(Optional.of(throwable));
            }
            this.reloadCompleteTime = Util.getMeasuringTimeMs();
            if (this.client.currentScreen != null) {
                this.client.currentScreen.init(this.client, context.getScaledWindowWidth(), context.getScaledWindowHeight());
            }
        }
    }

    public IntSupplier getBRAND() {
        return FileReader.getBoolean("should_reload_color") ? SUPP : BRAND_ARGB;
    }
}
