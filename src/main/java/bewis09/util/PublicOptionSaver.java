package bewis09.util;

import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PublicOptionSaver {
    public static Map<String, String> values = new HashMap<>();

    public static File file = new File(FabricLoader.getInstance().getGameDir()+"\\bewisclient\\public");

    static {
        reload();
    }

    public static void reload() {
        values.clear();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String[] str = scanner.nextLine().split(" ",2);
                if(str.length==2)
                    values.put(str[0],str[1]);
            }
        } catch (FileNotFoundException e) {
            try {
                file.createNewFile();
            } catch (Exception ignored) {}
        }
    }

    public static void write(String key, String value) {
        values.remove(key);
        values.put(key,value);
        try (PrintWriter writer = new PrintWriter(file)) {
            for (Map.Entry<String, String> strings : values.entrySet()) {
                writer.print(strings.getKey()+" "+strings.getValue());
                writer.println();
            }
            writer.flush();
        } catch (Exception ignored) {}
    }
}
