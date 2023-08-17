package bewis09.screen;

import bewis09.util.TextHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;

public class ContactScreen extends AbstractOptionScreen {

    private final Screen parent;
    public static String googleForm = "https://forms.gle/rRQVEyCLnUbaWYpS6";
    public static String discord = "https://discord.gg/kuUyGUeEZS";
    public static String github = "https://github.com/Bewis09/bewisclient/issues";
    public static String modrinth = "https://modrinth.com/mod/bewisclient";

    public ContactScreen(Screen parent) {
        this.parent = parent;
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(parent);
    }

    @Override
    protected void init() {
        this.addDrawableChild(ButtonWidget.builder(TextHelper.getText("feedback"), button -> {
            ConfirmLinkScreen.open(googleForm,this,true);
        }).dimensions(width/2-146,22,292,20).tooltip(Tooltip.of(TextHelper.getText("feedback_info"))).build());
        this.addDrawableChild(ButtonWidget.builder(TextHelper.getText("discord"), button -> {
            ConfirmLinkScreen.open(discord,this,true);
        }).dimensions(width/2-146,44,292,20).tooltip(Tooltip.of(TextHelper.getText("discord_info"))).build());
        this.addDrawableChild(ButtonWidget.builder(TextHelper.getText("issues"), button -> {
            ConfirmLinkScreen.open(github,this,true);
        }).dimensions(width/2-146,66,292,20).tooltip(Tooltip.of(TextHelper.getText("issues_info"))).build());
        this.addDrawableChild(ButtonWidget.builder(TextHelper.getText("modrinth"), button -> {
            ConfirmLinkScreen.open(modrinth,this,true);
        }).dimensions(width/2-146,88,292,20).tooltip(Tooltip.of(TextHelper.getText("modrinth_info"))).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        drawBackground(context);
        context.drawCenteredTextWithShadow(textRenderer,TextHelper.getText("select_contact_method"),width/2,7,-1);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public int maxHeight() {
        return 0;
    }
}
