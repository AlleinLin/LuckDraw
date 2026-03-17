package io.prizewheel.adapters.generator;

import io.prizewheel.core.port.output.IdGeneratorPort;
import io.prizewheel.core.service.SnowflakeIdGenerator;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ID生成器适配器
 * 
 * @author Allein
 * @since 2.0.0
 */
@Component
public class IdGeneratorAdapter implements IdGeneratorPort {

    private final SnowflakeIdGenerator snowflakeIdGenerator;

    public IdGeneratorAdapter() {
        this.snowflakeIdGenerator = new SnowflakeIdGenerator(1, 1);
    }

    public IdGeneratorAdapter(long workerId, long datacenterId) {
        this.snowflakeIdGenerator = new SnowflakeIdGenerator(workerId, datacenterId);
    }

    @Override
    public Long nextId() {
        return snowflakeIdGenerator.nextId();
    }

    @Override
    public List<Long> nextIds(int count) {
        return snowflakeIdGenerator.nextIds(count);
    }
}
