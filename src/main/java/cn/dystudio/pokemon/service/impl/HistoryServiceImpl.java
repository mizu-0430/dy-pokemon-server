package cn.dystudio.pokemon.service.impl;

import cn.dystudio.pokemon.entity.History;
import cn.dystudio.pokemon.mapper.HistoryMapper;
import cn.dystudio.pokemon.service.HistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * PokeApi调用记录 服务实现类
 * </p>
 *
 * @author 张永清
 * @since 2023-03-02
 */
@Service
public class HistoryServiceImpl extends ServiceImpl<HistoryMapper, History> implements HistoryService {

}
