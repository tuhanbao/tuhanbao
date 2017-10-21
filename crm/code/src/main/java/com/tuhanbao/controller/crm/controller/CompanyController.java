package com.tuhanbao.controller.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tuhanbao.api.crm.model.crm.Company;
import com.tuhanbao.api.crm.service.crm.ICompanyService;
import com.tuhanbao.base.util.objutil.TimeUtil;

@Controller
@RequestMapping(value = "company", produces = "text/html;charset=UTF-8")
public class CompanyController extends BaseController {
	
	private static final String ACTIVATE = "activate";

	private static final String LIST_ALL = "listAll";
	
	@Autowired
	private ICompanyService companyService;

	@RequestMapping(ADD)
	@ResponseBody
	public Object add(String name, String shortName) {
		companyService.addCompany(name, shortName);
		return NULL;
	}
	
	@RequestMapping(UPDATE)
	@ResponseBody
	public Object update(Company company) {
		companyService.modifyCompany(company);
		return NULL;
	}
	
	@RequestMapping(LIST_ALL)
	@ResponseBody
	public Object listAll() {
		return companyService.select(null);
	}
	
	/**
	 * 如果一个公司不处于激活状态，账号是不允许使用的
	 * @param id
	 * @param validTerm 有效期截止日期
	 */
	@RequestMapping(ACTIVATE)
	@ResponseBody
	public Object activate(long id, String validTerm) {
		//如果日期小于当前，是无效参数
		long time = TimeUtil.getTimeByDay(validTerm);
		companyService.activate(id, time);
		return NULL;
	}
}
