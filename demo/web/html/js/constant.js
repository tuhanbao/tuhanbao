/**
 * Created by liuhanhui on 2017/1/4.
 */
define(function () {
    var currentUser;
    var value = {
        success_code: 0,
        sort_asc: 1,
        sort_desc: 0
    };
    var FollowingStage = {
        FOLLOW_UP: 0,
        DEEP_NEGOTIATION: 1,
        NEGOTIATING_SUCCESS: 2
    };
    var SendStatus = {
        SEND_SUCCESS: 0, //已发送
        TO_SEND: 1, //未发送
        SEND_FAIL: 2 //发送失败
    };
    var AwardType = {
        NONGZHUANG_INTEGRAL: 1,//农庄积分
        INVESTMENT_CERTIFICATE: 2,//投资券
        COUPON: 3,//加息券
        MAIL_COUPON: 4,//商城券
        FOOD_STAMPS: 5//粮票
    };

    var FilterType = {
        CUSTOM:1,//客户管理
        INVEST:2,//投资行为分析
    };

    var DataType = {
        STRING: "STRING",
        INT: "INT",
        DATE: "DATE",
    };

    var Opeartion = {
        EQUAL: 1,//"=="
        NOT_EQUAL: 2,//"<>"
        LESS_EQUAL: 3,//"<="
        GREATER_EQUAL: 4,//">="
        LESS: 5,//"<"
        GREATER: 6,//">"
        LIKE: 7,//like
        BETWEEN: 8,//between
        IN: 9,//in,
        NOT_LIKE: 10,//not like
        NOT_IN: 11,//not in
        IS_NULL: 12,//is null
        NOT_NULL: 13//is not null

    };

    var MoneyBackStatus = {
        BACK:4,
        UNBACK:5,
    }

    var Sex = [
        {
            "name":"男",
            "value":1
        },
        {
            "name":"女",
            "value":0
        },
    ]
	
    var VipType = [
        {
            "name":"长线客户",
            "value":1
        },
        {
            "name":"短线客户",
            "value":2
        },
		{
            "name":"中线客户",
            "value":3
        },
        {
            "name":"中长线客户",
            "value":4
        },
    ]

    var CustomType = [
        {
            "name":"复合肥用户",
            "value":1
        },
        {
            "name":"非复合肥用户",
            "value":2
        },
    ]

    return {
        value: value,
        FollowingStage: FollowingStage,
        SendStatus: SendStatus,
        AwardType: AwardType,
        FilterType: FilterType,
        DataType: DataType,
        Operation: Opeartion,
        Sex: Sex,
        CustomType: CustomType,
        MoneyBackStatus: MoneyBackStatus,
        currentUser: currentUser,
        VipType: VipType,
    };
});