package bewis09.hud;

import bewis09.util.ExtraInfo;
import net.fabricmc.fabric.api.mininglevel.v1.FabricMineableTags;
import net.fabricmc.fabric.api.mininglevel.v1.MiningLevelManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

import java.util.List;
import java.util.Map;

public class TiwylaHud extends LineHudElement {

    static Identifier identifier = new Identifier("extra");

    public TiwylaHud() {
        super(0,5,Horizontal.CENTER,Vertical.TOP,150, List.of(Text.of(""),Text.of(""),Text.of("")),true);
    }

    public static Text convertToHearths(double health, double maxhealth, double absorbtion) {
        try {
            maxhealth = roundUpAndHalf(maxhealth);
            health = ((double)(int) (health*10))/10f;
            absorbtion = ((double)(int) (absorbtion*10))/10f;
            if (maxhealth > 15.0) {
                return Text.literal((health + " / " + maxhealth * 2 + " HP"));
            }
            health = roundUpAndHalf(health);
            absorbtion = roundUpAndHalf(absorbtion);
            boolean isHalf = !(health == (int) health);
            boolean isAbso = !(absorbtion == (int) absorbtion);
            boolean isMaxHalf = !(maxhealth == (int) (((double) (int)(maxhealth * 2)) / 2));
            int maxhealthleft = (int) (maxhealth - (((int)health) + (isHalf?1:0))+(isMaxHalf?1:0));
            return Text.literal("❤".repeat((int) (health))).setStyle(Style.EMPTY.withColor(0xFF0000))
                    .append(Text.literal(isHalf ? "\uE0aa" : "").setStyle(Style.EMPTY.withFont(identifier).withColor(0xFFFFFF)))
                    .append(Text.literal("❤".repeat(maxhealthleft)).setStyle(Style.EMPTY.withColor(0xFFFFFF)))
                    .append(Text.literal("❤".repeat((int) (absorbtion))).setStyle(Style.EMPTY.withColor(0xFFFF00)))
                    .append(Text.literal(isAbso ? "\uE0ab" : "").setStyle(Style.EMPTY.withFont(identifier).withColor(0xFFFFFF)));
        } catch (Exception e) {
            return Text.of("");
        }
    }

    public static double roundUpAndHalf(double i) {
        return 500-((int)(1000-i))/2d;
    }

    public static String getTool(Block block) {
        if(block.getDefaultState().isIn(BlockTags.AXE_MINEABLE)) return "Axe";
        if(block.getDefaultState().isIn(BlockTags.PICKAXE_MINEABLE)) return "Pickaxe";
        if(block.getDefaultState().isIn(BlockTags.HOE_MINEABLE)) return "Hoe";
        if(block.getDefaultState().isIn(BlockTags.SHOVEL_MINEABLE)) return "Shovel";
        if(block.getDefaultState().isIn(FabricMineableTags.SHEARS_MINEABLE)) return "Shears";
        if(block.getDefaultState().isIn(FabricMineableTags.SWORD_MINEABLE)) return "Sword";
        return "None";
    }

    public static String getLevel(Block block) {
        int level = (MiningLevelManager.getRequiredMiningLevel(block.getDefaultState())==-1 ? block.getDefaultState().isToolRequired() ? 0 : -1 : MiningLevelManager.getRequiredMiningLevel(block.getDefaultState()))+1;
        Map<Integer, String> map = Map.of(
                0,"None",
                1,"Wood",
                2,"Stone",
                3,"Iron",
                4,"Diamond"
        );
        return map.getOrDefault(level,"other");
    }

    @Override
    public String getId() {
        return "TIWYLA";
    }

    @Override
    public void paint(MatrixStack stack) {
        super.paint(stack);
    }

    @Override
    public float getSize() {
        return 1f;
    }

    @Override
    public int getHeight() {
        return getTexts().size()*11+(getTexts().isEmpty()?0:3);
    }

    @Override
    public List<Text> getTexts() {
        assert MinecraftClient.getInstance().crosshairTarget != null;
        if(MinecraftClient.getInstance().crosshairTarget instanceof EntityHitResult hit) {
            return List.of(
                    hit.getEntity().getName(),
                    hit.getEntity() instanceof LivingEntity livingEntity ? convertToHearths(livingEntity.getHealth(),livingEntity.getMaxHealth(),livingEntity.getAbsorptionAmount()) : Text.of("")
            );
        } else if (MinecraftClient.getInstance().crosshairTarget instanceof BlockHitResult hit) {
            assert MinecraftClient.getInstance().world != null;
            BlockState block = MinecraftClient.getInstance().world.getBlockState(hit.getBlockPos());
            return MinecraftClient.getInstance().crosshairTarget.getType()== HitResult.Type.MISS ? List.of() : List.of(
                    block.getBlock().getName(),
                    ExtraInfo.get(block).getFirstLineText(block),
                    ExtraInfo.get(block).getSecondLineText(block)
            );
        } else {
            return List.of();
        }
    }
}
