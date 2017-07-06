package com.tuhanbao.base.util.io.codeGenarator.tableUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.tuhanbao.base.util.io.txt.TxtUtil;
import com.tuhanbao.base.util.objutil.FileUtil;

public class TableManager {
	private static final Map<String, ImportTable> TABLES = new HashMap<String, ImportTable>();
	
	public static void addTable(ImportTable t) {
		if (t == null) return;
		TABLES.put(t.getName(), t);
	}
	
	public static ImportTable getTable(String name) {
		return TABLES.get(name);
	}

	public static void generateTableConstants(String url, String fullPath) throws IOException {
		TxtUtil.write(FileUtil.appendPath(fullPath, "TableConstants.java"), 
				TableUtil.getConstantClassStr(url, TABLES.values()));
	}

    public static Map<String, ImportTable> getAllTables() {
        return TABLES;
    }
}
