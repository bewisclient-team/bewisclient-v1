package bewis09.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class OptionScreen extends Screen {

    double ext1 = 0;
    double ext2 = 0;
    double ext3 = 0;
    double ext4 = 0;
    double ext5 = 0;
    int current = -1;

    Identifier identifier = new Identifier("bewisclient","gui/screen/option_background.png");

    public OptionScreen() {
        super(Text.empty());
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        fill(matrices,0,height/2-56-2, (int) (100+ext1),height/2-36-2,0xFF000000);
        fill(matrices,0,height/2-32-2, (int) (100+ext2),height/2-12-2,0xFF000000);
        fill(matrices,0,height/2-8-2, (int) (100+ext3),height/2+12-2,0xFF000000);
        fill(matrices,0,height/2+16-2, (int) (100+ext4),height/2+36-2,0xFF000000);
        fill(matrices,0,height/2+40-2, (int) (100+ext5),height/2+60-2,0xFF000000);

        fill(matrices, (int) (100+ext1),height/2-56-2, (int) (103+ext1),height/2-36-2,current==0 ? 0xFF00FF00 : -1);
        fill(matrices, (int) (100+ext2),height/2-32-2, (int) (103+ext2),height/2-12-2,current==1 ? 0xFF00FF00 : -1);
        fill(matrices, (int) (100+ext3),height/2-8-2, (int) (103+ext3),height/2+12-2,current==2 ? 0xFF00FF00 : -1);
        fill(matrices, (int) (100+ext4),height/2+16-2, (int) (103+ext4),height/2+36-2, current==3 ? 0xFF00FF00 : -1);
        fill(matrices, (int) (100+ext5),height/2+40-2, (int) (103+ext5),height/2+60-2, 0xFFFF0000);
        drawTextWithShadow(matrices,textRenderer, Text.translatable("bewis.option.widgets"),5,height/2-56-2+7,-1);
        drawTextWithShadow(matrices,textRenderer,Text.translatable("bewis.option.render"),5,height/2-32-2+7,-1);
        drawTextWithShadow(matrices,textRenderer,Text.translatable("bewis.option.mods"),5,height/2-8-2+7,-1);
        drawTextWithShadow(matrices,textRenderer,Text.translatable("bewis.option.cosmetics"),5,height/2+16-2+7,-1);
        drawTextWithShadow(matrices,textRenderer,Text.translatable("bewis.option.others"),5,height/2+40-2+7,-1);
        ext1 *= 0.95f;
        ext2 *= 0.95f;
        ext3 *= 0.95f;
        ext4 *= 0.95f;
        ext5 *= 0.95f;
        current = -1;
        if(mouseX<100+ext1 && mouseY < height/2-36-2 && mouseY > height/2-56-2) {
            ext1++;
            current = 0;
        }
        if(mouseX<100+ext2 && mouseY < height/2-12-2 && mouseY > height/2-32-2) {
            ext2++;
            current = 1;
        }
        if(mouseX<100+ext3 && mouseY < height/2+12-2 && mouseY > height/2-8-2) {
            ext3++;
            current = 2;
        }
        if(mouseX<100+ext4 && mouseY < height/2+36-2 && mouseY > height/2+16-2) {
            ext4++;
            current = 3;
        }
        if(mouseX<100+ext5 && mouseY < height/2+60-2 && mouseY > height/2+40-2) {
            ext5++;
            current = 4;
        }
        RenderSystem.setShaderTexture(0,identifier);
        int widthForPic = (int) ((width>height ? 1009f/height*(width-150) : width)*0.8f);
        int heightForPic = (int) (widthForPic*(height/(float)(width-150)));
        widthForPic = (int) Math.min(widthForPic,1920*0.8f);
        heightForPic = (int) Math.min(heightForPic,1009*0.8f);
        drawTexture(matrices,150,0,width-150,height, (float) 1920/2-widthForPic/2f+(mouseX-width/2f)/10f, 1009/2f-heightForPic/2f+(mouseY-height/2f)/10f,widthForPic, heightForPic,1920,1009);
        drawVerticalLine(matrices,150,0,height,-1);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(mouseX<100+ext1 && mouseY < height/2f-36-2 && mouseY > height/2f-56-2) {
            MinecraftClient.getInstance().setScreen(new WidgetScreen());
        }
        if(mouseX<100+ext2 && mouseY < height/2f-12-2 && mouseY > height/2f-32-2) {
            MinecraftClient.getInstance().setScreen(new OptionListScreen(LegacyOptionScreen.getOptions()));
        }
        if(mouseX<100+ext3 && mouseY < height/2f+12-2 && mouseY > height/2f-8-2) {
            MinecraftClient.getInstance().setScreen(new ModScreen(this));
        }
        if(mouseX<100+ext4 && mouseY < height/2f+36-2 && mouseY > height/2f+16-2) {
            MinecraftClient.getInstance().setScreen(new CosmeticsScreen());
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
