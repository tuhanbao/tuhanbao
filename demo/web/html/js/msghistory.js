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
    $("#sendDateStart").datetimepicker({
        format: "yyyy-mm-dd hh:ii:ss",
    });
    $("#sendDateEnd").datetimepicker({
        format: "yyyy-mm-dd hh:ii:ss",
    });
    function retrieveData2(sSource, aoData, fnCallBack) {
        var sEcho = aoData[0].value;
        var displayStart = aoData[3].value;
        var displayLength = aoData[4].value;
        $.ajax({
            "type": "get",
            "contentType": "application/json",
            "url": sSource,
            "data": {
                "smsName": $("#msgName").val(),
                "sendVipName": $("#phone").val(),
                "status": $("#sendStatus").val(),
                "startDate": $("#sendDateStart").val(),
                "endDate": $("#sendDateEnd").val(),
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
    $("input:checkbox[name=allChecked]").click(function () {
        if (this.checked) {
            $("input:checkbox[name=checkboxIds]").prop("checked","checked");
            $("input:checkbox[name=allChecked]").prop("checked","checked");
        }else {
            $("input:checkbox[name=checkboxIds]").removeAttr("checked");
            $("input:checkbox[name=allChecked]").removeAttr("checked");
        }
    });
    $("#example2").on("click", 'input:checkbox[name=checkboxIds]', function () {
        var flag=true;
        $("input:checkbox[name=checkboxIds]").each(function(){
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
    var table2 = $('#example2').DataTable({
        "paging": true,
        "iDisplayLength": 10,
        "lengthChange": false,
        "searching": false,
        "ordering": false,
        "info": true,
        "autoWidth": false,
        "bServerSide": true,
        "sAjaxSource": api.msgHistory,
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
                    var html = '<td><input name="checkboxIds" type="checkbox" value="' + data + '"></td>';
                    return html;
                }
            },
            {
                "targets": [1],
                "data": "msg_name",
                "orderable": false
            },
            {
                "targets": [2],
                "data": "showPhone",
                "orderable": false
            },
            {
                "targets": [3],
                "data": "send_date",
                "orderable": false,
                "render": function (data, type, full, meta) {
                    var newDate = new Date();
                    newDate.setTime(data);
                    return newDate.toLocaleString();
                }

            },
            {
                "targets": [4],
                "data": "send_content",
                "orderable": false,
            }
        ]
    });

    $("#deleteBtn").click(function () {
        var checkboxIds = getCheckedData();
        if (checkboxIds.split(",").length < 1){
            swal({
                title: "请至少选择一条记录",
                type: "error"
            });
            return;
        }
        $.ajax({
            url: api.msgDelete,
            type: "post",
            data: {"ids": checkboxIds},
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

    $("#detailBtn").click(function () {
        var checkboxIds = getCheckedData();
        if (checkboxIds === "" || checkboxIds.split(",").length != 1){
            swal({
                title: "请选择一条记录",
                type: "error"
            });
            return;
        };
        $.ajax({
            url: api.msgDetail,
            type: "get",
            data: {"id": checkboxIds},
            async: false,
            success: function (result) {
                result = JSON.parse(result);
                if (result.code === constant.value.success_code) {
                    var data = result.data;
                    $("#msgNameNew").val(data.msg_name);
                    $("#phoneNew").val(data.showPhone);
                    $("#sendDateNew").val(new Date(data.send_date).toLocaleString());
                    // $("#sendStatusNew").val(data.msg.send_status);
                    $("#sendContentNew").val(data.send_content);
                    $("#msgNameNew").attr("disabled", "true");
                    $("#phoneNew").attr("disabled", "true");
                    $("#sendDateNew").attr("disabled", "true");
                    // $("#sendStatusNew").attr("disabled", "true");
                    $("#sendContentNew").attr("disabled", "true");
                    $("#myModalMsg").modal("show");
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
    $("#clsMsg_btn").click(function () {
        clearModalInput();
        $("#myModalMsg").modal("hide");
    });
    $("#search_btn").click(function () {
        $("#example2").DataTable().ajax.reload();
    });
    var clearModalInput = function () {
        $("#msgNameNew").val();
        $("#phoneNew").val();
        $("#sendDateNew").val();
        $("#sendStatusNew").val();
        $("#sendContentNew").val();
        $("#msgNameNew").removeAttr("disabled");
        $("#phoneNew").removeAttr("disabled");
        $("#sendDateNew").removeAttr("disabled");
        $("#sendStatusNew").removeAttr("disabled");
        $("#sendContentNew").removeAttr("disabled");
    };
    var getCheckedData = function () {
        var checkboxIds = "";
        $('input:checkbox[name=checkboxIds]:checked').each(function(i){
            if(0==i){
                checkboxIds = $(this).val();
            }else{
                checkboxIds += (","+$(this).val());
            }
        });
        return checkboxIds;
    };
    return {};
});