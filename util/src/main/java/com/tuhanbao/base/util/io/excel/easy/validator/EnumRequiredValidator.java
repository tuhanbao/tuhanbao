package com.td.ca.base.util.io.excel.easy.validator;

import com.td.ca.base.util.exception.BaseErrorCode;
import com.td.ca.base.util.objutil.StringUtil;
import com.td.ca.base.util.rm.ResourceManager;

import java.lang.reflect.Field;

public class EnumRequiredValidator extends RequiredValidator {

    public EnumRequiredValidator(Field field, String headName) {
        super(field, headName);
    }

    @Override
    public int getErrorCode() {
        return BaseErrorCode.ILLEGAL_INCOMING_ARGUMENT;
    }
}