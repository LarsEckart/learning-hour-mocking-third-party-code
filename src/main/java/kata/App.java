package kata;

import okhttp3.OkHttpClient;

import java.io.IOException;

class App {

    public static void main(String[] args) throws IOException {
        PokeClient pokeClient = new PokeClient("https://pokeapi.co/api/v2/", new OkHttpClient());

        for (int i = 1; i < 25; i++) {
            System.out.println(pokeClient.getPokemonNameAndLocations(i));
        }
    }
}
