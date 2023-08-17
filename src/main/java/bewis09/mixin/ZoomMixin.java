package bewis09.mixin;

import bewis09.main.Main;
import bewis09.util.FileReader;
import bewis09.util.ZoomImplementer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public class ZoomMixin implements ZoomImplementer {

    double zoomfactor = 1;

    double zoomStart = 1;

    double zoomEnd = 1;

    long zoomStartTime = 0;

    boolean lastZoomed = false;

    double lastZoomGoal = 0.23;

    double zoomgoal = 0.23;

    @Inject(method = "getFov",at=@At("RETURN"),cancellable = true)
    private void getFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
        if(false) {
            if (!FileReader.getBoolean("instant_zoom")) {
                if (Main.isZoomed) {
                    zoomfactor = (zoomfactor * 9 + zoomgoal) / 10f;
                } else {
                    zoomfactor = (zoomfactor * 9 + 1) / 10f;
                    zoomgoal = 0.23f;
                }
                cir.setReturnValue(cir.getReturnValue() * (zoomfactor));
            } else {
                cir.setReturnValue(cir.getReturnValue() * (Main.isZoomed ? zoomgoal : 1));
            }
        } else {
            if (!FileReader.getBoolean("instant_zoom")) {
                if (Main.isZoomed != lastZoomed || zoomgoal != lastZoomGoal) {
                    zoomStart = getZoomFactor();
                    if (!Main.isZoomed) {
                        if (lastZoomed) {
                            zoomEnd = 1;
                            zoomgoal = 0.23f;
                        }
                    } else {
                        zoomEnd = zoomgoal;
                    }
                    lastZoomGoal = zoomgoal;
                    zoomStartTime = System.currentTimeMillis();
                }
                lastZoomed = Main.isZoomed;
                cir.setReturnValue(getZoomFactor() * cir.getReturnValue());
            } else {
                cir.setReturnValue(cir.getReturnValue() * (Main.isZoomed ? zoomgoal : 1));
            }
        }
    }

    public double getZoomFactor() {
        long l = System.currentTimeMillis();
        if(zoomStartTime+100 > l) {
            return ((zoomEnd)*((l - zoomStartTime)/100f))+((zoomStart)*(1-(l - zoomStartTime)/100f));
        } else {
            return (zoomEnd);
        }
    }

    @Override
    public void setGoal(double d) {
        zoomgoal = d;
    }

    @Override
    public double getGoal() {
        return zoomgoal;
    }
}
