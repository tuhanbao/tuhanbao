package com.td.ca.base.util.io.excel.easy.validator;

import com.td.ca.base.util.exception.AppException;
import com.td.ca.base.util.exception.BaseErrorCode;
import com.td.ca.base.util.objutil.StringUtil;
import com.td.ca.base.util.rm.ResourceManager;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class RequiredValidator extends DefaultValidator {

    public RequiredValidator(Field field, String headName) {
        super(field, headName);
    }

    @Override
    public String checkInner(Object value) {
        if (isEmpty(value)) {
            return getErrorMsg();
        } else {
            return null;
        }
    }

    @Override
    public int getErrorCode() {
        return BaseErrorCode.EMPTY_ERROR;
    }

    public static boolean isEmpty(Object value) {
        return value == null || (value instanceof String && StringUtil.isEmptyTrim(value.toString()));
    }
}