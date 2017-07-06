package com.tuhanbao.thirdapi.cache;

import java.util.List;

import com.tuhanbao.base.util.db.IField;

public abstract class AbstractCacheKey implements ICacheKey {
    public Class<?> getModelClassName() {
        return null;
    }

    @Override
    public List<IField> getFields() {
        return null;
    }
}
