package com.td.ca.base.util.io.excel.easy;

import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.handler.context.SheetWriteHandlerContext;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.td.ca.base.util.io.excel.easy.constraint.Constraint;

import java.util.List;
import java.util.Objects;

public class ExplicitListCellWriterHandler implements CellWriteHandler {

    private final List<Constraint> constraints;

    public ExplicitListCellWriterHandler(List<Constraint> constraints) {
        this.constraints = constraints;
    }

    public void afterCellCreate(CellWriteHandlerContext context) {
        if (Objects.isNull(constraints) || constraints.isEmpty()) {
            return;
        }
        Constraint constraint = constraints.get(context.getColumnIndex());
        if (constraint != null) {
            context.getExcelContentProperty().setConverter(constraint);
        }
    }
}
