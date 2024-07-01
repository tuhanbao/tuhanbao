package com.td.ca.base.util.io.excel.easy.validator;

import com.td.ca.base.util.exception.BaseErrorCode;
import com.td.ca.base.util.rm.ResourceManager;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class UniqueValidator extends DefaultValidator {

    private Set<Object> set = new HashSet<>();

    public UniqueValidator(Field field, String headName) {
        super(field, headName);
    }

    @Override
    public String checkInner(Object value) {
        try {
            if (RequiredValidator.isEmpty(value)) {
                return ResourceManager.getResource(BaseErrorCode.EMPTY_ERROR + "", headName);
            } else {
                if (set.contains(value)) {
                    return getErrorMsg();
                } else {
                    set.add(value);
                    return null;
                }
            }
        } catch (Throwable e) {
            return ResourceManager.getResource(BaseErrorCode.EMPTY_ERROR + "", headName);
        }
    }

    @Override
    public int getErrorCode() {
        return BaseErrorCode.UNIQUE_ERROR;
    }
}