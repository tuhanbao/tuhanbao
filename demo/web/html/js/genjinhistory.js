/**
 * Created by liuhanhui on 2017/1/4.
 */
requirejs.config({
    baseUrl: '',
    paths: {
        datatables2: "/web/js/datatables/js/jquery.dataTables"
    }
});
define(['/web/html/js/api.js', '/web/html/js/constant.js', '/web/js/bootstrap.js', 'datatables2', '/web/js/jquery.slimscroll.min.js', '/web/js/sweetalert.min.js', '/web/js/bootstrap-datetimepicker.js'], function (api, constant, js1, js2, js3, js4, js5) {
    $('#createDateStart').datetimepicker({
        // format: "yyyy-mm-dd hh:ii:ss",
        format: "yyyy-mm-dd hh:ii:ss",
    });
    $('#createDateEnd').datetimepicker({
        format: "yyyy-mm-dd hh:ii:ss",
    });
    $('#followDateStart').datetimepicker({
        format: "yyyy-mm-dd hh:ii:ss",
    });
    $('#followDateEnd').datetimepicker({
        format: "yyyy-mm-dd hh:ii:ss",
    });
    $("#followDateStartNew").datetimepicker({
        format: "yyyy-mm-dd hh:ii:ss",
    });
    $("input:checkbox[name=allChecked]").click(function () {
        if (this.checked) {
            $("input:checkbox[name=customerIds]").prop("checked","checked");
            $("input:checkbox[name=allChecked]").prop("checked","checked");
        }else {
            $("input:checkbox[name=customerIds]").removeAttr("checked");
            $("input:checkbox[name=allChecked]").removeAttr("checked");
        }
    });
    $("#example2").on("click", 'input:checkbox[name=customerIds]', function () {
        var flag=true;
        $("input:checkbox[name=customerIds]").each(function(){
            if(!this.checked){
                flag=false;
            }
        });
        if (flag) {
            $("input:checkbox[name=allChecked]").prop("checked","checked");
        }else {
            $("input:checkbox[name=allChecked]").removeAttr("checked");
        }
    });
    function retrieveData2(sSource, aoData, fnCallBack) {
        // var aoDataLength = aoData.length;
        var sEcho = aoData[0].value;
        var displayStart = aoData[3].value;
        var displayLength = aoData[4].value;
        // var sortNameIndex = aoData[aoDataLength - 3].value * 2 + 5;
        // var sortName = aoData[sortNameIndex].value;
        // var sortType = aoData[aoDataLength - 2].value == "asc" ? constant.value.sort_asc : constant.value.sort_desc;
        $.ajax({
            "type": "get",
            "contentType": "application/json",
            "url": sSource,
            "data": {
                "customId": $("#customerId").val(),
                "customName":$("#customerName").val(),
                "flowStage":$("#followStage").val(),
                "startCreateDate":$("#createDateStart").val(),
                "endCreateDate":$("#createDateEnd").val(),
                "startFollowDate":$("#followDateStart").val(),
                "endFollowDate":$("#followDateEnd").val(),
                "pageSize": displayLength,
                "pageNum": (displayStart / displayLength) + 1,

            },
            "success": function (resp) {
                resp = JSON.parse(resp);
                console.log(resp);
                var result = {
                    "sEcho": sEcho,
                    "iTotalRecords": resp.data.totalNum,
                    "iTotalDisplayRecords": resp.data.totalNum,
                    "aaData": resp.data.dataList
                };
                fnCallBack(result);
            }
        });
    };

    var table2 = $('#example2').DataTable({
        "paging": true,
        "iDisplayLength": 10,
        "lengthChange": false,
        "searching": false,
        "ordering": false,
        "info": true,
        "autoWidth": false,
        "bServerSide": true,
        "sAjaxSource": api.genjinHistory,
        "fnServerData": retrieveData2,
        "columnDefs": [
            {
                "defaultContent": "",
                "targets": "_all"
            },
            {
                "targets": [0],
                "data": "id",
                "orderable": false,
                "render": function (data, type, full, meta) {
                    var html = '<td><input name="customerIds" type="checkbox" value="' + data + '"></td>';
                    return html;
                }
            },
            {
                "targets": [1],
                "data": "custom_id",
                "orderable": false
            },
            {
                "targets": [2],
                "data": "custom.name",
                "orderable": false
            },
            {
                "targets": [3],
                "data": "create_date",
                "orderable": false,
                "render": function (data, type, full, meta) {
                    var newDate = new Date();
                    newDate.setTime(data);
                    return newDate.toLocaleString();
                }
            },
            {
                "targets": [4],
                "data": "following_date",
                "orderable": false,
                "render": function (data, type, full, meta) {
                    var newDate = new Date();
                    newDate.setTime(data);
                    return newDate.toLocaleString();
                }
            },
            {
                "targets": [5],
                "data": "following_stage",
                "orderable": false,
                "render": function (data, type, full, meta) {
                    if (data === constant.FollowingStage.FOLLOW_UP) {
                        return "初步跟进";
                    }
                    else if (data == constant.FollowingStage.DEEP_NEGOTIATION) {
                        return "深度谈判";
                    }
                    else if (data == constant.FollowingStage.NEGOTIATING_SUCCESS) {
                        return "谈判成功";
                    }
                }
            },
            {
                "targets": [6],
                "data": "contents",
                "orderable": false,
            }
        ]
    });
    $("#search_btn").click(function () {
        $("#example2").DataTable().ajax.reload();
    });
    $("#addGenjinRecord_btn").click(function () {
        $("#myModalGenjin").modal("show");
    });
    var getCheckedData = function () {
        var customerId = "";
        $('input:checkbox[name=customerIds]:checked').each(function(i){
            if(0==i){
                customerId = $(this).val();
            }else{
                customerId += (","+$(this).val());
            }
        });
        return customerId;
    };
    $("#edit_btn").click(function () {
        var customerId = getCheckedData();
        if (customerId == "" || !(customerId.split(",").length == 1)) {
            swal({
                title: "请选择一条记录",
                type: "error"
            });
            return;
        };
        $.ajax({
            url: api.genjinDetail,
            type: "get",
            data: {"id": customerId},
            async: false,
            success: function (result) {
                result = JSON.parse(result);
                if (result.code === constant.value.success_code) {
                    var data = result.data;
                    $("#customerIdNew").val(data.custom_id);
                    $("#customerIdNew").attr("disabled", "true");
                    console.log(data);
                    $("#followDateStartNew").val(new Date(data.following_date).toLocaleString('chinese',{hour12:false}).replace(new RegExp("/",'gm'),"-"));
                    $("#followStageNew").val(data.following_stage);
                    $("#genjinResult").val(data.contents);
                    $("#doGenjin_btn").hide();
                    $("#myModalGenjin").modal("show");
                    $("#updateGenjin_btn").show();

                }
                else {
                    swal({
                        title: result.msg,
                        type: "error"
                    });
                }
            }
        });

    });
    $("#updateGenjin_btn").click(function () {
        var customerId = getCheckedData();
        var params = {
            "id":customerId,
            "dateStr":$("#followDateStartNew").val(),
            "flowStage":$("#followStageNew").val(),
            "content":$("#genjinResult").val(),
        };
        $.ajax({
            url: api.genjinUpdate,
            type: "post",
            data: params,
            async: false,
            success: function (result) {
                result = JSON.parse(result);
                if (result.code === constant.value.success_code) {
                    $("#myModalGenjin").modal("hide");
                    swal({
                        title: "更新成功",
                        type: "success"
                    });
                    clearModalInput();
                    $("#updateGenjin_btn").hide();
                    $("#example2").DataTable().ajax.reload();
                }
                else {
                    swal({
                        title: result.msg,
                        type: "error"
                    });
                }
            }
        });
    });
    $("#detail_btn").click(function () {
        var customerId = getCheckedData();
        if (customerId == "" || !(customerId.split(",").length == 1)) {
            swal({
                title: "请选择一条记录",
                type: "error"
            });
            return;
        };
        $.ajax({
            url: api.genjinDetail,
            type: "get",
            data: {"id": customerId},
            async: false,
            success: function (result) {
                result = JSON.parse(result);
                if (result.code === constant.value.success_code) {
                    var data = result.data;
                    $("#customerIdNew").val(data.custom_id);
                    $("#customerIdNew").attr("disabled", "true");
                    console.log(data);
                    $("#followDateStartNew").val(new Date(data.following_date).toLocaleString());
                    $("#followDateStartNew").attr("disabled", "true");
                    $("#followStageNew").val(data.following_stage);
                    $("#followStageNew").attr("disabled", "true");
                    $("#genjinResult").val(data.contents);
                    $("#genjinResult").attr("disabled", "true");
                    $("#doGenjin_btn").hide();
                    $("#myModalGenjin").modal("show");

                }
                else {
                    swal({
                        title: result.msg,
                        type: "error"
                    });
                }
            }
        });
    });
    $("#clsGenjin_btn").click(function () {
        clearModalInput();
        $("#myModalGenjin").modal("hide");
    });
    $("#delete_btn").click(function () {
        var customerId = getCheckedData();
        if (customerId.split(",").length < 1) {
            swal({
                title: "请至少选择一条记录",
                type: "error"
            });
            return;
        };
        $.ajax({
            url: api.genjinDelete,
            type: "post",
            data: {"ids": customerId},
            async: false,
            success: function (result) {
                result = JSON.parse(result);
                if (result.code === constant.value.success_code) {
                    $("#myModalGenjin").modal("hide");
                    swal({
                        title: "删除成功",
                        type: "success"
                    });
                    $("#example2").DataTable().ajax.reload();
                }
                else {
                    swal({
                        title: result.msg,
                        type: "error"
                    });
                }
            }
        });
    });
    $("#doGenjin_btn").click(function () {
        var params = {
            "customId" : $("#customerIdNew").val(),
            "dateStr":$("#followDateStartNew").val(),
            "flowStage":$("#followStageNew").val(),
            "content":$("#genjinResult").val(),
        }
        if(!$("#customerIdNew").val()){
            sweetAlert("客户ID必填");
            return;
        }
        if(!$("#followDateStartNew").val()){
            sweetAlert("跟进时间必填");
            return;
        }
        if(!$("#genjinResult").val()){
            sweetAlert("跟进结果必填");
            return;
        }
        if(!$("#followStageNew").val()){
            sweetAlert("跟进阶段必填");
            return;
        }
        $.ajax({
            url: api.genjinAdd,
            type: "post",
            data: params,
            async: false,
            success: function (result) {
                result = JSON.parse(result);
                if (result.code === constant.value.success_code) {
                    $("#myModalGenjin").modal("hide");
                    swal({
                        title: "新增成功",
                        type: "success"
                    });
                    clearModalInput();
                    $("#example2").DataTable().ajax.reload();
                }
                else {
                    swal({
                        title: result.msg,
                        type: "error"
                    });
                }
            }
        });
    });
    var clearModalInput = function () {
        $("#customerIdNew").val("");
        $("#followDateStartNew").val("");
        $("#followStageNew").val("");
        $("#genjinResult").val("");
        $("#customerIdNew").removeAttr("disabled");
        $("#followDateStartNew").removeAttr("disabled");
        $("#followStageNew").removeAttr("disabled");
        $("#genjinResult").removeAttr("disabled");
        $("#doGenjin_btn").show();
    };
    return {};
});