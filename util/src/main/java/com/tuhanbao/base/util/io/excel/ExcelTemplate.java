package com.tuhanbao.base.util.io.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.io.IOUtil;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.FileUtil;

public class ExcelTemplate {
	
	private Sheet srcSheet;
	
	private Workbook srcWb;
	
	public ExcelTemplate(String url, int index) {
		InputStream fi = null;
        try
        {
            fi = new FileInputStream(url);
            srcWb = WorkbookFactory.create(fi);

            srcSheet = srcWb.getSheetAt(index);
            
            CellStyle columnStyle = srcSheet.getColumnStyle(0);
            CellStyle cellStyle = srcSheet.getRow(0).getCell(0).getCellStyle();
			System.out.println(columnStyle.getBorderTop());
            System.out.println(columnStyle.getTopBorderColor());
            System.out.println(cellStyle.getBorderTop());
            System.out.println(cellStyle.getTopBorderColor());
//            System.out.println(cellStyle.get());
            System.out.println(cellStyle.getBottomBorderColor());
        } catch (Exception e) {
        	LogManager.error(e);
		}
        finally
        {
            IOUtil.close(fi);
        }
	}
	
	public void createSheet(String url, String name) throws Exception {
		Workbook wb = null;
        FileOutputStream fo = null;
        FileInputStream fi = null;
        try
        {
            File file = new File(url);
			if (!file.exists())
            {
            	if (!file.getParentFile().exists()) FileUtil.createDir(file.getParentFile());
                Excel2007Util.createXLS(url);
            }
            
			fi = new FileInputStream(url);
            wb = WorkbookFactory.create(fi);
            
    		Sheet cloneSheet = null;
    		if (name == null || Constants.EMPTY.equals(name)) {
    			cloneSheet = wb.createSheet();
    		} else {
    			int index = wb.getSheetIndex(name);
    			if (index == -1)
    				cloneSheet = wb.createSheet(name);
    			else {
    				cloneSheet = wb.getSheetAt(index);
    				// 先清除掉所有的数据
    				int rowNum = cloneSheet.getLastRowNum() + 1;
    				for (int i = 0; i < rowNum; i++) {
    					Row row = cloneSheet.getRow(i);
    					if (row != null) {
    						cloneSheet.removeRow(row);
    					}
    				}
    			}
    		}

    		cloneSheet.setDefaultColumnWidth(srcSheet.getDefaultColumnWidth());
    		cloneSheet.setDefaultRowHeight(srcSheet.getDefaultRowHeight());
//    		int rowSize = srcSheet.getTopRow();
    		// for (int i = 0; i < rowSize; i++) {
    		// cloneSheet.set(i, srcSheet.isColumnHidden(i));
    		// cloneSheet.setColumnWidth(i, srcSheet.getColumnWidth(i));
    		// }
    		Row[] rows = Excel2007Util.getRows(srcSheet);
    		if (rows != null && rows.length > 0) {
    			//拷贝column的默认格式
    			Cell[] cells = Excel2007Util.getCells(rows[0]);
    			for (int i = 0; i < cells.length; i++) {
    				cloneSheet.setColumnHidden(i, srcSheet.isColumnHidden(i));
    				cloneSheet.setColumnWidth(i, srcSheet.getColumnWidth(i));
    				CellStyle cs = srcSheet.getColumnStyle(i);
    				cloneSheet.setDefaultColumnStyle(i, cs);
    	//			System.out.println(cs.getBorderBottom() + "  " + cs.getBottomBorderColor());
    	//			cs.setBorderBottom((short) 1);
    	//			cs.setBottomBorderColor((short) 50);
    	//			System.out.println(cs.getBorderBottom() + "  " + cs.getBottomBorderColor());
    			}
    			
    			int rowSize = rows.length;
    			for (int i = 0; i < rowSize; i++) {
    				Row row = rows[i];
    				if (row == null) continue;
    				Cell[] cols = Excel2007Util.getCells(row);;
    				int colSize = cols.length;
    				Row cloneRow = cloneSheet.createRow(i);
    				cloneRow.setHeight(row.getHeight());
    				cloneRow.setRowStyle(row.getRowStyle());
    				for (int j = 0; j < colSize; j++) {
    					Cell cell = cols[j];
    					if (cell == null) continue;
    					Cell cloneCell = cloneRow.createCell(j);
    					cloneCell.setCellComment(cell.getCellComment());
    					CellStyle cloneCs = wb.createCellStyle();
    					CellStyle cs = cell.getCellStyle();
    					cloneCs.setAlignment(cs.getAlignment());
    					cloneCs.setBorderBottom(cs.getBorderBottom());
    					cloneCs.setBottomBorderColor(cs.getBottomBorderColor());
    					cloneCs.setBorderLeft(cs.getBorderLeft());
    					cloneCs.setLeftBorderColor(cs.getLeftBorderColor());
    					cloneCs.setBorderRight(cs.getBorderRight());
    					cloneCs.setRightBorderColor(cs.getRightBorderColor());
    					cloneCs.setBorderTop(cs.getBorderTop());
    					cloneCs.setTopBorderColor(cs.getTopBorderColor());
    					cloneCs.setFont(srcWb.getFontAt(cs.getFontIndex()));
    					
//    					cloneCs.set
//						cloneCell.setCellStyle(cloneCs);
    					cloneCell.setCellValue(cell.getStringCellValue());
    				}
    			}
    		}
            
            fi.close();
            fo = new FileOutputStream(url);
            wb.write(fo);
        }
        finally
        {
            IOUtil.close(fo);
            IOUtil.close(fi);
        }
	}
	
	/**
	 * poi有更完善的clone方法，但是没法使用，
	 * 本clone方法只拷贝了常用的属性拷贝，仅包括一下属性
	 * 
	 * 1.sheet 格式
	 * 
	 * @param srcSheet
	 * @param clonedSheet
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 */
//	private static Sheet cloneSheet(Sheet srcSheet, String url, String name) throws InvalidFormatException, IOException {
//		FileInputStream fi = new FileInputStream(url);
//
//		return cloneSheet;
//	}
}
