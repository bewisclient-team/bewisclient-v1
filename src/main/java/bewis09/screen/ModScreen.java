package bewis09.screen;

import bewis09.modmenu.ModMenu;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.Person;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ModScreen extends Screen {
    private final Screen parent;
    public double scroll = 0;

    public List<ButtonWidget> list = new ArrayList<>();
    public List<ButtonWidget> list2 = new ArrayList<>();
    public List<ButtonWidget> list3 = new ArrayList<>();
    public static Identifier MOD_BUTTONS = new Identifier("bewisclient","gui/modmenu_buttons.png");

    public static final List<String> EXCLUDED_MODS = List.of(
        "fabric-crash-report-info-v1",
            "fabric-registry-sync-v0",
            "fabric-convention-tags-v1",
            "fabric-events-interaction-v0",
            "fabric-rendering-fluids-v1",
            "fabric-rendering-data-attachment-v1",
            "fabric-blockrenderlayer-v1",
            "fabric-rendering-v1",
            "fabric-resource-loader-v0",
            "fabric-lifecycle-events-v1",
            "fabric-entity-events-v1",
            "fabric-particles-v1",
            "fabric-transitive-access-wideners-v1",
            "fabric-content-registries-v0",
            "fabric-dimensions-v1",
            "fabric-models-v0",
            "fabric-renderer-indigo"
    );

    public ModScreen(Screen parent) {
        super(Text.of(""));
        this.parent = parent;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        int i = -1;
        for (ModContainer mod : getAllDisplayMods()) {
            i++;
            fill(matrices,width/2-200, (int) (i*80-scroll)+5,width/2+200,(int) (i*80-scroll)+80,0xFF000000);
            drawBorder(matrices,width/2-200, (int) (i*80-scroll)+5,400,75,-1);
            drawCenteredTextWithShadow(matrices,textRenderer,Text.literal(mod.getMetadata().getName()+" ").setStyle(Style.EMPTY.withColor(Formatting.WHITE)).append(Text.literal(mod.getMetadata().getId()).setStyle(Style.EMPTY.withColor(0x555555))),width/2,(int) (i*80-scroll)+10,0);
            drawHorizontalLine(matrices,width/2-195,width/2+195,(int) (i*80-scroll)+20,0xAAAAAAAA);
            int p = -1;
            for (OrderedText text : textRenderer.wrapLines(StringVisitable.plain(mod.getMetadata().getDescription()),380)) {
                p++;
                if(p==4) break;
                drawTextWithShadow(matrices,textRenderer,text,width/2-195,(int) (i*80-scroll)+25+(13*p),0xDDDDDD);
            }
        }
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        scroll = Math.max(0,Math.min(scroll+amount*-10,(80*getAllDisplayMods().size())-height+5));
        updateButtons();
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    protected void init() {
        list = new ArrayList<>();
        list2 = new ArrayList<>();
        list3 = new ArrayList<>();
        for (ModContainer container : getAllDisplayMods()) {
            TexturedButtonWidget widget = addDrawableChild(new TexturedButtonWidget(width/2+175,0,20,20,0,ModMenu.screenMap.containsKey(container)?0:40,ModMenu.screenMap.containsKey(container)?20:0,MOD_BUTTONS,60,60,(a)->{
                if (ModMenu.screenMap.containsKey(container)) {
                    MinecraftClient.getInstance().setScreen(ModMenu.screenMap.get(container).create(this));
                }
            }));
            list.add(widget);
            ButtonWidget widget2 = addDrawableChild(new TexturedButtonWidget(width/2+151,0,20,20,20,container.getMetadata().getAuthors().size()==0?40:0,container.getMetadata().getAuthors().size()==0?0:20,MOD_BUTTONS,60,60,(a)->{}){
                @Override
                public boolean mouseClicked(double mouseX, double mouseY, int button) {
                    return false;
                }
            });
            if(container.getMetadata().getAuthors().size()!=0)
                widget2.setTooltip(Tooltip.of(Text.of("Authors:"+toTextListFromAuthor(container.getMetadata().getAuthors()))));
            widget2.active = container.getMetadata().getAuthors().size()!=0;
            list2.add(widget2);
            ButtonWidget widget3 = addDrawableChild(new TexturedButtonWidget(width/2+127,0,20,20,40, container.getMetadata().getContact().get("homepage").isEmpty() ?40:0, container.getMetadata().getContact().get("homepage").isEmpty() ?0:20,MOD_BUTTONS,60,60,(a)->{
                assert client != null;
                client.setScreen(new ConfirmLinkScreen(b -> {
                    if(b) {
                        Util.getOperatingSystem().open(container.getMetadata().getContact().get("homepage").orElse(""));
                    }
                    client.setScreen(this);
                },container.getMetadata().getContact().get("homepage").orElse(""),true));
            }));
            widget3.setTooltip(Tooltip.of(Text.of(container.getMetadata().getContact().get("homepage").orElse(""))));
            list3.add(widget3);
        }
        updateButtons();
    }

    public String toTextListFromAuthor(Collection<Person> authors) {
        StringBuilder builder = new StringBuilder();
        for (Person p : authors) {
            builder.append("\n").append(p.getName());
        }
        return builder.toString();
    }

    public void updateButtons() {
        int i = -1;
        for (ButtonWidget buttonWidget : list) {
            i++;
            buttonWidget.setY((int) (i*80-scroll)+55);
        }
        i = -1;
        for (ButtonWidget buttonWidget : list2) {
            i++;
            buttonWidget.setY((int) (i*80-scroll)+55);
        }
        i = -1;
        for (ButtonWidget buttonWidget : list3) {
            i++;
            buttonWidget.setY((int) (i*80-scroll)+55);
        }
    }

    @Override
    public void close() {
        assert client != null;
        client.setScreen(parent);
    }

    public List<ModContainer> getAllDisplayMods() {
        List<ModContainer> modContainers = new ArrayList<>();
        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            if(!(mod.getMetadata().getId().contains("fabric") && mod.getMetadata().getId().contains("api")) && !EXCLUDED_MODS.contains(mod.getMetadata().getId()))
                modContainers.add(mod);
        }
        return modContainers;
    }
}
