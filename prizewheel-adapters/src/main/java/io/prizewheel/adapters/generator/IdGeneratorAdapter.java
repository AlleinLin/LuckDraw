package io.prizewheel.adapters.generator;

import io.prizewheel.core.port.output.IdGeneratorPort;
import io.prizewheel.core.service.SnowflakeIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;

/**
 * ID生成器适配器
 * 
 * 支持配置化workerId和datacenterId，适用于分布式部署场景
 * 配置项：
 * - prizewheel.id-generator.worker-id: 工作机器ID (0-31)
 * - prizewheel.id-generator.datacenter-id: 数据中心ID (0-31)
 * 
 * 如未配置，将自动根据IP地址生成workerId
 * 
 * @author Allein
 * @since 2.0.0
 */
@Component
public class IdGeneratorAdapter implements IdGeneratorPort {

    private static final Logger log = LoggerFactory.getLogger(IdGeneratorAdapter.class);

    @Value("${prizewheel.id-generator.worker-id:-1}")
    private long configuredWorkerId;

    @Value("${prizewheel.id-generator.datacenter-id:1}")
    private long datacenterId;

    private SnowflakeIdGenerator snowflakeIdGenerator;

    @PostConstruct
    public void init() {
        long workerId = configuredWorkerId;
        
        if (workerId < 0) {
            workerId = generateWorkerIdFromIP();
            log.info("未配置workerId，根据IP自动生成: {}", workerId);
        }
        
        if (workerId < 0 || workerId > 31) {
            log.warn("workerId超出范围[0-31]，使用默认值1");
            workerId = 1;
        }
        
        if (datacenterId < 0 || datacenterId > 31) {
            log.warn("datacenterId超出范围[0-31]，使用默认值1");
            datacenterId = 1;
        }
        
        this.snowflakeIdGenerator = new SnowflakeIdGenerator(workerId, datacenterId);
        log.info("ID生成器初始化完成 workerId:{} datacenterId:{}", workerId, datacenterId);
    }

    @Override
    public Long nextId() {
        return snowflakeIdGenerator.nextId();
    }

    @Override
    public List<Long> nextIds(int count) {
        return snowflakeIdGenerator.nextIds(count);
    }

    private long generateWorkerIdFromIP() {
        try {
            InetAddress ip = getLocalInetAddress();
            if (ip == null) {
                return 1L;
            }
            byte[] mac = NetworkInterface.getByInetAddress(ip).getHardwareAddress();
            if (mac == null) {
                return 1L;
            }
            long id = ((0x000000FF & (long) mac[mac.length - 2]) | (0x0000FF00 & (((long) mac[mac.length - 1]) << 8))) >> 6;
            id = id % 32;
            return id;
        } catch (SocketException e) {
            log.warn("根据IP生成workerId失败，使用默认值", e);
            return 1L;
        }
    }

    private InetAddress getLocalInetAddress() {
        try {
            InetAddress candidateAddress = null;
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            if (networkInterfaces == null) {
                return InetAddress.getLocalHost();
            }
            
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress.isSiteLocalAddress()) {
                        if (candidateAddress == null) {
                            candidateAddress = inetAddress;
                        }
                        if (inetAddress.getHostAddress().indexOf('.') > 0) {
                            return inetAddress;
                        }
                    }
                }
            }
            
            return candidateAddress != null ? candidateAddress : InetAddress.getLocalHost();
        } catch (SocketException | UnknownHostException e) {
            log.warn("获取本地IP地址失败", e);
            return null;
        }
    }
}
