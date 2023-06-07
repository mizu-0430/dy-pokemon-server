package cn.dystudio.pokemon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author 张永清
 * @since 2023-03-06
 */
@Getter
@Setter
@TableName("pokemon_list_national")
public class PokemonListNational {

    /**
     * 主键
     */
    @TableId
    private Integer speciesId;

    /**
     * 名称
     */
    private String name;

    /**
     * 名称
     */
    private String description;

    /**
     * 属性1
     */
    private String type1;

    /**
     * 属性2
     */
    private String type2;

    /**
     * 默认图标
     */
    private String icon;

    /**
     * 默认贴图
     */
    private String sprite;

}
