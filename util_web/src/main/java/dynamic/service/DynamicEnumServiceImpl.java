/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.td.ca.web.db.dynamic.service;

import com.td.ca.base.dataservice.filter.Filter;
import com.td.ca.base.util.exception.AppException;
import com.td.ca.base.util.objutil.ArrayUtil;
import com.td.ca.web.db.dynamic.constant.config.DynamicErrorCode;
import com.td.ca.web.db.dynamic.constant.config.DynamicTableConstants;
import com.td.ca.web.db.dynamic.constant.enums.EnumType;
import com.td.ca.web.db.dynamic.model.DynamicEnum;
import com.td.ca.web.db.dynamic.model.DynamicEnumItem;
import com.td.ca.web.filter.TableSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service("dynamicEnumService")
public class DynamicEnumServiceImpl extends DynamicServiceImpl<DynamicEnum> {

    private static final TableSelector CASCAD_SELECTOR = new TableSelector(DynamicTableConstants.DynamicEnum.TABLE);

    @Autowired
    private DynamicColumnServiceImpl columnService;

    static {
        CASCAD_SELECTOR.joinTable(DynamicTableConstants.DynamicEnumItem.TABLE);
    }

    public void deleteEnum(long id) {
        DynamicEnum dynamicEnum = getEnum(id);
        checkEnumIsUse(dynamicEnum);

        this.deleteRelative(dynamicEnum);
    }

    public void checkEnumIsUse(DynamicEnum dynamicEnum) {
        Filter filter = new Filter();
        filter.andFilter(DynamicTableConstants.DynamicColumn.ARGS, dynamicEnum.getId());
        filter.andFilter(DynamicTableConstants.DynamicColumn.DATA_TYPE, dynamicEnum.getEnumType().value);
        if (ArrayUtil.isEmpty(columnService.select(filter))) {
            throw new AppException(DynamicErrorCode.ENUM_IS_USE);
        }
    }

    public DynamicEnum getEnum(long id) {
        Filter filter = new Filter();
        filter.andFilter(DynamicTableConstants.DynamicEnum.ID, id);
        List<DynamicEnum> list = this.select(CASCAD_SELECTOR, filter);
        if (list != null && !list.isEmpty()) {
            DynamicEnum dynamicEnum = list.get(0);
            sort(dynamicEnum.getDynamicEnumItems());
            return dynamicEnum;
        } else {
            return null;
        }
    }

    public List<DynamicEnum> getEnumsByType(EnumType type) {
        Filter filter = new Filter();
        filter.andFilter(DynamicTableConstants.DynamicEnum.ENUM_TYPE, type.value);
        List<DynamicEnum> enums = this.select(CASCAD_SELECTOR, filter);

        for (DynamicEnum e : enums) {
            List<DynamicEnumItem> items = e.getDynamicEnumItems();
            sort(items);
        }
        return enums;
    }

    private void sort(List<DynamicEnumItem> items) {
        if (items == null) {
            return;
        }
        Map<Long, DynamicEnumItem> map = new HashMap<>();
        items.forEach(item -> map.put(item.getId(), item));

        for (Iterator<DynamicEnumItem> it = items.iterator(); it.hasNext();) {
            DynamicEnumItem item = it.next();
            long parentId = item.getParentId();
            if (parentId > 0) {
                DynamicEnumItem parent = map.get(parentId);
                if (parent != null) {
                    List<DynamicEnumItem> children = parent.getDynamicEnumItems();
                    if (children == null) {
                        parent.setDynamicEnumItems(new ArrayList<>());
                        children = parent.getDynamicEnumItems();
                    }
                    children.add(item);
                }
                it.remove();
            }
        }
    }
}