package bewis09.hud;

import bewis09.drawable.OptionButtonWidget;
import bewis09.drawable.OptionSliderWidget;
import bewis09.option.BooleanOption;
import bewis09.option.Option;
import bewis09.option.SliderOption;
import bewis09.option.SwitchOption;
import bewis09.screen.WidgetConfigScreen;
import bewis09.screen.WidgetScreen;
import bewis09.util.ExtraInfo;
import bewis09.util.FileReader;
import bewis09.util.TextHelper;
import net.fabricmc.fabric.api.mininglevel.v1.FabricMineableTags;
import net.fabricmc.fabric.api.mininglevel.v1.MiningLevelManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;

import java.util.ArrayList;
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
            if (maxhealth > 13.0) {
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
        if(block.getDefaultState().isIn(BlockTags.AXE_MINEABLE)) return "Tool: Axe";
        if(block.getDefaultState().isIn(BlockTags.PICKAXE_MINEABLE)) return "Tool: Pickaxe";
        if(block.getDefaultState().isIn(BlockTags.HOE_MINEABLE)) return "Tool: Hoe";
        if(block.getDefaultState().isIn(BlockTags.SHOVEL_MINEABLE)) return "Tool: Shovel";
        if(block.getDefaultState().isIn(FabricMineableTags.SHEARS_MINEABLE)) return "Tool: Shears";
        if(block.getDefaultState().isIn(FabricMineableTags.SWORD_MINEABLE)) return "Tool: Sword";
        return "Tool: None";
    }

    public static String getLevel(Block block) {
        int level = (MiningLevelManager.getRequiredMiningLevel(block.getDefaultState())==-1 ? block.getDefaultState().isToolRequired() ? 0 : -1 : MiningLevelManager.getRequiredMiningLevel(block.getDefaultState()))+1;
        Map<Integer, String> map = Map.of(
                0,"Mining Level: None",
                1,"Mining Level: Wood",
                2,"Mining Level: Stone",
                3,"Mining Level: Iron",
                4,"Mining Level: Diamond"
        );
        return map.getOrDefault(level,"Mining Level: Other");
    }

    @Override
    public String getId() {
        return "TIWYLA";
    }

    @Override
    public void paint(DrawContext context) {
        super.paint(context);
        if (getTarget() instanceof BlockHitResult hit && getTarget().getType() == HitResult.Type.BLOCK && !FileReader.getBoolean("block_tiwyla_icons")) {
            assert MinecraftClient.getInstance().world != null;
            context.drawItem(MinecraftClient.getInstance().world.getBlockState(hit.getBlockPos()).getBlock().asItem().getDefaultStack(),getX()+5,getY()+10);
        } else if (getTarget() instanceof EntityHitResult hit && getTarget().getType() == HitResult.Type.ENTITY && hit.getEntity() instanceof LivingEntity livingEntity && !FileReader.getBoolean("entity_tiwyla_icons")) {
            drawEntity(context,getX()+12,getY()+18, Math.min((int) (10/Math.pow(livingEntity.getHeight(),0.57)),(int) (10/Math.pow(livingEntity.getWidth(),0.57))), livingEntity);
        }
    }

    private HitResult getTarget() {
        return MinecraftClient.getInstance().currentScreen instanceof WidgetScreen || MinecraftClient.getInstance().currentScreen instanceof WidgetConfigScreen ? new BlockHitResult(new Vec3d(0,-100,0), Direction.DOWN,new BlockPos(0,-100,0),false) : MinecraftClient.getInstance().crosshairTarget;
    }

    public static void drawEntity(DrawContext context, int x, int y, int size, LivingEntity entity) {
        if (entity!=null && MinecraftClient.getInstance().getEntityRenderDispatcher().camera != null) {
            Quaternionf quaternionf = (new Quaternionf()).rotateZ((float) Math.PI);
            Quaternionf quaternionf2 = (new Quaternionf()).rotateX((float) (-Math.PI/6f));
            quaternionf.mul(quaternionf2);
            float h = entity.bodyYaw;
            float i = entity.getYaw();
            float j = entity.getPitch();
            float k = entity.prevHeadYaw;
            float l = entity.headYaw;
            entity.bodyYaw = 180.0F - 45;
            entity.setYaw(180.0F - 45);
            entity.setPitch(0);
            entity.headYaw = entity.getYaw();
            entity.prevHeadYaw = entity.getYaw();
            InventoryScreen.drawEntity(context, x, y, size, quaternionf, quaternionf2, entity);
            entity.bodyYaw = h;
            entity.setYaw(i);
            entity.setPitch(j);
            entity.prevHeadYaw = k;
            entity.headYaw = l;
        }
    }

    @Override
    public boolean sizeCustom() {
        return false;
    }

    @Override
    public float getDefSize() {
        return 1f;
    }

    @Override
    public int getHeight() {
        return getTexts().size()*11+(getTexts().isEmpty()?0:3);
    }

    @Override
    public int getWidth() {
        if(getTarget() instanceof BlockHitResult hit && getTarget().getType() == HitResult.Type.BLOCK) {
            assert MinecraftClient.getInstance().world != null;
            BlockState block = MinecraftClient.getInstance().world.getBlockState(hit.getBlockPos());
            return Math.max(super.getWidth(),MinecraftClient.getInstance().textRenderer.getWidth(block.getBlock().getName())+50);
        }
        return super.getWidth();
    }

    @Override
    public List<Text> getTexts() {
        assert getTarget() != null;
        if(getTarget() instanceof BlockHitResult hit) {
            assert MinecraftClient.getInstance().world != null;
            BlockState block = MinecraftClient.getInstance().world.getBlockState(hit.getBlockPos());
            return getTarget().getType()== HitResult.Type.MISS ? List.of() : getBlockList(block);
        } else if (getTarget() instanceof EntityHitResult hit) {
            return List.of(
                    hit.getEntity().getName(),
                    hit.getEntity() instanceof LivingEntity livingEntity ? convertToHearths(livingEntity.getHealth(),livingEntity.getMaxHealth(),livingEntity.getAbsorptionAmount()) : Text.of("")
            );
        } else {
            return List.of();
        }
    }

    public List<Text> getBlockList(BlockState state) {
        List<Text> list = new ArrayList<>(List.of(state.getBlock().getName()));
        list.add(ExtraInfo.get(state.getBlock()).getFirst(state));
        list.add(ExtraInfo.get(state.getBlock()).getSecond(state));
        return list;
    }

    @Override
    public List<Option> getOptions() {
        ArrayList<Option> list = new ArrayList<>();
        list.add(new SliderOption(TextHelper.getText("alpha"),"Alpha_"+getId(),1, (float) 0x96 / 0xFF,0,2){
            @Override
            public void clickExtra(double value) {
                alpha = value;
                FileReader.setByFirst("Boolean","DefaultAlpha_"+getId(),false);
                ((BooleanOption)list.get(3)).bl = false;
                ((OptionButtonWidget)list.get(3).getButtons().get(0)).setMessage(Text.of("FALSE"));
            }
        });
        list.add(new BooleanOption(TextHelper.getText("block_tiwyla_icons"),"block_tiwyla_icons"));
        list.add(new BooleanOption(TextHelper.getText("entity_tiwyla_icons"),"entity_tiwyla_icons"));
        list.add(new BooleanOption(TextHelper.getText("default_alpha"),"DefaultAlpha_"+getId()){
            @Override
            public void clickExtra(boolean enabled) {
                if(enabled) {
                    alpha = 0x96/(float)0xFF;
                    FileReader.setByFirst("Double","Alpha_"+getId(),0x96/(float)0xFF);
                    ((OptionSliderWidget)list.get(0).getButtons().get(0)).setValue(alpha);
                    (this).bl = true;
                    ((OptionButtonWidget)this.getButtons().get(0)).setMessage(Text.of("TRUE"));
                    FileReader.setByFirst("Boolean", "DefaultAlpha_"+getId(), bl);
                }
            }
        });
        list.add(new SwitchOption(TextHelper.getText("first_tiwyla"),"first_tiwyla",List.of("tool","level","progress_tool","progress_level","extra_tool","extra_level")));
        list.add(new SwitchOption(TextHelper.getText("second_tiwyla"),"second_tiwyla",List.of("level","tool","progress_tool","progress_level","extra_tool","extra_level")));
        return list;
    }
}
