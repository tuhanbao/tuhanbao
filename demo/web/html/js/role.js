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
    var zNodes =[];
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
        "sAjaxSource": api.roleList,
        "fnServerData": retrieveData,
        "columnDefs": [
            {
                "targets": [0],
                "data": "id",
            },
            {
                "targets": [1],
                "data": "role_name",
            },
            {
                "targets": [2],
                "data": "mark",
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
                "data": "id",
                "render": function (data, type, full, meta) {
                    var html = "<div class='col-md-12 text-center'>"
                    html += '<button  type="button" class="btn btn-success col-md-4 col-md-offset-1" data-toggle="' + data + '">修改</button>';
                    html += '<button  type="button" class="btn btn-danger col-md-4 col-md-offset-1" data-toggle="' + data + '">删除</button>';
                    html += '</div>';
                    return html;
                }
            }
        ]
    });


    $("#example2").on('click', '.btn-success', function () {
        initModal();
        placeRoleDetail($(this).attr("data-toggle"));
        $("#btn_roleAdd").hide();
        $("#btn_roleUpdate").show();
        $("#myModal").modal("show");

    });
    $("#example2").on('click', '.btn-danger', function () {
        deleteRole($(this).attr("data-toggle"));
        $("#example2").DataTable().ajax.reload();
    });
    function placeRoleDetail(roleId) {
        $.ajax({
            "type": "get",
            "url": api.roleDetail,
            "data": {roleId: roleId},
            "success": function (result) {
                console.log(result);
                if (result.code === constant.value.success_code) {
                    if (result.data.permission != undefined) {
                        var permissions = result.data.permission;
                        for (var k = 0; k < permissions.length; k ++) {
                            for(var i = 0; i < zNodes.length; i ++) {
                                if (permissions[k].id === zNodes[i].id) {
                                    zNodes[i]["checked"] = true;
                                }
                            }
                        }
                    }
                    var setting = {
                        check: {
                            enable: true
                        },
                        data: {
                            simpleData: {
                                enable: true
                            }
                        },
                    };
                    $.fn.zTree.init($("#treeDemo"), setting, zNodes);
                    var roleDetail = result.data.role;
                    $("#roleId").val(roleDetail.id);
                    $("#inputRoleName").val(roleDetail.role_name);
                    $("#inputRolePs").val(roleDetail.mark);
                } else {
                    swal({
                        title: result.msg,
                        type: "error"
                    })
                }
            }
        });
    };

    function deleteRole(id) {
        $.ajax({
            "type": "post",
            "url": api.roleDele,
            "data": {roleId: id},
            "success": function (result) {
                if (result.code === constant.value.success_code) {
                    swal({
                        title: "删除成功",
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
    };
    $("#addRole").click(function () {
        initModal();
        $("#btn_roleAdd").show();
        $("#btn_roleUpdate").hide();
        $("#myModal").modal("show");
    });
    var initParentPower = function () {
        zNodes=[];
        $.get(api.powerList, null, function (result) {
            console.log(result);
            if (result.code === constant.value.success_code) {
                var powerList = result.data;
                // console.log(powerList.length);
                var setting = {
                    check: {
                        enable: true
                    },
                    data: {
                        simpleData: {
                            enable: true
                        }
                    },
                };
                for(var i = 0 ; i < powerList.length; i ++) {
                    var node = {
                        id:powerList[i].id,
                        pId:powerList[i].parent_id,
                        name:powerList[i].name,
                        open: true,
                    };
                    if (powerList[i].is_menu === 1) {
                        node['icon'] = '/css/zTreeStyle/img/diy/menu1.png';
                    }
                    else if (powerList[i].is_menu === 0) {
                        node['icon'] = '/css/zTreeStyle/img/diy/button1.png';
                    }
                    zNodes.push(node);
                }
                $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            }
            else {
                swal({
                    title: result.msg,
                    type: "error"
                });
            }
        })
    };
    var initModal = function () {
        $("#roleId").val("");
        $("#inputRoleName").val("");
        $("#inputRolePs").val("");
    }
    $("#btn_roleAdd").click(function () {
        var treeObj=$.fn.zTree.getZTreeObj("treeDemo");
        nodes=treeObj.getCheckedNodes(true);
        checkId = "";
        for(var i=0; i<nodes.length; i++){
            checkId += nodes[i].id + ",";
        };
        console.log(checkId);
        var params = {
            roleName: $("#inputRoleName").val(),
            permissionIds: checkId.substr(0,checkId.length-1),
            mark: $("#inputRolePs").val(),
        };
        $.ajax({
            "type": "post",
            "url": api.roleAdd,
            "data": params,
            "success": function (result) {
                if (result.code === constant.value.success_code) {
                    swal({
                        title: "添加成功",
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

    });
    $("#btn_roleUpdate").click(function () {
        var treeObj=$.fn.zTree.getZTreeObj("treeDemo");
        nodes=treeObj.getCheckedNodes(true);
        checkId = "";
        for(var i=0; i<nodes.length; i++){
            checkId += nodes[i].id + ",";
        };
        console.log(checkId);
        var params = {
            roleId: $("#roleId").val(),
            roleName: $("#inputRoleName").val(),
            permissionIds: checkId.substr(0,checkId.length-1),
            mark: $("#inputRolePs").val(),
        };
        $.ajax({
            "type": "post",
            "url": api.roleEdit,
            "data": params,
            "success": function (result) {
                if (result.code === constant.value.success_code) {
                    swal({
                        title: "修改成功",
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

    });
    initParentPower();
    initModal();
    return {};
})
;