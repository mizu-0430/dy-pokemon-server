package cn.dystudio.pokemon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 图鉴
 * </p>
 *
 * @author 张永清
 * @since 2023-03-02
 */
@Getter
@Setter
public class Species {

    /**
     * 主键
     */
    @TableId(value = "species_id", type = IdType.AUTO)
    private Integer speciesId;

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
    private Integer order;

    /**
     * 雌性比例
     */
    private Integer genderRate;

    /**
     * 基本捕获率
     */
    private Integer captureRate;

    /**
     * 基本好感度
     */
    private Integer baseHappiness;

    /**
     * 基础孵化数
     */
    private Integer hatchCounter;

    /**
     * 是否宝宝宝可梦
     */
    private Boolean isBaby;

    /**
     * 是否传说宝可梦
     */
    private Boolean isLegendary;

    /**
     * 是否神话宝可梦
     */
    private Boolean isMythical;

    /**
     * 是否有性别差异
     */
    private Boolean hasGenderDifferences;

    /**
     * 是否切换多形态
     */
    private Boolean formsSwitchable;

    /**
     * 成长速率
     */
    private String growthRate;

    /**
     * 蛋组1
     */
    private String eggGroup1;

    /**
     * 蛋组2
     */
    private String eggGroup2;

    /**
     * 颜色
     */
    private String color;

    /**
     * 形状
     */
    private String shape;

    /**
     * 栖息地
     */
    private String habitat;

    /**
     * 世代
     */
    private String generation;

    /**
     * 进化前
     */
    private String evolvesFromSpecies;

    /**
     * 名称
     */
    private String names;

    /**
     * 分类
     */
    private String genera;

    /**
     * 形态
     */
    private String varieties;

    /**
     * 图鉴序号
     */
    private String pokedexNumbers;

    /**
     * 图鉴描述
     */
    private String flavorTextEntries;

    /**
     * 进化链ID
     */
    private Integer evolutionChainId;

}
