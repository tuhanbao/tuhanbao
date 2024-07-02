/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.td.ca.web.db.dynamic.model;


import lombok.Data;

@Data
public class OldColumn {
    public String colName;

    public String defaultValue;

    public String dataType;
}
