package bewis09.mixin;

import bewis09.util.FileReader;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    StatusEffectInstance instance = new StatusEffectInstance(StatusEffects.NIGHT_VISION,1000,0,true,false,false);

    @Inject(method = "hasStatusEffect",at=@At("HEAD"), cancellable = true)
    public void inject(StatusEffect effect, CallbackInfoReturnable<Boolean> cir) {
        if(effect == StatusEffects.NIGHT_VISION && FileReader.getBoolean("night_vision") && MinecraftClient.getInstance().cameraEntity!=null) {
            if (this.getEntityName().equals(MinecraftClient.getInstance().cameraEntity.getEntityName())) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "getStatusEffect",at=@At("HEAD"), cancellable = true)
    public void injectNext(StatusEffect effect, CallbackInfoReturnable<StatusEffectInstance> cir) {
        if(effect == StatusEffects.NIGHT_VISION && FileReader.getBoolean("night_vision") && MinecraftClient.getInstance().cameraEntity!=null) {
            if (this.getEntityName().equals(MinecraftClient.getInstance().cameraEntity.getEntityName())) {
                cir.setReturnValue(instance);
            }
        }
    }
}
