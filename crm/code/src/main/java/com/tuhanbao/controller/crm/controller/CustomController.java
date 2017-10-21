package com.tuhanbao.controller.crm.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tuhanbao.api.crm.model.crm.Company;
import com.tuhanbao.api.crm.model.crm.Custom;
import com.tuhanbao.api.crm.service.crm.ICompanyService;
import com.tuhanbao.api.crm.service.crm.ICustomService;
import com.tuhanbao.base.util.objutil.TimeUtil;

@Controller
@RequestMapping(value = "custom", produces = "text/html;charset=UTF-8")
public class CustomController extends BaseController {
	
	@Autowired
	private ICustomService customService;

	@Autowired
	private ICompanyService companyService;
	
	private static final String LIST_ALL = "listAll";

	@RequestMapping(ADD)
	@ResponseBody
	public Object add(HttpServletRequest request, String name, String shortName) {
		// TODO
//		Company company = super.getCompany(request);
		Company company = companyService.selectById(1);
		
		Custom custom = new Custom(company);
		custom.setName("tubie");
		customService.add(custom);
		return NULL;
	}
	
	@RequestMapping(LIST_ALL)
	@ResponseBody
	public Object listAll() {
		Company company = companyService.selectById(1);
		return customService.select(company, null);
	}
}
