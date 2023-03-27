package bewis09.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Text.class)
@Environment(EnvType.CLIENT)
public interface TextMixin {

    @Inject(method = "translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;",at = @At("HEAD"))
    private static void translatable(String key, CallbackInfoReturnable<MutableText> cir) {
        MutableText of = MutableText.of(new TranslatableTextContent(key, null, TranslatableTextContent.EMPTY_ARGUMENTS));
        if(Objects.equals(of.getString(), key)) {
            System.out.println(key);
        }
    }
}
