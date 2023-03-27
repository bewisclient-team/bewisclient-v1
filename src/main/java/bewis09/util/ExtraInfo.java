package bewis09.util;

import bewis09.hud.TiwylaHud;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

public final class ExtraInfo {
    private final TextGetter firstLine;
    private final TextGetter secondLine;
    private static final ExtraInfo defaultInfo = new ExtraInfo();

    private static final Map<Block, ExtraInfo> infos = new HashMap<>();

    public ExtraInfo(TextGetter firstLine, TextGetter secondLine) {
        this.firstLine = firstLine;
        this.secondLine = secondLine;
    }

    public ExtraInfo(TextGetter secondLine) {
        this(getDefaultFirstLine(),secondLine);
    }

    public ExtraInfo() {
        this(getDefaultSecondLine());
    }

    public static void register(ExtraInfo extraInfo, Block block) {
        infos.put(block,extraInfo);
    }

    public static ExtraInfo get(Block block) {
        return infos.getOrDefault(block,getDefault());
    }

    public static ExtraInfo get(BlockState block) {
        return get(block.getBlock());
    }

    public static TextGetter getDefaultSecondLine() {
        return state -> Text.of("Mining Level: "+TiwylaHud.getLevel(state.getBlock()));
    }

    public static ExtraInfo getDefault() {
        return defaultInfo;
    }

    public static TextGetter getDefaultFirstLine() {
        return state -> Text.of("Tool: "+TiwylaHud.getTool(state.getBlock()));
    }

    public interface TextGetter {
        Text getText(BlockState state);
    }

    public Text getFirstLineText(BlockState state) {
        return firstLine.getText(state);
    }

    public Text getSecondLineText(BlockState state) {
        return secondLine.getText(state);
    }

    public static Text withText(BlockState state, Property<?> property) {
        return Text.literal(firstStringUp(property.getName().toLowerCase()))
                .append(": ")
                .append(property.createValue(state).value().toString().toLowerCase());
    }

    public static String firstStringUp(String str) {
        String[] strings = str.split("_");
        StringBuilder s = new StringBuilder();
        for (String st : strings) {
            s.append(st.replaceFirst(".", String.valueOf(st.charAt(0)).toUpperCase()));
        }
        return s.toString();
    }
}
