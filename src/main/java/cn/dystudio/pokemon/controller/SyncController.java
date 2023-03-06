package cn.dystudio.pokemon.controller;

import cn.dystudio.pokemon.service.PokemonService;
import cn.dystudio.pokemon.service.PokemonSpeciesService;
import cn.dystudio.pokemon.util.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sync")
public class SyncController {

    public static ObjectMapper json = new ObjectMapper();
    public static boolean isSync = false;
    public static String syncModule = "";
    public static Integer totalSize = 0;
    public static Integer syncSize = 0;

    private final PokemonService pokemonService;
    private final PokemonSpeciesService pokemonSpeciesService;

    public SyncController(
            PokemonService pokemonService,
            PokemonSpeciesService pokemonSpeciesService) {
        this.pokemonService = pokemonService;
        this.pokemonSpeciesService = pokemonSpeciesService;
    }

    @GetMapping("/check")
    public String check() {
        try {
            return Response.sync();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.fail();
        }
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
