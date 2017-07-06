package com.tuhanbao.autotool.mvc.web;

import java.util.List;

import com.tuhanbao.Constants;
import com.tuhanbao.autotool.mvc.J2EETable;
import com.tuhanbao.base.util.clazz.ClazzUtil;
import com.tuhanbao.base.util.io.codeGenarator.tableUtil.DataType;
public class JsCreator {

	private static final String gap = "    ";
	private static final String gap2 = gap + gap;
	private static final String gap3 = gap2 + gap;
	private static final String gap4 = gap3 + gap;
	
	public String getJsFile(J2EETable table){
		StringBuilder sb = new StringBuilder();
		getBasicTable(sb, table);
		getInitValidate(sb, table);
		getDoEdit(sb, table);
		getDoShow(sb, table);
		getDoDel(sb, table);
		getDoReInit(sb, table);
		getParsedate(sb);
		getDatePicker(sb,table);
		return sb.toString();
	}
	
	public String getSelectJsFile(){
		StringBuilder sb = new StringBuilder();
		getInit(sb);
		return sb.toString();
	}
	
	private void getBasicTable(StringBuilder sb , J2EETable table){
		
		sb.append("var table=null;").append(Constants.ENTER);
		sb.append("$(function(){").append(Constants.ENTER);
		sb.append(gap).append("table = $('#dictTable').bootstrapTable({").append(Constants.ENTER);
		sb.append(gap2).append("method: 'get',").append(Constants.ENTER);
		sb.append(gap2).append("classes:\"table table-hover table-condensed\",").append(Constants.ENTER);
		String url = ClazzUtil.firstCharLowerCase(table.getModelName()) + Constants.FILE_SEP + "list";
		sb.append(gap2).append("url: '"+ url +"',").append(Constants.ENTER);
		sb.append(gap2).append("cache: false,").append(Constants.ENTER);
		sb.append(gap2).append("toolbar:\"#dictTool\",").append(Constants.ENTER);
		sb.append(gap2).append("striped: true,").append(Constants.ENTER);
		sb.append(gap2).append("pagination: true,").append(Constants.ENTER);
		sb.append(gap2).append("searchOnEnterKey:true,").append(Constants.ENTER);
		sb.append(gap2).append("sidePagination:\"server\",").append(Constants.ENTER);
		sb.append(gap2).append("idField:\"id\",").append(Constants.ENTER);
		
		sb.append(gap2).append("smartDisplay:true,").append(Constants.ENTER);
		sb.append(gap2).append("sortName:\"\",").append(Constants.ENTER);
		
		sb.append(gap2).append("smartDisplay:true,").append(Constants.ENTER);
		sb.append(gap2).append("pageSize: 10,").append(Constants.ENTER);
		sb.append(gap2).append("pageList:[\"10\",\"20\",\"50\",\"100\"],").append(Constants.ENTER);
		
		sb.append(gap2).append("search: true,").append(Constants.ENTER);
		sb.append(gap2).append("showColumns: true,").append(Constants.ENTER);
		sb.append(gap2).append("showRefresh: true,").append(Constants.ENTER);
		sb.append(gap2).append("clickToSelect: true,").append(Constants.ENTER);
		sb.append(gap2).append("singleSelect:false,").append(Constants.ENTER);
		sb.append(gap2).append("columns: [").append(Constants.ENTER);

		List<com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportColumn> list  = table.getColumns();
		for(com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportColumn column : list){
			if(column.isPK()){
				sb.append(gap2).append("{ field: 'ck', title: '编号', radio:true },").append(Constants.ENTER);
			}else if(!column.isPK() && column.getDataType().equals(com.tuhanbao.base.util.io.codeGenarator.tableUtil.DataType.BOOLEAN)){
				sb.append(gap2).append("{ field: '"+ ClazzUtil.getVarName(column.getName()) +"', title: '" + column.getComment() + "', align: 'left',  formatter:function(value,index,row){ return genBoolean(value)}").append(Constants.ENTER);
			}else if(!column.isPK() && column.getDataType().equals(com.tuhanbao.base.util.io.codeGenarator.tableUtil.DataType.DATE)){
				sb.append(gap2).append("{ field: '"+ ClazzUtil.getVarName(column.getName()) +"', title: '" + column.getComment() + "', align: 'left', formatter:parsedate},").append(Constants.ENTER);
			}else{
				sb.append(gap2).append("{ field: '"+ ClazzUtil.getVarName(column.getName()) +"', title: '" + column.getComment() + "', align: 'left' },").append(Constants.ENTER);
			}
		}
		
		
		sb.append(gap2).append("]").append(Constants.ENTER).append(gap).append(" });").append(Constants.ENTER);
		
		sb.append(gap).append("var addValidator = initValidate(\"#addDictForm\");").append(Constants.ENTER);
		sb.append(gap).append("var editValidator = initValidate(\"#editDictForm\");").append(Constants.ENTER);
		sb.append(gap).append("$(\"#editDictForm\").ajaxForm({").append(Constants.ENTER);
		
		sb.append(gap2).append("target:'#btn-edit', ").append(Constants.ENTER);
		sb.append(gap2).append("clearForm:true,").append(Constants.ENTER);
		sb.append(gap2).append("dataType:'json',").append(Constants.ENTER);
		sb.append(gap2).append("beforeSubmit:  function(formData, jqForm, options){").append(Constants.ENTER);
		sb.append(gap3).append("return editValidator.valid();").append(Constants.ENTER);
		sb.append(gap2).append("},").append(Constants.ENTER);
		
		sb.append(gap2).append("success:function(responseText, statusText, xhr, $form){").append(Constants.ENTER);
		sb.append(gap3).append("if(responseText.errorCode != 0){").append(Constants.ENTER);
		sb.append(gap4).append("$.messager.alert(\"fail_message\",responseText.errorMessage);").append(Constants.ENTER);
		sb.append(gap3).append(" }else{").append(Constants.ENTER);
		sb.append(gap4).append("$(\"#addDictModal\").modal(\"hide\");").append(Constants.ENTER);
		sb.append(gap4).append("table.bootstrapTable(\"refresh\");").append(Constants.ENTER);
		sb.append(gap3).append("}").append(Constants.ENTER).append(gap2).append("}").append(Constants.ENTER).append(gap).append("});").append(Constants.ENTER);
		
		sb.append(gap).append("$(\"#addDictForm\").ajaxForm({").append(Constants.ENTER);
		sb.append(gap2).append("target:'#btn-add',").append(Constants.ENTER);
		sb.append(gap2).append("clearForm:true,").append(Constants.ENTER);
		sb.append(gap2).append("dataType:'json',").append(Constants.ENTER);
		sb.append(gap2).append("beforeSubmit:  function(formData, jqForm, options){").append(Constants.ENTER);
		sb.append(gap3).append("return addValidator.valid();").append(Constants.ENTER);
		sb.append(gap2).append("},").append(Constants.ENTER);
		sb.append(gap2).append("success:function(responseText, statusText, xhr, $form){").append(Constants.ENTER);
		sb.append(gap3).append("if(responseText.errorCode != 0){").append(Constants.ENTER);
		sb.append(gap4).append("$.messager.alert(\"添加字典信息失败\",responseText.errorMessage);").append(Constants.ENTER);
		sb.append(gap3).append("}else{").append(Constants.ENTER);
		sb.append(gap4).append("$(\"#addDictModal\").modal(\"hide\");").append(Constants.ENTER);
		sb.append(gap4).append("table.bootstrapTable(\"refresh\");").append(Constants.ENTER);
		sb.append(gap3).append("}").append(Constants.ENTER).append(gap2).append("}").append(Constants.ENTER).append(gap).append("});").append(Constants.ENTER);
		
		sb.append(gap).append("$(\".btn-edit\").bind(\"click\",doEdit);").append(Constants.ENTER);
		sb.append(gap).append("$(\".btn-del\").bind(\"click\",doDel);").append(Constants.ENTER);
		sb.append(gap).append("$(\"#btn-detail\").bind(\"click\",doShow);").append(Constants.ENTER);
		sb.append(gap).append("$(\"#btn-reInit\").bind(\"click\",doReInit);").append(Constants.ENTER);
		sb.append("});").append(Constants.ENTER);
	}
	
	private void getInitValidate(StringBuilder sb, J2EETable table){
		sb.append("function initValidate(formId){").append(Constants.ENTER);
		sb.append(gap).append("var validator = $(formId).validate({").append(Constants.ENTER);
		sb.append(gap2).append("rules: {").append(Constants.ENTER);
		List<com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportColumn> list  = table.getColumns();
		for(com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportColumn column : list){
			if(!column.isPK() && (column.getDataType().equals(DataType.BIGDEECIMAL) || column.getDataType().equals(DataType.FLOAT)
					 || column.getDataType().equals(DataType.INT)|| column.getDataType().equals(DataType.LONG)
					 || column.getDataType().equals(DataType.SHORT)|| column.getDataType().equals(DataType.DOUBLE))){
				sb.append(gap3).append(ClazzUtil.getVarName(column.getName()) + ": {required: true, number:"+ column.getLength() +"},").append(Constants.ENTER);
				sb.append(gap3).append("edit" + ClazzUtil.getVarName(column.getName()) + ": {required: true, number:"+ column.getLength() +"},").append(Constants.ENTER);
			}
			
		}
		sb.append(gap2).append("}").append(Constants.ENTER);
		sb.append(gap).append("});").append(Constants.ENTER);
		sb.append(gap).append("return validator;").append(Constants.ENTER);
		sb.append("}").append(Constants.ENTER);
	}
	
	private void getDoEdit(StringBuilder sb , J2EETable table){
		sb.append("function doEdit(){").append(Constants.ENTER);
		sb.append(gap).append("var rows =table.bootstrapTable(\"getSelections\");").append(Constants.ENTER);
		
		sb.append(gap).append("if(rows && rows.length ==1){").append(Constants.ENTER);
		List<com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportColumn> list  = table.getColumns();
		for (com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportColumn column : list) {
			if (column.getDataType().equals(DataType.DATE)) {
				sb.append(gap2).append("rows[0]." + ClazzUtil.getVarName(column.getName()) +" = parsedate(rows[0]."
			    + ClazzUtil.getVarName(column.getName()) +",null,null);").append(Constants.ENTER);
			}
		}
		sb.append(gap2).append("autoEdit(rows[0]);").append(Constants.ENTER);
		sb.append(gap2).append("$(\"#editDictModal\").modal(\"show\")").append(Constants.ENTER);
		sb.append(gap).append("}else{").append(Constants.ENTER);
		sb.append(gap2).append("$.messager.alert(\"提示\", \"请选择要编辑的记录!\");").append(Constants.ENTER);
		sb.append(gap2).append("}").append(Constants.ENTER).append(gap).append("}").append(Constants.ENTER);
	}
	
	private void getDoShow(StringBuilder sb, J2EETable table){
		sb.append("function doShow(){").append(Constants.ENTER);
		sb.append(gap).append("var rows = table.bootstrapTable(\"getSelections\");").append(Constants.ENTER);
		sb.append(gap).append("if(rows && rows.length ==1){").append(Constants.ENTER);
		String url = ClazzUtil.getVarName(table.getModelName()) + Constants.FILE_SEP + "detail?id=rows[0].id";
		sb.append(gap2).append("showDetail(\""+ url +"\");").append(Constants.ENTER);
		sb.append(gap).append(" }else{").append(Constants.ENTER);
		sb.append(gap2).append("$.messager.alert(\"提示\", \"请选择记录!\");").append(Constants.ENTER);
		sb.append(gap).append("}").append(Constants.ENTER).append("}").append(Constants.ENTER);
	}
	
	private void getDoDel(StringBuilder sb, J2EETable table){
	
		sb.append("function doDel(){").append(Constants.ENTER);
		sb.append(gap).append("var rows = table.bootstrapTable(\"getSelections\");").append(Constants.ENTER);
		sb.append(gap).append("if(rows && rows.length ==1){").append(Constants.ENTER);
		sb.append(gap2).append("$.messager.confirm(\"警告\", \"您确认要删除此记录吗?\", function() {").append(Constants.ENTER);
	
		String url = ClazzUtil.getVarName(table.getModelName()) + Constants.FILE_SEP + "delete";
		sb.append(gap3).append("$.post(\""+ url +"\", {\"id\":rows[0].id}, function(){$(\"#dictTable\").bootstrapTable(\"refresh\");})").append(Constants.ENTER);
		sb.append(gap2).append("});").append(Constants.ENTER);
		sb.append(gap).append("}else{").append(Constants.ENTER);
		sb.append(gap2).append("$.messager.alert(\"提示\", \"请选择要删除的记录!\");").append(Constants.ENTER);
		sb.append(gap).append("}").append(Constants.ENTER).append("}").append(Constants.ENTER);
	}
	
	private void getDoReInit(StringBuilder sb, J2EETable table){
		sb.append("function doReInit() {").append(Constants.ENTER);
		sb.append(gap).append("$.messager.confirm(\"提示\",\"重新加载字典时间较长，请耐心等待\",function () {").append(Constants.ENTER);
		String url = ClazzUtil.getVarName(table.getModelName()) + Constants.FILE_SEP + "init";
		sb.append(gap2).append("$.post(\""+ url +"\",{},function(data){").append(Constants.ENTER);
		sb.append(gap3).append("if(data.errorCode == \"0\"){").append(Constants.ENTER);
		sb.append(gap4).append("$.messager.popup(\"加载成功\")").append(Constants.ENTER);
		sb.append(gap3).append("}else{").append(Constants.ENTER);
		sb.append(gap4).append("$.messager.alert(\"提示\",\"加载失败\");").append(Constants.ENTER);
		sb.append(gap3).append("}").append(Constants.ENTER);
		sb.append(gap2).append("})").append(Constants.ENTER);
		sb.append(gap).append("})").append(Constants.ENTER);
		sb.append("}").append(Constants.ENTER);
	}
	
	private void getParsedate(StringBuilder sb){
		sb.append("function parsedate(value,index,row){").append(Constants.ENTER);
		sb.append(gap).append("if(value.time == null) return value").append(Constants.ENTER);
		sb.append(gap).append("var date = new Date(value.time);").append(Constants.ENTER);
		sb.append(gap).append("var datetime = date.getFullYear()").append(Constants.ENTER);
		sb.append(gap).append("+ \"-\" + ((date.getMonth() + 1) > 10 ? (date.getMonth() + 1) : \"0\" + (date.getMonth() + 1))").append(Constants.ENTER);
		sb.append(gap).append("+ \"-\" + (date.getDate() < 10 ? \"0\" + date.getDate() : date.getDate());").append(Constants.ENTER);
		sb.append(gap).append("return datetime;").append(Constants.ENTER);
		sb.append(gap).append("}").append(Constants.ENTER);
	}
	
	private void getDatePicker(StringBuilder sb, J2EETable table) {
		sb.append("$(document).ready(function(){").append(Constants.ENTER);
		List<com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportColumn> list = table.getColumns();
		for (com.tuhanbao.base.util.io.codeGenarator.tableUtil.ImportColumn column : list) {
			//新增
			if (!column.isPK() && column.getDataType().equals(DataType.DATE)) {
				sb.append(gap).append("$(\"#"+ ClazzUtil.getVarName(column.getName()) +"\").datetimepicker({").append(Constants.ENTER);
				sb.append(gap2).append("format: 'yyyy-mm-dd',").append(Constants.ENTER);
				sb.append(gap2).append("minView:2,").append(Constants.ENTER);
				sb.append(gap2).append("language:'zh-CN'").append(Constants.ENTER);
				sb.append("});").append(Constants.ENTER);
			}
			//编辑
			if (!column.isPK() && column.getDataType().equals(DataType.DATE)) {
				sb.append(gap).append("$(\"#edit"+ ClazzUtil.getVarName(column.getName()) +"\").datetimepicker({").append(Constants.ENTER);
				sb.append(gap2).append("format: 'yyyy-mm-dd',").append(Constants.ENTER);
				sb.append(gap2).append("minView:2,").append(Constants.ENTER);
				sb.append(gap2).append("language:'zh-CN'").append(Constants.ENTER);
				sb.append(gap).append("});").append(Constants.ENTER);
			}
		}
		sb.append("});").append(Constants.ENTER);
	}
	
	private void getInit(StringBuilder sb){
		
		sb.append("$(function() {").append(Constants.ENTER);
		sb.append(gap).append("initSelect();").append(Constants.ENTER);
		sb.append("});").append(Constants.ENTER);
		
		getInitSelect(sb);
		getProvinceFous(sb);
		getProvinceChange(sb);
		getInitAreas(sb);
		getCityChange(sb);
		getCountyChange(sb);
		
	}
	
	private void getInitSelect(StringBuilder sb){
		sb.append("function initSelect() {").append(Constants.ENTER);
		sb.append(gap).append("var data = new Array();").append(Constants.ENTER);
		sb.append(gap).append("data[0] = { id : \"1\", value : \"男\" };").append(Constants.ENTER);
		sb.append(gap).append("var orgHtml = '<option value=\"\">全部</option>';").append(Constants.ENTER);
		sb.append(gap).append("$.each(data, function(n, obj) {").append(Constants.ENTER);
		sb.append(gap2).append("orgHtml += '<option value=\"' + obj.id + '\">' + obj.value + '</option>';").append(Constants.ENTER);
		sb.append(gap).append("});").append(Constants.ENTER);
		sb.append(gap).append("$(\"#selectorg\").html(orgHtml);").append(Constants.ENTER);
		sb.append("}").append(Constants.ENTER);
	}
	
	private void getProvinceFous(StringBuilder sb){
		sb.append("$('#provIdSelect').focus(function () {").append(Constants.ENTER);
		sb.append(gap).append("initAreas();").append(Constants.ENTER);
		sb.append("});").append(Constants.ENTER);
	}
	
	private void getProvinceChange(StringBuilder sb){
		sb.append("$('#provIdSelect').change(function () {").append(Constants.ENTER);
		sb.append(gap).append("var selectedProvOpt = $('#provIdSelect option:selected');").append(Constants.ENTER);
		sb.append(gap).append("$('input[name=\"provName\"]').val(selectedProvOpt.text());").append(Constants.ENTER);
		sb.append(gap).append("var selectedProv = selectedProvOpt.val();").append(Constants.ENTER);
		sb.append(gap).append("var citiesOpt = \"<option selected value=''>-地市-</option>\";").append(Constants.ENTER);
		sb.append(gap).append("var citys = area[selectedProv].children;").append(Constants.ENTER);
		sb.append(gap).append("for (var i in citys) {").append(Constants.ENTER);
		sb.append(gap2).append("citiesOpt = citiesOpt + \"<option value=\" + i + \" >\" + citys[i].name + \"</option>\";").append(Constants.ENTER);
		sb.append(gap).append("}").append(Constants.ENTER);
		sb.append(gap).append("$('#cityIdSelect').html(citiesOpt);").append(Constants.ENTER);
		sb.append("});").append(Constants.ENTER);
	}
	
	private void getInitAreas(StringBuilder sb){
		sb.append("function initAreas() {").append(Constants.ENTER);
		sb.append(gap).append("var provOpt = \"<option selected value=''>-省份-</option>\";").append(Constants.ENTER);
		sb.append(gap).append("for (var i in area) {").append(Constants.ENTER);
		sb.append(gap).append("provOpt = provOpt + \"<option value=\" + i + \" >\" + area[i].name + \"</option>\";").append(Constants.ENTER);
		sb.append(gap).append("}").append(Constants.ENTER);
		sb.append(gap).append("$('#provIdSelect').html(provOpt);").append(Constants.ENTER);
		sb.append("}").append(Constants.ENTER);
	}
	
	private void getCityChange(StringBuilder sb){
		sb.append("$('#cityIdSelect').change(function () {").append(Constants.ENTER);
		sb.append(gap).append("var selectedCityOpt = $('#cityIdSelect option:selected');").append(Constants.ENTER);
		sb.append(gap).append("var selectedProvOpt = $('#provIdSelect option:selected');").append(Constants.ENTER);
		sb.append(gap).append("$('input[name=\"cityName\"]').val(selectedCityOpt.text());").append(Constants.ENTER);
		sb.append(gap).append("var selectedCity = selectedCityOpt.val();").append(Constants.ENTER);
		sb.append(gap).append("var selectedProv = selectedProvOpt.val();").append(Constants.ENTER);
		sb.append(gap).append("var data = $('#countyIdSelect').data(selectedCity);").append(Constants.ENTER);
		sb.append(gap).append("var countiesOpt = \"<option selected value=''>-区县-</option>\";").append(Constants.ENTER);
		sb.append(gap).append("var citys = area[selectedProv].children;").append(Constants.ENTER);
		sb.append(gap).append("var opts = citys[selectedCity].children;").append(Constants.ENTER);
		sb.append(gap).append("for (var i in opts) {").append(Constants.ENTER);
		sb.append(gap2).append("countiesOpt = countiesOpt + \" <option value=\" + opts[i].id + \" >\" + opts[i].name + \"</option>\";").append(Constants.ENTER);
		sb.append(gap).append("}").append(Constants.ENTER);
		sb.append(gap).append("$('#countyIdSelect').html(countiesOpt);").append(Constants.ENTER);
		sb.append("});").append(Constants.ENTER);
	}
	
	private void getCountyChange(StringBuilder sb){
		sb.append("$('#countyIdSelect').change(function(){").append(Constants.ENTER);
		sb.append(gap).append("var selectedCountyOpt = $('#countyIdSelect option:selected');").append(Constants.ENTER);
		sb.append(gap).append("$('input[name=\"countyName\"]').val(selectedCountyOpt.text());").append(Constants.ENTER);
		sb.append("});").append(Constants.ENTER);
	}
	
}
