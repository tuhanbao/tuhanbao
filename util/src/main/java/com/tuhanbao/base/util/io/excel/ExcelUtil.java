package com.tuhanbao.base.util.io.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.io.IOUtil;
import com.tuhanbao.base.util.objutil.FileUtil;
import com.tuhanbao.base.util.objutil.StringUtil;

public class ExcelUtil {
    public static final DataFormatter FORMATTER = new DataFormatter();

    /**
     * 创建sheet时使用
     * 
     * 可以给一个excel新创建一个sheet，如果excel文件不存在，会新建
     * 
     * @param url
     * @param index
     * @param name
     * @param result
     * @throws Exception
     */
    public static void createSheet(String url, String name, String[][] result) throws Exception {
        Workbook wb = null;
        FileOutputStream fo = null;
        FileInputStream fi = null;

        try {
            File file = new File(url);
            if (!file.exists()) {
                if (!file.getParentFile().exists()) FileUtil.createDir(file.getParentFile());
                createXLS(url);
            }

            fi = new FileInputStream(url);
            wb = WorkbookFactory.create(fi);
            Sheet sheet = null;
            if (name == null || Constants.EMPTY.equals(name)) {
                sheet = wb.createSheet();
            }
            else {
                int index = wb.getSheetIndex(name);
                if (index == -1) sheet = wb.createSheet(name);
                else {
                    sheet = wb.getSheetAt(index);
                    // 先清除掉所有的数据
                    int rowNum = sheet.getLastRowNum() + 1;
                    for (int i = 0; i < rowNum; i++) {
                        Row row = sheet.getRow(i);
                        if (row != null) {
                            sheet.removeRow(row);
                        }
                    }
                }
            }

            if (result != null) {
                int rowNum = result.length;
                for (int i = 0; i < rowNum; i++) {
                    Row row = sheet.createRow(i);
                    String[] rowValue = result[i];
                    for (int j = 0, colNum = rowValue.length; j < colNum; j++) {
                        Cell cell = row.createCell(j);
                        cell.setCellValue(rowValue[j]);
                    }
                }
            }

            fi.close();
            fo = new FileOutputStream(url);
            wb.write(fo);
        }
        finally {
            IOUtil.close(fo);
            IOUtil.close(fi);
        }
    }

    public static void createXLS(String url) throws Exception {
        Workbook wb = new XSSFWorkbook();
        FileOutputStream fo = null;

        try {
            fo = new FileOutputStream(url);
            wb.write(fo);
        }
        finally {
            IOUtil.close(fo);
        }
    }

    /**
     * 给一个已有的excel的已存在的sheet页内容进行覆盖
     * 
     * @param url
     * @param index
     * @param result
     * @throws Exception
     */
    public static void writeSheet(String url, int index, String[][] result) throws Exception {
        writeSheet(url, index, result, false);
    }

    /**
     * 给一个已有的excel的已存在的sheet页内容进行覆盖
     * 
     * @param url
     * @param index
     *            从0开始
     * @param result
     * @param append
     *            是否对在原有的基础上增写，如果append = false，将清除掉原有的所有数据
     * @throws Exception
     */
    public static void writeSheet(String url, int index, String[][] result, boolean append) throws Exception {
        Workbook wb = null;
        FileOutputStream fo = null;
        FileInputStream fi = null;

        try {
            File file = new File(url);
            if (!file.exists()) {
                if (!file.getParentFile().exists()) FileUtil.createDir(file.getParentFile());
                createXLS(url);
            }

            fi = new FileInputStream(url);
            wb = WorkbookFactory.create(fi);
            Sheet sheet = wb.getSheetAt(index);

            int alreadyRowNum = sheet.getLastRowNum() + 1;
            if (!append) {
                int rowNum = sheet.getLastRowNum() + 1;
                // 先清除掉所有的数据
                for (int i = 0; i < rowNum; i++) {
                    Row row = sheet.getRow(i);
                    if (row != null) {
                        sheet.removeRow(row);
                    }
                }
                alreadyRowNum = 0;
            }

            int rowNum = result.length;
            for (int i = 0; i < rowNum; i++) {
                Row row = sheet.createRow(alreadyRowNum + i);
                String[] rowValue = result[i];
                for (int j = 0, colNum = rowValue.length; j < colNum; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellValue(rowValue[j]);
                }
            }
            fi.close();
            fo = new FileOutputStream(url);
            wb.write(fo);
        }
        finally {
            IOUtil.close(fo);
            IOUtil.close(fi);
        }
    }

    public static String getSheetName(String url, int index) {
        InputStream fi = null;
        try {
            fi = new FileInputStream(url);
            Workbook wb = WorkbookFactory.create(fi);
            return wb.getSheetName(index);
        }
        catch (Exception e) {
            throw MyException.getMyException(e);
        }
        finally {
            IOUtil.close(fi);
        }
    }

    public static int getSheetIndex(String url, String name) {
        InputStream fi = null;
        try {
            fi = new FileInputStream(url);
            Workbook wb = WorkbookFactory.create(fi);
            return wb.getSheetIndex(name);
        }
        catch (Exception e) {
            throw MyException.getMyException(e);
        }
        finally {
            IOUtil.close(fi);
        }
    }

    public static String[][][] read(String url) {
        try {
            return read(new FileInputStream(url), false, true);
        }
        catch (FileNotFoundException e) {
            throw MyException.getMyException(e);
        }
    }

    public static String[][] read(String url, int index) {
        return read(url, index, true);
    }

    public static String[][] read(String url, int index, boolean autoTrim) {
        return read(url, index, false, autoTrim);
    }

    public static String[][] read(InputStream fi, int index) {
        return read(fi, index, true);
    }

    public static String[][] read(InputStream fi, int index, boolean autoTrim) {
        return read(fi, index, false, autoTrim);
    }

    /**
     * @param url
     * @param index
     * @param formulasNotResults
     *            是否自动计算公式
     * @return
     */
    public static String[][] read(String url, int index, boolean formulasNotResults, boolean autoTrim) {
        InputStream fi = null;
        String[][] result = null;
        try {
            fi = new FileInputStream(url);
            result = read(fi, index, formulasNotResults, autoTrim);
        }
        catch (Exception e) {
            throw MyException.getMyException(e);
        }

        return result;
    }

    /**
     * @param url
     * @param index
     * @param formulasNotResults
     *            是否自动计算公式
     * @return
     */
    public static String[][][] read(InputStream fi, boolean formulasNotResults, boolean autoTrim) {
        String[][][] result = null;
        try {
            Workbook wb = WorkbookFactory.create(fi);
            int num = wb.getNumberOfSheets();
            result = new String[num][][];

            for (int i = 0; i < num; i++) {
                Sheet sheet = wb.getSheetAt(i);
                int rowNum = sheet.getLastRowNum() + 1;
                String[][] sheetArray = new String[rowNum][];
                for (int j = 0; j < rowNum; j++) {
                    sheetArray[j] = getRowArray(sheet, j, formulasNotResults, autoTrim);
                }
                result[i] = sheetArray;
            }
        }
        catch (Exception e) {
            throw MyException.getMyException(e);
        }
        finally {
            IOUtil.close(fi);
        }

        return result;
    }

    /**
     * @param url
     * @param index
     * @param formulasNotResults
     *            是否自动计算公式
     * @return
     */
    public static String[][] read(InputStream fi, int index, boolean formulasNotResults, boolean autoTrim) {
        String[][] result = null;
        try {
            Workbook wb = WorkbookFactory.create(fi);

            Sheet sheet = wb.getSheetAt(index);
            int rowNum = sheet.getLastRowNum() + 1;
            result = new String[rowNum][];
            for (int i = 0; i < rowNum; i++) {
                result[i] = getRowArray(sheet, i, formulasNotResults, autoTrim);
            }
        }
        catch (Exception e) {
            throw MyException.getMyException(e);
        }
        finally {
            IOUtil.close(fi);
        }

        return result;
    }

    protected static Row[] getRows(Sheet sheet) {
        Row[] result = null;
        try {
            int rowNum = sheet.getLastRowNum() + 1;
            result = new Row[rowNum];
            for (int i = 0; i < rowNum; i++) {
                result[i] = sheet.getRow(i);
            }
        }
        catch (Exception e) {
            throw MyException.getMyException(e);
        }
        return result;
    }

    private static String[] getRowArray(Sheet sheet, int rowNum, boolean formulasNotResults, boolean autoTrim) {
        if (sheet.getRow(rowNum) == null) {
            return new String[0];
        }
        Row row = sheet.getRow(rowNum);
        short colNum = row.getLastCellNum();
        if (colNum < 0) colNum = 0;
        String[] result = new String[colNum];

        for (int i = 0; i < colNum; i++) {
            Cell cell = row.getCell(i);
            result[i] = getString(cell);
        }

        if (result != null) {
            for (int i = 0, length = result.length; i < length; i++) {
                if (result[i] == null) result[i] = Constants.EMPTY;
                if (autoTrim) result[i] = result[i].trim();
            }
        }

        return result;

    }

    protected static Cell[] getCells(Row row) {
        if (row == null) {
            return new Cell[0];
        }
        short colNum = row.getLastCellNum();
        if (colNum < 0) colNum = 0;
        Cell[] result = new Cell[colNum];

        for (int i = 0; i < colNum; i++) {
            result[i] = row.getCell(i);
        }

        return result;
    }

    public static String getString(Cell cell) {
        return getString(cell, false);
    }

    public static String getString(Cell cell, boolean formulasNotResults) {
        if (cell == null) return Constants.EMPTY;
        if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            if (formulasNotResults) {
                return cell.getCellFormula();
            }
            else {
                if (cell.getCachedFormulaResultType() == Cell.CELL_TYPE_STRING) {
                    return handleStringCell(cell);
                }
                else {
                    return handleNonStringCell(cell, FORMATTER);
                }
            }
        }
        else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            return handleStringCell(cell);
        }
        else {
            return handleNonStringCell(cell, FORMATTER);
        }
    }

    private static String handleStringCell(Cell cell) {
        return cell.getRichStringCellValue().getString();
    }

    private static String handleNonStringCell(Cell cell, DataFormatter formatter) {
        int type = cell.getCellType();
        if (type == Cell.CELL_TYPE_FORMULA) {
            type = cell.getCachedFormulaResultType();
        }

        if (type == Cell.CELL_TYPE_NUMERIC) {
            CellStyle cs = cell.getCellStyle();

            if (cs.getDataFormatString() != null) {
                return formatter.formatRawCellContents(cell.getNumericCellValue(), cs.getDataFormat(), cs.getDataFormatString());
            }
        }

        // No supported styling applies to this cell
        XSSFCell xcell = (XSSFCell)cell;
        return xcell.getRawValue();
    }

    public static boolean isExcel(String url) {
        if (StringUtil.isEmpty(url)) return false;

        for (ExcelType et : ExcelType.values()) {
            if (url.endsWith(et.suffix)) {
                return true;
            }
        }
        return false;
    }
}
