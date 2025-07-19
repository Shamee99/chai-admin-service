package org.shamee.common.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.shamee.common.db.typehandler.ArrayToListTypeHandler;
import org.shamee.common.util.context.SecurityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis Plus 配置
 *
 * @author shamee
 * @since 2024-01-01
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * MyBatis Plus 拦截器配置
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        
        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.POSTGRE_SQL));
        
        // 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        
        return interceptor;
    }

    /**
     * 自动填充处理器
     */
    @Component
    public static class AutoFillMetaObjectHandler implements MetaObjectHandler {

        @Override
        public void insertFill(MetaObject metaObject) {
            LocalDateTime now = LocalDateTime.now();
            // TODO: 从当前登录用户获取用户ID，这里暂时设置为1
            String currentUserId = getCurrentUserId();
            
            this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, now);
            this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, now);
            this.strictInsertFill(metaObject, "createBy", String.class, currentUserId);
            this.strictInsertFill(metaObject, "updateBy", String.class, currentUserId);
            this.strictInsertFill(metaObject, "version", Integer.class, 1);
        }

        @Override
        public void updateFill(MetaObject metaObject) {
            LocalDateTime now = LocalDateTime.now();
            // TODO: 从当前登录用户获取用户ID，这里暂时设置为1
            String currentUserId = getCurrentUserId();
            
            this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, now);
            this.strictUpdateFill(metaObject, "updateBy", String.class, currentUserId);
        }

        /**
         * 获取当前用户ID
         * TODO: 后续集成认证后从SecurityContext或ThreadLocal获取
         */
        private String getCurrentUserId() {
            // 暂时返回固定值，后续需要从认证上下文获取
            return SecurityUtils.getUserId();
        }
    }

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> {
            // 注册自定义类型处理器
            configuration.getTypeHandlerRegistry().register(ArrayToListTypeHandler.class);
        };
    }
}