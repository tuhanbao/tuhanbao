package com.td.ca.base.util.io.excel.easy;

import com.alibaba.excel.converters.ConverterKeyBuild;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.handler.context.SheetWriteHandlerContext;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.td.ca.base.util.io.excel.easy.constraint.Constraint;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.util.List;
import java.util.Objects;

public class ExplicitListSheetWriterHandler implements SheetWriteHandler {

    private final List<Constraint> constraints;

    public ExplicitListSheetWriterHandler(List<Constraint> constraints) {
        this.constraints = constraints;
    }

    @Override
    public void afterSheetCreate(SheetWriteHandlerContext context) {
        if (Objects.isNull(constraints) || constraints.isEmpty()) {
            return;
        }

        WriteSheetHolder writeSheetHolder = context.getWriteSheetHolder();
        for (int i = 0; i < constraints.size(); i++) {
            Constraint constraint = constraints.get(i);
            if (constraint != null) {
                constraint.apply(i, writeSheetHolder);
            }
        }
    }
}
