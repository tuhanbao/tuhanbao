package com.tuhanbao.autotool.mvc.web;

import java.util.List;

import com.tuhanbao.Constants;
import com.tuhanbao.autotool.mvc.J2EETable;
import com.tuhanbao.base.util.clazz.ClazzUtil;
public class JspCreator{

	private static final String gap = "    ";
	private static final String gap2 = gap + gap;
	private static final String gap3 = gap2 + gap;
	private static final String gap4 = gap3 + gap;
	private static final String gap5 = gap4 + gap;
	private static final String gap6 = gap5 + gap;
	private static final String gap7 = gap6 + gap;
	private static final String gap8 = gap7 + gap;
	
	public String getJspFile(J2EETable table){
		StringBuilder sb = new StringBuilder();
		getHeadModel(sb, table);
		getTableModel(sb, table);
		getOperateModel(sb, table ,"添加", "addDictModal", "addDictForm" ,"add");
		getOperateModel(sb, table ,"编辑", "editDictModal", "editDictForm", "edit");
		getFooterModel(sb, ClazzUtil.firstCharLowerCase(table.getModelName()));
		return sb.toString();
	}

	public String getSelectJspFile(J2EETable table){
		StringBuilder sb = new StringBuilder();
		getSelectModel(sb, table);
		return sb.toString();
	}
	
	private void getHeadModel(StringBuilder sb, J2EETable table){
		sb.append("<%@ page contentType=\"text/html;charset=UTF-8\" language=\"java\" %>").append(Constants.ENTER);
		sb.append("<%   String path = request.getContextPath();").append(Constants.ENTER);
		sb.append(gap).append("String basePath = request.getScheme() + \"://\" + request.getServerName() + \":\" + request.getServerPort()+ path + \"/\";%>").append(Constants.ENTER);
		sb.append("<!DOCTYPE html>").append(Constants.ENTER).append("<html>").append(Constants.ENTER).append("<head>").append(Constants.ENTER);
		sb.append(gap).append("<base href=\"<%=basePath%>\">").append(Constants.ENTER);
		sb.append(gap).append("<title>").append(table.getComment()).append("</title>").append(Constants.ENTER);
		sb.append(gap).append("<jsp:include page=\"/common/head.jsp\"></jsp:include>").append(Constants.ENTER);
		sb.append(gap).append("<link href=\"static/table/bootstrap-table.min.css\" rel=\"stylesheet\">").append(Constants.ENTER);
		sb.append("</head>").append(Constants.ENTER);
		sb.append("<body class=\"container-fluid content\">").append(Constants.ENTER);
		sb.append("<section class=\"content-header\">").append(Constants.ENTER);
		sb.append(gap).append("<h1>").append(table.getComment()).append("<small>").append(table.getComment()).append("</small></h1>").append(Constants.ENTER);
		sb.append(gap).append("<ol class=\"breadcrumb\">").append(Constants.ENTER);
		sb.append(gap2).append("<li><a href=\"#\"><i class=\"icon icon-home\"></i> 首页 </a></li>").append(Constants.ENTER);
		sb.append(gap2).append("<li class=\"active\">TITLE1</li>").append(Constants.ENTER);
		sb.append(gap).append("</ol>").append(Constants.ENTER).append("</section>").append(Constants.ENTER);
		sb.append(Constants.ENTER);
	}
	
	private void getTableModel(StringBuilder sb, J2EETable table){
		
		sb.append("<div class=\"col-md-12\" id=\"dictTool\" style=\"padding:0;\">").append(Constants.ENTER);
		sb.append(gap).append("<div class=\"btn-group btn-group-sm\" role=\"group\" aria-label=\"...\">").append(Constants.ENTER);
		sb.append(gap2).append("<button type=\"button\" class=\"btn btn-primary\" data-toggle=\"modal\" data-target=\"#addDictModal\"><i class='icon icon-plus'></i> 添加 </button>").append(Constants.ENTER);
		sb.append(gap2).append("<button type=\"button\" id=\"btn-reInit\" class=\"btn btn-info\"><i class='icon icon-print'></i> 加载</button>").append(Constants.ENTER);
		sb.append(gap2).append("<button type=\"button\" class=\"btn btn-edit btn-warning\"><i class='icon icon-edit'></i> 修改</button>").append(Constants.ENTER);
		sb.append(gap2).append("<button type=\"button\" class=\"btn btn-del btn-danger\"><i class='icon icon-remove'></i> 删除</button>").append(Constants.ENTER);
		sb.append(gap).append("</div>").append(Constants.ENTER).append("</div>").append(Constants.ENTER);
		
		sb.append("<div class=\"col-md-12\">").append(Constants.ENTER);
		sb.append(gap).append("<table id=\"dictTable\"></table>").append(Constants.ENTER);
		sb.append("</div>").append(Constants.ENTER);
		sb.append(Constants.ENTER);
	}
	
	private void getOperateModel(StringBuilder sb, J2EETable table, String fieldName, String  operateId, String formId, String operate){
		
		sb.append("<div class=\"modal fade\" id= \"").append(operateId).append("\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"dictLabel\" aria-hidden=\"true\">").append(Constants.ENTER);
		sb.append(gap).append("<div class=\"modal-dialog\">").append(Constants.ENTER);
		sb.append(gap2).append("<div class=\"modal-content\">").append(Constants.ENTER);
		sb.append(gap3).append("<div class=\"modal-header\">").append(Constants.ENTER);
		sb.append(gap4).append("<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\">").append(Constants.ENTER);
		sb.append(gap5).append("<span aria-hidden=\"true\">&times;</span>").append(Constants.ENTER);
		sb.append(gap4).append("</button>").append(Constants.ENTER);
		sb.append(gap4).append("<h4 class=\"modal-title\" id=\"dictLabel\">").append(fieldName).append("</h4>").append(Constants.ENTER);
		sb.append(gap3).append("</div>").append(Constants.ENTER);
		sb.append(gap3).append("<div class=\"modal-body\">").append(Constants.ENTER);
		String url =  ClazzUtil.firstCharLowerCase(table.getModelName()) + Constants.FILE_SEP + operate;
		sb.append(gap4).append("<form class=\"form-horizontal\" method=\"post\" action=\""+ url +"\" id=\""+ formId +"\">").append(Constants.ENTER);
		if(operate.equals("edit")){
			sb.append(gap5).append("<input type=\"hidden\" id=\"editid\" name=\"id\">").append(Constants.ENTER);
		}
		List<com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportColumn> list  = table.getColumns();
		boolean edit = false;
		if(operate.equals("edit")){
			edit = true;
		}
		for(com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportColumn column : list){
			if(!column.isPK()){
				if(column.getDataType().equals(com.tuhanbao.base.util.io.codeGenarator.tableUtil.DataType.BOOLEAN)){
					getFieldBoolModel(sb, column.getComment(), ClazzUtil.getVarName(column.getName()), edit);
				}else{
					getFieldCommonModel(sb, column.getComment(), ClazzUtil.getVarName(column.getName()), edit);
				}
			}
		}
		
		sb.append(gap4).append("</form>").append(Constants.ENTER);
		sb.append(gap3).append("</div>").append(Constants.ENTER);
		
		sb.append(gap3).append("<div class=\"modal-footer\">").append(Constants.ENTER);
		sb.append(gap4).append("<button type=\"reset\" class=\"btn btn-default\" data-dismiss=\"modal\">关闭</button>").append(Constants.ENTER);
		sb.append(gap4).append("<button type=\"submit\" id = \"btn-"+ operate +"\" class=\"btn btn-primary btn-save\" form=\"" + formId + "\">保存</button>").append(Constants.ENTER);
		sb.append(gap3).append("</div>").append(Constants.ENTER);
		
		sb.append(gap2).append("</div>").append(Constants.ENTER);
		sb.append(gap).append("</div>").append(Constants.ENTER);
		sb.append("</div>").append(Constants.ENTER);
		sb.append(Constants.ENTER);
	}
	
	private void getFieldCommonModel(StringBuilder sb, String fieldName, String colName, boolean isEdit){

		sb.append(gap5).append("<div class=\"form-group\">").append(Constants.ENTER);
		sb.append(gap6).append("<label for=\""+ colName +"\" class=\"col-sm-2 control-label\">").append(fieldName).append("</label>").append(Constants.ENTER);
		sb.append(gap6).append("<div class=\"col-sm-6\">").append(Constants.ENTER);
		if(isEdit){
			sb.append(gap7).append("<input type=\"text\" class=\"form-control\" id=\"edit"+ colName +"\" name=\""+ colName +"\">").append(Constants.ENTER);
		}else{
			sb.append(gap7).append("<input type=\"text\" class=\"form-control\" id=\""+ colName +"\" name=\""+ colName +"\">").append(Constants.ENTER);
		}
		sb.append(gap6).append("</div>").append(Constants.ENTER);
		sb.append(gap6).append("<small class=\"help-block col-sm-4\"></small>").append(Constants.ENTER);
		sb.append(gap5).append("</div>").append(Constants.ENTER);
	}
	
	private void getFieldBoolModel(StringBuilder sb, String fieldName, String colName, boolean isEdit){
		
		sb.append(gap5).append("<div class=\"form-group\">").append(Constants.ENTER);
		sb.append(gap6).append("<label for=\""+ colName +"\" class=\"col-sm-2 control-label\">").append(fieldName).append("</label>").append(Constants.ENTER);
		sb.append(gap6).append("<div class=\"col-sm-6\">").append(Constants.ENTER);
		sb.append(gap7).append("<label class=\"radio-inline\">").append(Constants.ENTER);
		if(isEdit){
			colName = "edit"+ colName;
		}
		sb.append(gap8).append("<input type=\"radio\" name=\""+ colName +"\" value=\"1\" checked=\"checked\">是");
		sb.append(gap7).append("</label>").append(Constants.ENTER);
		
		sb.append(gap7).append("<label class=\"radio-inline\">").append(Constants.ENTER);
		sb.append(gap8).append("<input type=\"radio\" name=\"\" value=\"0\">否");
		sb.append(gap7).append("</label>").append(Constants.ENTER);
		sb.append(gap6).append("</div>").append(Constants.ENTER);
		sb.append(gap5).append("</div>").append(Constants.ENTER);
	}
	
	private void getFooterModel(StringBuilder sb, String modelName){
		
		sb.append("<jsp:include page=\"/common/footjs.jsp\"></jsp:include>").append(Constants.ENTER);
		sb.append("<jsp:include page=\"/common/footjs-table.jsp\"></jsp:include>").append(Constants.ENTER);
		sb.append("<script type=\"text/javascript\" src=\"static/ztree/js/jquery.ztree.all-3.5.min.js\"></script>").append(Constants.ENTER).append("</html>");
		sb.append("<script type=\"text/javascript\" src=\"static/tokenfield/bootstrap-tokenfield.min.js\"></script>").append(Constants.ENTER).append("</html>");
		sb.append("<script type=\"text/javascript\" src=\"static/bootstrap/js/bootstrap-datetimepicker.min.js\" charset=\"UTF-8\"></script>").append(Constants.ENTER).append("</html>");
		sb.append("<script type=\"text/javascript\" src=\"static/bootstrap/js/bootstrap-datetimepicker.zh-CN.js\" charset=\"UTF-8\"></script>").append(Constants.ENTER).append("</html>");
		sb.append("<script type=\"text/javascript\" src=\"static/myjs/"+ modelName +".js\"></script>").append(Constants.ENTER);
		sb.append("</body>").append(Constants.ENTER).append("</html>");
	}
	
	private void getSelectModel(StringBuilder sb , J2EETable table){
		sb.append("<%@ page contentType=\"text/html;charset=UTF-8\" language=\"java\" %>").append(Constants.ENTER);
		sb.append("<%   String path = request.getContextPath();").append(Constants.ENTER);
		sb.append(gap).append("String basePath = request.getScheme() + \"://\" + request.getServerName() + \":\" + request.getServerPort()+ path + \"/\";%>").append(Constants.ENTER);
		sb.append("<!DOCTYPE html>").append(Constants.ENTER).append("<html>").append(Constants.ENTER).append("<head>").append(Constants.ENTER);
		sb.append(gap).append("<base href=\"<%=basePath%>\">").append(Constants.ENTER);
		sb.append(gap).append("<jsp:include page=\"/common/head.jsp\"></jsp:include>").append(Constants.ENTER);
		sb.append(gap).append("<link href=\"static/table/bootstrap-table.min.css\" rel=\"stylesheet\">").append(Constants.ENTER);
		sb.append(gap).append(" <link rel=\"stylesheet\" href=\"static/css/area.css\">").append(Constants.ENTER);
		sb.append("</head>").append(Constants.ENTER);
		sb.append("<body class=\"container-fluid content\">").append(Constants.ENTER);
		sb.append(gap).append("<div class=\"form-group col-md-4 input-box-list\">").append(Constants.ENTER);
		sb.append(gap2).append("<label for=\"\" class=\" font12 input-box-list-title\">Name：</label>").append(Constants.ENTER);
		sb.append(gap2).append("<div class=\"input-box-list-value\">").append(Constants.ENTER);
		sb.append(gap3).append("<select class=\"form-control no-appearance\" id=\"selectorg\">").append(Constants.ENTER);
		sb.append(gap4).append("<option value=\"\">全部</option>").append(Constants.ENTER);
		sb.append(gap3).append("</select>").append(Constants.ENTER);
		sb.append(gap2).append("</div>").append(Constants.ENTER).append(gap).append("</div>").append(Constants.ENTER);
		
		sb.append(gap).append("<div class=\"form-horizontal row\" style=\"margin: 0 0 20px 0;\">").append(Constants.ENTER);
		sb.append(gap2).append("<div class=\"form-group col-md-4 input-box-list\">").append(Constants.ENTER);
		sb.append(gap3).append("<label for=\"input1\" class=\" font12 input-box-list-title label-base\">所属地区：</label>").append(Constants.ENTER);
		sb.append(gap3).append("<div class=\"input-box-list-value\">").append(Constants.ENTER);
		sb.append(gap4).append("<select name=\"provId\" class=\"form-control no-appearance area-select\" id=\"provIdSelect\">").append(Constants.ENTER);
		sb.append(gap5).append("<option selected value=\"\">-省份-</option>").append(Constants.ENTER);
		sb.append(gap5).append("<option value=\"\">-加载中...-</option>").append(Constants.ENTER);
		sb.append(gap4).append("</select>").append(Constants.ENTER);
		sb.append(gap4).append("<select name=\"cityId\" class=\"form-control no-appearance area-select\" id=\"cityIdSelect\">").append(Constants.ENTER);
		sb.append(gap5).append("<option value=\"\">-地市-</option>").append(Constants.ENTER);
		sb.append(gap5).append("<option value=\"\">-请选择地市-</option>").append(Constants.ENTER);
		sb.append(gap4).append("</select>").append(Constants.ENTER);
		sb.append(gap4).append("<select name=\"countyId\" class=\"form-control no-appearance area-select\" id=\"countyIdSelect\">").append(Constants.ENTER);
		sb.append(gap5).append(" <option value=\"\">-区县-</option>").append(Constants.ENTER);
		sb.append(gap5).append("<option value=\"\">-请选择地市-</option>").append(Constants.ENTER);
		sb.append(gap4).append(" </select>").append(Constants.ENTER);
		sb.append(gap3).append("</div>").append(Constants.ENTER);
		sb.append(gap2).append("</div>").append(Constants.ENTER);
		sb.append("<jsp:include page=\"/common/footjs.jsp\"></jsp:include>").append(Constants.ENTER);
		sb.append("<script type=\"text/javascript\" src=\"static/myjs/"+ ClazzUtil.firstCharLowerCase(table.getModelName()) +"Select.js\"></script>").append(Constants.ENTER);
		sb.append("</body>").append(Constants.ENTER).append("</html>").append(Constants.ENTER);
	}
	
}
