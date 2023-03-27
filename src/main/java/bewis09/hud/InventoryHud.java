package bewis09.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class InventoryHud extends HudElement {
    public InventoryHud() {
        super(5,5,Horizontal.RIGHT,Vertical.BOTTOM,180,60);
    }

    @Override
    public String getId() {
        return "INVENTORY";
    }

    @Override
    public void paint(MatrixStack stack) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 3; j++) {
                RenderSystem.enableBlend();
                RenderSystem.setShaderTexture(0,new Identifier("bewisclient","gui/screen/slot.png"));
                drawTexture(stack,getX()+i*20,getY()+j*20,0,0,20,20,20,20);
                assert MinecraftClient.getInstance().player != null;
                MinecraftClient.getInstance().getItemRenderer().renderInGui(stack,MinecraftClient.getInstance().player.getInventory().getStack(j*9+i+9), getX()+i*20+2,getY()+j*20+2);
                MinecraftClient.getInstance().getItemRenderer().renderGuiItemOverlay(stack,MinecraftClient.getInstance().textRenderer,MinecraftClient.getInstance().player.getInventory().getStack(j*9+i+9), getX()+i*20+2,getY()+j*20+2);
            }
        }
    }

    @Override
    public float getSize() {
        return 1f;
    }
}
