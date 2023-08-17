package bewis09.update;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.Scanner;

public class UpdateChecker {

    public static String hasNewUpdate() {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("https://raw.githubusercontent.com/Bewis09/bewisclientUpdateChecker/main/updates.txt");

        try {
            HttpResponse response = httpClient.execute(request);
            Scanner scanner = new Scanner(response.getEntity().getContent());
            while (scanner.hasNextLine()) {
                String s = scanner.nextLine();
                if(s.contains(FabricLoader.getInstance().getModContainer("bewisclient").get().getMetadata().getVersion().getFriendlyString())) return s.split(" ").length > 0 ? s.split(" ")[1] : "VE";
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
