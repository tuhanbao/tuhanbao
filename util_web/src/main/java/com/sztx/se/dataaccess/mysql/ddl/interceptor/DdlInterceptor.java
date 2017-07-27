package com.sztx.se.dataaccess.mysql.ddl.interceptor;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sztx.se.dataaccess.mysql.ddl.TableNameFinder;
import com.sztx.se.dataaccess.mysql.util.DataSourceUtil;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.StringUtil;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.truncate.Truncate;
import net.sf.jsqlparser.statement.update.Update;

/**
 * 
 * @author zhihongp
 * 
 */
public class DdlInterceptor {

	protected CCJSqlParserManager parserManager = new CCJSqlParserManager();

	protected static boolean ddlFlag;

	public static void setDdlFlag(boolean ddlFlag) {
		DdlInterceptor.ddlFlag = ddlFlag;
	}

	/**
	 * 
	 * @param interceptSql
	 * @return
	 */
	protected boolean getDdlFlag(String interceptSql, List<String> tableList) {
		Set<String> tableSet = DataSourceUtil.ddlConfigMap.keySet();
		String patternString = "[\\s\\S]*(" + StringUtil.join(tableSet, "|") + ")[\\s\\S]*";
		Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(interceptSql);

		if (interceptSql != null && !"".equals(interceptSql) && matcher.matches()) {
			if (tableList == null || tableList.isEmpty()) {
				return false;
			}

			Iterator<String> iterator = tableSet.iterator();

			while (iterator.hasNext()) {
				if (tableList.contains(iterator.next())) {
					return true;
				}
			}
		}

		return false;
	}

	protected List<String> getTableList(String interceptSql) {
		String sql = interceptSql.replaceAll("[\\s]+", " ").trim();
		Statement statement = null;
		List<String> tableList = null;

		try {
			statement = parserManager.parse(new StringReader(sql));
		} catch (JSQLParserException e) {
		}

		if (statement != null) {
			if (statement instanceof Select) {
				Select select = (Select) statement;
				TableNameFinder tableNameFinder = new TableNameFinder();
				tableList = new ArrayList<String>();
				tableList.addAll(tableNameFinder.getTableList(select));
			} else if (statement instanceof Insert) {
				Insert insert = (Insert) statement;
				Table table = insert.getTable();
				tableList = new ArrayList<String>();
				tableList.add(table.getName());
			} else if (statement instanceof Update) {
				Update update = (Update) statement;
				Table table = update.getTable();
				tableList = new ArrayList<String>();
				tableList.add(table.getName());
			} else if (statement instanceof Delete) {
				Delete delete = (Delete) statement;
				Table table = delete.getTable();
				tableList = new ArrayList<String>();
				tableList.add(table.getName());
			} else if (statement instanceof CreateTable) {
				CreateTable createTable = (CreateTable) statement;
				Table table = createTable.getTable();
				tableList = new ArrayList<String>();
				tableList.add(table.getName());
			} else if (statement instanceof Drop) {
				Drop drop = (Drop) statement;
				String type = drop.getType();

				if ("TABLE".equals(type)) {
					tableList = new ArrayList<String>();
					tableList.add(drop.getName());
				}
			} else if (statement instanceof Replace) {
				Replace replace = (Replace) statement;
				Table table = replace.getTable();
				tableList = new ArrayList<String>();
				tableList.add(table.getName());
			} else if (statement instanceof Truncate) {
				Truncate truncate = (Truncate) statement;
				Table table = truncate.getTable();
				tableList = new ArrayList<String>();
				tableList.add(table.getName());
			}
		} else if (sql.toUpperCase().indexOf("SHOW CREATE TABLE") != -1) {
			String patternString = "SHOW CREATE TABLE\\s+(\\S+)";
			Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(sql);
			tableList = new ArrayList<String>();

			while (matcher.find()) {
				String table = matcher.group(1).trim();
				tableList.add(StringUtil.trimString(table, "`"));
			}
		} else if (sql.toUpperCase().indexOf("INSERT INTO") != -1) {
			String patternString = "INSERT INTO\\s+(\\S+)\\s+";
			Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(sql);
			tableList = new ArrayList<String>();

			while (matcher.find()) {
				String table = matcher.group(1).trim();
				tableList.add(StringUtil.trimString(table, "`"));
			}
		}

		return tableList;
	}

	/**
	 * 获取某列的值
	 * 
	 * @param sql
	 * @param columnName
	 * @return
	 * @throws JSQLParserException
	 */
	protected Object getColumnValue(String sql, String columnName) {
		Statement statement = null;

		try {
			statement = parserManager.parse(new StringReader(sql));
		} catch (JSQLParserException e) {
			LogManager.error("Parse sql exception, " + sql, e);
		}

		if (statement != null) {
			if (statement instanceof Insert) {
				Insert insert = (Insert) statement;
				List<?> columnList = insert.getColumns();

				for (int i = 0; i < columnList.size(); i++) {
					Column column = (Column) columnList.get(i);

					if (columnName.equals(column.getColumnName())) {
						ItemsList itemsList = insert.getItemsList();

						if (itemsList instanceof ExpressionList) {
							ExpressionList expressionList = (ExpressionList) itemsList;

							if (expressionList.getExpressions().get(i) != null) {
								String columnValue = expressionList.getExpressions().get(i).toString();
								return columnValue.replaceAll("^[\"|']|[\"|']$", "");
							} else {
								return null;
							}
						}
					}
				}
			} else {
				String patternString = columnName + "\\s=\\s(.*?)[\\s|;]";
				Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
				Matcher matcher = pattern.matcher(sql + " ");

				while (matcher.find()) {
					String columnValue = matcher.group(1).trim();
					return columnValue.replaceAll("^[\"|']|[\"|']$", "");
				}
			}
		}

		return null;
	}

}