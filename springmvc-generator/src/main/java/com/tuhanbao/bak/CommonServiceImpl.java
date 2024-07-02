/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package {package};

import {base_project_head}.base.dataservice.ServiceBean;
import {base_project_head}.base.util.db.table.Table;
import {base_project_head}.base.util.exception.AppException;
import {base_project_head}.base.util.snowflake.SnowFlakeUtil;
import {base_project_head}.web.db.dynamic.service.CommonService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 给动态表使用
 *
 * @author wWX497819
 *
 */
@Service("commonService")
@Transactional
public class CommonServiceImpl extends ServiceImpl<ServiceBean> implements CommonService<ServiceBean> {

    @Value("${spring.datasource.{module}.url}")
    private String url;

    @Override
    public void init() {
    }

    @Override
    public Table getCacheTable() {
        throw new AppException("not support this method.");
    }

    public void deleteData(Table table, String id) {
        this.deleteById(table, id);
    }

    @Override
    protected Object nextId() {
        return SnowFlakeUtil.nextId();
    }

    public void deleteBatch(Table table, long[] ids) {
        List<ServiceBean> list = new ArrayList<>(ids.length);
        for (long id : ids) {
            ServiceBean sb = new ServiceBean(table);
            sb.setKeyValue(id);
            list.add(sb);
        }
        this.delete(list);
    }

    @Override
    public String getDBInstance() {
        // 暂时先简单处理，有复杂情况在改进
        // 找到？，然后往前找一个/，截取中间
        int endIndex = this.url.indexOf("?");
        if (endIndex != -1) {
            int startIndex = this.url.lastIndexOf("/", endIndex);
            return this.url.substring(startIndex + 1, endIndex);
        }
        return null;
    }
}
