package bewis09.cape.mixin;

import bewis09.cape.Cape;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractClientPlayerEntity.class)
@Environment(EnvType.CLIENT)
public abstract class AbstractClientPlayerEntityMixin {

    @Shadow protected abstract @Nullable PlayerListEntry getPlayerListEntry();

    /**
     * @author Mojang
     * @reason Why Not?
     */

    @Overwrite
    public @Nullable Identifier getCapeTexture() {
        PlayerListEntry playerListEntry = this.getPlayerListEntry();
        if (playerListEntry!=null && playerListEntry.getProfile().getName().equals(MinecraftClient.getInstance().getSession().getUsername()) && Cape.getCurrentCape() != null) {
            return Cape.getCurrentCape().getIdentifier(Cape.getCurrentCape().getFrame());
        }
        return playerListEntry == null ? null : playerListEntry.getCapeTexture();
    }
}
