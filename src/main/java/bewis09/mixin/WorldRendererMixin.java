package bewis09.mixin;

import bewis09.util.FileReader;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.gen.Invoker;

import static bewis09.main.Main.rgbValue;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    /**
     * @author Mojang
     * @reason Why not
     */

    @Overwrite
    private void drawBlockOutline(MatrixStack matrices, VertexConsumer vertexConsumer, Entity entity, double cameraX, double cameraY, double cameraZ, BlockPos pos, BlockState state) {
        String str = FileReader.getColor("blockhitcolor","000000");
        try {
            float r = Integer.decode("0x" + str.charAt(0) + str.charAt(1)) / 256f;
            float g = Integer.decode("0x" + str.charAt(2) + str.charAt(3)) / 256f;
            float b = Integer.decode("0x" + str.charAt(4) + str.charAt(5)) / 256f;
            float a = (float) FileReader.getByFirstIntFirst("Double", "blockhitalpha", 0.4);
            if (FileReader.getBoolean("blockhitrgb")) {
                r = b = g = 0;
                if (rgbValue <= 10 || rgbValue >= 50) r = 1;
                if (rgbValue < 10) g = rgbValue / 10f;
                if (rgbValue >= 10 && rgbValue <= 30) g = 1;
                if (rgbValue > 10 && rgbValue < 20) r = 1 - ((rgbValue - 10) / 10f);
                if (rgbValue > 20 && rgbValue < 30) b = ((rgbValue - 20) / 10f);
                if (rgbValue > 30 && rgbValue < 40) g = 1 - ((rgbValue - 30) / 10f);
                if (rgbValue >= 40 && rgbValue <= 50) r = ((rgbValue - 40) / 10f);
                if (rgbValue > 50) b = 1 - ((rgbValue - 50) / 10f);
                if (rgbValue >= 30 && rgbValue <= 50) b = 1;
            }
            if (!FileReader.getBoolean("customblockhit")) {
                r = b = g = 0;
                a = 0.4f;
            }
            draw(matrices, vertexConsumer, state.getOutlineShape(MinecraftClient.getInstance().world, pos, ShapeContext.of(entity)), (double) pos.getX() - cameraX, (double) pos.getY() - cameraY, (double) pos.getZ() - cameraZ, r, g, b, a);
        } catch (Exception e) {
            draw(matrices, vertexConsumer, state.getOutlineShape(MinecraftClient.getInstance().world, pos, ShapeContext.of(entity)), (double) pos.getX() - cameraX, (double) pos.getY() - cameraY, (double) pos.getZ() - cameraZ, 0.0f, 0.0f, 0.0f, 0.4f);
        }
    }

    @Invoker("drawCuboidShapeOutline")
    @SuppressWarnings("all")
    static void draw(MatrixStack matrices, VertexConsumer vertexConsumer, VoxelShape shape, double offsetX, double offsetY, double offsetZ, float red, float green, float blue, float alpha) {}
}
