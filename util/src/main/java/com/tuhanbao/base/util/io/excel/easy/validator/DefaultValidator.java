package com.td.ca.base.util.io.excel.easy.validator;

import com.td.ca.base.util.clazz.ClazzUtil;
import com.td.ca.base.util.exception.BaseErrorCode;
import com.td.ca.base.util.objutil.StringUtil;
import com.td.ca.base.util.rm.ResourceManager;

import java.lang.reflect.Field;

public abstract class DefaultValidator implements ImportValidator {

    protected Field field;

    protected String headName;

    public DefaultValidator(Field field, String headName) {
        this.field = field;
        this.headName = headName;
    }

    @Override
    public String check(Object data) {
        try {
            return checkInner(ClazzUtil.getVarValue(data, field));
        } catch (Throwable e) {
            return ResourceManager.getResource(BaseErrorCode.EMPTY_ERROR + "", headName);
        }
    }

    public String getErrorMsg() {
        return ResourceManager.getResource(getErrorCode() + "", headName);
    }

    public abstract String checkInner(Object value) throws Exception;

    public abstract int getErrorCode();
}