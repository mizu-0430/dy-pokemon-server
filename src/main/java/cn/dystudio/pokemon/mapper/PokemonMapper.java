package cn.dystudio.pokemon.mapper;

import cn.dystudio.pokemon.entity.Pokemon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 宝可梦 Mapper 接口
 * </p>
 *
 * @author 张永清
 * @since 2023-03-05
 */
@Mapper
public interface PokemonMapper extends BaseMapper<Pokemon> {

}
