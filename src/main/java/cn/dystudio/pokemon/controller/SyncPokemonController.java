package cn.dystudio.pokemon.controller;

import cn.dystudio.pokemon.service.PokemonService;
import cn.dystudio.pokemon.service.PokemonSpeciesService;
import cn.dystudio.pokemon.util.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/syncPokemon")
public class SyncPokemonController {

    private final PokemonService pokemonService;
    private final PokemonSpeciesService pokemonSpeciesService;

    public SyncPokemonController(
            PokemonService pokemonService,
            PokemonSpeciesService pokemonSpeciesService) {
        this.pokemonService = pokemonService;
        this.pokemonSpeciesService = pokemonSpeciesService;
    }

    @GetMapping("/pokemon")
    public String pokemon() {
        if (SyncController.isSync) {
            return Response.sync();
        }
        new Thread(pokemonService::sync).start();
        return Response.success();
    }

    @GetMapping("/pokemonSpecies")
    public String pokemonSpecies() {
        if (SyncController.isSync) {
            return Response.sync();
        }
        new Thread(pokemonSpeciesService::sync).start();
        return Response.success();
    }

}
