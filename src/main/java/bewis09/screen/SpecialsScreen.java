package bewis09.screen;

import bewis09.util.TextHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SpecialsScreen extends Screen {

    double scroll = 0;

    private final Screen parent;
    List<Map.Entry<Supplier<Screen>,Text>> supplierList = List.of(
            new AbstractMap.SimpleEntry<>(() -> new MacroScreen(SpecialsScreen.this), TextHelper.getText("macros")),
            new AbstractMap.SimpleEntry<>(() -> new LegacyCrosshairScreen(this), TextHelper.getText("crosshair")));

    public SpecialsScreen(Screen parent) {
        super(Text.empty());
        this.parent = parent;
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(parent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackgroundTexture(context);
        int p = -1;
        context.fill(width/4-10,0,width/4*3+10,height,0x77000000);
        context.drawVerticalLine(width/4-10,0,height,-1);
        context.drawVerticalLine(width/4*3+10,0,height,-1);
        for (Map.Entry<Supplier<Screen>,Text> entry : supplierList) {
            p++;
            context.fill(width/4, (int) ((p*44)+10+scroll), (int) (width*(3f/4)), (int) ((p*44)+50+scroll),0xFF000000);
            context.drawBorder(width/4, (int) ((p*44)+10+scroll), width/2,40,-1);
            context.drawCenteredTextWithShadow(textRenderer,entry.getValue(),width/2, (int) ((p*44)+16+scroll),-1);
        }
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    protected void init() {
        scroll=Math.min(Math.max(scroll,-((supplierList.size()*44)+16)+height),0);
        int p = -1;
        for (Map.Entry<Supplier<Screen>,Text> entry : supplierList) {
            p++;
            this.addDrawableChild(ButtonWidget.builder(ScreenTexts.PROCEED, button -> MinecraftClient.getInstance().setScreen(entry.getKey().get())).dimensions(width/2-50, (int) ((p*44)+27+scroll),100,20).build());
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        scroll+=amount*10;
        clearAndInit();
        return super.mouseScrolled(mouseX, mouseY, amount);
    }
}