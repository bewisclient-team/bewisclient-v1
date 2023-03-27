package bewis09.mixin;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DrawableHelper.class)
public interface DrawableHelperMixin {
    @Accessor @Mutable
    static void setOPTIONS_BACKGROUND_TEXTURE(Identifier identifier) {}
}
