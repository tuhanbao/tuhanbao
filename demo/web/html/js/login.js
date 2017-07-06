/**
 * Created by shine on 2017/2/21.
 */
requirejs.config({
    baseUrl: '',
    paths: {
        datatables2: "http://cdn.datatables.net/1.10.13/js/jquery.dataTables.min"
    }
});
define(['/web/html/js/api.js', '/web/html/js/constant.js', '/web/js/bootstrap.js', 'datatables2', '/web/js/jquery.slimscroll.min.js', '/web/js/sweetalert.min.js', '/web/js/bootstrap-datetimepicker.js'], function (api, constant, js1, js2, js3, js4, js5) {
    $("#btn_login").click(function () {
        var params = {
            loginName: $("#username").val(),
            password: $("#password").val()
        }
        $.ajax({
            "type": "post",
            "url": api.login,
            "data": params,
            "success": function (result) {
                if (result.code === constant.value.success_code) {
                    swal({
                        title: "登录成功",
                        type: "success"
                    });
                    // constant.currentUser = result.data;
                    // localStorage.currentUser = result.data;
                    localStorage.setItem("user", JSON.stringify(result.data));
                    window.location.href="/web/html/index.html";
                } else {
                    swal({
                        title: result.msg,
                        type: "error"
                    })
                }
            }
        });
    });

    $("#btn_login1").click(function () {
        var params = {
            loginName: $("#username").val(),
            password: $("#password").val()
        }
        $.ajax({
            "type": "post",
            "url": api.test1,
            "data": params,
            "success": function (result) {
                if (result.code === constant.value.success_code) {
                    swal({
                        title: "登录成功",
                        type: "success"
                    });
                    // constant.currentUser = result.data;
                    // localStorage.currentUser = result.data;
                    localStorage.setItem("user", JSON.stringify(result.data));
                    window.location.href="/web/html/index.html";
                } else {
                    swal({
                        title: result.msg,
                        type: "error"
                    })
                }
            }
        });
    });

    $("#btn_login2").click(function () {
        var params = {
            loginName: $("#username").val(),
            password: $("#password").val()
        }
        $.ajax({
            "type": "post",
            "url": api.test2,
            "data": params,
            "success": function (result) {
                if (result.code === constant.value.success_code) {
                    swal({
                        title: "登录成功",
                        type: "success"
                    });
                    // constant.currentUser = result.data;
                    // localStorage.currentUser = result.data;
                    localStorage.setItem("user", JSON.stringify(result.data));
                    window.location.href="/web/html/index.html";
                } else {
                    swal({
                        title: result.msg,
                        type: "error"
                    })
                }
            }
        });
    });
    return {};
});