package bewis09.util;

import bewis09.hud.HudElement;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class FileReader {

    public static String folder = FabricLoader.getInstance().getGameDir()+"\\bewisclient\\profiles";

    public static List<Profile> profiles = new ArrayList<>();

    static {
        load();
        boolean o = true;
        for (Profile profile : profiles) {
            if(PublicOptionSaver.values.getOrDefault("Profile","default").equals(profile.s)) {
                currentProfile = profile;
                o=false;
            }
        }
        if(o) {
            currentProfile = profiles.get(0);
        }
    }

    public static Profile currentProfile;

    public static void load() {
        File folder = new File(FileReader.folder);
        folder.mkdirs();
        boolean bl =true;
        try {
            profiles.clear();
            for (File file : Objects.requireNonNull(folder.listFiles())) {
                Scanner scanner = new Scanner(file);
                List<String[]> list = new ArrayList<>();
                while (scanner.hasNextLine()) {
                    String string = scanner.nextLine();
                    String[] strings = string.split(" ");
                    list.add(strings);
                }
                String z = "\\\\";
                String[] str = file.getPath().split(z);
                profiles.add(new Profile(str[str.length-1],list.toArray(new String[0][0])));
                if(str[str.length-1].equals("default.bof"))
                    bl = false;
                scanner.close();
            }
        } catch (FileNotFoundException ignored) {}
        if(bl) {
            Profile p = new Profile("default.bof",getDefaults());
            profiles.add(p);
            try {
                new File(FabricLoader.getInstance().getGameDir() + "\\bewisclient\\profiles\\default.bof").createNewFile();
                try {
                    try (PrintWriter writer = new PrintWriter(FabricLoader.getInstance().getGameDir() + "\\bewisclient\\profiles\\"+p.s)) {
                        for (Object[] strings : p.strings) {
                            for (Object str2 : strings) {
                                writer.print(str2);
                                writer.print(" ");
                            }
                            writer.println();
                        }
                        writer.flush();
                    }
                } catch (Exception e) {
                    Log.log("Error whilst saving values");
                    e.printStackTrace();
                }
            } catch (IOException ignored) {}
        }
        if(currentProfile != null) {
            boolean o = true;
            for (Profile profile : profiles) {
                if(currentProfile.s.equals(profile.s)) {
                    currentProfile = profile;
                    o=false;
                }
            }
            if(o) {
                currentProfile = profiles.get(0);
            }
        }
    }

    public static String[][] getDefaults() {
        List<String[]> listList = new ArrayList<>();
        Scanner scanner;
        try {
            scanner = new Scanner(new File(FabricLoader.getInstance().getGameDir() + "\\bewisclient\\options.bof"));
        } catch (FileNotFoundException e) {
            return new String[0][];
        }
        while (scanner.hasNextLine()) {
            listList.add((scanner.nextLine().split(" ")));
        }
        scanner.close();
        return listList.toArray(new String[][]{});
    }

    public static void init() {
    }

    public record Profile(String s, String[]... strings) {
    }

    public static String[] getByFirst(String type, String str) {
        List<String[]> list = new ArrayList<>();
        for (String[] strS : currentProfile.strings){
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
            List<String[]> list = new ArrayList<>(Arrays.stream(currentProfile.strings).toList());
            int i = -1;
            boolean bl = false;
            List<Object> o = new ArrayList<>(List.of(values));
            List<String> l = new ArrayList<>();
            for (Object o2 : o) {
                l.add(o2.toString());
            }
            l.add(0,str);
            l.add(0,type);
            for (String[] strings : list) {
                i++;
                if (strings.length >= 2 && Objects.equals(strings[0], type) && Objects.equals(strings[1], str)) {
                    list.set(i, l.toArray(new String[0]));
                    bl=true;
                }
            }
            if(!bl)
                list.add(l.toArray(new String[0]));
            try (PrintWriter writer = new PrintWriter(FabricLoader.getInstance().getGameDir() + "\\bewisclient\\profiles\\"+currentProfile.s)) {
                for (Object[] strings : list) {
                    for (Object str2 : strings) {
                        writer.print(str2);
                        writer.print(" ");
                    }
                    writer.println();
                }
                writer.flush();
            }
            load();
        } catch (Exception e) {
            Log.log("Error whilst saving values");
            e.printStackTrace();
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
