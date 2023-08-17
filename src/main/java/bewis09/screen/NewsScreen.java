package bewis09.screen;

import bewis09.drawable.OptionScreenClickableWidget;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import javax.imageio.ImageIO;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static bewis09.util.TextHelper.getText;
import static bewis09.util.HttpInputStramGetter.getInputStream;

public class NewsScreen extends AbstractOptionScreen {

    private final Screen parent;
    public static final List<NewsTwoItem> news;
    public static Item firstItem;
    public static Identifier image;

    static {
        List<Item> items = new ArrayList<>();

        try {
            {

                Reader reader = new InputStreamReader(getInputStream("https://bewisclient.github.io/news.json"));

                Gson gson = new GsonBuilder().create();

                items = new ArrayList<>(Arrays.asList(gson.fromJson(reader, Item[].class)));

                SortListByDate(items);

                firstItem = items.get(0);

                image = getImage(firstItem.image);

                assert image != null;

            }
            {

                Reader reader = new InputStreamReader(getInputStream("https://api.modrinth.com/v2/project/bewisclient/version"));

                Gson gson = new GsonBuilder().create();

                VersionItem[] list = gson.fromJson(reader, VersionItem[].class);

                for (VersionItem version : list) {
                    if(Objects.equals(version.version_type, "release") || FabricLoader.getInstance().getModContainer("bewisclient").get().getMetadata().getVersion().getFriendlyString().contains("beta")) {
                        items.add(new Item("Version: "+version.version_number.split("-")[0],version.changelog,version.date_published.split("T")[0]));
                    }
                }

            }

            items.remove(0);

            SortListByDate(items);

        } catch (Exception e) {
            e.printStackTrace();

            firstItem = null;
        }

        if(items.size()>1 && !items.get(0).title.split(":")[0].equals("Version") && items.get(1).title.split(":")[0].equals("Version")) {
            Item item = items.get(1);
            items.remove(item);
            items.add(0,item);
        }

        news=new ArrayList<>();

        for (int i = 0; i < items.size(); i+=2) {
            int width = 144;
            int widthBefore1 = -1;
            int widthBefore2;
            NewsTwoItem item = null;
            while (item == null || (item.list2 != null && item.list1.size() != item.list2.size())) {
                if (items.size() > i + 1)
                    item = (new NewsTwoItem(width, MinecraftClient.getInstance().textRenderer.wrapLines(StringVisitable.plain(items.get(i).text), width - 8), MinecraftClient.getInstance().textRenderer.wrapLines(StringVisitable.plain(items.get(i + 1).text), 284 - width - 8), Text.literal(items.get(i).title), Text.literal(items.get(i + 1).title)));
                else
                    item = (new NewsTwoItem(width, MinecraftClient.getInstance().textRenderer.wrapLines(StringVisitable.plain(items.get(i).text), width - 8), null, Text.literal(items.get(i).title), null));
                if (item.list2!=null && item.list1.size() != item.list2.size()) {

                    widthBefore2 = widthBefore1;
                    widthBefore1 = width;

                    if (item.list1.size() > item.list2.size()) {
                        width++;
                    } else {
                        width--;
                    }

                    if(widthBefore2 == width) {
                        item = new NewsTwoItem(item.width, new ArrayList<>(item.list1), new ArrayList<>(item.list2), item.title1, item.title2);
                        if (item.list1.size() > item.list2.size()) {
                            item.list2.add(Text.of("").asOrderedText());
                        } else {
                            item.list1.add(Text.of("").asOrderedText());
                        }
                        break;
                    }
                }
                if (width<100 || width>184) {
                    item = new NewsTwoItem(item.width, new ArrayList<>(item.list1), new ArrayList<>(item.list2), item.title1, item.title2);
                    while (item.list1.size() != item.list2.size()) {
                        if (item.list1.size() > item.list2.size()) {
                            item.list2.add(Text.of("").asOrderedText());
                        } else {
                            item.list1.add(Text.of("").asOrderedText());
                        }
                    }
                    break;
                }
            }
            news.add(item);
        }
    }

    public NewsScreen(Screen parent) {
        this.parent = parent;
    }

    public static void SortListByDate(List<Item> stringList) {
        stringList.sort((s1, s2) -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date1 = dateFormat.parse(s1.date);
                Date date2 = dateFormat.parse(s2.date);
                return -date1.compareTo(date2);
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        });
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        drawBackground(context);
        int z = 6;
        {
            List<OrderedText> orderedTexts = textRenderer.wrapLines(Text.literal(firstItem.text), 280);
            int zStart = z;
            z += (25 + 13 * orderedTexts.size());
            context.fill(width / 2 - 144, (int) (zStart - scroll), width / 2 + 144, (int) (z - 4 - scroll), 0xFF000000);
            context.drawBorder(width / 2 - 144, (int) (z - (25 + 13 * orderedTexts.size()) - scroll), 288, (21 + 13 * orderedTexts.size()), -1);
            context.drawCenteredTextWithShadow(textRenderer, Text.literal(firstItem.title).append(Text.literal(" (" + firstItem.date + ")").setStyle(Style.EMPTY.withColor(Formatting.GRAY))), width / 2, (int) (z - (25 + 13 * orderedTexts.size()) + 5 - scroll), -1);
            for (OrderedText o : orderedTexts) {
                context.drawTextWithShadow(textRenderer, o, width / 2 - 140, (int) (z - (13 * orderedTexts.size()) + orderedTexts.indexOf(o) * 13 - 4 - scroll), -1);
            }
            if(image != null) {
                context.drawBorder(width / 2 - 144, (int) (z - (25 + 13 * orderedTexts.size()) - scroll) + (21 + 13 * orderedTexts.size()), 288, 162, -1);
                context.drawTexture(image, width / 2 - 143, (int) (z - (25 + 13 * orderedTexts.size()) - scroll) + (21 + 13 * orderedTexts.size()), 286, 161, 0, 0, 286, 161, 286, 161);
                z += 162;
            }
        }
        boolean b = false;
        for (NewsTwoItem item : news) {
            int zStart = z;
            {
                List<OrderedText> orderedTexts = item.list1;
                z += (25 + 13 * orderedTexts.size());
                context.fill(width / 2 - 144, (int) (zStart - scroll), width / 2 - 144 + item.width, (int) (z - 4 - scroll), (!b && OptionScreenClickableWidget.newVersion && item.title1.getString().split(":")[0].equals("Version")) ? hovered(mouseX,mouseY,zStart,item,z) ? 0xFF003333 : 0xFF005555 : 0xFF000000);
                context.drawBorder(width / 2 - 144, (int) (z - (25 + 13 * orderedTexts.size()) - scroll), item.width, (21 + 13 * orderedTexts.size()), (!b && OptionScreenClickableWidget.newVersion && item.title1.getString().split(":")[0].equals("Version") &&
                            hovered(mouseX,mouseY,zStart,item,z)) ? 0xFF009999 : -1);
                context.drawCenteredTextWithShadow(textRenderer, item.title1, width / 2 - 144 + item.width / 2, (int) (z - (25 + 13 * orderedTexts.size()) + 5 - scroll), -1);
                for (OrderedText o : orderedTexts) {
                    context.drawTextWithShadow(textRenderer, o, width / 2 - 140, (int) (z - (13 * orderedTexts.size()) + orderedTexts.indexOf(o) * 13 - 4 - scroll), -1);
                }
                if (!b && OptionScreenClickableWidget.newVersion && item.title1.getString().split(":")[0].equals("Version") &&
                        hovered(mouseX,mouseY,zStart,item,z)) context.drawTooltip(textRenderer,getText("download"),mouseX,mouseY);
            }
            b = true;
            {
                if (item.list2 != null) {
                    List<OrderedText> orderedTexts = item.list2;
                    context.fill(width / 2 - 144 + item.width + 4, (int) (zStart - scroll), width / 2 + 144, (int) (z - 4 - scroll), 0xFF000000);
                    context.drawBorder(width / 2 - 144 + item.width + 4, (int) (z - (25 + 13 * orderedTexts.size()) - scroll), 284 - item.width, (21 + 13 * orderedTexts.size()), -1);
                    context.drawCenteredTextWithShadow(textRenderer, item.title2, width / 2 + 144 - (282-item.width)/2, (int) (z - (25 + 13 * orderedTexts.size()) + 5 - scroll), -1);
                    for (OrderedText o : orderedTexts) {
                        context.drawTextWithShadow(textRenderer, o, width / 2 - 140 + item.width + 4, (int) (z - (13 * orderedTexts.size()) + orderedTexts.indexOf(o) * 13 - 4 - scroll), -1);
                    }
                }
            }
        }
        super.render(context, mouseX, mouseY, delta);
    }

    public boolean hovered(int mouseX, int mouseY, int zStart, NewsTwoItem item,int z) {
        return mouseX > width / 2 - 144 &&
                mouseY > (int) (zStart - scroll) &&
                mouseX < width / 2 - 144 + item.width &&
                mouseY < (int) (z - 4 - scroll);
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(parent);
    }

    @Override
    public int maxHeight() {
        int z = 6;
        z+=(25+13*textRenderer.wrapLines(Text.literal(firstItem.text), 280).size());
        for (NewsTwoItem item : news) {
            z+=(25+13*item.list1.size());
        }
        if(image != null) {
            z += 162;
        }
        return z+2;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int z = 6;
        z+=(25+13*textRenderer.wrapLines(Text.literal(firstItem.text), 280).size());
        if(image != null)
            z += 162;
        int zStart = z;
        z+=(25+13*news.get(0).list1.size());
        if (OptionScreenClickableWidget.newVersion && hovered((int) mouseX, (int) mouseY,zStart,news.get(0),z)) {
            ConfirmLinkScreen.open("https://modrinth.com/mod/bewisclient",this,true);
            OptionScreenClickableWidget.playDownSound(MinecraftClient.getInstance().getSoundManager());
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void init() {
        correctScroll();
    }

    private record NewsTwoItem(int width, List<OrderedText> list1, List<OrderedText> list2, MutableText title1, MutableText title2) {

    }

    @SuppressWarnings("all")
    private static class Item {
        private String title;
        private String text;
        private String date;
        private String image;

        private Item(String title, String text, String date) {
            this.title = title;
            this.text = text;
            this.date = date;
        }
    }

    @SuppressWarnings("all")
    public static class VersionItem {
        public String version_number;
        public String changelog;
        public String date_published;
        public String version_type;
    }

    public static Identifier getImage(String item) {

        try {
            Scanner scanner = new Scanner(getInputStream("https://raw.githubusercontent.com/bewisclient/bewisclient.github.io/main/images/"+item+".png"));
            if(scanner.hasNextLine() && scanner.nextLine().equals("404: Not Found"))
                return null;
            File file = new File(FabricLoader.getInstance().getGameDir()+"\\bewisclient\\cache\\news\\"+item+".png");
            file.getParentFile().mkdirs();
            file.createNewFile();
            InputStream stream = getInputStream("https://raw.githubusercontent.com/bewisclient/bewisclient.github.io/main/images/"+item+".png");
            ImageIO.write(ImageIO.read(stream),"PNG",file);
            InputStream stream1 = new FileInputStream(file);
            NativeImage image = NativeImage.read(stream1);
            NativeImageBackedTexture backedTexture = new NativeImageBackedTexture(image);
            return MinecraftClient.getInstance().getTextureManager().registerDynamicTexture("image_"+item,backedTexture);
        } catch (Throwable ignored) {}
        return null;
    }
}
