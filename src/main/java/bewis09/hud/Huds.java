package bewis09.hud;

import java.util.ArrayList;
import java.util.List;

public class Huds {
    public static List<ElementRegister> list2 = new ArrayList<>();

    private static final List<HudElement> list = new ArrayList<>();

    public static void register(ElementRegister element) {
        list2.add(element);
    }

    public static void reload() {
        list.clear();
        for (ElementRegister e : list2) {
            list.add(e.register());
        }
    }

    public static List<HudElement> getList() {
        if(list.isEmpty())
            reload();
        return list;
    }

    public interface ElementRegister {
        HudElement register();
    }
}
