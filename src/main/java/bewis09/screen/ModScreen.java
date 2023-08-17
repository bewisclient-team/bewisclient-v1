package bewis09.screen;

import bewis09.modmenu.ModMenu;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModOrigin;
import net.fabricmc.loader.api.metadata.Person;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ModScreen extends Screen {
    private final Screen parent;

    public double scroll = 0;

    public double goalscroll = 0;

    double offset = -1;

    public ModContainer currentMod = getAllDisplayMods().get(0);

    public ModContainer currentOutMod = getAllDisplayMods().get(0);

    public static Map<ModContainer, Identifier> map = new HashMap<>();

    public List<ButtonWidget> list = new ArrayList<>();

    public List<ModContainer> mods;

    ButtonWidget website;
    ButtonWidget issues;

    Identifier modchild = new Identifier("bewisclient","gui/modchild.png");

    public static Identifier MOD_BUTTONS = new Identifier("bewisclient","gui/modmenu_buttons.png");

    public ModScreen(Screen parent) {
        super(Text.of(""));
        this.parent = parent;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        scroll = (scroll*6+goalscroll)/7;
        if(offset!=-1) {
            scroll = (getMaxScrollY())*((mouseY-offset)/(height-getScrollbarThumbHeight()));
            scroll = Math.max(0,Math.min(scroll,(40*getAllDisplayMods().size())-height+5));
            goalscroll = scroll;
        }
        updateButtons(mouseX, mouseY);
        this.renderBackgroundTexture(context);
        int i = -1;
        for (ModContainer mod : getAllDisplayMods()) {
            i++;
            boolean isChild = mod.getOrigin().getKind() != ModOrigin.Kind.PATH;
            boolean isHovered = mouseX>(isChild ? 40 : 5)&&mouseX<width/2-5&&mouseY>(int) (i * 40 - scroll) + 5&&mouseY<(int) (i * 40 - scroll) + 40;
            if(isChild) {
                context.drawTexture(modchild,5,(int) (i * 40 - scroll)+5,35,35,0,0,35,35,35,35);
            }
            int x = (isChild ? 35 : 0) + (isHovered ? 3 : 5);
            context.fill(x, (int) (i * 40 - scroll) + (isHovered?3:5), width / 2 - (isHovered?3:5), (int) (i * 40 - scroll) + 40 + (isHovered?2:0), isHovered ? 0xFF222222 : 0xFF000000);
            context.drawBorder(x, (int) (i * 40 - scroll) + (isHovered?3:5), width / 2 - (2*(isHovered?3:5))-(isChild ? 35 : 0), 35+(isHovered?4:0), -1);
            context.drawCenteredTextWithShadow(textRenderer, Text.literal(mod.getMetadata().getName()), width / 4 + 15+(isChild ? 35/2 : 0), (int) (i * 40 - scroll) + 10 - (isHovered?2:0), -1);
            context.drawCenteredTextWithShadow(textRenderer, Text.literal(mod.getMetadata().getId()), width / 4 + 15+(isChild ? 35/2 : 0), (int) (i * 40 - scroll) + 23 - (isHovered?2:0), 0xFFAAAAAA);
            context.drawVerticalLine(39 + (isHovered?2:0) + (isChild ? 35 : 0), (int) (i * 40 - scroll) + (isHovered?3:5), (int) (i * 40 - scroll) + 40 +(isHovered?2:0), -1);
            NativeImage image;
            Identifier identifier = null;
            if (!map.containsKey(mod)) {
                try {
                    String p = (mod.getOrigin().getPaths().get(0).toString());
                    String o = mod.getPath(mod.getMetadata().getIconPath(128).orElseThrow()).toString().replaceFirst(".", "");

                    JarFile jarFile = new JarFile(p);

                    JarEntry entry = jarFile.getJarEntry(o);
                    InputStream inputStream = jarFile.getInputStream(entry);

                    image = NativeImage.read(inputStream);

                    inputStream.close();
                    jarFile.close();

                    assert this.client != null;

                    identifier = this.client.getTextureManager().registerDynamicTexture(mod.getMetadata().getId(), new NativeImageBackedTexture(image));

                } catch (Exception e) {
                    if (!(e instanceof NoSuchElementException)) {
                        try {
                            String o = mod.getPath(mod.getMetadata().getIconPath(128).orElseThrow()).toString().replace("/","\\");

                            assert this.client != null;

                            identifier = this.client.getTextureManager().registerDynamicTexture(mod.getMetadata().getId(), new NativeImageBackedTexture(NativeImage.read(new File(o).toURL().openStream())));
                        } catch (Exception ignored) {}
                    }
                }

                if(mod.getMetadata().getId().equals("minecraft"))
                    identifier = new Identifier("bewisclient","gui/minecraft.png");
                if(mod.getMetadata().getId().equals("java"))
                    identifier = new Identifier("bewisclient","gui/java.png");
                map.put(mod, identifier);
            }
            if (map.get(mod) != null) {
                context.drawTexture(map.get(mod),(isHovered?4:6) + (isChild ? 35 : 0), (int) (i * 40 - scroll) + (isHovered?4:6), 33+(isHovered?4:0), 33+(isHovered?4:0),0, 0,  33+(isHovered?4:0), 33+(isHovered?4:0), 33+(isHovered?4:0), 33+(isHovered?4:0));
            }
        }
        renderRightSite(context);
        drawScrollbar(context);
        super.render(context, mouseX, mouseY, delta);
    }

    private void renderRightSite(DrawContext context) {
        context.drawVerticalLine(width/2+10,0,height,-1);
        int middle = (width/2+width+65) /2;
        context.fill(width/2+15,5,width/2+15+50,55,0xFF000000);
        context.drawBorder(width/2+15,5,50,50,-1);
        if(map.get(currentMod) != null) {
            context.drawTexture(map.get(currentMod), width / 2 + 16, 6, 48, 48, 0, 0, 48, 48, 48, 48);
        }
        context.drawCenteredTextWithShadow(textRenderer,currentMod.getMetadata().getName(),middle,10,-1);
        context.drawCenteredTextWithShadow(textRenderer,currentMod.getMetadata().getId(),middle,25,0xFFAAAAAA);
        context.drawHorizontalLine(width/2+70,width-5,43,-1);
        int startY = 60;
        for (int j = 0; j < 6; j++) {
            context.drawHorizontalLine(width/2+15,width-5,startY,0xFFAAAAAA);
            List<OrderedText> list = null;
            switch (j) {
                case 0 -> list = textRenderer.wrapLines(StringVisitable.plain(currentMod.getMetadata().getDescription()), (int) (width/2f-20));
                case 1 -> list = textRenderer.wrapLines(StringVisitable.plain("Authors: "+toTextListFromAuthor(currentMod.getMetadata().getAuthors())), (int) (width/2f-20));
                case 2 -> list = textRenderer.wrapLines(StringVisitable.plain("Environment: "+currentMod.getMetadata().getEnvironment().name()), (int) (width/2f-20));
                case 3 -> list = textRenderer.wrapLines(StringVisitable.plain("Licence: "+currentMod.getMetadata().getLicense()), (int) (width/2f-20));
                case 4 -> list = textRenderer.wrapLines(StringVisitable.plain("Homepage: "+currentMod.getMetadata().getContact().asMap().getOrDefault("homepage","unknown")), (int) (width/2f-40));
                case 5 -> list = textRenderer.wrapLines(StringVisitable.plain("Issues: "+currentMod.getMetadata().getContact().asMap().getOrDefault("issues","unknown")), (int) (width/2f-40));
            }
            int i = -1;
            for (OrderedText text : list) {
                i++;
                context.drawTextWithShadow(textRenderer,text,width/2+15,8+startY+(i*13),-1);
            }
            startY+=10+list.size()*13;
        }

    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        goalscroll = Math.max(0,Math.min(goalscroll+amount*-20,(40*getAllDisplayMods().size())-height+5));
        updateButtons(mouseX, mouseY);
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    private void drawScrollbar(DrawContext context) {
        int i = this.getScrollbarThumbHeight();
        int g = width/2-5;
        int j = g + 4;
        int k = g + 4 + 8;
        int l = 0;
        if(getMaxScrollY()!=0)
            l = (int) Math.max(0, (this.scroll) * (this.height - i) / this.getMaxScrollY());
        int m = l + i;
        context.fill(j, l, k, m, -8355712);
        context.fill(j, l, k - 1, m - 1, -4144960);
    }

    private double getMaxScrollY() {
        return (40*getAllDisplayMods().size())-height+5;
    }

    private int getScrollbarThumbHeight() {
        return (int) ((height*height)/(float)(getMaxScrollY()+height));
    }

    @Override
    protected void init() {
        list = new ArrayList<>();
        for (ModContainer container : getAllDisplayMods()) {
            TexturedButtonWidget widget = addDrawableChild(new TexturedButtonWidget(width / 2 -30, 0, 20, 20, 0, ModMenu.screenMap.containsKey(container) ? 0 : 40, ModMenu.screenMap.containsKey(container) ? 20 : 0, MOD_BUTTONS, 60, 60, (a) -> {
                if (ModMenu.screenMap.containsKey(container)) {
                    MinecraftClient.getInstance().setScreen(ModMenu.screenMap.get(container).create(this));
                }
            }));
            list.add(widget);
        }
        website = this.addDrawableChild(new TexturedButtonWidget(width - 25, 0, 20, 20,40, 0, 20, MOD_BUTTONS,60,60, button -> {
            ConfirmLinkScreen.open(currentMod.getMetadata().getContact().asMap().getOrDefault("homepage",""), this, false);
        }));
        issues = this.addDrawableChild(new TexturedButtonWidget(width - 25, 0, 20, 20,40, 0, 20, MOD_BUTTONS,60,60, button -> {
            ConfirmLinkScreen.open(currentMod.getMetadata().getContact().asMap().getOrDefault("issues",""), this, false);
        }));
    }

    public String toTextListFromAuthor(Collection<Person> authors) {
        StringBuilder builder = new StringBuilder();
        for (Person p : authors) {
            builder.append("\n").append(p.getName());
        }
        return builder.toString();
    }

    public void updateButtons(double mouseX, double mouseY) {
        int i = -1;
        for (ButtonWidget buttonWidget : list) {
            boolean isChild = getAllDisplayMods().get(i + 1).getOrigin().getKind() != ModOrigin.Kind.PATH;
            boolean isHovered = mouseX > (isChild ? 40 : 5) && mouseX < width / 2f - 5 && mouseY > (int) ((i + 1) * 40 - scroll) + 5 && mouseY < (int) ((i + 1) * 40 - scroll) + 40;
            i++;
            buttonWidget.setY((int) (i * 40 - scroll) + 15 + (isHovered ? 2 : 0));
            buttonWidget.setX(width / 2 - 30 + (isHovered ? 2 : 0));
        }
        website.active = true;
        {
            int startY = 60;
            for (int j = 0; j < 5; j++) {
                List<OrderedText> list = null;
                switch ( j ) {
                    case 0 -> list = textRenderer.wrapLines(StringVisitable.plain(currentMod.getMetadata().getDescription()), (int) (width / 2f - 20));
                    case 1 -> list = textRenderer.wrapLines(StringVisitable.plain("Authors: " + toTextListFromAuthor(currentMod.getMetadata().getAuthors())), (int) (width / 2f - 20));
                    case 2 -> list = textRenderer.wrapLines(StringVisitable.plain("Environment: " + currentMod.getMetadata().getEnvironment().name()), (int) (width / 2f - 20));
                    case 3 -> list = textRenderer.wrapLines(StringVisitable.plain("Licence: " + currentMod.getMetadata().getLicense()), (int) (width / 2f - 20));
                    case 4 -> list = textRenderer.wrapLines(StringVisitable.plain("Homepage: " + currentMod.getMetadata().getContact().asMap().getOrDefault("homepage", "unknown")), (int) (width / 2f - 40));
                }
                startY += 10 + list.size() * 13;
            }
            website.setY(startY - 21);
        }
        if (currentMod.getMetadata().getContact().get("homepage").isEmpty()) {
            website.active = false;
        }
        issues.active = true;
        int startY = 60;
        for (int j = 0; j < 6; j++) {
            List<OrderedText> list = null;
            switch ( j ) {
                case 0 -> list = textRenderer.wrapLines(StringVisitable.plain(currentMod.getMetadata().getDescription()), (int) (width / 2f - 20));
                case 1 -> list = textRenderer.wrapLines(StringVisitable.plain("Authors: " + toTextListFromAuthor(currentMod.getMetadata().getAuthors())), (int) (width / 2f - 20));
                case 2 -> list = textRenderer.wrapLines(StringVisitable.plain("Environment: " + currentMod.getMetadata().getEnvironment().name()), (int) (width / 2f - 20));
                case 3 -> list = textRenderer.wrapLines(StringVisitable.plain("Licence: " + currentMod.getMetadata().getLicense()), (int) (width / 2f - 20));
                case 4 -> list = textRenderer.wrapLines(StringVisitable.plain("Homepage: " + currentMod.getMetadata().getContact().asMap().getOrDefault("homepage", "unknown")), (int) (width / 2f - 40));
                case 5 -> list = textRenderer.wrapLines(StringVisitable.plain("Issues: " + currentMod.getMetadata().getContact().asMap().getOrDefault("issues", "unknown")), (int) (width / 2f - 40));
            }
            startY += 10 + list.size() * 13;
        }
        issues.setY(startY - 21);
        if (currentMod.getMetadata().getContact().get("issues").isEmpty()) {
            issues.active = false;
        }
    }

    @Override
    public void close() {
        assert client != null;
        client.setScreen(parent);
    }

    public List<ModContainer> getAllDisplayMods() {
        if (mods == null) {
            List<ModContainer> modContainers = new ArrayList<>();
            for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
                if (mod.getOrigin().getKind() == ModOrigin.Kind.PATH) {
                    int o = 0;
                    block: for (ModContainer co : modContainers) {
                        String idCo = co.getMetadata().getId();
                        String idMod = mod.getMetadata().getId();
                        int d = 0;
                        while (idCo != null && idMod != null && idCo.length() != d && idMod.length() != d) {
                            d++;
                            if(idCo.charAt(d-1) < idMod.charAt(d-1)) {
                                break block;
                            }
                            if(idCo.charAt(d-1) != idMod.charAt(d-1)) {
                                break;
                            }
                        }
                        o++;
                    }
                    modContainers.add(o,mod);
                }
            }
            List<ModContainer> modContainers1 = new ArrayList<>();
            for (ModContainer con : modContainers) {
                modContainers1.add(0,con);
            }
            mods = modContainers1;
        }
        List<ModContainer> mods2 = new ArrayList<>(mods);
        if(currentOutMod != null && currentOutMod.getContainedMods().size()!=0) {
            mods2.addAll(mods2.indexOf(currentOutMod)+1,currentOutMod.getContainedMods());
        }
        return mods2;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int i = this.getScrollbarThumbHeight();
        int l = 0;
        if(getMaxScrollY()!=0)
            l = (int) Math.max(0, (this.scroll) * (this.height - i) / this.getMaxScrollY());
        int m = l + i;
        if(mouseY>l && mouseY<m && mouseX>(width/2f-1) && mouseX<(width/2f+7)) {
            offset = mouseY-l;
            return true;
        } else {
            offset = -1;
        }
        boolean b = super.mouseClicked(mouseX, mouseY, button);
        if(!b) {
            for (int j = 0; j < getAllDisplayMods().size(); j++) {
                boolean isHovered = mouseX>5&&mouseX<width/2f-5&&mouseY>(int) (j * 40 - scroll) + 5&&mouseY<(int) (j * 40 - scroll) + 40;
                if(isHovered && button == 0) {
                    currentMod = getAllDisplayMods().get(j);
                    currentOutMod = currentMod.getOrigin().getKind() == ModOrigin.Kind.PATH ? currentMod : currentOutMod;
                    clearAndInit();
                    scroll = Math.max(0,Math.min(scroll,(40*getAllDisplayMods().size())-height+5));
                    goalscroll = Math.max(0,Math.min(scroll,(40*getAllDisplayMods().size())-height+5));
                }
            }

        }
        return b;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        offset = -1;
        return super.mouseReleased(mouseX, mouseY, button);
    }
}
