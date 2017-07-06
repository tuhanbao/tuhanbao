package com.tuhanbao.thirdapi.pay;

import com.tuhanbao.thirdapi.ProjectType;

/**
 * 其他项目组如果涉及到新增，联系王兵
 * 18908084641
 * 
 * @author Administrator
 *
 */
public enum PayType {
    /*
     * 哈哈厨房大A保证金
     */
    YYT_A(ProjectType.YYT, 2200),
    
    /*
     * 哈哈厨房B代付
     */
    YYT_B(ProjectType.YYT, 2201);
    
    //支付前缀
    private int payId;

    private ProjectType projectType;
    
    private PayType(ProjectType projectType, int payId) {
        this.projectType = projectType;
        this.payId = payId;
    }
    
    public int getPayId() {
        return this.payId;
    }

    public ProjectType getProjectType() {
        return this.projectType;
    }
}
