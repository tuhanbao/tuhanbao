package com.tuhanbao.web.filter;

import com.tuhanbao.base.util.db.table.Column;

public interface ISpecialFilterItemHandler {
    void handle(WebFilterItem item, Column col);
}
