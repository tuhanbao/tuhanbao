/**
 * Created by HeartZeus on 2017/2/24.
 */
requirejs.config({
    baseUrl: '',
    paths: {
        datatables2: "http://cdn.datatables.net/1.10.13/js/jquery.dataTables.min"
    }
});
define(['/web/html/js/api.js', '/web/html/js/constant.js', '/web/js/bootstrap.js', 'datatables2', '/web/js/jquery.slimscroll.min.js', '/web/js/sweetalert.min.js', '/web/js/bootstrap-datetimepicker.js'], function (api, constant, js1, js2, js3, js4, js5) {
    $('input[type="checkbox"].flat-red, input[type="radio"].flat-red').iCheck({
        checkboxClass: 'icheckbox_flat-green',
        radioClass: 'iradio_flat-green'
    });
    $('input[name="birthdayMsgSwitch"]').on('ifChecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定
        var value = event.target.value;
        if (value === "0") {
            $("#birthdayMsgSet").hide();
        }
        else if (value === "1") {
            $("#birthdayMsgSet").show();
        }
    });
    $("#save_btn").click(function () {
        var url = api.birthAutoSendUpdate;
        var value;
        $("input[name='birthdayMsgSwitch']").each(function(){
            if(true == $(this).is(':checked')){
                value = $(this).val();
            }
        });
        if(!$("#msgcontent").val()){
            sweetAlert("短信内容必填");
            return;
        }
        $.ajax({
            type: "post",
            url: url,
            async: false,
            data: {
                "isOpen": value,
                "time": $("#msgTime").val(),
                "content": $("#msgcontent").val()
            },
            success: function (resp) {
                resp = JSON.parse(resp);
                console.log(resp);
                if (resp.code === constant.value.success_code) {
                    swal({
                        title: "更新成功",
                        type: "success"
                    });
                    initData();
                }
                else {
                    swal({
                        title: resp.msg,
                        type: "error"
                    });
                }
            }
        });
    });
    var initData = function () {
        var url = api.birthAutoSendGet;
        $.ajax({
            "type": "get",
            "contentType": "application/json",
            "url": url,
            "data": {},
            "success": function (resp) {
                resp = JSON.parse(resp);
                console.log(resp);
                if (resp.code === constant.value.success_code) {
                    $("#msgcontent").val(resp.data.content);
                    $("#msgTime").val(resp.data.time);
                    if (resp.data.is_open === 1) {//启动
                        $('input[name="birthdayMsgSwitch"]').eq(0).iCheck('check');
                        $("#birthdayMsgSet").show();
                    }
                    else {
                        $('input[name="birthdayMsgSwitch"]').eq(1).iCheck('check');
                        $("#birthdayMsgSet").hide();
                    }
                }
                else {
                    swal({
                        title: resp.msg,
                        type: "error"
                    });
                }
            }
        });
    };

    initData();
    return {};
});