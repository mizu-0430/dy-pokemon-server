package cn.dystudio.pokemon.service;

import cn.dystudio.pokemon.entity.Pokemon;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 宝可梦 服务类
 * </p>
 *
 * @author 张永清
 * @since 2023-03-05
 */
public interface PokemonService extends IService<Pokemon> {

    @Transactional
    void sync();

}
