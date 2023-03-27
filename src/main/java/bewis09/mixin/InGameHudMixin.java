package bewis09.mixin;

import bewis09.hud.*;
import bewis09.util.FileReader;
import bewis09.util.InGameHudImplementer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.BlockItem;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin implements InGameHudImplementer {

    @Shadow @Final private static Identifier PUMPKIN_BLUR;

    @Shadow @Final private ItemRenderer itemRenderer;

    @Shadow private int scaledHeight;

    @Shadow private int scaledWidth;

    @Shadow @Final private MinecraftClient client;

    @Shadow private int heldItemTooltipFade;

    @Shadow private ItemStack currentStack;

    @Shadow public abstract TextRenderer getTextRenderer();

    @Inject(method = "<init>", at=@At("RETURN"))
    public void inject(MinecraftClient client, ItemRenderer itemRenderer, CallbackInfo ci) {
        Huds.register(TiwylaHud::new);
        Huds.register(DayHud::new);
        Huds.register(FpsHud::new);
        Huds.register(BiomeHud::new);
        Huds.register(CoordinateHud::new);
        Huds.register(InventoryHud::new);
        Huds.register(CpsHud::new);
        Huds.register(SleepTimerHud::new);
        Huds.register(KeyHud::new);
        Huds.register(SpeedHud::new);
    }

    @Inject(method = "render", at=@At("RETURN"))
    public void inject(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        RenderSystem.enableBlend();
        renderWidgets(matrices, false);
    }

    public void renderWidgets(MatrixStack matrices, boolean bl) {
        for (HudElement element : Huds.getList()) {
            if(element.isVisible() || bl)
                element.paint(matrices);
        }
    }

    @Inject(method = "renderOverlay", at=@At("HEAD"), cancellable = true)
    public void inject(MatrixStack matrices, Identifier texture, float opacity, CallbackInfo ci) {
        if(FileReader.getBoolean("disable_pumpkin_overlay")&&texture==PUMPKIN_BLUR){
            if(FileReader.getBoolean("show_pumpkin_icon",true))
                itemRenderer.renderInGui(matrices,Items.CARVED_PUMPKIN.getDefaultStack(),scaledWidth/2+94,scaledHeight-20);
            RenderSystem.enableBlend();
            ci.cancel();
        }
    }

    @Inject(method = "renderHeldItemTooltip",at=@At("HEAD"),cancellable = true)
    public void renderHeldItemTooltip(MatrixStack matrices, CallbackInfo ci) {
        if(FileReader.getBoolean("helditeminfo")) {
            this.client.getProfiler().push("selectedItemName");
            if (this.heldItemTooltipFade > 0 && !this.currentStack.isEmpty()) {
                int l;
                int k = this.scaledHeight - 59;
                assert this.client.interactionManager != null;
                if (!this.client.interactionManager.hasStatusBars()) {
                    k += 14;
                }
                if ((l = (int) ((float) this.heldItemTooltipFade * 256.0f / 10.0f)) > 255) {
                    l = 255;
                }
                List<Text> tooltipList = getTooltipFromItem(this.currentStack);
                int g = tooltipList.size();
                if (l > 0) {
                    k -= (this.getTextRenderer().fontHeight) * (g - 1);
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    int i = 0;
                    for (Text t : tooltipList) {
                        i = Math.max(i, this.getTextRenderer().getWidth(t));
                    }
                    int j = (this.scaledWidth - i) / 2;
                    InGameHud.fill(matrices, j - 2, k - 2, j + i + 2, (k + ((this.getTextRenderer().fontHeight) * g)) + 2, this.client.options.getTextBackgroundColor(0));
                    int h = -1;
                    for (Text t : tooltipList) {
                        h++;
                        DrawableHelper.drawCenteredTextWithShadow(matrices, getTextRenderer(), t.asOrderedText(), this.scaledWidth / 2, k + (this.getTextRenderer().fontHeight) * h, 0xFFFFFF + (l << 24));
                    }
                    RenderSystem.disableBlend();
                }
            }
            this.client.getProfiler().pop();
            ci.cancel();
        }
    }

    public List<Text> getTooltipFromItem(ItemStack stack) {
        List<Text> list = new ArrayList<>();
        MutableText mutableText = Text.empty().append(stack.getName()).formatted(this.currentStack.getRarity().formatting);
        if (this.currentStack.hasCustomName()) {
            mutableText.formatted(Formatting.ITALIC);
        }
        list.add(mutableText);
        appendShulkerBoxInfo(stack,list);
        appendBookInfo(stack,list);
        ItemStack.appendEnchantments(list,stack.getEnchantments());
        ItemStack.appendEnchantments(list,(EnchantedBookItem.getEnchantmentNbt(stack)));
        boolean b = false;
        int i = 0;
        if(!(getValue(FileReader.getByFirstIntFirst("Double","maxinfolength",0.5f),1,10)==0)) {
            while (list.size() > (getValue(FileReader.getByFirstIntFirst("Double","maxinfolength",5),1,10))+1) {
                i++;
                list.remove(list.size() - 1);
                b = true;
            }
        }
        if(b) list.add(Text.translatable("bewisclient.hud.andmore", i).formatted(Formatting.GRAY).formatted(Formatting.ITALIC));
        return list;
    }

    @SuppressWarnings("deprecation")
    public void appendBookInfo(ItemStack i, List<Text> list) {
        if(i.itemMatches(Items.WRITTEN_BOOK.getRegistryEntry())) {
            i.getItem().appendTooltip(i,this.client.world, list, TooltipContext.Default.BASIC);
        }
    }

    public void appendShulkerBoxInfo(ItemStack d, List<Text> list) {
        try {
            if (d.getItem() instanceof BlockItem) {
                if (((BlockItem) d.getItem()).getBlock() instanceof ShulkerBoxBlock) {
                    DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(27, ItemStack.EMPTY);
                    Inventories.readNbt(Objects.requireNonNull(BlockItem.getBlockEntityNbt(d)), defaultedList);
                    for (ItemStack itemStack : defaultedList) {
                        if(itemStack.getCount()!=0) {
                            MutableText mutableText = itemStack.getName().copy();
                            mutableText.append(" x").append(String.valueOf(itemStack.getCount())).formatted(Formatting.GRAY);
                            list.add(mutableText);
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
    }

    int getValue(double value, int min, int max) {
        return (int) Math.round((value * (max - min) + min));
    }
}
