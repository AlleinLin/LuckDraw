package io.prizewheel.core.port.output;

import java.util.List;

/**
 * ID生成器输出端口
 * 
 * @author Allein
 * @since 1.0.0
 */
public interface IdGeneratorPort {

    /**
     * 生成下一个ID
     *
     * @return ID
     */
    Long nextId();

    /**
     * 批量生成ID
     *
     * @param count 数量
     * @return ID列表
     */
    List<Long> nextIds(int count);
}
