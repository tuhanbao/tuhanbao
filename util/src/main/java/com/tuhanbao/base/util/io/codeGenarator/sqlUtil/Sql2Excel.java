package com.tuhanbao.base.util.io.codeGenarator.sqlUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.tuhanbao.base.util.io.codeGenarator.tableUtil.DBType;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.DataType;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportColumn;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportTable;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.src.TableSrcUtilFactory;
import com.tuhanbao.base.util.io.excel.ExcelUtil;
import com.tuhanbao.base.util.io.txt.TxtUtil;

/**
 * 用于将一个数据库表导成sql文件后，解析此表，变成固定格式的excel表格
 * 
 * @author tuhanbao
 *
 */
public class Sql2Excel
{
    public static void handler(String sqlUrl, String excelUrl, String sheetName, DBType dbType) throws Exception
    {
        String s = TxtUtil.read(sqlUrl);
        StringTokenizer st = new StringTokenizer(s, "\n\r");
        
        List<ImportTable> list = new ArrayList<ImportTable>();
        ImportTable table = null;
        while(st.hasMoreElements())
        {
            String tem = st.nextToken();
            if (tem.startsWith("CREATE TABLE"))
            {
                table = new ImportTable(tem);
                list.add(table);
            }
            else if (tem.startsWith("  `"))
            {
                table.addColumn(getColumn(table, tem, dbType));
            }
        }
        
        ExcelUtil.createSheet(excelUrl, sheetName, getArray(list));
        
    }
    
    public static ImportColumn getColumn(ImportTable table, String s, DBType dbType)
    {
        int index = s.indexOf("`");
        int index2 = s.indexOf("`", index + 1);
        
        String name = s.substring(index + 1, index2);
        int length = 0;
        String type = null;
        
        //找到下一个空格，或,
        index = index2 + 1;
        index2 = s.indexOf(" ", index + 1);
        if (index2 == -1)
        {
            index2 = s.indexOf(",", index + 1);
        }
        
        String typeLength = s.substring(index, index2);
        
        int l = typeLength.indexOf("(");
        if (l != -1)
        {
            int r = typeLength.indexOf(")", l);
            length = Integer.valueOf(typeLength.substring(l + 1, r));
            type = typeLength.substring(0, l);
        }
        else
        {
            length = 0;
            type = typeLength;
        }
        
        DataType dataType = DataType.valueOf(type);
		return new ImportColumn(table, name, dataType, TableSrcUtilFactory.getDBDataType(dataType, dbType), length);
    }

    private static String[][] getArray(List<ImportTable> list)
    {
        String[][] result = new String[0][];
        for (ImportTable table : list)
        {
            result = append(result, table.toArray());
            result = append(result, new String[1][0]);
        }
        return result;
    }
    
    private static String[][] append(String[][] array1, String[][] array2)
    {
        String[][] array = new String[array1.length + array2.length][];
        System.arraycopy(array1, 0, array, 0, array1.length);
        System.arraycopy(array2, 0, array, array1.length, array2.length);
        return array;
    }

}
