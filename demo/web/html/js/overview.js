/**
 * Created by HeartZeus on 2017/2/24.
 */
requirejs.config({
    baseUrl: '',
    paths: {
        datatables2: "http://cdn.datatables.net/1.10.13/js/jquery.dataTables.min"
    }
});
define(['/web/html/js/api.js', '/web/html/js/constant.js', '/web/js/bootstrap.js', 'datatables2', '/web/js/jquery.slimscroll.min.js', '/web/js/sweetalert.min.js'], function (api, constant, js1, js2, js3, js4) {

    var initWorkBenchCon = function () {
        // console.log("生日提醒");
        var url = api.workBenchCon;
        $.ajax({
            "type": "get",
            "contentType": "application/json",
            "url": url,
            "data": {},
            "success": function (resp) {
                resp = JSON.parse(resp);
                // console.log(resp);
                var birthday = resp.data.birth;
                var investRemind = resp.data.investRemind;
                var backMoneyAmount = resp.data.backMoneyAmount;
                var backMoneyNum = resp.data.backMoneyNum;
                $("#birthday").html(birthday + "位客户今日生日");
                $("#backMoney").html('今日' +backMoneyNum+ '位客户回款 累计回款金额￥' + backMoneyAmount / 100);
                $("#investRemind").html('今日' +investRemind+ '位客户投资降幅50%以上');
            }
        });
    };
    var init = function () {
        initWorkBenchCon();
    };

    init();
    return {};

});