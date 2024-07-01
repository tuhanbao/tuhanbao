package com.td.ca.base.util.io.excel.easy;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.util.ClassUtils;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.td.ca.base.util.exception.AppException;
import com.td.ca.base.util.io.excel.easy.constraint.BooleanConstraint;
import com.td.ca.base.util.io.excel.easy.constraint.Constraint;
import com.td.ca.base.util.io.excel.easy.constraint.EnumConstraint;
import com.td.ca.base.util.io.excel.easy.constraint.TimeConstraint;
import com.td.ca.base.util.objutil.ArrayUtil;
import com.td.ca.base.util.objutil.StringUtil;
import com.td.ca.base.util.rm.ResourceManager;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class EasyExcelUtil {

    public static <E> void writeSheet(OutputStream out, String sheetName, ExportType exportType, List<E> list) throws IOException {
        writeSheet(out, sheetName, exportType, list, null);
    }

    /**
     * list可能为null或empty，可以通过clazz来指定表头
     */
    public static <E> void writeSheet(OutputStream out, String sheetName, ExportType exportType, List<E> list, Class<?> clazz) throws IOException {
        // excel读取csv时根据bom头字段确定编码格式，不设置UTF8中文为乱码
        if (exportType == exportType.CSV) {
            setUtf8BomToStream(out);
        }

        ExcelWriterBuilder builder;
        if (clazz != null || ArrayUtil.isEmpty(list)) {
            builder = EasyExcel.write(out, clazz);
        } else {
            clazz = list.get(0).getClass();
            builder = EasyExcel.write(out, clazz);
        }
        builder.excelType(exportType.getExcelTypeEnum());

        initWriteBuilder(clazz, exportType, builder);
        builder.registerWriteHandler(setHeaderStyle(IndexedColors.LIGHT_GREEN, (short) 10));

        ExcelWriterSheetBuilder sheetBuilder;
        if (StringUtil.isEmpty(sheetName)) {
            sheetBuilder = builder.sheet();
        } else {
            sheetBuilder = builder.sheet(sheetName);
        }

        sheetBuilder.doWrite(list);
    }

    public static <E> List<E> readSheet(InputStream inputStream, ExportType exportType, Class<E> clazz) throws IOException {
        return readSheet(inputStream, 0, exportType, clazz);
    }

    public static <E> List<E> readSheet(InputStream inputStream, int sheetIndex, ExportType exportType, Class<E> clazz) throws IOException {
        return readSheet(inputStream, sheetIndex, 1, exportType, clazz);
    }

    public static <E> List<E> readSheet(InputStream inputStream, int sheetIndex, int headRowNumber, ExportType exportType, Class<E> clazz) throws IOException {
        return readSheet(inputStream, sheetIndex, null, headRowNumber, exportType, clazz);
    }

    public static <E> List<E> readSheet(InputStream inputStream, String sheetName, int headRowNumber, ExportType exportType, Class<E> clazz) throws IOException {
        return readSheet(inputStream, null, sheetName, headRowNumber, exportType, clazz);
    }

    private static <E> List<E> readSheet(InputStream inputStream, Integer sheetIndex, String sheetName, int headRowNumber, ExportType exportType, Class<E> clazz) throws IOException {
        EasyExcelReadListener<E> readListener = new EasyExcelReadListener<>(clazz, headRowNumber - 1);
        ExcelReaderBuilder excelReaderBuilder =
                EasyExcel.read(inputStream, readListener).excelType(exportType.getExcelTypeEnum()).headRowNumber(headRowNumber);
        excelReaderBuilder.head(clazz);
        excelReaderBuilder.sheet(sheetIndex, sheetName).doRead();
        return readListener.getResult();
    }

    private static void setUtf8BomToStream(OutputStream outputStream) {
        try {
            outputStream.write(0xef);
            outputStream.write(0xbb);
            outputStream.write(0xbf);
            outputStream.flush();
        } catch (IOException e) {
            throw AppException.getAppException(e);
        }
    }

    private static void initWriteBuilder(Class<?> clazz, ExportType exportType, ExcelWriterBuilder builder) {
        Map<Integer, Field> sortedAllFiledMap = new TreeMap<>();
        ClassUtils.declaredFields(clazz, sortedAllFiledMap, false, null);

        builder.head(getHeaders(sortedAllFiledMap));
        List<Constraint> constraintList = getConstraints(sortedAllFiledMap);
        if (exportType != ExportType.CSV) {
            builder.registerWriteHandler(new ExplicitListSheetWriterHandler(constraintList));
        }
        builder.registerWriteHandler(new ExplicitListCellWriterHandler(constraintList));
    }

    private static List<List<String>> getHeaders(Map<Integer, Field> sortedAllFiledMap) {
        List<List<String>> headers = new ArrayList<>();
        for (Map.Entry<Integer, Field> entry : sortedAllFiledMap.entrySet()) {
            Field value = entry.getValue();
            DiyExcelProperty excelProperty = value.getAnnotation(DiyExcelProperty.class);
            String languageKey = excelProperty == null ? null : excelProperty.value();
            if (!StringUtil.isEmpty(languageKey)) {
                String name = ResourceManager.getResource(languageKey);
                if (excelProperty.required()) {
                    name += "(*)";
                }
                headers.add(ArrayUtil.newList(name));
            }
        }
        return headers;
    }

    public static List<Constraint> getConstraints(Map<Integer, Field> sortedAllFiledMap) {
        List<Constraint> constraintList = new ArrayList<>(sortedAllFiledMap.size());
        for (Map.Entry<Integer, Field> entry : sortedAllFiledMap.entrySet()) {
            Field value = entry.getValue();
            DiyExcelProperty excelProperty = value.getAnnotation(DiyExcelProperty.class);
            if (excelProperty == null) {
                constraintList.add(null);
                continue;
            }

            Class<?> type = value.getType();
            Class<?> enumClass = getEnumClass(type, excelProperty);
            String timeFormat = excelProperty.timeFormat();
            if (enumClass != null) {
                constraintList.add(EnumConstraint.createEnumConstraint(enumClass));
            } else if (type == Boolean.class || type == boolean.class) {
                constraintList.add(BooleanConstraint.getInstance());
            } else if (!StringUtil.isEmpty(timeFormat)) {
                constraintList.add(TimeConstraint.createTimeConstraint(timeFormat));
            } else {
                constraintList.add(null);
            }
        }
        return constraintList;
    }

    public static Class<?> getEnumClass(Class<?> type, DiyExcelProperty excelProperty) {
        if (excelProperty != null && excelProperty.enumClass() != null && excelProperty.enumClass() != NoEnum.class) {
            return excelProperty.enumClass();
        } else if (type.isEnum()) {
            return type;
        } else {
            return null;
        }
    }


    private static HorizontalCellStyleStrategy setHeaderStyle(IndexedColors headerColor, Short headerHeight) {
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 头颜色
        headWriteCellStyle.setFillForegroundColor(headerColor.getIndex());
        WriteFont headWriteFont = new WriteFont();

        headWriteFont.setFontHeightInPoints(headerHeight);
        headWriteCellStyle.setWriteFont(headWriteFont);
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
    }
}
