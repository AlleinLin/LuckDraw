package io.prizewheel.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * PrizeWheel应用启动类
 * 
 * @author Allein
 * @since 1.0.0
 */
@SpringBootApplication
public class PrizeWheelApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrizeWheelApplication.class, args);
        System.out.println("========================================");
        System.out.println("  PrizeWheel Service Started Successfully");
        System.out.println("========================================");
    }
}
