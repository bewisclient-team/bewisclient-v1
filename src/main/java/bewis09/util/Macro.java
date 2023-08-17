package bewis09.util;

import java.util.List;

public record Macro(String name, List<String> commands, net.minecraft.client.util.InputUtil.Key key) {}