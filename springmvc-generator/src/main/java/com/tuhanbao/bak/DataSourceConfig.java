/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2021-2021. All rights reserved.
 */

package {project_head}.api.service.{modulename};

import com.alibaba.druid.pool.DruidDataSource;
import {base_project_head}.base.util.db.table.data.BooleanValue;
import {base_project_head}.base.util.exception.AppException;
import {base_project_head}.base.util.objutil.StringUtil;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@MapperScan(basePackages = "{project_head}.impl.mapper.{modulename}", sqlSessionFactoryRef = "{moduleName}SqlSessionFactory")
public class {ModuleName}DataSourceConfig {

    @Value("${spring.datasource.{modulename}.url}")
    private String url;
    @Value("${spring.datasource.{modulename}.username}")
    private String username;
    @Value("${spring.datasource.{modulename}.password}")
    private String password;
    @Value("${spring.datasource.{modulename}.driver-class-name}")
    private String driver;

    // :为非必选字段
    @Value("${spring.datasource.{modulename}.filters:}")
    private String filters;
    @Value("${spring.datasource.{modulename}.max-active:}")
    private String max_active;
    @Value("${spring.datasource.{modulename}.initial-size:}")
    private String initial_size;
    @Value("${spring.datasource.{modulename}.max-wait:}")
    private String max_wait;
    @Value("${spring.datasource.{modulename}.min-idle:}")
    private String min_idle;
    @Value("${spring.datasource.{modulename}.time-between-eviction-runs-millis:}")
    private String time_between_eviction_runs_millis;
    @Value("${spring.datasource.{modulename}.min-evictable-idle-time-millis:}")
    private String min_evictable_idle_time_millis;
    @Value("${spring.datasource.{modulename}.validation-query:}")
    private String validation_query;
    @Value("${spring.datasource.{modulename}.test-while-idle:}")
    private String test_while_idle;
    @Value("${spring.datasource.{modulename}.test-on-borrow:}")
    private String test_on_borrow;
    @Value("${spring.datasource.{modulename}.test-on-return:}")
    private String test_on_return;
    @Value("${spring.datasource.{modulename}.pool-prepared-statements:}")
    private String pool_prepared_statements;
    @Value("${spring.datasource.{modulename}.max-open-prepared-statements:}")
    private String max_open_prepared_statements;
    @Value("${spring.datasource.{modulename}.max-pool-prepared-statement-per-connection-size:}")
    private String max_pool_prepared_statement_per_connection_size;
    @Value("${spring.datasource.{modulename}.break-after-acquire-failure:}")
    private String break_after_acquire_failure;
    @Value("${spring.datasource.{modulename}.connection-error-retry-attempts:}")
    private String connection_error_retry_attempts;
    @Value("${spring.datasource.{modulename}.not-full-timeout-retry-count:}")
    private String not_full_timeout_retry_count;
    @Value("${spring.datasource.{modulename}.socket-timeout}")
    private String socketTimeout;

    @Bean(name = "{moduleName}DataSource"){primary}
    public DataSource getDataSource() throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        druidDataSource.setDriverClassName(driver);

        setFilters(druidDataSource);
        setMaxActive(druidDataSource);
        setInitialSize(druidDataSource);
        setMaxWait(druidDataSource);
        setMinIdle(druidDataSource);
        setTimeBetweenEvictionRunsMillis(druidDataSource);
        setMinEvictableIdleTimeMillis(druidDataSource);
        setValidationQuery(druidDataSource);
        setTestWhileIdle(druidDataSource);
        setTestOnBorrow(druidDataSource);
        setTestOnReturn(druidDataSource);
        setPoolPreparedStatements(druidDataSource);
        setMaxOpenPreparedStatements(druidDataSource);
        setMaxPoolPreparedStatementPerConnectionSize(druidDataSource);
        setConnectionErrorRetryAttempts(druidDataSource);
        setBreakAfterAcquireFailure(druidDataSource);
        setNotFullTimeoutRetryCount(druidDataSource);
        setSocketTimeout(druidDataSource);
        return druidDataSource;
    }

    @Bean(name = "{moduleName}TransactionManager"){primary}
    public DataSourceTransactionManager getDataSourceTransaction(@Qualifier("{moduleName}DataSource") DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);
        return dataSourceTransactionManager;
    }

    @Bean("{moduleName}SqlSessionFactory"){primary}
    public SqlSessionFactoryBean getSqlSessionFactoryBean(@Qualifier("{moduleName}DataSource") DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
        try {
            sqlSessionFactoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
            return sqlSessionFactoryBean;
        } catch (Exception e) {
            throw AppException.getAppException(e);
        }
    }

    private void setNotFullTimeoutRetryCount(DruidDataSource druidDataSource) {
        if (!StringUtil.isEmpty(not_full_timeout_retry_count)) {
            druidDataSource.setNotFullTimeoutRetryCount(Integer.valueOf(not_full_timeout_retry_count));
        }
    }

    private void setBreakAfterAcquireFailure(DruidDataSource druidDataSource) {
        if (!StringUtil.isEmpty(break_after_acquire_failure)) {
            druidDataSource.setBreakAfterAcquireFailure(BooleanValue.valueOf(break_after_acquire_failure).getValue());
        }
    }

    private void setConnectionErrorRetryAttempts(DruidDataSource druidDataSource) {
        if (!StringUtil.isEmpty(connection_error_retry_attempts)) {
            druidDataSource.setConnectionErrorRetryAttempts(Integer.valueOf(connection_error_retry_attempts));
        }
    }

    private void setMaxPoolPreparedStatementPerConnectionSize(DruidDataSource druidDataSource) {
        if (!StringUtil.isEmpty(max_pool_prepared_statement_per_connection_size)) {
            druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(Integer.valueOf(max_pool_prepared_statement_per_connection_size));
        }
    }

    private void setMaxOpenPreparedStatements(DruidDataSource druidDataSource) {
        if (!StringUtil.isEmpty(max_open_prepared_statements)) {
            druidDataSource.setMaxOpenPreparedStatements(Integer.valueOf(max_open_prepared_statements));
        }
    }

    private void setPoolPreparedStatements(DruidDataSource druidDataSource) {
        if (!StringUtil.isEmpty(pool_prepared_statements)) {
            druidDataSource.setPoolPreparedStatements(BooleanValue.valueOf(pool_prepared_statements).getValue());
        }
    }

    private void setTestOnReturn(DruidDataSource druidDataSource) {
        if (!StringUtil.isEmpty(test_on_return)) {
            druidDataSource.setTestOnReturn(BooleanValue.valueOf(test_on_return).getValue());
        }
    }

    private void setTestOnBorrow(DruidDataSource druidDataSource) {
        if (!StringUtil.isEmpty(test_on_borrow)) {
            druidDataSource.setTestOnBorrow(BooleanValue.valueOf(test_on_borrow).getValue());
        }
    }

    private void setTestWhileIdle(DruidDataSource druidDataSource) {
        if (!StringUtil.isEmpty(test_while_idle)) {
            druidDataSource.setTestWhileIdle(BooleanValue.valueOf(test_while_idle).getValue());
        }
    }

    private void setValidationQuery(DruidDataSource druidDataSource) {
        if (!StringUtil.isEmpty(validation_query)) {
            druidDataSource.setValidationQuery(validation_query);
        }
    }

    private void setMinEvictableIdleTimeMillis(DruidDataSource druidDataSource) {
        if (!StringUtil.isEmpty(min_evictable_idle_time_millis)) {
            druidDataSource.setMinEvictableIdleTimeMillis(Long.valueOf(min_evictable_idle_time_millis));
        }
    }

    private void setTimeBetweenEvictionRunsMillis(DruidDataSource druidDataSource) {
        if (!StringUtil.isEmpty(time_between_eviction_runs_millis)) {
            druidDataSource.setTimeBetweenEvictionRunsMillis(Long.valueOf(time_between_eviction_runs_millis));
        }
    }

    private void setMinIdle(DruidDataSource druidDataSource) {
        if (!StringUtil.isEmpty(min_idle)) {
            druidDataSource.setMinIdle(Integer.valueOf(min_idle));
        }
    }

    private void setMaxWait(DruidDataSource druidDataSource) {
        if (!StringUtil.isEmpty(max_wait)) {
            druidDataSource.setMaxWait(Long.valueOf(max_wait));
        }
    }

    private void setInitialSize(DruidDataSource druidDataSource) {
        if (!StringUtil.isEmpty(initial_size)) {
            druidDataSource.setInitialSize(Integer.valueOf(initial_size));
        }
    }

    private void setMaxActive(DruidDataSource druidDataSource) {
        if (!StringUtil.isEmpty(max_active)) {
            druidDataSource.setMaxActive(Integer.valueOf(max_active));
        }
    }

    private void setFilters(DruidDataSource druidDataSource) throws SQLException {
        if (!StringUtil.isEmpty(filters)) {
            druidDataSource.setFilters(filters);
        }
    }

    private void setSocketTimeout(DruidDataSource druidDataSource) {
        if (!StringUtil.isEmpty(socketTimeout)) {
            druidDataSource.setSocketTimeout(Integer.parseInt(socketTimeout));
        }
    }
}
