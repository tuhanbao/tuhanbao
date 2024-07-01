package com.td.ca.base.util.io.excel.easy.constraint;

import com.td.ca.base.Constants;
import com.td.ca.base.util.rm.ResourceManager;

public class BooleanConstraint extends ExplicitListConstraint<Boolean> {

    private static final BooleanConstraint INSTANCE = new BooleanConstraint();

    private BooleanConstraint() {
        super(new Object[]{Boolean.TRUE, Boolean.FALSE}, new String[]{ResourceManager.getResource(Constants.YES),
                ResourceManager.getResource(Constants.NO)});
    }

    public static Constraint getInstance() {
        return INSTANCE;
    }

    @Override
    public Class<?> supportJavaTypeKey() {
        return Boolean.class;
    }
}