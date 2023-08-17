package bewis09.util;

import bewis09.hud.TiwylaHud;
import bewis09.mixin.ClientPlayerInteractionManagerMixin;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

public final class ExtraInfo {
    public static final Map<Block,ExtraInfo> infos = new HashMap<>();
    private final TextGetter tool;
    private final TextGetter level;
    private final TextGetter progress;
    private final TextGetter extra;

    public static final ExtraInfo defaultInfo = new ExtraInfo();

    public ExtraInfo(TextGetter extra) {
        this((state) -> Text.of(TiwylaHud.getTool(state.getBlock())), (state) -> Text.of(TiwylaHud.getLevel(state.getBlock())), (state) -> {
            assert MinecraftClient.getInstance().interactionManager != null;
            return Text.of(
                    "Progress: "+ ((int)(((ClientPlayerInteractionManagerMixin)MinecraftClient.getInstance().interactionManager).getCurrentBreakingProgress()*100))+"%"
            );
        }, extra);
    }

    public ExtraInfo() {
        this(null);
    }

    public ExtraInfo(TextGetter tool, TextGetter level, TextGetter progress, TextGetter extra) {
        this.tool = tool;
        this.level = level;
        this.progress = progress;
        this.extra = extra;
    }

    public static ExtraInfo get(Block block) {
        return infos.getOrDefault(block,defaultInfo);
    }

    public static Text withText(BlockState state, Property<?> property) {
        return Text.literal(firstStringUp(property.getName().toLowerCase())).append(": ").append(property.createValue(state).value().toString().toLowerCase());
    }

    public static String firstStringUp(String str) {
        String[] strings = str.split("_");
        StringBuilder s = new StringBuilder();

        for (String st : strings) {
            s.append(st.replaceFirst(".", String.valueOf(st.charAt(0)).toUpperCase()));
        }

        return s.toString();
    }

    public interface TextGetter {
        Text getText(BlockState state);
    }

    public Text getFirst(BlockState state) {
        Text text = Text.of("");
        assert MinecraftClient.getInstance().interactionManager != null;
        switch (FileReader.getSwitch("first_tiwyla","tool")) {
            case "tool" -> text = tool.getText(state);
            case "level" -> text = level.getText(state);
            case "progress_tool" -> text = (MinecraftClient.getInstance().interactionManager.getBlockBreakingProgress() == -1 ? tool : progress).getText(state);
            case "progress_level" -> text = (MinecraftClient.getInstance().interactionManager.getBlockBreakingProgress() == -1 ? level : progress).getText(state);
            case "extra_tool" -> text = (extra == null ? tool : extra).getText(state);
            case "extra_level" -> text = (extra == null ? level : extra).getText(state);
        }
        return text;
    }

    public Text getSecond(BlockState state) {
        Text text = Text.of("");
        assert MinecraftClient.getInstance().interactionManager != null;
        switch (FileReader.getSwitch("second_tiwyla","extra_level")) {
            case "tool" -> text = tool.getText(state);
            case "level" -> text = level.getText(state);
            case "progress_tool" -> text = (MinecraftClient.getInstance().interactionManager.getBlockBreakingProgress() == -1 ? tool : progress).getText(state);
            case "progress_level" -> text = (MinecraftClient.getInstance().interactionManager.getBlockBreakingProgress() == -1 ? level : progress).getText(state);
            case "extra_tool" -> text = (extra == null ? tool : extra).getText(state);
            case "extra_level" -> text = (extra == null ? level : extra).getText(state);
        }
        return text;
    }

    public static void register(ExtraInfo extraInfo, Block block) {
        infos.put(block,extraInfo);
    }
}
