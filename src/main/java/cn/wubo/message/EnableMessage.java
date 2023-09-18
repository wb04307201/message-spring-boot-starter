package cn.wubo.message;

import cn.wubo.message.config.MessageConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用钉钉机器人
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({MessageConfiguration.class})
public @interface EnableMessage {
}
