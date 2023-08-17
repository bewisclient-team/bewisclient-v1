package bewis09.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.InputStream;

public class HttpInputStramGetter {
    public static InputStream getInputStream(String str) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(str);
        HttpResponse response = httpClient.execute(request);
        return response.getEntity().getContent();
    }
}
