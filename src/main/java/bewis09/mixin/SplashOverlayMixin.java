package bewis09.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourceReload;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(SplashOverlay.class)
public abstract class SplashOverlayMixin extends Overlay {
    @Shadow @Final private MinecraftClient client;
    @Shadow private long reloadCompleteTime;
    @Shadow private long reloadStartTime;
    @Shadow @Final private ResourceReload reload;
    @Shadow @Final private Consumer<Optional<Throwable>> exceptionHandler;
    @Shadow @Final private boolean reloading;

    @Shadow protected abstract void renderProgressBar(MatrixStack matrices, int minX, int minY, int maxX, int maxY, float opacity);

    double tick = 0;
    long lasttick = System.currentTimeMillis();

    private static final Identifier grass = new Identifier("bewisclient","gui/startanimation/grass.png");
    private static final Identifier pig = new Identifier("bewisclient","gui/startanimation/pig.png");
    private static final Identifier logo = new Identifier("bewisclient","gui/logo_long.png");
    private static final Identifier background = new Identifier("textures/block/green_wool.png");

    /**
     * @author Mojang
     * @reason Why Not?
     */
    @Overwrite
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.enableBlend();
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();
        long l = Util.getMeasuringTimeMs();
        if (this.reloading && this.reloadStartTime == -1L) {
            this.reloadStartTime = l;
        }
        float f = this.reloadCompleteTime > -1L ? (float)(l - this.reloadCompleteTime) / 1000.0F : -1.0F;
        float g = this.reloadStartTime > -1L ? (float)(l - this.reloadStartTime) / 500.0F : -1.0F;
        float alpha;
        if(f >= 1.0f)
            alpha = 1.0F - MathHelper.clamp(f - 1.0F, 0.0F, 1.0F);
        else if (reloading)
            alpha = MathHelper.clamp(g, 0.0F, 1.0F);
        else
            alpha = 1;
        RenderSystem.setShaderTexture(0, background);
        RenderSystem.setShaderColor(0.25F, 0.25F, 0.25F, alpha);
        drawTexture(matrices, 0, 0, 0, 0.0F, 0.0F, client.getWindow().getScaledWidth(), client.getWindow().getScaledHeight(), 32, 32);
        RenderSystem.setShaderColor(1,1,1,alpha);
        for (int i = 31; i < 40; i++) {
            if(Math.min(300,Math.max((i+1)*100-30*tick,0))!=300) {
                RenderSystem.setShaderTexture(0, grass);
                drawTexture(matrices, width / 10 * (i - 31) + width / 10-width/18, (int) (height - width / 10 - Math.min(300, Math.max((i + 1) * 100 - 30 * tick, 0)) - width / 20), (width / 9), width / 9, 0, 0, 160, 144, 160, 144);
            }
        }
        for (int i = 21; i < 31; i++) {
            if(Math.min(300,Math.max((i+1)*100-30*tick,0))!=300) {
                RenderSystem.setShaderTexture(0, grass);
                drawTexture(matrices, width / 10 * (i - 21) + width / 20-width/18, (int) (height - width / 10 - Math.min(300, Math.max((i + 1) * 100 - 30 * tick, 0)) - width / 40), (width / 9), width / 9, 0, 0, 160, 144, 160, 144);
            }
        }
        for (int i = 10; i < 21; i++) {
            if (Math.min(300, Math.max((i + 1) * 100 - 30 * tick, 0)) != 300) {
                RenderSystem.setShaderTexture(0, grass);
                drawTexture(matrices, width / 10 * (i-10)-width/18, (int) (height - width / 10 - Math.min(300, Math.max((i + 1) * 100 - 30 * tick, 0))), (width / 9), width / 9, 0, 0, 160, 144, 160, 144);
            }
        }
        for (int i = 0; i < 10; i++) {
            if(Math.min(300,Math.max((i+1)*100-30*tick,0))!=300) {
                RenderSystem.setShaderTexture(0, grass);
                drawTexture(matrices, width / 10 * (i) + width / 20-width/18, (int) (height - width / 10 - Math.min(300, Math.max((i + 1) * 100 - 30 * tick, 0)) + width / 40), (width / 9), width / 9, 0, 0, 160, 144, 160, 144);
            }
        }
        if(tick>120) {
            RenderSystem.setShaderTexture(0,pig);
            RenderSystem.setShaderColor(1F, 1F, 1F, Math.min(alpha,(float) Math.min(1, (tick - 120) / 10f)));
            RenderSystem.enableBlend();
            drawTexture(matrices,width/10,height-width/5,width/8, width/8,0,0, 350,324, 350,324);
        }
        if(tick>140) {
            RenderSystem.setShaderTexture(0,logo);
            RenderSystem.setShaderColor(1F, 1F, 1F, Math.min(alpha,(float) Math.min(1,(tick-140)/20f)));
            RenderSystem.enableBlend();
            drawTexture(matrices,width/6,(int) (height/2-height*(1/3f)+(1/5.5)),(int) (width*(2/3f)), (int) (width*(2/3f)*(1/5.5)),0,0, 954,172,954,172);
        }
        RenderSystem.setShaderColor(1,1,1,alpha);
        if (f >= 2.0F) {
            assert this.client != null;
            this.client.setOverlay(null);
        }

        if (this.reloadCompleteTime == -1L && this.reload.isComplete() && (!this.reloading || g >= 2.0F)) {
            try {
                this.reload.throwException();
                this.exceptionHandler.accept(Optional.empty());
            } catch (Throwable var23) {
                this.exceptionHandler.accept(Optional.of(var23));
            }

            this.reloadCompleteTime = Util.getMeasuringTimeMs();
            if (this.client.currentScreen != null) {
                this.client.currentScreen.init(this.client, this.client.getWindow().getScaledWidth(), this.client.getWindow().getScaledHeight());
            }
        }
        if(lasttick<System.currentTimeMillis()-0.05) {
            lasttick = System.currentTimeMillis();
            tick();
        }
    }

    public void tick() {
        tick+=1.4;
    }
}
