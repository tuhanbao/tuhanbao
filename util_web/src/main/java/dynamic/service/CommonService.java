/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.td.ca.web.db.dynamic.service;

import com.td.ca.base.dataservice.ServiceBean;
import com.td.ca.base.util.db.table.Table;
import com.td.ca.web.service.IService;

/**
 * 给动态表使用
 *
 * @author wWX497819
 *
 */
public interface CommonService<E> extends IService<ServiceBean> {
    void deleteById(Table table, Object pkValue);

    String getDBInstance();
}
