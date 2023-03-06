package cn.dystudio.pokemon.service.impl;

import cn.dystudio.pokemon.controller.SyncController;
import cn.dystudio.pokemon.entity.PokemonSpecies;
import cn.dystudio.pokemon.mapper.PokemonSpeciesMapper;
import cn.dystudio.pokemon.service.PokemonSpeciesService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.List;

/**
 * <p>
 * 宝可梦种族 服务实现类
 * </p>
 *
 * @author 张永清
 * @since 2023-03-02
 */
@Service
public class PokemonSpeciesServiceImpl extends ServiceImpl<PokemonSpeciesMapper, PokemonSpecies> implements PokemonSpeciesService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${pokeApi.path}")
    private String apiUrl;

    @Override
    public void sync() {
        SyncController.isSync = true;
        SyncController.totalSize = 0;
        SyncController.syncSize = 0;
        SyncController.syncModule = "pokemon-species";
        try {
            logger.info("==================================================> pokemon-species");
            RestTemplate template = new RestTemplate();
            String url = apiUrl + "pokemon-species?offset=0&limit=10000";
            String result = template.getForObject(url, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode results = objectMapper.readTree(result);
            JsonNode speciesList = results.get("results");
            SyncController.totalSize = speciesList.size();
            for (JsonNode species : speciesList) {
                SyncController.syncSize++;
                logger.info("=========================> " + species.get("name"));
                result = template.getForObject(species.get("url").asText(), String.class);
                JsonNode jn = objectMapper.readTree(result);
                List<PokemonSpecies> spList = list(
                        new QueryWrapper<PokemonSpecies>().lambda()
                                .eq(PokemonSpecies::getName, species.get("name").asText())
                );
                PokemonSpecies entity;
                if (CollectionUtils.isEmpty(spList)) {
                    entity = new PokemonSpecies();
                    entity.setSpeciesId(jn.get("id").asInt());
                } else {
                    entity = spList.get(0);
                }
                entity.setName(jn.get("name").asText());
                entity.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                if (!jn.get("order").isNull())
                    entity.setOrder(jn.get("order").asInt());
                if (!jn.get("gender_rate").isNull())
                    entity.setGenderRate(jn.get("gender_rate").asInt());
                if (!jn.get("capture_rate").isNull())
                    entity.setCaptureRate(jn.get("capture_rate").asInt());
                if (!jn.get("base_happiness").isNull())
                    entity.setBaseHappiness(jn.get("base_happiness").asInt());
                if (!jn.get("hatch_counter").isNull())
                    entity.setHatchCounter(jn.get("hatch_counter").asInt());
                if (!jn.get("is_baby").isNull())
                    entity.setIsBaby(jn.get("is_baby").asBoolean());
                if (!jn.get("is_legendary").isNull())
                    entity.setIsLegendary(jn.get("is_legendary").asBoolean());
                if (!jn.get("is_mythical").isNull())
                    entity.setIsMythical(jn.get("is_mythical").asBoolean());
                if (!jn.get("has_gender_differences").isNull())
                    entity.setHasGenderDifferences(jn.get("has_gender_differences").asBoolean());
                if (!jn.get("forms_switchable").isNull())
                    entity.setFormsSwitchable(jn.get("forms_switchable").asBoolean());
                if (!jn.get("growth_rate").isNull())
                    entity.setGrowthRate(jn.get("growth_rate").get("name").asText());
                try {
                    JsonNode eggGroups = jn.get("egg_groups");
                    entity.setEggGroup1(eggGroups.get(0).get("name").asText());
                    entity.setEggGroup2(eggGroups.get(1).get("name").asText());
                } catch (Exception ignored) {
                }
                if (!jn.get("color").isNull())
                    entity.setColor(jn.get("color").get("name").asText());
                if (!jn.get("shape").isNull())
                    entity.setShape(jn.get("shape").get("name").asText());
                if (!jn.get("habitat").isNull())
                    entity.setHabitat(jn.get("habitat").get("name").asText());
                if (!jn.get("generation").isNull())
                    entity.setGeneration(jn.get("generation").get("name").asText());
                if (!jn.get("evolves_from_species").isNull())
                    entity.setEvolvesFromSpecies(jn.get("evolves_from_species").get("name").asText());
                if (!jn.get("names").isNull()) {
                    try {
                        String namesZh = null;
                        String namesEn = null;
                        String names = null;
                        for (JsonNode node : jn.get("names")) {
                            if ("zh-Hans".equals(node.get("language").get("name").asText())) {
                                namesZh = node.get("name").asText();
                            }
                            if ("en".equals(node.get("language").get("name").asText())) {
                                namesEn = node.get("name").asText();
                            }
                        }
                        if (namesEn != null) names = namesEn;
                        if (namesZh != null) names = namesZh;
                        entity.setDescription(names);
                    } catch (Exception ignored) {
                    }
                }
                if (!jn.get("genera").isNull()) {
                    try {
                        String generaZh = null;
                        String generaEn = null;
                        String generaJa = null;
                        String genera = null;
                        for (JsonNode node : jn.get("genera")) {
                            if ("zh-Hans".equals(node.get("language").get("name").asText())) {
                                generaZh = node.get("genus").asText();
                            }
                            if ("en".equals(node.get("language").get("name").asText())) {
                                generaEn = node.get("genus").asText();
                            }
                            if ("ja".equals(node.get("language").get("name").asText())) {
                                generaJa = node.get("genus").asText();
                            }
                        }
                        if (generaJa != null) genera = generaJa;
                        if (generaEn != null) genera = generaEn;
                        if (generaZh != null) genera = generaZh;
                        entity.setGenera(genera);
                    } catch (Exception ignored) {
                    }
                }
                if (!jn.get("varieties").isNull()) {
                    try {
                        ArrayNode varieties = objectMapper.createArrayNode();
                        for (JsonNode node : jn.get("varieties")) {
                            if (!node.get("is_default").asBoolean()) {
                                varieties.add(node.get("pokemon").get("name").asText());
                            }
                        }
                        entity.setVarieties(varieties.toString());
                    } catch (Exception ignored) {
                    }
                }
                if (!jn.get("pokedex_numbers").isNull()) {
                    try {
                        ObjectNode pokedex = objectMapper.createObjectNode();
                        for (JsonNode node : jn.get("pokedex_numbers")) {
                            pokedex.put(node.get("pokedex").get("name").asText(), node.get("entry_number").asInt());
                        }
                        entity.setPokedexNumbers(pokedex.toString());
                    } catch (Exception ignored) {
                    }
                }
                if (!jn.get("flavor_text_entries").isNull()) {
                    try {
                        ObjectNode entries = objectMapper.createObjectNode();
                        for (JsonNode node : jn.get("flavor_text_entries")) {
                            if ("zh-Hans".equals(node.get("language").get("name").asText())) {
                                entries.put(node.get("version").get("name").asText(), node.get("flavor_text").asText());
                            }
                        }
                        entity.setFlavorTextEntries(entries.toString());
                    } catch (Exception ignored) {
                    }
                }
                if (!jn.get("evolution_chain").isNull()) {
                    List<String> list = List.of(jn.get("evolution_chain").get("url").asText().split("/"));
                    entity.setEvolutionChainId(Integer.valueOf(list.get(list.size() - 1)));
                }
                if (CollectionUtils.isEmpty(spList)) {
                    save(entity);
                    logger.info("===========> insert");
                } else {
                    updateById(entity);
                    logger.info("===========> update");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            logger.info("==================================================> pokemon-species done");
            SyncController.isSync = false;
            SyncController.totalSize = 0;
            SyncController.syncSize = 0;
            SyncController.syncModule = "";
        }
    }

}
