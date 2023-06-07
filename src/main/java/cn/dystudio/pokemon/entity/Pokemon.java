package cn.dystudio.pokemon.entity;

import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 宝可梦
 * </p>
 *
 * @author 张永清
 * @since 2023-03-05
 */
@Getter
@Setter
public class Pokemon {

    /**
     * 主键
     */
    @TableId
    private Integer pokemonId;

    /**
     * 更新日期
     */
    private Timestamp updateDate;

    /**
     * 名称
     */
    private String name;

    /**
     * 序号
     */
    @TableField("`order`")
    private Integer order;

    /**
     * 基础经验值
     */
    private Integer baseExperience;

    /**
     * 身高（0.1M）
     */
    private Integer height;

    /**
     * 体重（0.1KG）
     */
    private Integer weight;

    /**
     * 是否默认形态
     */
    private Boolean isDefault;

    /**
     * 种族
     */
    private String species;

    /**
     * 特性1
     */
    private String ability1;

    /**
     * 特性2
     */
    private String ability2;

    /**
     * 特性3
     */
    private String ability3;

    /**
     * 属性1
     */
    private String type1;

    /**
     * 属性2
     */
    private String type2;

    /**
     * HP种族值
     */
    private Integer statHp;

    /**
     * 攻击种族值
     */
    private Integer statAtk;

    /**
     * 防御种族值
     */
    private Integer statDef;

    /**
     * 特攻种族值
     */
    private Integer statSat;

    /**
     * 特防种族值
     */
    private Integer statSde;

    /**
     * 速度种族值
     */
    private Integer statSpd;

    /**
     * 努力值
     */
    private String efforts;

    /**
     * 默认图标
     */
    private String iconFrontDefault;

    /**
     * 异性图标
     */
    private String iconFrontFemale;

    /**
     * 默认贴图
     */
    private String spriteFrontDefault;

    /**
     * 闪光默认贴图
     */
    private String spriteFrontDefault2;

    /**
     * 异性贴图
     */
    private String spriteFrontFemale;

    /**
     * 闪光异性贴图
     */
    private String spriteFrontFemale2;

    /**
     * 默认背面贴图
     */
    private String spriteBackDefault;

    /**
     * 闪光默认背面贴图
     */
    private String spriteBackDefault2;

    /**
     * 异性背面贴图
     */
    private String spriteBackFemale;

    /**
     * 闪光异性背面贴图
     */
    private String spriteBackFemale2;

    /**
     * 绘图
     */
    private String imageDefault;

    /**
     * 闪光绘图
     */
    private String imageDefault2;

    /**
     * 所有形态
     */
    private String forms;

    /**
     * 版本序号
     */
    private String gameIndices;

    /**
     * 携带物品
     */
    private String heldItems;

    /**
     * 出现地点ID
     */
    private Integer locationArea;

    /**
     * 技能
     */
    private String moves;

}
