package com.hhnz.api.cfcrm.model.cfcrm;

import com.tuhanbao.base.dataservice.ServiceBean;
import com.hhnz.api.cfcrm.constants.TableConstants;
import com.tuhanbao.base.dataservice.MetaObject;
import com.tuhanbao.base.util.db.table.data.StringValue;
import java.util.List;

public class AreaMO extends ServiceBean {
    protected AreaMO() {
        this(new MetaObject(TableConstants.T_AREA.TABLE));
    }

    protected AreaMO(MetaObject mo) {
        super(mo);
    }

    public String getAreaId() {
        StringValue value = (StringValue)getValue(TableConstants.T_AREA.AREA_ID);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setAreaId(String value) {
        setValue(TableConstants.T_AREA.AREA_ID, StringValue.valueOf(value));
    }

    public String getAreaName() {
        StringValue value = (StringValue)getValue(TableConstants.T_AREA.AREA_NAME);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setAreaName(String value) {
        setValue(TableConstants.T_AREA.AREA_NAME, StringValue.valueOf(value));
    }

    public String getParentId() {
        StringValue value = (StringValue)getValue(TableConstants.T_AREA.PARENT_ID);
        if (value == null) return null;
        else return value.getValue();
    }

    public void setParentId(String value) {
        setValue(TableConstants.T_AREA.PARENT_ID, StringValue.valueOf(value));
    }

    public Area getParent() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_AREA.PARENT_ID);
        return result == null || result.isEmpty() ? null : (Area)result.get(0);
    }

    public Area removeParent() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_AREA.PARENT_ID);
        return result == null || result.isEmpty() ? null : (Area)result.get(0);
    }

    public void setParent(Area value) {
        this.setFKBean(TableConstants.T_AREA.PARENT_ID, value);
    }

    @SuppressWarnings("unchecked")
    public List<Area> getAreas() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_AREA.PARENT_ID, true);
        return (List<Area>)result;
    }

    @SuppressWarnings("unchecked")
    public List<Area> removeAreas() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_AREA.PARENT_ID, true);
        return (List<Area>)result;
    }

    public void setAreas(List<Area> value) {
        this.setFKBean(TableConstants.T_AREA.PARENT_ID, value, true);
    }

    @SuppressWarnings("unchecked")
    public List<VipPrimaryInfo> getVipPrimaryInfos() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_VIP_PRIMARY_INFO.VIP_PROVINCE_ID);
        return (List<VipPrimaryInfo>)result;
    }

    @SuppressWarnings("unchecked")
    public List<VipPrimaryInfo> removeVipPrimaryInfos() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_VIP_PRIMARY_INFO.VIP_PROVINCE_ID);
        return (List<VipPrimaryInfo>)result;
    }

    public void setVipPrimaryInfos(List<VipPrimaryInfo> value) {
        this.setFKBean(TableConstants.T_VIP_PRIMARY_INFO.VIP_PROVINCE_ID, value);
    }

    @SuppressWarnings("unchecked")
    public List<VipPrimaryInfo> getVipPrimaryInfosByVipCity() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_VIP_PRIMARY_INFO.VIP_CITY_ID);
        return (List<VipPrimaryInfo>)result;
    }

    @SuppressWarnings("unchecked")
    public List<VipPrimaryInfo> removeVipPrimaryInfosByVipCity() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_VIP_PRIMARY_INFO.VIP_CITY_ID);
        return (List<VipPrimaryInfo>)result;
    }

    public void setVipPrimaryInfosByVipCity(List<VipPrimaryInfo> value) {
        this.setFKBean(TableConstants.T_VIP_PRIMARY_INFO.VIP_CITY_ID, value);
    }

    @SuppressWarnings("unchecked")
    public List<VipPrimaryInfo> getVipPrimaryInfosByVipDistrict() {
        List<? extends ServiceBean> result = this.getFKBean(TableConstants.T_VIP_PRIMARY_INFO.VIP_DISTRICT_ID);
        return (List<VipPrimaryInfo>)result;
    }

    @SuppressWarnings("unchecked")
    public List<VipPrimaryInfo> removeVipPrimaryInfosByVipDistrict() {
        List<? extends ServiceBean> result = this.removeFKBean(TableConstants.T_VIP_PRIMARY_INFO.VIP_DISTRICT_ID);
        return (List<VipPrimaryInfo>)result;
    }

    public void setVipPrimaryInfosByVipDistrict(List<VipPrimaryInfo> value) {
        this.setFKBean(TableConstants.T_VIP_PRIMARY_INFO.VIP_DISTRICT_ID, value);
    }

}