package cn.dystudio.pokemon.controller;

import cn.dystudio.pokemon.entity.History;
import cn.dystudio.pokemon.entity.Species;
import cn.dystudio.pokemon.service.HistoryService;
import cn.dystudio.pokemon.service.SpeciesService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/sync")
public class SyncController {

    @Value("${pokeApi.path}")
    private String apiUrl;

    private final HistoryService historyService;
    private final SpeciesService speciesService;

    public SyncController(
            HistoryService historyService,
            SpeciesService speciesService) {
        this.historyService = historyService;
        this.speciesService = speciesService;
    }

    private void saveHistory(String type, String result) {
        History history = new History();
        history.setType(type);
        history.setContent(result);
        historyService.save(history);
    }

    @GetMapping("/species")
    public String species() {
        RestTemplate template = new RestTemplate();
        String url = apiUrl + "pokemon-species?offset=0&limit=10000";
        try {
            String result = template.getForObject(url, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode results = objectMapper.readTree(result);
            saveHistory("species", results.toString());
            JsonNode speciesList = results.get("results");
            System.out.println("==================================================> species");
            for (JsonNode species : speciesList) {
                System.out.println("=========================> " + species.get("name"));


                if (speciesService.count(
                        new QueryWrapper<Species>().lambda()
                                .eq(Species::getName, species.get("name").asText())
                ) == 0) {
                    result = template.getForObject(species.get("url").asText(), String.class);
                    saveHistory("species-" + species.get("name").asText(), result);
                    JsonNode jn = objectMapper.readTree(result);
                    Species sp = new Species();
                    sp.setName(jn.get("name").asText());
                    if (!jn.get("order").isNull())
                        sp.setOrder(jn.get("order").asInt());
                    if (!jn.get("gender_rate").isNull())
                        sp.setGenderRate(jn.get("gender_rate").asInt());
                    if (!jn.get("capture_rate").isNull())
                        sp.setCaptureRate(jn.get("capture_rate").asInt());
                    if (!jn.get("base_happiness").isNull())
                        sp.setBaseHappiness(jn.get("base_happiness").asInt());
                    if (!jn.get("hatch_counter").isNull())
                        sp.setHatchCounter(jn.get("hatch_counter").asInt());
                    if (!jn.get("is_baby").isNull())
                        sp.setIsBaby(jn.get("is_baby").asBoolean());
                    if (!jn.get("is_legendary").isNull())
                        sp.setIsLegendary(jn.get("is_legendary").asBoolean());
                    if (!jn.get("is_mythical").isNull())
                        sp.setIsMythical(jn.get("is_mythical").asBoolean());
                    if (!jn.get("has_gender_differences").isNull())
                        sp.setHasGenderDifferences(jn.get("has_gender_differences").asBoolean());
                    if (!jn.get("forms_switchable").isNull())
                        sp.setFormsSwitchable(jn.get("forms_switchable").asBoolean());
                    if (!jn.get("growth_rate").isNull())
                        sp.setGrowthRate(jn.get("growth_rate").get("name").asText());
                    try {
                        JsonNode eggGroups = jn.get("egg_groups");
                        sp.setEggGroup1(eggGroups.get(0).get("name").asText());
                        sp.setEggGroup2(eggGroups.get(1).get("name").asText());
                    } catch (Exception ignored) {
                    }
                    if (!jn.get("color").isNull())
                        sp.setColor(jn.get("color").get("name").asText());
                    if (!jn.get("shape").isNull())
                        sp.setShape(jn.get("shape").get("name").asText());
                    if (!jn.get("habitat").isNull())
                        sp.setHabitat(jn.get("habitat").get("name").asText());
                    if (!jn.get("generation").isNull())
                        sp.setGeneration(jn.get("generation").get("name").asText());
                    if (!jn.get("evolves_from_species").isNull())
                        sp.setEvolvesFromSpecies(jn.get("evolves_from_species").get("name").asText());
                    if (!jn.get("names").isNull()) {
                        try {
                            String names = null;
                            for (JsonNode node : jn.get("names")) {
                                if ("zh-Hans".equals(node.get("language").get("name").asText())) {
                                    names = node.get("name").asText();
                                    break;
                                }
                                if ("en".equals(node.get("language").get("name").asText())) {
                                    names = node.get("name").asText();
                                    break;
                                }
                            }
                            if (names == null)
                                names = jn.get("names").get(0).get("name").asText();
                            sp.setNames(names);
                        } catch (Exception ignored) {
                        }
                    }
                    if (!jn.get("genera").isNull()) {
                        try {
                            String genera = null;
                            for (JsonNode node : jn.get("genera")) {
                                if ("zh-Hans".equals(node.get("language").get("name").asText())) {
                                    genera = node.get("genus").asText();
                                    break;
                                }
                                if ("en".equals(node.get("language").get("name").asText())) {
                                    genera = node.get("genus").asText();
                                    break;
                                }
                            }
                            if (genera == null)
                                genera = jn.get("genera").get(0).get("genus").asText();
                            sp.setNames(genera);
                        } catch (Exception ignored) {
                        }
                    }

                    if (!jn.get("varieties").isNull())
                        sp.setVarieties(jn.get("varieties").toString());
                    if (!jn.get("pokedex_numbers").isNull())
                        sp.setPokedexNumbers(jn.get("pokedex_numbers").toString());
                    if (!jn.get("flavor_text_entries").isNull())
                        sp.setFlavorTextEntries(jn.get("flavor_text_entries").toString());
//                    if (!jn.get("evolution_chain").isNull())
//                        sp.setEvolutionChainUrl(jn.get("evolution_chain").get("url").asText());
                    speciesService.save(sp);
                    System.out.println("===========> ok");
                } else {
                    System.out.println("===========> skip");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ok";
    }

}
