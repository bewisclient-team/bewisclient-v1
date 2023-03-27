package bewis09.mixin;

import bewis09.util.FileReader;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.minecraft.client.render.FogShape;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin {

    @Shadow public static void clearFog() {}

    @Invoker("getFogModifier")
    public static BackgroundRenderer.StatusEffectFogModifier getFogMod(Entity entity, float tickDelta){
        return null;
    }

    @Inject(method = "applyFog",at=@At("HEAD"),cancellable = true)
    private static void applyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci) {
        CameraSubmersionType cameraSubmersionType = camera.getSubmersionType();
        Entity entity = camera.getFocusedEntity();
        BackgroundRenderer.FogData fogData = new BackgroundRenderer.FogData(fogType);
        BackgroundRenderer.StatusEffectFogModifier statusEffectFogModifier = getFogMod(entity, tickDelta);
        if (cameraSubmersionType == CameraSubmersionType.LAVA) {
            if (entity.isSpectator() || FileReader.getBoolean("lava")) {
                fogData.fogStart = -8.0f;
                fogData.fogEnd = (float) (viewDistance * (FileReader.getByFirstIntFirst("Double","lava_view",0.5d)));
            } else if (entity instanceof LivingEntity && ((LivingEntity)entity).hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) {
                fogData.fogStart = 0.0f;
                fogData.fogEnd = 3.0f;
            } else {
                fogData.fogStart = 0.25f;
                fogData.fogEnd = 1.0f;
            }
        } else if (cameraSubmersionType == CameraSubmersionType.POWDER_SNOW) {
            if (entity.isSpectator() || FileReader.getBoolean("powder_snow")) {
                fogData.fogStart = -8.0f;
                fogData.fogEnd = viewDistance * 0.5f;
            } else {
                fogData.fogStart = 0.0f;
                fogData.fogEnd = 2.0f;
            }
        } else if (statusEffectFogModifier != null) {
            LivingEntity livingEntity = (LivingEntity)entity;
            StatusEffectInstance statusEffectInstance = livingEntity.getStatusEffect(statusEffectFogModifier.getStatusEffect());
            if (statusEffectInstance != null) {
                statusEffectFogModifier.applyStartEndModifier(fogData, livingEntity, statusEffectInstance, viewDistance, tickDelta);
            }
        } else if (cameraSubmersionType == CameraSubmersionType.WATER && !FileReader.getBoolean("water")) {
            fogData.fogStart = -8.0f;
            fogData.fogEnd = 96.0f;
            if (entity instanceof ClientPlayerEntity clientPlayerEntity) {
                fogData.fogEnd *= Math.max(0.25f, clientPlayerEntity.getUnderwaterVisibility());
                RegistryEntry<Biome> registryEntry = clientPlayerEntity.world.getBiome(clientPlayerEntity.getBlockPos());
                if (registryEntry.isIn(BiomeTags.HAS_CLOSER_WATER_FOG)) {
                    fogData.fogEnd *= 0.85f;
                }
            }
            if (fogData.fogEnd > viewDistance) {
                fogData.fogEnd = viewDistance;
                fogData.fogShape = FogShape.CYLINDER;
            }
        } else if (thickFog && !FileReader.getBoolean("nether")) {
            fogData.fogStart = viewDistance * 0.05f;
            fogData.fogEnd = Math.min(viewDistance, 192.0f) * 0.5f;
        } else if (fogType == BackgroundRenderer.FogType.FOG_SKY) {
            fogData.fogStart = 0.0f;
            fogData.fogEnd = viewDistance;
            fogData.fogShape = FogShape.CYLINDER;
        } else {
            float f = MathHelper.clamp(viewDistance / 10.0f, 4.0f, 64.0f);
            fogData.fogStart = viewDistance - f;
            fogData.fogEnd = viewDistance;
            fogData.fogShape = FogShape.CYLINDER;
        }
        RenderSystem.setShaderFogStart(fogData.fogStart);
        RenderSystem.setShaderFogEnd(fogData.fogEnd);
        RenderSystem.setShaderFogShape(fogData.fogShape);
        if(FileReader.getBoolean("nofog") && (fogType != BackgroundRenderer.FogType.FOG_SKY || FileReader.getBoolean("sky_fog")) &&
                cameraSubmersionType != CameraSubmersionType.WATER &&
                cameraSubmersionType != CameraSubmersionType.LAVA &&
                cameraSubmersionType != CameraSubmersionType.POWDER_SNOW)
            clearFog();
        ci.cancel();
    }
}
