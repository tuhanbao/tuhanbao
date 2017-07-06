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
define(['/web/html/js/api.js', '/web/html/js/constant.js', '/web/js/bootstrap.js', 'datatables2', '/web/js/jquery.slimscroll.min.js', '/web/js/sweetalert.min.js','/web/js/jquery.ztree.core.js', '/web/js/jquery.ztree.excheck.js'], function (api, constant, js1, js2, js3, js4) {
    var currentTreeNode = {
        id: 0
    };
    function initTree() {
        var setting = {
            data: {
                simpleData: {
                    enable: true
                }
            },
            callback: {
                onClick: onClick
            },
            view: {
                selectedMulti: false
            }
        };
        var zNodes =[];
        $.ajax({
            url: api.powerList,
            type: "get",
            data: {
                isContainBtn: 0
            },
            async: false,
            success: function (result) {
                console.log(result);
                if (result.code === constant.value.success_code) {
                    var data = result.data;
                    zNodes = [];
                    for(var i = 0 ; i < data.length; i ++) {
                        // var temp = data[i];
                        var node = {
                            id:data[i].id,
                            pId:data[i].parent_id,
                            name:data[i].name,
                            open: true
                        }
                        if (data[i].is_menu === 1) {
                            node['icon'] = '/css/zTreeStyle/img/diy/menu1.png';
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

            }
        });

    }
    initTree();
    function onClick(event, treeId, treeNode, clickFlag) {
        currentTreeNode = treeNode;
        $("#example1").DataTable().ajax.reload();
        $("#example2").DataTable().ajax.reload();
    }
    

    function retrieveData1(sSource, aoData, fnCallBack) {
        var sEcho = aoData[0].value;
        $.ajax({
            "type": "get",
            "contentType": "application/json",
            "url": sSource,
            "data": {
                "permissionId": currentTreeNode.id,
                "isMenu": 1,

            },
            "success": function (resp) {
                console.log(resp);
                var result = {
                    "sEcho": sEcho,
                    "iTotalRecords": resp.data.length,
                    "iTotalDisplayRecords": resp.data.length,
                    "aaData": resp.data
                }
                fnCallBack(result);
            }
        });
    }


    var table1 = $('#example1').DataTable({
        "paging": false,
        // "iDisplayLength": 10,
        "lengthChange": false,
        "searching": false,
        "ordering": false,
        "info": false,
        "autoWidth": true,
        "bServerSide": true,
        "sAjaxSource": api.powerChildren,
        "fnServerData": retrieveData1,
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
                "data": "name",
            },
            {
                "targets": [2],
                "data": "is_menu",
                "render": function (data, type, full, meta) {
                    if (data == 1) {
                        return "是";
                    }
                    else if (data == 0) {
                        return "否";
                    }
                    else {
                        return "未知";
                    }
                }
            },
            {
                "targets": [3],
                "data": "sort",
            },
            {
                "targets": [4],
                "data": "url",

            },
            {
                "targets": [5],
                "data": "parent_id",
            },
            {
                "targets": [6],
                "data": "mark",
            },
            {
                "targets": [7],
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
    function retrieveData2(sSource, aoData, fnCallBack) {
        var sEcho = aoData[0].value;
        $.ajax({
            "type": "get",
            "contentType": "application/json",
            "url": sSource,
            "data": {
                "permissionId": currentTreeNode.id,
                "isMenu": 0,

            },
            "success": function (resp) {
                console.log(resp);
                var result = {
                    "sEcho": sEcho,
                    "aaData": resp.data
                }
                fnCallBack(result);
            }
        });
    }
    var table2 = $('#example2').DataTable({
        "paging": false,
        // "iDisplayLength": 10,
        "lengthChange": false,
        "searching": false,
        "ordering": false,
        "info": false,
        "autoWidth": false,
        "bServerSide": true,
        "sAjaxSource": api.powerChildren,
        "fnServerData": retrieveData2,
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
                "data": "name",
            },
            {
                "targets": [2],
                "data": "url",
            },
            {
                "targets": [3],
                "data": "parent_id",
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

    $("#addSource").click(function () {
        if (currentTreeNode.id === 0) {
            swal({
                title: "请先选中上级菜单",
                type: "error"
            });
            return;
        }
        initParentPower();
        $("#inputPowerParent").val(currentTreeNode.name);
        $("#inputPowerParent").attr("disabled", "disabled");
        $("#inputPowerIsMenu").val("1");
        $("#inputPowerIsMenu").attr("disabled", "disabled");
        $("#btn_powerAdd").show();
        $("#btn_powerUpdate").hide();
        $("#myModal").modal("show");
    });

    $("#addBtn").click(function () {
        if (currentTreeNode.id === 0) {
            swal({
                title: "请先选中上级菜单",
                type: "error"
            });
            return;
        }
        initParentPower();
        $("#inputPowerParent").val(currentTreeNode.name);
        $("#inputPowerParent").attr("disabled", "disabled");
        $("#inputPowerIsMenu").val("0");
        $("#inputPowerIsMenu").attr("disabled", "disabled");
        $("#btn_powerAdd").show();
        $("#btn_powerUpdate").hide();
        $("#myModal").modal("show");
    });
    var initParentPower = function () {
        $("#inputPowerName").val("");
        $("#inputPowerUrl").val("");
        $("#inputPowerIsMenu").val("");
        $("#inputPowerParent").val("");
        $("#inputPowerSort").val("");
        $("#inputPowerPs").val("");
        $("#inputPowerParent").removeAttr("disabled");
        $("#inputPowerIsMenu").removeAttr("disabled");
        // $("#inputPowerParent").attr("disabled", "disabled");

    };

    $("#btn_powerAdd").click(function () {
        var params = {
            "name": $("#inputPowerName").val(),
            "parentId": currentTreeNode.id,
            "url": $("#inputPowerUrl").val(),
            "sort": $("#inputPowerSort").val(),
            "isMenu": $("#inputPowerIsMenu").val(),
            "mark": $("#inputPowerPs").val(),
            // "remark": $("#inputPowerPs").val(),
        };
        $.ajax({
            // url: api.powerAdd,
            // data: params,
            // async: false,
            // type: "post",
            "type": "post",
            "url": api.powerAdd,
            "data": params,
            "success": function (result) {
                if (result.code === constant.value.success_code) {
                    swal({
                        title: "添加成功",
                        type: "success"
                    });
                    $("#myModal").modal("hide");
                    initTree();
                } else {
                    swal({
                        title: result.msg,
                        type: "error"
                    })
                }
            }
        });
        $("#example1").DataTable().ajax.reload();
        $("#example2").DataTable().ajax.reload();
    });

    $("#btn_powerUpdate").click(function () {
        var params = {
            "id": $("#powerId").val(),
            "name": $("#inputPowerName").val(),
            "url": $("#inputPowerUrl").val(),
            "sort": $("#inputPowerSort").val(),
            "mark": $("#inputPowerPs").val(),
            // "remark": $("#inputPowerPs").val(),
        }
        $.ajax({
            // url: api.powerEdit,
            // data: params,
            // async: false,
            // type: "post",
            "type": "post",
            "url": api.powerEdit,
            "data": params,
            "success": function (result) {
                if (result.code === constant.value.success_code) {
                    swal({
                        title: "修改成功",
                        type: "success"
                    });
                    $("#myModal").modal("hide");
                    initTree();
                } else {
                    swal({
                        title: result.msg,
                        type: "error"
                    })
                }
            }
        });
        $("#example1").DataTable().ajax.reload();
        $("#example2").DataTable().ajax.reload();
    });

    function updatePower(id) {
        var params = {
            "id": id
        }
        $.ajax({
            // url: api.powerDetail,
            // data: {"id": id},
            // async: false,
            // type: "get",
            "type": "post",
            "url": api.powerDetail,
            "data": params,
            "success": function (result) {
                if (result.code === constant.value.success_code) {
                    var data = result.data;
                    $("#powerId").val(id);
                    $("#inputPowerName").val(data.name);
                    $("#inputPowerUrl").val(data.url);
                    $("#inputPowerIsMenu").val(data.is_menu);
                    $("#inputPowerParent").val(data.parent_id);
                    $("#inputPowerSort").val(data.sort);
                    $("#inputPowerPs").val(data.mark);
                    $("#inputPowerParent").attr("disabled", "disabled");
                    $("#inputPowerIsMenu").attr("disabled", "disabled");
                    $("#btn_powerAdd").hide();
                    $("#btn_powerUpdate").show();
                    $("#myModal").modal("show");
                } else {
                    swal({
                        title: result.msg,
                        type: "error"
                    })
                }
            }
        });
    };


    function deletePower(id) {
        $.ajax({
            url: api.powerDele,
            data: {permissionId: id},
            async: false,
            type: "post",
            success: function (result) {
                if (result.code === constant.value.success_code) {
                    swal({
                        title: "删除成功",
                        type: "success"
                    });
                    initTree();
                } else {
                    swal({
                        title: result.msg,
                        type: "error"
                    })
                }
            }
        });
    };

    $("#example1").on("click", ".btn-success", function () {
        updatePower($(this).attr("data-toggle"));
    });

    $("#example1").on("click", ".btn-danger", function () {
        deletePower($(this).attr("data-toggle"));
        $("#example1").DataTable().ajax.reload();
    });

    $("#example2").on("click", ".btn-success", function () {
        updatePower($(this).attr("data-toggle"));
    });

    $("#example2").on("click", ".btn-danger", function () {
        deletePower($(this).attr("data-toggle"));
        $("#example2").DataTable().ajax.reload();
    });
    // type = 1:更新 type = 2:删除

    return {};
})
;