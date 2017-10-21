package com.tuhanbao.api.crm.service.crm;

import com.tuhanbao.api.crm.model.crm.Company;
import com.tuhanbao.web.service.IService;

public interface ICompanyService extends IService<Company> {

	void addCompany(String name, String shortName);

	/**
	 * ����һ����˾�ķ�����Ч����
	 * @param id
	 * @param time
	 */
	void activate(long id, long time);

	void modifyCompany(Company company);
}