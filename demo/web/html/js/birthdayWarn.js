/**
 * Created by shine on 2017/2/21.
 */
/**
 * Created by liuhanhui on 2017/1/4.
 */
requirejs.config({
    baseUrl: '',
    paths: {
        datatables2: "http://cdn.datatables.net/1.10.13/js/jquery.dataTables.min"
    }
});
define(['/web/html/js/api.js', '/web/html/js/constant.js', '/web/js/bootstrap.js', 'datatables2', '/web/js/jquery.slimscroll.min.js', '/web/js/sweetalert.min.js', '/web/js/bootstrap-datetimepicker.js', '/web/html/js/areautil.js'], function (api, constant, js1, js2, js3, js4, js5, areautil) {
    $("#dateFrom").datetimepicker({
        format: "yyyy-mm-dd hh:ii:ss",
    });
    $("#dateTo").datetimepicker({
        format: "yyyy-mm-dd hh:ii:ss",
    });
    $("#msgsenddate").datetimepicker({
        format: "yyyy-mm-dd hh:ii:ss",
    });
    $("#giftsenddate").datetimepicker({
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
                "customId":$("#customId").val(),
                "customName":$("#customName").val(),
                "tel":$("#tel").val(),
                "month":$("#startDate").val(),
                "day":$("#endDate").val(),
                "sex":$("#sex option:selected").val(),
                "pageSize": displayLength,
                "pageNum": (displayStart / displayLength) + 1

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
        "sAjaxSource": api.birthdayWarn,
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
                "data": "id",
                "orderable": false
            },
            {
                "targets": [2],
                "data": "name",
                "orderable": false
            },
            {
                "targets": [3],
                "data": "phone",
                "orderable": true
            },
            {
                "targets": [4],
                "data": "sex",
                "orderable": false,
                "render": function (data, type, full, meta) {
                    if (data === 1) {
                        return "男";
                    }
                    else if (data == 0) {
                        return "女";
                    }
                }
            },
            {
                "targets": [5],
                "data": "day",
                "orderable": false,
                "render": function (data, type, full, meta) {
                    // return new Date(data).toLocaleDateString();
                    var birth = full.month + "月" + data + "日";
                    return birth;
                }
            },{
                "targets": [6],
                "data": "id",
                "orderable": false,
                "render": function (data, type, full, meta) {
                    // full取省市区
                    console.log(full);
                    var provinceId = full.vip_province_id;
                    var cityId = full.vip_city_id;
                    var countryId = full.vip_district_id;

                    if (provinceId === "" || provinceId === undefined) {
                        return "--";
                    }

                    if (cityId === "" || cityId === undefined) {
                        return "--";
                    }

                    if (countryId === "" || countryId === undefined) {
                        return "--";
                    }

                    var provinceList = areautil.getProvince();
                    var provinceName = areautil.getAreaName(provinceList, provinceId);

                    var cityList = areautil.getCity(provinceId);
                    var cityName = areautil.getAreaName(cityList, cityId);

                    var countryList = areautil.getCountry(provinceId, cityId);
                    var countryName = areautil.getAreaName(countryList, countryId);

                    // var detailAddress = full.detail_address;

                    return provinceName + cityName + countryName;
                }
            }
            // {
            //     "targets": [6],
            //     "data": "id",
            //     "orderable": false,
            //     "render": function (data, type, full, meta) {
            //         var provinceId = full.vip_province_id;
            //         var cityId = full.vip_city_id;
            //         var countryId = full.vip_district_id;
            //         return areautil.getFullAddress(provinceId, cityId, countryId);
            //     }
            // }
        ]
    });
    $('input[type="checkbox"].flat-red, input[type="radio"].flat-red').iCheck({
        checkboxClass: 'icheckbox_flat-green',
        radioClass: 'iradio_flat-green'
    });
    $("#msg_btn").click(function () {
        var customIdArray= getCheckedData();
        if (customIdArray === ""){
            swal({
                title: "请至少选择一条记录",
                type: "error"
            });
            return;
        };
        $("#doMessageAll_btn").hide();
        $("#doMessage_btn").show();
        $("#myModalMsg").modal("show");
    });
    $("#msg_all_btn").click(function () {
        $("#doMessageAll_btn").show();
        $("#doMessage_btn").hide();
        $("#myModalMsg").modal("show");
    });
    $("#doMessageAll_btn").click(function () {
        var url = api.sendMsgByFilterId;
        var msgName = $("#messageName").val();
        var msgContent =  $("#messageContent").val();
        var sendType= $("input[name='msgSendType']:checked").val();
        var date = $("#msgdatebody").find("input").val();
        var params = {
            "msgName":msgName,
            "msgContent":msgContent,
            "sendType":sendType,
            "date":date,
            "customId":$("#customId").val(),
            "customName":$("#customName").val(),
            "tel":$("#tel").val(),
            "startDate":$("#startDate").val(),
            "endDate":$("#endDate").val(),
            "sex":$("#sex option:selected").val(),
        };
        $.ajax({
            type : "post",
            url:url,
            data : params,
            success : function(resp){
                resp = JSON.parse(resp);
                console.log(resp);
                if (resp.code === constant.value.success_code) {
                    swal({
                        title: "发送成功",
                        type: "success"
                    });
                    clearMsgModalInput();
                    $("#myModalMsg").modal("hide");
                    return;
                }else {
                    swal({
                        title: resp.msg,
                        type: "error"
                    });
                };
            }
        });
    });
    $("#clsMessage_btn").click(function () {
        $("#myModalMsg").modal("hide");
    });
    $("#doMessage_btn").click(function(){
        var customIdArray= getCheckedData();
        if (customIdArray === ""){
            swal({
                title: "请至少选择一条记录",
                type: "error"
            });
            return;
        };
        var msgName = $("#messageName").val();
        var msgContent =  $("#messageContent").val();
        var sendType= $("input[name='msgSendType']:checked").val();
        var date = $("#msgdatebody").find("input").val();
        $.ajax({
            type : "post",
            url :api.sendMsg,
            data : {
                msgName:msgName,
                msgContent:msgContent,
                sendType:sendType,
                date:date,
                customIdArray:customIdArray
            },
            success : function(resp){
                resp = JSON.parse(resp);
                console.log(resp);
                if (resp.code === constant.value.success_code) {
                    swal({
                        title: "发送成功",
                        type: "success"
                    });
                    clearMsgModalInput();
                    $("#myModalMsg").modal("hide");
                    return;
                }else {
                    swal({
                        title: resp.msg,
                        type: "error"
                    });
                };
            }
        });
    });

    $("#doGift_btn").click(function(){
        var customIdArray= getCheckedData();
        if (customIdArray === ""){
            swal({
                title: "请至少选择一条记录",
                type: "error"
            });
            return;
        };
        if(!$("#giftName").val()){
            sweetAlert("奖励名称不能为空");
            return;
        }
        if(!$("#jifenGift").val()){
            sweetAlert("积分数量不能为空");
            return;
        }
        var customIdArray="";
        $('input[name="checkboxIds"]:checked').each(function(){
            customIdArray+=($(this).val()+",");
        });
        customIdArray=customIdArray.substring(0,customIdArray.length-1);
        $.ajax({
            type : "post",
            url :api.sendAward,
            data : {
                customIdArray:customIdArray,
                awardName:$("#giftName").val(),
                sendType:$("input[name='giftSendType']:checked").val(),
                amount:$("#jifenGift").val(),
                couponGroupId:$("#awardSelect").val(),
                sendAwardDate:$("#giftbody").find("input").val()
            },
            success : function(data){
                console.log(data);
                $("#myModalGift").modal("hide");

            }
        });
    });
    $('input[name="msgSendType"]').on('ifChecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定
        var value = event.target.value;
        if (value === "0") {
            $("#msgDate").hide();
        }
        else if (value === "1") {
            $("#msgDate").show();
        }
    });
    $("#gift_btn").click(function () {
        $("#myModalGift").modal("show");
    });
    $("#clsGift_btn").click(function () {
        $("#myModalGift").modal("hide");
    });
    $("#search_btn").click(function(){
        $("#example2").DataTable().ajax.reload();
    });
    $('input[name="giftSendType"]').on('ifChecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定
        var value = event.target.value;
        if (value === "0") {
            $("#giftDate").hide();
        }
        else if (value === "1") {
            $("#giftDate").show();
        }
    });
    var clearMsgModalInput = function () {
        $("#messageName").val("");
        $("#messageContent").val("");
        $("input[name='msgSendType']").eq(0).iCheck("check");
        $("#msgdatebody").find("input").val("");
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
    return {};
});
