package com.tuhanbao.base.util.io.codeGenarator.tableUtil;

/**
 * 字段类型
 * @author tuhanbao
 *
 */
public enum DataType
{
    STRING("String"), TEXT("String"), BLOB("String"),
    BYTE("int", "Integer"), SHORT("int", "Integer"), INT("int", "Integer"),
    LONG("long", "Long"), BYTEARRAY("ByteBuffer"), BOOLEAN("boolean", "Boolean"), 
    DATE("Date", "Date"), FLOAT("float", "Float"), DOUBLE("double", "Double"), BIGDEECIMAL("BigDecimal"),
    OBJECT("Object");
    
    //oracle
//    VARCHAR2("String", "String"), NVARCHAR2("String", "String"), CHAR("String", "String"), 
//    CLOB("String", "String"), BLOB("String", "String"),
//    DATE("Date", "Date"), TIMESTAMP("Date", "Date"), NUMBER("BigDecimal", "BigDecimal"),
//    NCLOB("Object", "Object"),
//	
//	
//	
//	//mysql
//    TINYINT("BigDecimal", "BigDecimal"), BIGDECIMAL("BigDecimal", "BigDecimal"), BIGINT("BigDecimal", "BigDecimal"),
//    INT("BigDecimal", "BigDecimal"), DATETIME("Date", "Date"),SMALLINT("BigDecimal", "BigDecimal"),
//    VARCHAR("String", "String");
//	;
	
    private String name, bigName;

	private DataType(String name, String bigName)
    {
        this.name = name;
        this.bigName = bigName;
    }

	private DataType(String name)
    {
    	this(name, name);
    }
    
    public static DataType getValue(String type) {
    	return null;
    }

    public String getName() {
		return name;
	}

	public String getBigName() {
		return bigName;
	}
	
	/**
	 * 获取框架包的自定义类型
	 * @see com.tuhanbao.base.util.db.table.data
	 * @return
	 */
	public String getDIYValue() {
		if (this == DATE) return "TimeValue";
		else if (this.name.equals(INT.getName())) return "IntValue";
		else return this.bigName + "Value";
	}

	public static DataType getDataType(String str) {
		return valueOf(str.toUpperCase());
	}
}
