package bewis09.hud;

import bewis09.drawable.OptionButtonWidget;
import bewis09.drawable.OptionSliderWidget;
import bewis09.option.BooleanOption;
import bewis09.option.Option;
import bewis09.option.SliderOption;
import bewis09.util.TextHelper;
import bewis09.util.FileReader;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class InventoryHud extends HudElement {
    public InventoryHud() {
        super(5,5,Horizontal.RIGHT,Vertical.BOTTOM,180,60);
        this.alpha = FileReader.getByFirstIntFirst("Double","Alpha_"+getId(), 1);
    }

    @Override
    public String getId() {
        return "INVENTORY";
    }

    @Override
    public void paint(DrawContext context) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 3; j++) {
                RenderSystem.enableBlend();
                RenderSystem.setShaderColor(1, 1, 1, (float) alpha);
                context.drawTexture(new Identifier("bewisclient","gui/screen/slot.png"),getX()+i*20,getY()+j*20,0,0,20,20,20,20);
                assert MinecraftClient.getInstance().player != null;
                context.drawItem(MinecraftClient.getInstance().player.getInventory().getStack(j*9+i+9), getX()+i*20+2,getY()+j*20+2);
                context.drawItemInSlot(MinecraftClient.getInstance().textRenderer,MinecraftClient.getInstance().player.getInventory().getStack(j*9+i+9), getX()+i*20+2,getY()+j*20+2);
                RenderSystem.setShaderColor(1,1,1,1);
            }
        }
    }

    public List<Option> getOptions() {
        ArrayList<Option> list = new ArrayList<>();
        list.add(new SliderOption(TextHelper.getText("alpha"),"Alpha_"+getId(),1, (float) 0x96 / 0xFF,0,2){
            @Override
            public void clickExtra(double value) {
                alpha = value;
                FileReader.setByFirst("Boolean","DefaultAlpha_"+getId(),false);
                ((OptionButtonWidget)list.get(1).getButtons().get(0)).setMessage(Text.of("FALSE"));
                ((BooleanOption)list.get(1)).bl = false;
            }
        });
        list.add(new BooleanOption(TextHelper.getText("default_alpha"),"DefaultAlpha_"+getId()){
            @Override
            public void clickExtra(boolean enabled) {
                if(enabled) {
                    alpha = 1;
                    FileReader.setByFirst("Double","Alpha_"+getId(),1);
                    ((OptionSliderWidget)list.get(0).getButtons().get(0)).setValue(alpha);
                    ((OptionButtonWidget)this.getButtons().get(0)).setMessage(Text.of("TRUE"));
                } else {
                    ((OptionButtonWidget)this.getButtons().get(0)).setMessage(Text.of("FALSE"));
                }
            }
        });
        return list;
    }

    @Override
    public float getDefSize() {
        return 1f;
    }
}
