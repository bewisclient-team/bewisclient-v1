package bewis09.cape;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static bewis09.util.HttpInputStramGetter.getInputStream;

public class OwnCapeLoader {
    public static List<Identifier> getOwnCape(String playerName) {

        try {
            Scanner scanner = new Scanner(getInputStream("https://raw.githubusercontent.com/Bewis09/bewisclientUpdateChecker/main/"+playerName.toLowerCase()+".txt"));
            List<Identifier> list = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String s = scanner.nextLine();
                if(s.contains("404: Not Found"))
                    return list;
                File file = new File(FabricLoader.getInstance().getGameDir()+"\\bewisclient\\cache\\"+s+".png");
                file.getParentFile().mkdirs();
                file.createNewFile();
                InputStream stream = getInputStream("https://raw.githubusercontent.com/Bewis09/bewisclientUpdateChecker/main/"+s+".png");
                ImageIO.write(ImageIO.read(stream),"PNG",file);
                InputStream stream1 = new FileInputStream(file);
                NativeImage image = NativeImage.read(stream1);
                NativeImageBackedTexture backedTexture = new NativeImageBackedTexture(image);
                list.add(MinecraftClient.getInstance().getTextureManager().registerDynamicTexture("own_"+s,backedTexture));
            }
            return list;
        } catch (Throwable ignored) {}
        return null;
    }
}
