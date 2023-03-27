package bewis09.mixin;

import bewis09.util.Log;
import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(Main.class)
public class MinecraftClientMixin {
    @Inject(method = "main([Ljava/lang/String;)V",at=@At("HEAD"))
    private static void inject(String[] args, CallbackInfo ci) {
        Log.log(Arrays.stream(args).toList().toArray());
    }
}
