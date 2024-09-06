package com.example.gamehubbackend.services.rawg;

import com.example.gamehubbackend.models.Game;
import com.example.gamehubbackend.models.rawg.RawgGameDetail;
import com.example.gamehubbackend.models.rawg.RawgGameList;
import com.example.gamehubbackend.models.rawg.RawgGameResponse;
import com.example.gamehubbackend.models.rawg.RawgGenre;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class RawgService {

    private final RestClient restClient;

    @Value("${rawg.api.key}")
    private String apiKey;

    public RawgService(@Value("${basic.url}") String basicUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(basicUrl)
                .build();
    }

    @Cacheable("games")
    public RawgGameList loadAllGames() {
        RawgGameResponse body = restClient.get()
                .uri("api/games?page_size=500&page=1&key=" + apiKey)
                .retrieve()
                .body(RawgGameResponse.class);

        if (body == null) {
            return null;
        }

        return new RawgGameList(
                body.next(),
                body.previous(),
                body.results()
                .stream()
                .map(game -> new Game(
                        String.valueOf(game.id()),
                        game.name(),
                        game.genres().stream()
                                .map(RawgGenre::name)
                                .toList(),
                        game.released(),
                        game.platforms().stream()
                                .map(wrapper -> wrapper.platform().name())
                                .toList(),
                        game.background_image()
                ))
                .toList());
    }

    @Cacheable("gameDetail")
    public RawgGameDetail loadGameDetail(String gameId) {

       return restClient.get()
                .uri("/api/games/" + gameId + "?key=" + apiKey)
                .retrieve()
                .body(RawgGameDetail.class);
    }
}
