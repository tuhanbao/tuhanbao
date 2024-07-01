package com.td.ca.base.util.io.excel.easy;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.support.cglib.beans.BeanMap;
import com.alibaba.excel.util.BeanMapUtils;
import com.alibaba.excel.util.ClassUtils;
import com.alibaba.excel.util.ConverterUtils;
import com.td.ca.base.Constants;
import com.td.ca.base.util.exception.AppException;
import com.td.ca.base.util.io.excel.easy.constraint.BooleanConstraint;
import com.td.ca.base.util.io.excel.easy.constraint.Constraint;
import com.td.ca.base.util.io.excel.easy.constraint.EnumConstraint;
import com.td.ca.base.util.io.excel.easy.constraint.TimeConstraint;
import com.td.ca.base.util.io.excel.easy.validator.EnumRequiredValidator;
import com.td.ca.base.util.io.excel.easy.validator.ImportValidator;
import com.td.ca.base.util.io.excel.easy.validator.RequiredValidator;
import com.td.ca.base.util.io.excel.easy.validator.UniqueValidator;
import com.td.ca.base.util.objutil.StringUtil;
import com.td.ca.base.util.rm.ResourceManager;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class EasyExcelReadListener<T extends Object> extends AnalysisEventListener<T> {

    public static final String IMPORT_LINE = "IMPORT_LINE";
    public static final String IMPORT_FAIL = "IMPORT_FAIL";

    private List<T> list = new ArrayList();

    private Class<T> clazz;

    private StringBuilder errMsg = new StringBuilder();

    private List<ImportValidator> validators;

    private int headerRow = 0;

    public EasyExcelReadListener(Class<T> clazz, int headerRow) {
        this.clazz = clazz;
        this.headerRow = headerRow;
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        String err = checkData(data);
        int row = context.readRowHolder().getRowIndex();
        if (StringUtil.isEmpty(err)) {
            if (data instanceof DataWithRowNo) {
                // csv可能为回空
                if (((DataWithRowNo) data).isEmpty()) {
                    return;
                }
                ((DataWithRowNo) data).setRowNo(row);
            }
            list.add(data);
        } else {
            errMsg.append(ResourceManager.getResource(IMPORT_LINE, (row + 1) + "")).append(err).append(Constants.ENTER);
        }
    }

    private String checkData(T data) {
        if (validators == null || validators.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (ImportValidator validator : validators) {
            String err = validator.check(data);
            if (!StringUtil.isEmpty(err)) {
                sb.append(err).append(" ");
            }
        }
        return sb.toString();
    }

    public List<T> getResult() {
        if (StringUtil.isEmpty(errMsg.toString())) {
            return list;
        } else {
            errMsg.insert(0, ResourceManager.getResource(IMPORT_FAIL));
            throw new AppException(errMsg.toString());
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("doAfterAllAnalysed");
    }

    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        if (headMap == null || headMap.isEmpty()) {
            return;
        }
        ReadCellData<?> cellData = headMap.entrySet().stream().findFirst().get().getValue();
        if (cellData == null || cellData.getRowIndex() != headerRow) {
            return;
        }

        Map<Integer, Field> sortedAllFiledMap = new TreeMap<>();
        ClassUtils.declaredFields(clazz, sortedAllFiledMap, false, null);

        List<Constraint> constraintList = EasyExcelUtil.getConstraints(sortedAllFiledMap);
        Object resultModel;
        try {
            resultModel = clazz.newInstance();
        } catch (Exception e) {
            throw new ExcelDataConvertException(context.readRowHolder().getRowIndex(), 0,
                    new ReadCellData<>(CellDataTypeEnum.EMPTY), null,
                    "Can not instance class: " + clazz.getName(), e);
        }
        BeanMap dataMap = BeanMapUtils.create(resultModel);
        for (Map.Entry<Integer, Field> entry : sortedAllFiledMap.entrySet()) {
            Field field = entry.getValue();
            ExcelContentProperty excelContentProperty = ClassUtils.declaredExcelContentProperty(dataMap, clazz,
                    field.getName());
            excelContentProperty.setConverter(constraintList.get(entry.getKey()));
        }

        this.validators = initValidators(sortedAllFiledMap, headMap);
    }

    private List<ImportValidator> initValidators(Map<Integer, Field> sortedAllFiledMap, Map<Integer, ReadCellData<?>> headMap) {
        List<ImportValidator> validators = new ArrayList<>();
        for (Map.Entry<Integer, Field> entry : sortedAllFiledMap.entrySet()) {
            Field value = entry.getValue();
            DiyExcelProperty excelProperty = value.getAnnotation(DiyExcelProperty.class);

            if (excelProperty != null) {
                String headName = headMap.get(entry.getKey()).getStringValue();
                if (headName == null) {   // 表头有合并单元格，可能导致无法读取值
                    continue;
                }
                if (headName.endsWith("(*)")) {
                    headName.substring(0, headName.length() - 3);
                }
                if (excelProperty.required()) {
                    Class<?> type = value.getType();
                    Class<?> enumClass = EasyExcelUtil.getEnumClass(type, excelProperty);
                    if (enumClass != null) {
                        validators.add(new EnumRequiredValidator(value, headName));
                    } else {
                        validators.add(new RequiredValidator(value, headName));
                    }
                }

                if (excelProperty.unique()) {
                    validators.add(new UniqueValidator(value, headName));
                }
            }
        }
        return validators;
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        super.onException(exception, context);
    }

    @Override
    public void extra(CellExtra extra, AnalysisContext context) {
        super.extra(extra, context);
    }

    @Override
    public boolean hasNext(AnalysisContext context) {
        return super.hasNext(context);
    }
}
