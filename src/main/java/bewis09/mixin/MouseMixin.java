package bewis09.mixin;

import bewis09.main.Main;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Mouse.class)
public class MouseMixin {
    @Inject(method = "onMouseButton",at=@At("HEAD"))
    public void inject(long window, int button, int action, int mods, CallbackInfo ci) {
        if(action==0) {
            if (button == 0) {
                Main.leftList.add(System.currentTimeMillis());
            } else if (button == 1) {
                Main.rightList.add(System.currentTimeMillis());
            }
        }
    }
}
