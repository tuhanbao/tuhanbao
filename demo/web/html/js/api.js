/**
 * Created by liuhanhui on 2017/1/3.
 */
define(function () {
    // var host1 = "/cfssd/";
    var host1="/cf/";
    // var host1 = "/cfwb/";
    //  var host1 = "/cfgzh/"
    // var host1 = "/cfcrm/";
    // var host1 = "http://182.92.119.148:8080/cfcrm/"
    // var host1 = "/cfaly/"

    return {
        login: host1 + "user/login",
        userList: host1 + "user/listUser",

        msgHistory: host1 + "smsManage/getSmsHistory",
        msgDelete: host1 + "smsManage/delete",
        msgDetail: host1 + "smsManage/getSmsDetail",

        genjinHistory: host1 + "vipMarketingRecord/get",
        genjinAdd: host1 + "vipMarketingRecord/add",
        genjinDetail: host1 + "vipMarketingRecord/getVipMarketing",
        genjinUpdate: host1 + "vipMarketingRecord/update",
        genjinDelete: host1 + "vipMarketingRecord/delete",

        awardHistory: host1 + "award/get",

        customAutoFilter: host1 + "custom/getAutoFilterItem",//获取所有的自定义过滤
        customAllFilter: host1 + "custom/listAllFilter",//获取所有的条件项
        customAllFilterItem: host1 + "custom/listFilterItems",//获取某个自定义过滤的所有条件项
        customSaveFilter: host1 + "custom/saveFilter",//保存自定义过滤
        customFilterDelete: host1 + "custom/delFilter",//删除
        customMsg: host1 + "analyze/vipMsg",

        // customerInfo: host1 + "customer/customerinfo.json",
        // customerType: host1 + "customer/customertype.json",
        customerSort: host1 + "analyze/rankVip",

        workBenchCon: host1 + "analyze/workBenchCon",//工作台

        birthAutoSendGet: host1 + "send/get",
        birthAutoSendUpdate: host1 + "send/update",

        vipAdd: host1 + "analyze/addVip",//新增客户
        vipDel: host1 + "analyze/delVip",//删除客户
        vipEdit: host1 + "analyze/editVip",//编辑客户
        vipDetail: host1 + "analyze/getVipDetail",//客户详情
	exportInfo: host1 + "analyze/exportInfo",//导出客户信息
        customerSort: host1 + "analyze/rankVip",
        timeWarn: host1+"reminder/moneyBackReminder",
        moneyReduceWarn:host1 + "reminder/investmentReminder",
        birthdayWarn:host1 +"reminder/birthReminder",
        monthWran:host1 + "reminder/noinvestReminder",
        moneyBackPlan: host1 + "reminder/moneyBackPlan",
        moneyBackPlanMsg: host1 + "reminder/moneyBackPlanMsg",
        moneyBackMsg: host1 + "reminder/moneyBackMsg",//回款时间发短信
        investmentMsg: host1 + "reminder/investmentMsg",//投资降幅发短信
        birthMsg: host1 + "reminder/birthMsg",
        noinvestMsg: host1 + "reminder/noinvestMsg",
        customAwardMsg: host1 + "award/customAwardMsg",

        awards: host1 + "award/getAaward",

        sendMsgByFilterId: host1 + "send/sendMsgAll",
        sendMsg:host1+"send/sendMsgSelected",
        sendAward:host1+"send/sendAwardSelected",

        powerAdd: host1 + "user/addPermission",
        powerList: host1 + "user/listPermission",
        powerEdit: host1 + "user/editPermission ",
        powerDele: host1 + "user/deletePermission",
        powerChildren: host1 + "user/getChildPermission",
        powerDetail: host1 + " user/getPermissionDetail",

        roleAdd: host1 + "user/addRole",
        roleList: host1 + "user/getRoleList",
        roleDele: host1 + "user/deleteRole",
        roleEdit: host1 + "user/editRole",
        roleDetail: host1 + "user/getRoleDetail",

        userAdd: host1 + "user/addUser",
        userList: host1 + "user/getUserList",
        userEdit: host1 + "user/editUser",
        userDele: host1 + "user/deleteUser",
        userDetail: host1 + "user/getUserDetail",

        login: host1 + "user/login",

        test1: host1 + "downline/agent/test1",
        test2: host1 + "downline/agent/test2",

        uploadCustomInfo: host1 + "analyze/importCustomInfo",
        uploadInvestInfo: host1 + "analyze/importInvestInfo",
        uploadBackMoneyInfo: host1 + "analyze/importBackMoneyInfo",
    }
});
