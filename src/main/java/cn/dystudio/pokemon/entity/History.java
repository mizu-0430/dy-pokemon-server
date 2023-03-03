package cn.dystudio.pokemon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * PokeApi调用记录
 * </p>
 *
 * @author 张永清
 * @since 2023-03-02
 */
@Getter
@Setter
public class History {

    /**
     * 主键
     */
    @TableId(value = "history_id", type = IdType.AUTO)
    private Integer historyId;

    /**
     * 生成日期
     */
    private Timestamp updateDate;

    /**
     * 接口类型
     */
    private String type;

    /**
     * 接口内容
     */
    private String content;

}
