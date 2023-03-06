package cn.dystudio.pokemon.service.impl;

import cn.dystudio.pokemon.controller.SyncController;
import cn.dystudio.pokemon.entity.Pokemon;
import cn.dystudio.pokemon.mapper.PokemonMapper;
import cn.dystudio.pokemon.service.PokemonService;
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
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * 宝可梦 服务实现类
 * </p>
 *
 * @author 张永清
 * @since 2023-03-05
 */
@Service
public class PokemonServiceImpl extends ServiceImpl<PokemonMapper, Pokemon> implements PokemonService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${pokeApi.path}")
    private String apiUrl;

    @Value("${pokeApi.pokemon.sprite.url.prefix}")
    private String spriteUrlPrefix;

    @Override
    public void sync() {
        SyncController.isSync = true;
        SyncController.totalSize = 0;
        SyncController.syncSize = 0;
        SyncController.syncModule = "pokemon";
        try {
            logger.info("==================================================> pokemon");
            RestTemplate template = new RestTemplate();
            String url = apiUrl + "pokemon?offset=0&limit=10000";
            String result = template.getForObject(url, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode results = objectMapper.readTree(result);
            JsonNode pokemonList = results.get("results");
            SyncController.totalSize = pokemonList.size();
            for (JsonNode pokemon : pokemonList) {
                SyncController.syncSize++;
                logger.info("=========================> " + pokemon.get("name"));
                result = template.getForObject(pokemon.get("url").asText(), String.class);
                JsonNode jn = objectMapper.readTree(result);
                List<Pokemon> spList = list(
                        new QueryWrapper<Pokemon>().lambda()
                                .eq(Pokemon::getName, pokemon.get("name").asText())
                );
                Pokemon entity;
                if (CollectionUtils.isEmpty(spList)) {
                    entity = new Pokemon();
                    entity.setPokemonId(jn.get("id").asInt());
                } else {
                    entity = spList.get(0);
                }
                entity.setName(jn.get("name").asText());
                entity.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                if (!jn.get("order").isNull())
                    entity.setOrder(jn.get("order").asInt());
                if (!jn.get("base_experience").isNull())
                    entity.setBaseExperience(jn.get("base_experience").asInt());
                if (!jn.get("height").isNull())
                    entity.setHeight(jn.get("height").asInt());
                if (!jn.get("weight").isNull())
                    entity.setWeight(jn.get("weight").asInt());
                if (!jn.get("is_default").isNull())
                    entity.setIsDefault(jn.get("is_default").asBoolean());
                if (!jn.get("species").isNull())
                    entity.setSpecies(jn.get("species").get("name").asText());
                try {
                    JsonNode abilities = jn.get("abilities");
                    for (JsonNode ability : abilities) {
                        switch (ability.get("slot").asInt()) {
                            case 1 -> entity.setAbility1(ability.get("ability").get("name").asText());
                            case 2 -> entity.setAbility2(ability.get("ability").get("name").asText());
                            case 3 -> entity.setAbility3(ability.get("ability").get("name").asText());
                        }
                    }
                } catch (Exception ignored) {
                }
                try {
                    JsonNode types = jn.get("types");
                    for (JsonNode type : types) {
                        switch (type.get("slot").asInt()) {
                            case 1 -> entity.setType1(type.get("type").get("name").asText());
                            case 2 -> entity.setType2(type.get("type").get("name").asText());
                        }
                    }
                } catch (Exception ignored) {
                }
                try {
                    JsonNode stats = jn.get("stats");
                    ObjectNode efforts = objectMapper.createObjectNode();
                    for (JsonNode stat : stats) {
                        switch (stat.get("stat").get("name").asText()) {
                            case "hp" -> entity.setStatHp(stat.get("base_stat").asInt());
                            case "attack" -> entity.setStatAtk(stat.get("base_stat").asInt());
                            case "defense" -> entity.setStatDef(stat.get("base_stat").asInt());
                            case "special-attack" -> entity.setStatSat(stat.get("base_stat").asInt());
                            case "special-defense" -> entity.setStatSde(stat.get("base_stat").asInt());
                            case "speed" -> entity.setStatSpd(stat.get("base_stat").asInt());
                        }
                        if (stat.get("effort").asInt() > 0) {
                            efforts.put(stat.get("stat").get("name").asText(), stat.get("effort").asInt());
                        }
                    }
                    entity.setEfforts(efforts.toString());
                } catch (Exception ignored) {
                }
                if (!jn.get("forms").isNull()) {
                    try {
                        ArrayNode forms = objectMapper.createArrayNode();
                        for (JsonNode form : jn.get("forms")) {
                            forms.add(form.get("name").asText());
                        }
                        entity.setForms(forms.toString());
                    } catch (Exception ignored) {
                    }
                }
                if (!jn.get("game_indices").isNull()) {
                    try {
                        ObjectNode gameIndices = objectMapper.createObjectNode();
                        for (JsonNode indices : jn.get("game_indices")) {
                            gameIndices.put(indices.get("version").get("name").asText(), indices.get("game_index").asInt());
                        }
                        entity.setGameIndices(gameIndices.toString());
                    } catch (Exception ignored) {
                    }
                }
                if (!jn.get("held_items").isNull()) {
                    try {
                        ObjectNode items = objectMapper.createObjectNode();
                        for (JsonNode heldItem : jn.get("held_items")) {
                            ObjectNode details = objectMapper.createObjectNode();
                            for (JsonNode versionDetail : heldItem.get("version_details")) {
                                details.put(versionDetail.get("version").get("name").asText(), versionDetail.get("rarity").asInt());
                            }
                            items.put(heldItem.get("item").get("name").asText(), details.toString());
                        }
                        entity.setHeldItems(items.toString());
                    } catch (Exception ignored) {
                    }
                }
                if (!jn.get("location_area_encounters").isNull()) {
                    List<String> list = List.of(jn.get("location_area_encounters").asText().split("/"));
                    entity.setLocationArea(Integer.valueOf(list.get(list.size() - 2)));
                }
                if (!jn.get("sprites").isNull()) {
                    JsonNode sprites = jn.get("sprites");
                    int length = spriteUrlPrefix.length();
                    if (!sprites.get("front_default").isNull())
                        entity.setSpriteFrontDefault(sprites.get("front_default").asText().substring(length));
                    if (!sprites.get("front_female").isNull())
                        entity.setSpriteFrontFemale(sprites.get("front_female").asText().substring(length));
                    if (!sprites.get("front_shiny").isNull())
                        entity.setSpriteFrontDefault2(sprites.get("front_shiny").asText().substring(length));
                    if (!sprites.get("front_shiny_female").isNull())
                        entity.setSpriteFrontFemale2(sprites.get("front_shiny_female").asText().substring(length));
                    if (!sprites.get("back_default").isNull())
                        entity.setSpriteBackDefault(sprites.get("back_default").asText().substring(length));
                    if (!sprites.get("back_female").isNull())
                        entity.setSpriteBackFemale(sprites.get("back_female").asText().substring(length));
                    if (!sprites.get("back_shiny").isNull())
                        entity.setSpriteBackDefault2(sprites.get("back_shiny").asText().substring(length));
                    if (!sprites.get("back_shiny_female").isNull())
                        entity.setSpriteBackFemale2(sprites.get("back_shiny_female").asText().substring(length));
                    if (!sprites.get("versions").isNull()) {
                        Iterator<String> iterator = sprites.get("versions").fieldNames();
                        while (iterator.hasNext()) {
                            JsonNode generation = sprites.get("versions").get(iterator.next());
                            if (generation.has("icons")) {
                                JsonNode icons = generation.get("icons");
                                if (icons.has("front_default") && !icons.get("front_default").isNull()) {
                                    entity.setIconFrontDefault(icons.get("front_default").asText().substring(length));
                                }
                                if (icons.has("front_female") && !icons.get("front_female").isNull()) {
                                    entity.setIconFrontFemale(icons.get("front_female").asText().substring(length));
                                }
                            }
                        }
                    }
                }
                if (!jn.get("moves").isNull()) {
                    try {
                        ObjectNode moves = objectMapper.createObjectNode();
                        for (JsonNode move : jn.get("moves")) {
                            for (JsonNode detail : move.get("version_group_details")) {
                                String versionGroup = detail.get("version_group").get("name").asText();
                                if (!moves.has(versionGroup)) {
                                    moves.putArray(versionGroup);
                                }
                                ArrayNode array = (ArrayNode) moves.get(versionGroup);
                                ObjectNode learn = objectMapper.createObjectNode();
                                learn.put("n", move.get("move").get("name").asText());
                                learn.put("m", detail.get("move_learn_method").get("name").asText());
                                learn.put("l", detail.get("level_learned_at").asInt());
                                array.add(learn);
                                moves.replace(versionGroup, array);
                            }
                        }
                        entity.setMoves(moves.toString());
                    } catch (Exception ignored) {
                    }
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
            logger.info("==================================================> pokemon done");
            SyncController.isSync = false;
            SyncController.totalSize = 0;
            SyncController.syncSize = 0;
            SyncController.syncModule = "";
        }
    }

}
