/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.td.ca.web.db.dynamic.service;

import com.td.ca.base.dataservice.filter.Filter;
import com.td.ca.base.util.exception.AppException;
import com.td.ca.web.db.dynamic.constant.config.DynamicErrorCode;
import com.td.ca.web.db.dynamic.constant.config.DynamicTableConstants;
import com.td.ca.web.db.dynamic.model.DynamicEnum;
import com.td.ca.web.db.dynamic.model.DynamicEnumItem;
import com.td.ca.web.filter.TableSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("dynamicEnumItemService")
public class DynamicEnumItemServiceImpl extends DynamicServiceImpl<DynamicEnumItem> {

    private static final TableSelector CASCAD_SELECTOR = new TableSelector(DynamicTableConstants.DynamicEnumItem.TABLE);

    @Autowired
    private DynamicEnumServiceImpl enumService;

    static {
        CASCAD_SELECTOR.joinTable(DynamicTableConstants.DynamicEnumItem.TABLE).joinTable(DynamicTableConstants.DynamicEnumItem.TABLE)
            .joinTable(DynamicTableConstants.DynamicEnumItem.TABLE);
    }

    public void deleteItem(long id) {
        Filter filter = new Filter();
        filter.andFilter(DynamicTableConstants.DynamicEnumItem.ID, id);
        List<DynamicEnumItem> list = this.select(CASCAD_SELECTOR, filter);
        if (list != null && !list.isEmpty()) {
            DynamicEnumItem item = list.get(0);

            // 枚举一旦被使用，不允许删除
            DynamicEnum dynamicEnum = enumService.getEnum(id);
            enumService.checkEnumIsUse(dynamicEnum);
            this.deleteRelative(item);
        } else {
            throw new AppException(DynamicErrorCode.NO_RECORD_DATA);
        }
    }
}