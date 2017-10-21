package com.tuhanbao.impl.crm.service.crm;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tuhanbao.api.crm.constants.ErrorCode;
import com.tuhanbao.api.crm.constants.enums.ServerState;
import com.tuhanbao.api.crm.model.crm.Company;
import com.tuhanbao.api.crm.service.crm.ICompanyService;
import com.tuhanbao.base.util.exception.MyException;
import com.tuhanbao.base.util.log.LogManager;
import com.tuhanbao.base.util.objutil.StringUtil;
import com.tuhanbao.base.util.objutil.TimeUtil;

@Service("companyService")
@Transactional("crmTransactionManager")
public class CompanyServiceImpl extends ServiceImpl<Company> implements ICompanyService {

	private static final String SHORTNAME_REGEX = "[a-zA-Z]{1,10}";
	
	private static final String[] SQLS = new String[]{"create table T_CUSTOM_tubie(ID BIGINT COMMENT \"主键id\" AUTO_INCREMENT, NAME VARCHAR(63) COLLATE utf8_unicode_ci COMMENT \"名称\", TEL_NUM VARCHAR(23) COLLATE utf8_unicode_ci COMMENT \"电话号码\", STATE INT COMMENT \"状态\", MARK VARCHAR(255) COLLATE utf8_unicode_ci COMMENT \"备注\", OPERATOR_ID BIGINT COMMENT \"跟进人\", SEX INT COMMENT \"性别\", BIRTHDAY DATETIME COMMENT \"生日\", DIY_COLUMN1 VARCHAR(255) COLLATE utf8_unicode_ci COMMENT \"自定义字段1\", DIY_COLUMN2 VARCHAR(255) COLLATE utf8_unicode_ci COMMENT \"自定义字段2\", DIY_COLUMN3 VARCHAR(255) COLLATE utf8_unicode_ci COMMENT \"自定义字段3\", DIY_COLUMN4 VARCHAR(255) COLLATE utf8_unicode_ci COMMENT \"自定义字段4\", DIY_COLUMN5 VARCHAR(255) COLLATE utf8_unicode_ci COMMENT \"自定义字段5\", DIY_COLUMN6 VARCHAR(255) COLLATE utf8_unicode_ci COMMENT \"自定义字段6\", DIY_COLUMN7 VARCHAR(255) COLLATE utf8_unicode_ci COMMENT \"自定义字段7\", DIY_COLUMN8 VARCHAR(255) COLLATE utf8_unicode_ci COMMENT \"自定义字段8\", DIY_COLUMN9 VARCHAR(255) COLLATE utf8_unicode_ci COMMENT \"自定义字段9\", DIY_COLUMN10 VARCHAR(255) COLLATE utf8_unicode_ci COMMENT \"自定义字段10\", PRIMARY KEY (ID));",
			"create table T_FOLLOW_RECORD_tubie(ID BIGINT AUTO_INCREMENT, CUSTOM_ID BIGINT COMMENT \"CUSTOM信息\", STATE INT COMMENT \"跟进状态\", MARK VARCHAR(255) COLLATE utf8_unicode_ci COMMENT \"备注\", FOLLOW_TIME DATETIME COMMENT \"跟进时间\", DIY_COLUMN1 VARCHAR(255) COLLATE utf8_unicode_ci COMMENT \"自定义字段1\", DIY_COLUMN2 VARCHAR(255) COLLATE utf8_unicode_ci COMMENT \"自定义字段2\", DIY_COLUMN3 VARCHAR(255) COLLATE utf8_unicode_ci COMMENT \"自定义字段3\", PRIMARY KEY (ID));",
			"create table T_SMS_RECORD_tubie(ID BIGINT COMMENT \"主键id\" AUTO_INCREMENT, CUSTOM_ID BIGINT COMMENT \"电话号码\", USER_ID BIGINT COMMENT \"操作员\", SEND_DATE DATETIME COMMENT \"发送日期\", SEND_CONTENT VARCHAR(255) COLLATE utf8_unicode_ci COMMENT \"内容\", STATE INT COMMENT \"发送状态\", PRIMARY KEY (ID));"
	};
	
	
	public CompanyServiceImpl() {
		LogManager.info("new instance");
	}
	
	@Override
	public void addCompany(String name, String shortName) {
		//shortName必须是纯英文
		shortName = shortName.toUpperCase();
		checkShortName(shortName);

		Company company = new Company();
		company.setName(name);
		company.setShortName(shortName);
		company.setServerState(ServerState.ARREARAGE);
		this.add(company);
		
		
		createTable(shortName);
	}

	private void createTable(String shortName) {
		for (String sql : SQLS) {
			this.excuteSql(sql.replace("tubie", shortName));
		}
	}
	
	@Override
	public void modifyCompany(Company company) {
		String name = company.getName();
		if (StringUtil.isEmpty(name)) {
			throw new MyException(ErrorCode.ILLEGAL_INCOMING_ARGUMENT);
		}
		//shortName决定表结构，不让改
		company.setShortName(null);
		this.updateSelective(company);
	}
	
	private void checkShortName(String shortName) {
		if (StringUtil.isEmpty(shortName)) {
			throw new MyException(ErrorCode.ILLEGAL_INCOMING_ARGUMENT);
		}
		if (shortName.matches(SHORTNAME_REGEX)) {
			//因为company是全缓存表，不能从数据库查询
			List<Company> list = this.select(null);
			if (list == null) return;
			for (Company comp : list) {
				if (shortName.equals(comp.getShortName())) {
					throw new MyException(ErrorCode.COMPANY_SHORT_NAME_REPEAT, shortName);
				}
			}
		}
		else {
			throw new MyException(ErrorCode.SHORT_NAME_INVALID, shortName);
		}
	}

	@Override
	public void activate(long id, long time) {
		Company company = new Company();
		company.setId(id);
		company.setValidTerm(new Date(time));
		if (time >= TimeUtil.getTodayTime(0, 0, 0)) {
			company.setServerState(ServerState.RUNNING);
		}
		else{
			company.setServerState(ServerState.ARREARAGE);
		}
		this.updateSelective(company);
	}
}