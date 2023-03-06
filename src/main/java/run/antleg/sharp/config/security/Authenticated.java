package run.antleg.sharp.config.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO: 这里不再使用自定义注解，而是使用 @Secured 注解，通过一些权限来控制访问似乎更好一些。记得打开匿名认证
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Authenticated {
}
