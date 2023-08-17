package bewis09.util;

import bewis09.hud.HudElement;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class LegacyFileReader {
    static String[][] strings;

    public static void init() {}

    static {
        reloadStrings();
    }

    public static void reloadStrings() {
        List<String[]> listList = new ArrayList<>();
        Scanner scanner;
        File file = new File(FabricLoader.getInstance().getGameDir() + "\\bewisclient\\options.bof");
        try {
            scanner = new Scanner(new File(FabricLoader.getInstance().getGameDir() + "\\bewisclient\\options.bof"));
        } catch (FileNotFoundException e) {
            try {
                file.getParentFile().mkdirs();
                boolean bl = file.createNewFile();
                if(!bl) {
                    System.exit(-47624823);
                }
                scanner = new Scanner(file);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

        }
        while (scanner.hasNextLine()) {
            listList.add((scanner.nextLine().split(" ")));
        }
        strings = listList.toArray(new String[][]{});
    }

    public static String[] getByFirst(String type, String str) {
        List<String[]> list = new ArrayList<>();
        for (String[] strS : strings){
            if(strS.length!=0 && Objects.equals(strS[0], type)) {
                ArrayList<String> strings = new ArrayList<>(Arrays.stream(strS).toList());
                strings.remove(0);
                list.add(strings.toArray(new String[0]));
            }
        }
        for (String[] strS : list){
            if(strS.length!=0 && Objects.equals(strS[0], str)) {
                ArrayList<String> strings = new ArrayList<>(Arrays.stream(strS).toList());
                strings.remove(0);
                return (strings.toArray(new String[0]));
            }
        }
        return new String[0];
    }

    public static int getX(String str) {
        try {
            return Integer.parseInt(getByFirst("Widget", str)[0]);
        } catch (Exception e) {
            return -4269;
        }
    }

    public static int getY(String str) {
        try {
            return Integer.parseInt(getByFirst("Widget", str)[1]);
        } catch (Exception e) {
            return -4269;
        }
    }

    public static HudElement.Horizontal getHorizontal(String str) {
        try {
            return HudElement.Horizontal.valueOf(getByFirst("Widget",str)[2]);
        } catch (Exception e) {
            return null;
        }
    }

    public static HudElement.Vertical getVertical(String str) {
        try {
            return HudElement.Vertical.valueOf(getByFirst("Widget", str)[3]);
        } catch (Exception e) {
            return null;
        }
    }

    public static void setByFirst(String type, String str, Object... values) {
        try {
            List<Object[]> list = new ArrayList<>(Arrays.stream(strings).toList());
            int i = -1;
            boolean bl = false;
            List<Object> l = new ArrayList<>(List.of(values));
            l.add(0,str);
            l.add(0,type);
            for (String[] strings : strings) {
                i++;
                if (Objects.equals(strings[0], type) && Objects.equals(strings[1], str)) {
                    list.set(i, l.toArray());
                    bl=true;
                }
            }
            if(!bl)
                list.add(l.toArray());
            try (PrintWriter writer = new PrintWriter(FabricLoader.getInstance().getGameDir() + "\\bewisclient\\options.bof")) {
                for (Object[] strings : list) {
                    for (Object str2 : strings) {
                        writer.print(str2);
                        writer.print(" ");
                    }
                    writer.println();
                }
                writer.flush();
            }
            reloadStrings();
        } catch (Exception e) {
            Log.log("Error whilst saving values");
        }
    }

    public static double getByFirstIntFirst(String type, String str, double not) {
        try {
            return Double.parseDouble(getByFirst(type,str)[0]);
        } catch (Exception e) {
            return not;
        }
    }

    public static boolean getBoolean(String s) {
        try {
            return Boolean.parseBoolean(getByFirst("Boolean", s)[0]);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean getBoolean(String s, boolean defaultV) {
        try {
            return Boolean.parseBoolean(getByFirst("Boolean", s)[0]);
        } catch (Exception e) {
            return defaultV;
        }
    }

    public static String getColor(String name, String defaultV) {
        try {
            return getByFirst("Text", name)[0];
        } catch (Exception e) {
            return defaultV;
        }
    }

    public static String getSwitch(String name, String defaultV) {
        try {
            return getByFirst("Switch", name)[0];
        } catch (Exception e) {
            return defaultV;
        }
    }
}
