package bewis09.drawable;

import bewis09.util.TextHelper;
import bewis09.screen.NewsScreen;
import bewis09.screen.NotInWorldScreen;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import static bewis09.util.HttpInputStramGetter.getInputStream;

public record OptionScreenClickableWidget(Text text, PressAction action, @Nullable Identifier picture, boolean red) {

    static Identifier warning = new Identifier("bewisclient","gui/screen/warning.png");
    static Text version = TextHelper.getText("new_update");
    public static boolean newVersion;

    static {
        try {
            Reader reader = new InputStreamReader(getInputStream("https://api.modrinth.com/v2/project/bewisclient/version"));
            Gson gson = new GsonBuilder().create();
            NewsScreen.VersionItem[] list = gson.fromJson(reader, NewsScreen.VersionItem[].class);

            for (NewsScreen.VersionItem item : list) {
                if (item.version_type.equals("release") || FabricLoader.getInstance().getModContainer("bewisclient").get().getMetadata().getVersion().getFriendlyString().contains("beta")) {
                    if (!item.version_number.split("-")[0].equals(FabricLoader.getInstance().getModContainer("bewisclient").orElseThrow().getMetadata().getVersion().getFriendlyString().split("-")[0])) {
                        newVersion = true;
                    } else {
                        break;
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void render(DrawContext context, int width, int index, double scroll, int mouseX, int mouseY) {
        context.fill(width/2-142+(index%4)*73, (int) (((index/4)*73)+8-scroll),width/2-142+(index%4)*73+65, (int) (((index/4)*73)+8+65-scroll), red&& MinecraftClient.getInstance().world==null ? 0xAA880000 : 0xAA777777);
        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, text,width/2-142+(index%4)*73+32,(int) (((index/4)*73)+8+53-scroll),-1);
        if(mouseX>width/2-142+(index%4)*73 && mouseX<width/2-142+(index%4)*73+65 && mouseY>(int) (((index/4)*73)+8-scroll) && mouseY<(int) (((index/4)*73)+8+65-scroll)) {
            context.drawBorder(width/2-142+(index%4)*73, (int) (((index/4)*73)+8-scroll),65,65,0xDD000000);
        } else {
            context.drawBorder(width/2-142+(index%4)*73, (int) (((index/4)*73)+8-scroll),65,65,0xAABBBBBB);
        }
        if(picture != null) {
            context.drawTexture(picture,width/2-142+(index%4)*73+14,(int) (((index/4)*73)+8-scroll)+8,37,37,0,0,37,37,37,37);
            if(picture.getPath().equals("gui/icons/news.png") && newVersion) {
                int x = width / 2 - 142 + (index % 4) * 73 + 65 - 14;
                if (mouseX > x && mouseY > (int) (((index/4)*73)+8+65-scroll)-14 && mouseX < width/2-142+(index%4)*73+65-2 && mouseY < (int) (((index/4)*73)+8+65-scroll)-2)
                    context.drawTooltip(MinecraftClient.getInstance().textRenderer, version, mouseX, mouseY);
                context.drawTexture(warning, x,(int) (((index/4)*73)+8+65-scroll)-14,12,12,0,0,12,12,12,12);
            }
        }
    }

    public interface PressAction {
        Screen pressToScreen(Screen parent);
    }

    public static void playDownSound(SoundManager soundManager) {
        soundManager.play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    public void clickAll(int mouseX, int mouseY, int width, int index, int scroll) {
        if(mouseX>width/2-142+(index%4)*73 && mouseX<width/2-142+(index%4)*73+65 && mouseY>(int) (((index/4)*73)+8-scroll) && mouseY<(int) (((index/4)*73)+8+65-scroll)) {
            if(action!=null) {
                if (red && MinecraftClient.getInstance().world == null) MinecraftClient.getInstance().setScreen(new NotInWorldScreen(MinecraftClient.getInstance().currentScreen));
                else MinecraftClient.getInstance().setScreen(action.pressToScreen(MinecraftClient.getInstance().currentScreen));
            }
            playDownSound(MinecraftClient.getInstance().getSoundManager());
        }
    }
}
