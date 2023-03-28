package bewis09.screen;

import bewis09.cape.AbstractCape;
import bewis09.cape.Cape;
import bewis09.cape.Capes;
import bewis09.hat.Hat;
import bewis09.util.FileReader;
import bewis09.wings.Wing;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import org.joml.Quaternionf;
import org.lwjgl.glfw.GLFW;

public class CosmeticsScreen extends Screen {

    boolean isReversed = false;
    private double scrollY = 0;

    protected CosmeticsScreen() {
        super(Text.empty());
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        assert client != null;
        assert client.player != null;
        fill(matrices,13, height/2-70,83, height/2+70,0xFF000000);
        drawBorder(matrices,13, height/2-70,70,140,-1);
        drawEntity(matrices,48, height/2+50,50,(isReversed?1:-1)*(mouseX-48), height / 2f - mouseY,client.player, isReversed);
        fill(matrices,100,0,width,height,0x55000000);
        int z = 0;
        boolean bl = false;
        ItemStack stack = null;
        Wing d = Wing.current_wing;
        if(client.player.getInventory().getArmorStack(2).getItem()== Items.ELYTRA) {
            bl=true;
            stack = client.player.getInventory().getArmorStack(2);
            client.player.getInventory().armor.set(2,ItemStack.EMPTY);
        }
        Wing.current_wing=Wing.EMPTY;
        drawBorder(matrices,109, (int) (getScrollY()+5),64*Capes.CAPES.length+5,120,-1);
        fill(matrices,109, (int) (getScrollY()+5),109+64*Capes.CAPES.length+5, (int) (getScrollY()+125),0xAA000000);
        drawBorder(matrices,109, (int) (getScrollY()+135),64*Wing.WINGS.length+5,120,-1);
        fill(matrices,109, (int) (getScrollY()+135),109+64*Wing.WINGS.length+5, (int) (getScrollY()+255),0xAA000000);
        drawBorder(matrices,109, (int) (getScrollY()+265),64*Hat.HATS.length+5,120,-1);
        fill(matrices,109, (int) (getScrollY()+265),109+64*Hat.HATS.length+5, (int) (getScrollY()+385),0xAA000000);
        Hat hat = Hat.current_hat;
        Hat.current_hat = Hat.EMPTY;
        for (AbstractCape c : Capes.CAPES) {
            z++;
            Cape.setCurrentCape(c);
            fill(matrices,50+z*64, (int) (getScrollY()+10),50+z*64+60, (int) (getScrollY()+110),0xFF000000);
            drawBorder(matrices,50+z*64, (int) (getScrollY()+10),60,100,-1);
            drawEntity(matrices,80+z*64, (int) (getScrollY()+90),35, (float) (mouseX - (30 + z * 64) -32), (float) (30 - mouseY+getScrollY()),client.player, true);
        }
        z=0;
        Cape.setCurrentCape(null);
        for (Wing w : Wing.WINGS) {
            z++;
            Wing.current_wing = w;
            fill(matrices,50+z*64, (int) (getScrollY()+140),50+z*64+60, (int) (getScrollY()+240),0xFF000000);
            drawBorder(matrices,50+z*64, (int) (getScrollY()+140),60,100,-1);
            drawEntity(matrices,80+z*64, (int) (getScrollY()+220),35, (float) (mouseX - (30 + z * 64) -32), (float) (160 - mouseY+getScrollY()),client.player, true);
        }
        Wing.current_wing = Wing.EMPTY;
        z=0;
        for (Hat w : Hat.HATS) {
            z++;
            Hat.current_hat = w;
            fill(matrices,50+z*64, (int) (getScrollY()+270),50+z*64+60, (int) (getScrollY()+370),0xFF000000);
            drawBorder(matrices,50+z*64, (int) (getScrollY()+270),60,100,-1);
            drawEntityForMoved(matrices,80+z*64, (int) (getScrollY()+350),35, (float) (mouseX - (30 + z * 64) -32), (float) (290 - mouseY+getScrollY()),client.player, false);
        }
        if(bl)
            client.player.getInventory().armor.set(2,stack);
        Cape.setCurrentCape(Cape.getCurrentRealCape());
        Wing.current_wing = d;
        drawScrollbar(matrices);
        Hat.current_hat = hat;
        super.render(matrices, mouseX, mouseY, delta);
    }

    private double getScrollY() {
        return -scrollY;
    }

    @Override
    protected void init() {
        scrollY = Math.max(0,Math.min(-getMaxScrollY(),scrollY));
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("bewis.option.rotate"), button -> isReversed =!isReversed).dimensions(13, height/2+74,70,20).build());
        int z = 0;
        for (AbstractCape c : Capes.CAPES) {
            z++;
            int finalZ = z;
            this.addDrawableChild(ButtonWidget.builder(Text.translatable("bewis.option.use"), button -> {
                Cape.setCurrentRealCape(c);
                FileReader.setByFirst("Cosmetics","Cape", finalZ -1);
            }).dimensions(50+z*64+10, (int) (getScrollY()+100),40,20).build());
        }
        z=0;
        for (Wing c : Wing.WINGS) {
            z++;
            int finalZ1 = z;
            this.addDrawableChild(ButtonWidget.builder(Text.translatable("bewis.option.use"), button -> {
                Wing.current_wing = c;
                FileReader.setByFirst("Cosmetics","Wing", finalZ1 -1);
            }).dimensions(50+z*64+10, (int) (getScrollY()+230),40,20).build());
        }
        z=0;
        for (Hat c : Hat.HATS) {
            z++;
            int finalZ2 = z;
            this.addDrawableChild(ButtonWidget.builder(Text.translatable("bewis.option.use"), button -> {
                Hat.current_hat = c;
                FileReader.setByFirst("Cosmetics","Hat", finalZ2 -1);
            }).dimensions(50+z*64+10, (int) (getScrollY()+360),40,20).build());
        }
    }

    public void drawEntity(MatrixStack matrices, int x, int y, int size, float mouseX, float mouseY, LivingEntity entity, boolean isReversed) {
        float f = (float)Math.atan(mouseX / 40.0F);
        float g = (float)Math.atan(mouseY / 40.0F);
        Quaternionf quaternionf = (new Quaternionf()).rotateZ(3.1415927F);
        Quaternionf quaternionf2 = (new Quaternionf()).rotateX(g * 20.0F * 0.017453292F);
        quaternionf.mul(quaternionf2);
        float h = entity.bodyYaw;
        float i = entity.getYaw();
        float j = entity.getPitch();
        float k = entity.prevHeadYaw;
        float l = entity.headYaw;
        entity.bodyYaw = (isReversed?0:(180.0F)) + f * 20.0F;
        entity.setYaw((isReversed?0:(180.0F)) + f * 40.0F);
        entity.setPitch(-g * 20.0F);
        entity.headYaw = entity.getYaw();
        entity.prevHeadYaw = entity.getYaw();
        InventoryScreen.drawEntity(matrices, x, y, size, quaternionf, quaternionf2, entity);
        entity.bodyYaw = h;
        entity.setYaw(i);
        entity.setPitch(j);
        entity.prevHeadYaw = k;
        entity.headYaw = l;
    }

    public void drawEntityForMoved(MatrixStack matrices, int x, int y, int size, float mouseX, float mouseY, LivingEntity entity, boolean isReversed) {
        float f = mouseX%360-180;
        float g = (float)Math.atan(mouseY / 40.0F);
        Quaternionf quaternionf = (new Quaternionf()).rotateZ(3.1415927F);
        Quaternionf quaternionf2 = (new Quaternionf()).rotateX(g * 20.0F * 0.017453292F);
        quaternionf.mul(quaternionf2);
        float h = entity.bodyYaw;
        float i = entity.getYaw();
        float j = entity.getPitch();
        float k = entity.prevHeadYaw;
        float l = entity.headYaw;
        entity.bodyYaw = (isReversed?0:(180.0F)) + f;
        entity.setYaw((isReversed?0:(180.0F)) + f);
        entity.setPitch(-g * 20.0F);
        entity.headYaw = entity.getYaw();
        entity.prevHeadYaw = entity.getYaw();
        InventoryScreen.drawEntity(matrices, x, y, size, quaternionf, quaternionf2, entity);
        entity.bodyYaw = h;
        entity.setYaw(i);
        entity.setPitch(j);
        entity.prevHeadYaw = k;
        entity.headYaw = l;
    }

    private void drawScrollbar(MatrixStack matrices) {
        int i = this.getScrollbarThumbHeight();
        System.out.println(scrollY);
        int j = 91 + 4;
        int k = 91 + 4 + 8;
        int l = 0;
        if(getMaxScrollY()!=0)
            l = (int) Math.max(0, (-this.scrollY) * (this.height - i) / this.getMaxScrollY());
        int m = l + i;
        fill(matrices, j, l, k, m, -8355712);
        fill(matrices, j, l, k - 1, m - 1, -4144960);
    }

    private int getMaxScrollY() {
        return -Math.max(0,this.getContentsHeight() - height);
    }

    private int getScrollbarThumbHeight() {
        return (int) ((height*height)/(float)getContentsHeight());
    }

    public int getContentsHeight() {
        return 395;
    }

    private void setScrollY(double maxScrollY) {
        scrollY = Math.max(0,Math.min(-getMaxScrollY(),maxScrollY));
        clearAndInit();
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if(mouseX>91)
            setScrollY(this.scrollY - amount * this.getDeltaYPerScroll());
        return true;
    }

    private double getDeltaYPerScroll() {
        return 20;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean bl = keyCode == GLFW.GLFW_KEY_UP;
        boolean bl2 = keyCode == GLFW.GLFW_KEY_DOWN;
        if (bl || bl2) {
            double d = this.scrollY;
            this.setScrollY(this.scrollY + (double)(bl ? -1 : 1) * this.getDeltaYPerScroll());
            if (d != this.scrollY) {
                return true;
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
