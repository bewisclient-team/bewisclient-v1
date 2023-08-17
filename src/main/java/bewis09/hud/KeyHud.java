package bewis09.hud;

import bewis09.main.Main;
import bewis09.option.BooleanOption;
import bewis09.option.Option;
import bewis09.util.TextHelper;
import bewis09.util.FileReader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.GameOptions;

import java.util.List;

public class KeyHud extends HudElement {
    public KeyHud() {
        super(5,21,Horizontal.LEFT,Vertical.BOTTOM,64,86);
    }

    @Override
    public String getId() {
        return "KEY";
    }

    @Override
    public int getHeight() {
        return 42+ (FileReader.getBoolean("key_space")?17:0)+(FileReader.getBoolean("key_mouse")?17:0);
    }

    @Override
    public void paint(DrawContext context) {
        context.getMatrices().push();
        context.getMatrices().scale(getSize(),getSize(),getSize());
        GameOptions gameOptions = MinecraftClient.getInstance().options;
        context.fill(getX()+22,getY(),getX()+42,getY()+20,gameOptions.forwardKey.isPressed() ? getColorWithColor(0x7a7a7a) : getColor());
        context.fill(getX(),getY()+22,getX()+20,getY()+42,gameOptions.leftKey.isPressed() ? getColorWithColor(0x7a7a7a) : getColor());
        context.fill(getX()+22,getY()+22,getX()+42,getY()+42,gameOptions.backKey.isPressed() ? getColorWithColor(0x7a7a7a) : getColor());
        context.fill(getX()+44,getY()+22,getX()+64,getY()+42,gameOptions.rightKey.isPressed() ? getColorWithColor(0x7a7a7a) : getColor());
        if (FileReader.getBoolean("key_space"))
            context.fill(getX(),getY()+44,getX()+64,getY()+59,gameOptions.jumpKey.isPressed() ? getColorWithColor(0x7a7a7a) : getColor());
        if (FileReader.getBoolean("key_mouse")) {
            context.fill(getX(), getY() + 44+ (FileReader.getBoolean("key_space")?17:0), getX() + 31, getY() + 59+ (FileReader.getBoolean("key_space")?17:0), gameOptions.attackKey.isPressed() ? getColorWithColor(0x7a7a7a) : getColor());
            context.fill(getX() + 33, getY() + 44+ (FileReader.getBoolean("key_space")?17:0), getX() + 64, getY() + 59+ (FileReader.getBoolean("key_space")?17:0), gameOptions.useKey.isPressed() ? getColorWithColor(0x7a7a7a) : getColor());
        }
        context.getMatrices().pop();
        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        context.drawCenteredTextWithShadow(renderer, "W", (getX()+32), (getY()+4+3), -1);
        context.drawCenteredTextWithShadow(renderer, "A", (getX()+10), (getY()+26+3), -1);
        context.drawCenteredTextWithShadow(renderer, "S", (getX()+32), (getY()+26+3), -1);
        context.drawCenteredTextWithShadow(renderer, "D", (getX()+54), (getY()+26+3), -1);
        if (FileReader.getBoolean("key_space"))
            context.drawCenteredTextWithShadow(renderer, "SPACE", (getX()+32), (getY()+45+3), -1);
        if (FileReader.getBoolean("key_mouse")) {
            context.drawCenteredTextWithShadow(renderer, !FileReader.getBoolean("key_cps")?"LMB": Main.lCount() +" L", (getX() + 15), (getY() + 45 + 3)+ (FileReader.getBoolean("key_space")?17:0), -1);
            context.drawCenteredTextWithShadow(renderer, !FileReader.getBoolean("key_cps")?"RMB": Main.rCount() +" R", (getX() + 49), (getY() + 45 + 3)+ (FileReader.getBoolean("key_space")?17:0), -1);
        }
    }

    @Override
    public float getDefSize() {
        return 1f;
    }

    @Override
    public List<Option> getOptions() {
        List<Option> options = super.getOptions();
        options.add(new BooleanOption(TextHelper.getText("key_space"),"key_space"));
        options.add(new BooleanOption(TextHelper.getText("key_mouse"),"key_mouse"));
        options.add(new BooleanOption(TextHelper.getText("key_cps"),"key_cps"));
        return options;
    }
}