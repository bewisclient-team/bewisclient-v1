package bewis09.mixin;

import bewis09.hud.*;
import bewis09.screen.LegacyCrosshairScreen;
import bewis09.util.FileReader;
import bewis09.util.InGameHudImplementer;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.BlockItem;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Shadow @Final private static String SCOREBOARD_JOINER;

    @Shadow @Final private static int field_32169;

    @Shadow protected abstract boolean shouldRenderSpectatorCrosshair(HitResult hitResult);

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
        Huds.register(PingHud::new);
    }

    @Inject(method = "render", at=@At("RETURN"))
    public void inject(DrawContext context, float tickDelta, CallbackInfo ci) {
        RenderSystem.enableBlend();
        renderWidgets(context, false);
    }

    public void renderWidgets(DrawContext context, boolean bl) {
        for (HudElement element : Huds.getList()) {
            if((element.isVisible() || bl) && !MinecraftClient.getInstance().options.hudHidden)
                element.paint(context);
        }
    }

    @Inject(method = "renderOverlay", at=@At("HEAD"), cancellable = true)
    public void inject(DrawContext context, Identifier texture, float opacity, CallbackInfo ci) {
        if(FileReader.getBoolean("disable_pumpkin_overlay")&&texture==PUMPKIN_BLUR){
            if(FileReader.getBoolean("show_pumpkin_icon",true))
                context.drawItem(Items.CARVED_PUMPKIN.getDefaultStack(),scaledWidth/2+94,scaledHeight-20);
            RenderSystem.enableBlend();
            ci.cancel();
        }
    }

    @Inject(method = "renderHeldItemTooltip",at=@At("HEAD"),cancellable = true)
    public void renderHeldItemTooltip(DrawContext context, CallbackInfo ci) {
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
                    context.fill(j - 2, k - 2, j + i + 2, (k + ((this.getTextRenderer().fontHeight) * g)) + 2, this.client.options.getTextBackgroundColor(0));
                    int h = -1;
                    for (Text t : tooltipList) {
                        h++;
                        context.drawCenteredTextWithShadow(getTextRenderer(), t.asOrderedText(), this.scaledWidth / 2, k + (this.getTextRenderer().fontHeight) * h, 0xFFFFFF + (l << 24));
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
            while (list.size() > (getValue(FileReader.getByFirstIntFirst("Double","maxinfolength",0.5),1,10))+1) {
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

    @Inject(method = "renderScoreboardSidebar",at=@At("HEAD"),cancellable = true)
    private void renderScoreboardSidebar(DrawContext context, ScoreboardObjective objective, CallbackInfo ci) {
        if (FileReader.getByFirstIntFirst("Double","scoreboardsize", 0.5) != 0.5 || FileReader.getBoolean("noscoreboardnumbers")) {
            context.getMatrices().push();
            double scoreboardsize = FileReader.getByFirstIntFirst("Double","scoreboardsize",0.5)+0.5;
            context.getMatrices().scale((float)scoreboardsize,(float)scoreboardsize,(float)scoreboardsize);
            double g = (1/scoreboardsize);
            int i;
            Scoreboard scoreboard = objective.getScoreboard();
            Collection<ScoreboardPlayerScore> collection = scoreboard.getAllPlayerScores(objective);
            List<ScoreboardPlayerScore> list = collection.stream().filter(score -> score.getPlayerName() != null && !score.getPlayerName().startsWith("#")).collect(Collectors.toList());
            collection = list.size() > 15 ? Lists.newArrayList(Iterables.skip(list, collection.size() - 15)) : list;
            ArrayList<Pair<ScoreboardPlayerScore, MutableText>> list2 = Lists.newArrayListWithCapacity(collection.size());
            Text text = objective.getDisplayName();
            int j = i = this.getTextRenderer().getWidth(text);
            int k = (int) (this.getTextRenderer().getWidth(SCOREBOARD_JOINER) * scoreboardsize);
            for (ScoreboardPlayerScore scoreboardPlayerScore : collection) {
                Team team = scoreboard.getPlayerTeam(scoreboardPlayerScore.getPlayerName());
                MutableText text2 = Team.decorateName(team, Text.literal(scoreboardPlayerScore.getPlayerName()));
                list2.add(Pair.of(scoreboardPlayerScore, text2));
                j = Math.max(j, this.getTextRenderer().getWidth(text2) + k + this.getTextRenderer().getWidth(Integer.toString(scoreboardPlayerScore.getScore())));
            }
            int l = collection.size() * this.getTextRenderer().fontHeight;
            int m = (int) ((this.scaledHeight*g) / 2 + l / 3);
            int o = (int) ((this.scaledWidth*g) - (j) - 3);
            int p = 0;
            int q = this.client.options.getTextBackgroundColor(0.3f);
            int r = this.client.options.getTextBackgroundColor(0.4f);
            for (Pair<ScoreboardPlayerScore, MutableText> pair : list2) {
                ScoreboardPlayerScore scoreboardPlayerScore2 = pair.getFirst();
                Text text3 = pair.getSecond();
                String string = "" + Formatting.RED + scoreboardPlayerScore2.getScore();
                if(FileReader.getBoolean("noscoreboardnumbers")) string="";
                int t = m - ++p * this.getTextRenderer().fontHeight;
                int u = (int) (this.scaledWidth*g - 3 + 2);
                context.fill(o - 2, t, u, t + this.getTextRenderer().fontHeight, q);
                context.drawText(getTextRenderer(),text3, o, t, -1,false);
                context.drawText(getTextRenderer(), string, (u - this.getTextRenderer().getWidth(string)), t, -1,false);
                if (p != collection.size()) continue;
                context.fill(o - 2, t - this.getTextRenderer().fontHeight - 1, u, t - 1, r);
                context.fill(o - 2, t - 1, u, t, q);
                context.drawText(getTextRenderer(),text, (o + j / 2 - i / 2), (t - this.getTextRenderer().fontHeight), -1,false);
            }
            context.getMatrices().pop();
            ci.cancel();
        }
    }

    @Inject(method = "renderCrosshair",at=@At("HEAD"),cancellable = true)
    public void inject(DrawContext context, CallbackInfo ci) {
        GameOptions gameOptions = this.client.options;
        if(FileReader.getBoolean("custom_crosshair") && gameOptions.getPerspective().isFirstPerson() && (Objects.requireNonNull(this.client.interactionManager).getCurrentGameMode() != GameMode.SPECTATOR || this.shouldRenderSpectatorCrosshair(this.client.crosshairTarget)) && !gameOptions.debugEnabled && !gameOptions.hudHidden && !Objects.requireNonNull(this.client.player).hasReducedDebugInfo() && !(Boolean)gameOptions.getReducedDebugInfo().getValue()) {
            for (int d = 0; d < LegacyCrosshairScreen.p; d++) {
                for (int j = 0; j < LegacyCrosshairScreen.p; j++) {
                    int i = LegacyCrosshairScreen.pixels[d][j];
                    if (i != 0) {
                        double size = 20;
                        float scale = 0.04f * LegacyCrosshairScreen.s;
                        int width = (int) (scaledWidth / scale);
                        int height = (int) (scaledHeight / scale);
                        context.getMatrices().scale(scale, scale, scale);
                        context.fill((int) (width / 2 - size + (d * ((size * 2) / LegacyCrosshairScreen.p))), (int) (height / 2 - size + (j * (size * 2 / LegacyCrosshairScreen.p))), (int) (width / 2 - size + ((d + 1) * ((size * 2) / LegacyCrosshairScreen.p))), (int) (height / 2 - size + ((j + 1) * ((size * 2) / LegacyCrosshairScreen.p))), 0xFF000000 + i);
                        context.getMatrices().scale(1 / scale, 1 / scale, 1 / scale);
                    }
                }
            }
            ci.cancel();
        }
    }
}
