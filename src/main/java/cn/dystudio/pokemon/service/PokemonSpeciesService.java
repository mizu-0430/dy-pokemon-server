package cn.dystudio.pokemon.service;

import cn.dystudio.pokemon.entity.PokemonSpecies;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 宝可梦种族 服务类
 * </p>
 *
 * @author 张永清
 * @since 2023-03-02
 */
public interface PokemonSpeciesService extends IService<PokemonSpecies> {

    @Transactional
    void sync();

}
