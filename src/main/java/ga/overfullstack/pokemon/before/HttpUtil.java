package ga.overfullstack.pokemon.before;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public final class HttpUtil {
    private HttpUtil() {
    }
    
  static HttpResponse<String> getStringHttpResponse(String uri) throws IOException, InterruptedException {
    var client = HttpClient.newHttpClient();
    var request = HttpRequest.newBuilder(URI.create(uri))
        .header("accept", "application/json")
        .build();
    final var asString = HttpResponse.BodyHandlers.ofString();
    return client.send(request, asString);
  }

}
