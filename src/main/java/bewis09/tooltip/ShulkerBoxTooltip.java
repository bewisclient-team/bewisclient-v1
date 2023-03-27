package bewis09.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class ShulkerBoxTooltip implements TooltipComponent {
    public static final Identifier TEXTURE = new Identifier("textures/gui/container/bundle.png");
    private final DefaultedList<ItemStack> inventory;
    private final DyeColor color;

    public ShulkerBoxTooltip(ShulkerBoxTooltipData data) {
        this.inventory = data.getInventory();
        this.color = data.color();
    }

    @Override
    public int getHeight() {
        return this.getRows() * 18 + 6+16;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return this.getColumns() * 18 + 2+16;
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, MatrixStack matrices, ItemRenderer itemRenderer) {
        RenderSystem.setShaderColor(color.getColorComponents()[0], color.getColorComponents()[1], color.getColorComponents()[2], 1.0f);
        RenderSystem.setShaderTexture(0, new Identifier("bewisclient","gui/shulker_box_background.png"));
        DrawableHelper.drawTexture(matrices, x+1, y+1, 0, 0, 178, 70,178,70);
        int i = 0;
        for(int k = 0; k<3; k++) {
            for(int l = 0; l<9; l++) {
                int n = x + l * 18 + 1+8;
                int o = y + k * 18 + 1+8;
                this.drawSlot(n, o, i, textRenderer, matrices, itemRenderer);
                i++;
            }
        }
    }

    private void drawSlot(int x, int y, int index, TextRenderer textRenderer, MatrixStack matrices, ItemRenderer itemRenderer) {
        if (index >= this.inventory.size()) {
            return;
        }
        ItemStack itemStack = this.inventory.get(index);
        this.draw(matrices, x, y);
        RenderSystem.setShaderColor(1,1,1,1);
        itemRenderer.renderInGuiWithOverrides(matrices,itemStack, x + 1, y + 1, index);
        itemRenderer.renderGuiItemOverlay(matrices,textRenderer, itemStack, x + 1, y + 1);
    }

    private void draw(MatrixStack matrices, int x, int y) {
        RenderSystem.setShaderColor(color.getColorComponents()[0], color.getColorComponents()[1], color.getColorComponents()[2], 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        DrawableHelper.drawTexture(matrices, x, y, 0, 0, 18, 18, 128, 128);
    }

    private int getColumns() {
        return 9;
    }

    private int getRows() {
        return 3;
    }
}
