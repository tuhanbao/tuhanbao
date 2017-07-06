package com.tuhanbao.base.util.io.codeGenarator.tableUtil;

import java.util.ArrayList;
import java.util.List;

import com.tuhanbao.base.Constants;
import com.tuhanbao.base.util.objutil.StringUtil;

public class XlsTable
{
    private String name;
    private String tableName;
    private String cacheType;
    private List<XlsColumn> list = new ArrayList<XlsColumn>();
    
    private String idIndex;
    //是否生成mo的class文件
    private boolean isCreateMO = true;
    
    public XlsTable(String name, String[] array)
    {
        this.tableName = name;
        this.name = tableName.substring(0, 1).toUpperCase() + tableName.substring(1);
        this.cacheType = array[1].trim();
        if (array.length >= 3)
            this.idIndex = array[2].trim();
    }

    public void addColumn(XlsColumn col)
    {
        list.add(col);
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.GAP1 + "public static final class " + name).append(Constants.ENTER);
        sb.append(Constants.GAP1 + "{").append(Constants.ENTER);
        sb.append(Constants.GAP2 + "public static final Table TABLE = new Table(\"").append(tableName.toUpperCase()).append("\", ")
                .append("CacheType.").append(getCacheTypeStr()).append(");").append(Constants.ENTER);
        sb.append(Constants.ENTER);
        for (XlsColumn c : list)
        {
            sb.append(c.toString()).append(Constants.ENTER);
        }
        
        sb.append(Constants.GAP1 + "}").append(Constants.ENTER);
        
        return sb.toString();
    }

    private String getCacheTypeStr()
    {
        if ("NOT".equals(cacheType))
        {
            return "NOT_CACHE";
        }
        else if ("ALL".equals(cacheType))
        {
            return "CACHE_ALL";
        }
        else
        {
            return "AUTO";
        }
    }

    public static class XlsColumn implements IColumn
    {
        private String name;
        private String type;
        private long length;
        private String colName;
        private String keyInfo;
        private String defaultValue;

        public XlsColumn(String[] array)
        {
            this.colName = array[0];
            this.name = array[0].toUpperCase();
            this.type = array[1];
            if (array.length >= 3 && !StringUtil.isEmpty(array[2]))
            	this.length = Long.valueOf(array[2]);
            if (array.length >= 4)
            	this.keyInfo = array[3];
            if (array.length >= 5)
                this.defaultValue = array[4];
        }
        
        @Override
        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            sb.append(Constants.GAP2).append("public static final Column ").append(getName()).append(" = ColumnFactory.createColumn(TABLE, \"").append(colName.toUpperCase())
                    .append("\", DataType.").append(getDataType());
            if (isPK()) sb.append(", true");
            sb.append(");");
            return sb.toString();
        }
        
        public boolean isPK()
        {
            if (keyInfo != null)
            {
                String[] keys = keyInfo.split(Constants.COMMA);
                for (String key : keys)
                {
                    if ("PK".equals(key.toUpperCase()))
                    {
                        return true;
                    }
                }
            }
            
            return false;
        }
        
        public String[] getFK()
        {
            if (keyInfo != null)
            {
                String[] keys = keyInfo.split(Constants.COMMA);
                for (String key : keys)
                {
                    String fk[] = key.split(Constants.COLON);
                    if (fk[0].trim().toUpperCase().startsWith("FK"))
                    {
                        fk[0] = fk[0].trim().substring(3, fk[0].trim().length() - 1);
                        return fk;
                    }
                }
            }
            return null;
        }
        
        public String getName()
        {
            return name;
        }
        
        public DataType getDataType()
        {
            return DataType.valueOf(this.type.toUpperCase());
        }

        public String getType()
        {
            return type;
        }

        public void setType(String type)
        {
            this.type = type;
        }
        
        public String getDefaultValue()
        {
            return defaultValue;
        }

        public void setDefaultValue(String defaultValue)
        {
            this.defaultValue = defaultValue;
        }

        public long getLength()
        {
            return length;
        }

        public void setLength(long length)
        {
            this.length = length;
        }

        public String getColName()
        {
            return colName;
        }

        public void setColName(String colName)
        {
            this.colName = colName;
        }

        public String getKeyInfo()
        {
            return keyInfo;
        }

        public void setKeyInfo(String keyInfo)
        {
            this.keyInfo = keyInfo;
        }

        public void setName(String name)
        {
            this.name = name;
        }

		@Override
		public String getComment() {
			return Constants.EMPTY;
		}
    }
    

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public List<XlsColumn> getList()
    {
        return list;
    }

    public void setList(List<XlsColumn> list)
    {
        this.list = list;
    }

    public void setCacheType(String cacheType)
    {
        this.cacheType = cacheType;
    }
    
    public String getCacheType()
    {
        return cacheType;
    }

    public String getIdIndex()
    {
        return idIndex;
    }

    public void setIdIndex(String idIndex)
    {
        this.idIndex = idIndex;
    }

    public void setCreateMO(boolean value)
    {
        this.isCreateMO  = value;
    }
    
    public boolean isCreateMO()
    {
        return this.isCreateMO;
    }
}

