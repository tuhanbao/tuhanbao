package com.tuhanbao.thirdapi.cache;

import com.tuhanbao.base.dataservice.IDataGroup;
import com.tuhanbao.base.util.db.IField;

public interface ICacheKey extends IDataGroup<IField> {
    boolean isExpire();
    
    int getExpireTime();
}
