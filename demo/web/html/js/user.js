/**
 * Created by liuhanhui on 2017/1/4.
 */
requirejs.config({
    baseUrl: '',
    paths: {
        // datatables: '/plugins/datatables/dataTables.bootstrap.min',
        // datatables2: '/plugins/datatables/jquery.dataTables.min',
        datatables2: "/web/js/datatables/js/jquery.dataTables"
    }
});
// require(['datatables', 'datatables2']);
define(['/web/html/js/api.js', '/web/html/js/constant.js', '/web/js/bootstrap.js', 'datatables2', '/web/js/jquery.slimscroll.min.js', '/web/js/sweetalert.min.js', '/web/js/jquery.ztree.core.js', '/web/js/jquery.ztree.excheck.js'], function (api, constant, js1, js2, js3, js4, js5, js6) {
    function retrieveData(sSource, aoData, fnCallBack) {
        var sEcho = aoData[0].value;
        $.ajax({
            "type": "get",
            "contentType": "application/json",
            "url": sSource,
            "data": {

            },
            "success": function (resp) {
                console.log(resp);
                var result = {
                    "sEcho": sEcho,
                    "aaData": resp.data
                }
                fnCallBack(result);
            }
        })
        ;
    }

    var table = $('#example2').DataTable({
        "paging": false,
        "lengthChange": false,
        "searching": false,
        "ordering": false,
        "info": false,
        "autoWidth": false,
        "bServerSide": true,
        "sAjaxSource": api.userList,
        "fnServerData": retrieveData,
        "columnDefs": [
            {
                "defaultContent": "",
                "targets": "_all"
            },
            {
                "targets": [0],
                "data": "id",
            },
            {
                "targets": [1],
                "data": "login_name",
            },
            {
                "targets": [2],
                "data": "role.role_name",
            },
            {
                "targets": [3],
                "data": "create_date",
                "render": function (data, type, full, meta) {
                    return new Date(data).toLocaleString();
                }
            },
            {
                "targets": [4],
                "data": "mark",
            },
            {
                "targets": [5],
                "data": "id",
                "render": function (data, type, full, meta) {
                    var html = "<div class='col-md-12 text-center'>"
                    html += '<button  type="button" class="btn btn-success col-md-4 col-md-offset-1" data-toggle="' + data + '">修改</button>';
                    html += '<button  type="button" class="btn btn-danger col-md-4 col-md-offset-1" data-toggle="' + data + '">删除</button>';
                    html += '</div>'
                    return html;
                }
            }
        ]
    });
    $("#example2").on('click', '.btn-success', function () {
        initModal();
        initRole();
        placeUserDetail($(this).attr("data-toggle"));
        $("#btn_userAdd").hide();
        $("#btn_userUpdate").show();
        $("#myModal").modal("show");

    });
    $("#example2").on('click', '.btn-danger', function () {
        deleteUser($(this).attr("data-toggle"));
        $("#example2").DataTable().ajax.reload();
    });

    $("#addUser").click(function () {
        initModal();
        initRole();
        $("#btn_userUpdate").hide();
        $("#btn_userAdd").show();
        $("#myModal").modal("show");
    });
    var placeUserDetail = function (userId) {
        $.ajax({
            "type": "get",
            "url": api.userDetail,
            "data": {userId: userId},
            "success": function (result) {
                console.log(result);
                if (result.code === constant.value.success_code) {
                    var user = result.data;
                    $("#inputUserAccount").val(user.login_name);
                    $("#inputUserPassword").val("");
                    $("#inputUserRole").val(user.role_id);
                    $("#inputPowerMark").val(user.mark);
                    $("#userId").val(user.id);
                } else {
                    swal({
                        title: result.msg,
                        type: "error"
                    })
                }
            }
        });
    };
    var deleteUser = function (userId) {
        $.ajax({
            "type": "post",
            "url": api.userDele,
            "data": {userId: userId},
            "success": function (result) {
                if (result.code === constant.value.success_code) {
                    swal({
                        title: "删除成功",
                        type: "success"
                    });
                    $("#myModal").modal("hide");
                    $("#example2").DataTable().ajax.reload();
                } else {
                    swal({
                        title: result.msg,
                        type: "error"
                    })
                }
            }
        });
    };
    var initModal = function () {
        $("#userId").val("");
        $("#inputUserAccount").val("");
        $("#inputUserPassword").val("");
        $("#inputPowerMark").val("");
        $("#inputUserRole").val("");
    }
    var initRole = function () {
        $.get(api.roleList, null, function (result) {
            console.log(result);
            if (result.code === constant.value.success_code) {
                var roleList = result.data;
                var html = "";
                for (var i = 0 ; i < roleList.length; i ++) {
                  html +=   '<option value="' + roleList[i].id + '">' +roleList[i].role_name +'</option>';
                };
                $("#inputUserRole").html(html);
            }
            else {
                swal({
                    title: result.msg,
                    type: "error"
                });
            }
        })
    };
    $("#btn_userAdd").click(function () {
        var params = {
            account: $("#inputUserAccount").val(),
            password: $("#inputUserPassword").val(),
            mark: $("#inputPowerMark").val(),
            roleId: $("#inputUserRole").val(),
        };
        $.ajax({
            "type": "post",
            "url": api.userAdd,
            "data": params,
            "success": function (result) {
                if (result.code === constant.value.success_code) {
                    swal({
                        title: "添加成功",
                        type: "success"
                    });
                    $("#myModal").modal("hide");
                } else {
                    swal({
                        title: result.msg,
                        type: "error"
                    })
                }
            }
        });
        $("#example2").DataTable().ajax.reload();
    });
    $("#btn_userUpdate").click(function () {
        var params = {
            userId: $("#userId").val(),
            account: $("#inputUserAccount").val(),
            password: $("#inputUserPassword").val(),
            mark: $("#inputPowerMark").val(),
            roleId: $("#inputUserRole").val(),
        };
        $.ajax({
            "type": "post",
            "url": api.userAdd,
            "data": params,
            "success": function (result) {
                if (result.code === constant.value.success_code) {
                    swal({
                        title: "修改成功",
                        type: "success"
                    });
                    $("#myModal").modal("hide");
                } else {
                    swal({
                        title: result.msg,
                        type: "error"
                    })
                }
            }
        });
        $("#example2").DataTable().ajax.reload();
    });
    initRole();
    return {};
})
;