package kata;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

class PokeClient {

    private final OkHttpClient okHttpClient = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public String getName(int id) throws IOException {
        Request request = new Request.Builder()
                .get()
                .url("https://pokeapi.co/api/v2/pokemon/" + id)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            String rawJson = response.body().string();

            JsonNode jsonNode = objectMapper.readTree(rawJson);
            return jsonNode.get("name").asText();
        }
    }

    public List<String> getLocations(int id) throws IOException {
        Request request = new Request.Builder()
                .get()
                .url("https://pokeapi.co/api/v2/pokemon/" + id + "/encounters")
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            String rawJson = response.body().string();

            LocationResponse[] array = objectMapper.readValue(rawJson, LocationResponse[].class);
            return Arrays.stream(array)
                    .filter(x -> Arrays.stream(x.version_details()).anyMatch(y -> y.version().name().equals("red")))
                    .map(r -> r.location_area().name())
                    .toList();
        }
    }

    record LocationResponse(LocationArea location_area, VersionDetails[] version_details) {
    }

    record LocationArea(String name) {
    }

    record VersionDetails(Version version) {
    }

    record Version(String name) {
    }
}
