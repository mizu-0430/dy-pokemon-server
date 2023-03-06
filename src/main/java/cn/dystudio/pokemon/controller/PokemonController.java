package cn.dystudio.pokemon.controller;

import cn.dystudio.pokemon.service.PokemonListNationalService;
import cn.dystudio.pokemon.util.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pokemon")
public class PokemonController {

    private final PokemonListNationalService pokemonListNationalService;

    public PokemonController(
            PokemonListNationalService pokemonListNationalService) {
        this.pokemonListNationalService = pokemonListNationalService;
    }

    @GetMapping("/pokemonListNational")
    public String pokemonListNational() {
        try {
            return Response.success(null, SyncController.json.readValue(
                    SyncController.json.writeValueAsString(
                            pokemonListNationalService.list()
                    ), ArrayNode.class)
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Response.fail();
        }
    }

}
