package bewis09.mixin;

import bewis09.main.Main;
import bewis09.util.ZoomImplementer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Mouse.class)
public class MouseMixin {

    @Inject(method = "onMouseButton", at = @At("HEAD"))
    public void inject(long window, int button, int action, int mods, CallbackInfo ci) {
        if (action == 1) {
            if (button == 0) {
                Main.leftList.add(System.currentTimeMillis());
            } else if (button == 1) {
                Main.rightList.add(System.currentTimeMillis());
            }
        }
    }

    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    public void inject(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (window == MinecraftClient.getInstance().getWindow().getHandle() && Main.isZoomed) {
            ((ZoomImplementer)MinecraftClient.getInstance().gameRenderer).setGoal(MathHelper.clamp((((ZoomImplementer)MinecraftClient.getInstance().gameRenderer).getGoal())+(((ZoomImplementer)MinecraftClient.getInstance().gameRenderer).getGoal()*(vertical/-4)),0.005,0.5));
            ci.cancel();
        }
    }
}
