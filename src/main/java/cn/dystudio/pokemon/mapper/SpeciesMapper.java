package cn.dystudio.pokemon.mapper;

import cn.dystudio.pokemon.entity.Species;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 图鉴 Mapper 接口
 * </p>
 *
 * @author 张永清
 * @since 2023-03-02
 */
@Mapper
public interface SpeciesMapper extends BaseMapper<Species> {

}
