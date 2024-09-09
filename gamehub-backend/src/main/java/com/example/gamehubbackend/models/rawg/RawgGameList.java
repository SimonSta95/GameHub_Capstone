package com.example.gamehubbackend.models.rawg;

import com.example.gamehubbackend.models.Game;

import java.util.List;

public record RawgGameList(
        int count,
        String next,
        String previous,
        List<Game> games
) {

}
