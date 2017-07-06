/**
 * Created by liuhanhui on 2017/1/4.
 */
requirejs.config({
    baseUrl: '',
    paths: {
        datatables2: "http://cdn.datatables.net/1.10.13/js/jquery.dataTables.min"
    }
});
define(['/web/html/js/api.js', '/web/html/js/constant.js', '/web/js/bootstrap.js', 'datatables2', '/web/js/jquery.slimscroll.min.js', '/web/js/sweetalert.min.js', 'js/filterutil.js', 'js/areautil.js'], function (api, constant, js1, js2, js3, js4, filterutil, areautil) {
    var AutoFilterItem;

    var FilterId;
    var AllCustomer = true;
    var FilterItemAmount = 0;
    var FilterItemArray = "";

    var placeProvinceData = function () {
        var provinceData = areautil.getProvince();
        var html = "";
        for(var i = 0 ; i < provinceData.length; i ++) {
            html += '<option value="' +provinceData[i].value+ '">' + provinceData[i].text +'</option>';
        }
        $("#provinceSelectAdd").html(html);
    };

    $("#provinceSelectAdd").change(function(){
        placeCityData();
        placeCountryData();
    });

    var placeCityData = function () {
        var provinceId = $("#provinceSelectAdd").val();
        var cityData = areautil.getCity(provinceId);
        var html = "";
        for(var i = 0 ; i < cityData.length; i ++) {
            html += '<option value="' +cityData[i].value+ '">' + cityData[i].text +'</option>';
        }
        $("#citySelectAdd").html(html);
    };

    $("#citySelectAdd").change(function () {
        placeCountryData();
    });

    var placeCountryData = function () {
        var cityId =  $("#citySelectAdd").val();
        var provinceId = $("#provinceSelectAdd").val();
        var html = "";
        var countryData = areautil.getCountry(provinceId, cityId);
        for(var i = 0 ; i < countryData.length; i ++) {
            html += '<option value="' +countryData[i].value+ '">' + countryData[i].text +'</option>';
        }
        $("#countrySelectAdd").html(html);
    };

    //lll
    var placeProvinceEditData = function () {
        var provinceData = areautil.getProvince();
        var html = "";
        for(var i = 0 ; i < provinceData.length; i ++) {
            html += '<option value="' +provinceData[i].value+ '">' + provinceData[i].text +'</option>';
        }
        $("#provinceSelectEdit").html(html);
    };

    $("#provinceSelectEdit").change(function(){
        placeCityEditData();
        placeCountryEditData();
    });

    var placeCityEditData = function () {
        var provinceId = $("#provinceSelectEdit").val();
        var cityData = areautil.getCity(provinceId);
        var html = "";
        for(var i = 0 ; i < cityData.length; i ++) {
            html += '<option value="' +cityData[i].value+ '">' + cityData[i].text +'</option>';
        }
        $("#citySelectEdit").html(html);
    };

    $("#citySelectEdit").change(function () {
        placeCountryEditData();
    });

    var placeCountryEditData = function () {
        var cityId =  $("#citySelectEdit").val();
        var provinceId = $("#provinceSelectEdit").val();
        var html = "";
        var countryData = areautil.getCountry(provinceId, cityId);
        for(var i = 0 ; i < countryData.length; i ++) {
            html += '<option value="' +countryData[i].value+ '">' + countryData[i].text +'</option>';
        }
        $("#countrySelectEdit").html(html);
    };


    var listAllFilter = function () {
        var url = api.customAllFilter;
        $.ajax({
            "type": "get",
            "contentType": "application/json",
            "url": url,
            "data": {
                "filterType": constant.FilterType.INVEST,
            },
            "success": function (resp) {
                resp = JSON.parse(resp);
                var html = "";
                for(var i = 0 ; i < resp.data.length; i ++) {
                    var ele = resp.data[i];
                    html += '<button type="button" class="btn btn-info" data-toggle="'+ele.id+'">' + ele.name + '</button>';
                }
                html += '<button type="button" class="btn bg-maroon btn-flat">自定义</button>';
                $("#filterGroup").html(html);
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

    $("#filterGroup").on("click", "button", function () {
        var data = $(this).attr("data-toggle");
        if (data === undefined) {
            //自定义按钮
            getAutoFilterItem();
            $("#myModalDIY").modal("show");
        }else {
            FilterId = data;
            $("#example2").DataTable().ajax.reload();
        }
    });
    var getAutoFilterItem = function () {
        var url = api.customAutoFilter;
        $.ajax({
            "type": "get",
            "contentType": "application/json",
            "url": url,
            "data": {
                "filterType": constant.FilterType.INVEST,
            },
            "success": function (resp) {
                resp = JSON.parse(resp);
                AutoFilterItem = resp.data;
            }
        });
    };

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
                "filterId": FilterId,
                "pageSize": displayLength,
                "pageNum": (displayStart / displayLength) + 1,
            },
            "success": function (resp) {
                resp = JSON.parse(resp);
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
        "sAjaxSource": api.customMsg,
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
                "orderable": false
            },
            {
                "targets": [4],
                "data": "sex",
                "orderable": false,
                "render": function (data, type, full, meta) {
                    if (data === 1) {
                        return "男";
                    }
                    else if (data === 0) {
                        return "女";
                    }
                }
            },
            {
                "targets": [5],
                "data": "invitate_num",
                "orderable": false
            },{
                "targets": [6],
                "data": "day",
                "orderable": false,
                "render": function (data, type, full, meta) {
                    // return new Date(data).toLocaleDateString();
                    var birth = full.month + "月" + data + "日";
                    return birth;
                }
            },{
                "targets": [7],
                "data": "custom_type",
                "orderable": false,
                "render": function (data, type, full, meta) {
                    if (data === 1) {
                        return "复肥客户";
                    }
                    else if (data === 2) {
                        return "非复肥客户";
                    }
                }

            },{
                "targets": [8],
                "data": "vip_invest_infos[0].day_invest_balance",//!
                "orderable": false,
                "render": function (data, type, full, meta) {
                    data = data/100;
                    return data;

                }
            },{
                "targets": [9],
                "data": "vip_invest_infos[0].day_account_balance",//!
                "orderable": false,
                "render": function (data, type, full, meta) {
                    data = data/100;
                    return data;

                }
            },{
                "targets": [10],
                "data": "vip_invest_infos[0].day_add_invest_amount",//!
                "orderable": false,
                "render": function (data, type, full, meta) {
                    data = data/100;
                    return data;

                }
            },{
                "targets": [11],
                "data": "vip_invest_infos[0].total_invest_times",//!
                "orderable": false
            },{
                "targets": [12],
                "data": "vip_invest_infos[0].acc_inv_amount",//!
                "orderable": false,
                "render": function (data, type, full, meta) {
                    data = data/100;
                    return data;

                }
            },{
                "targets": [13],
                "data": "vip_invest_infos[0].acc_inv_year_amount",//!
                "orderable": false,
                "render": function (data, type, full, meta) {
                    data = data/100;
                    return data;

                }
            },{
                "targets": [14],
                "data": "vip_invest_infos[0].acc_invi_invest_persons",//!
                "orderable": false
            },{
                "targets": [15],
                "data": "vip_invest_infos[0].acc_invi_invest_amount",//!
                "orderable": false,
                "render": function (data, type, full, meta) {
                    data = data/100;
                    return data;

                }
            },{
                "targets": [16],
                "data": "vip_invest_infos[0].acc_inv_year_amount",//!
                "orderable": false,
                "render": function (data, type, full, meta) {
                    data = data/100;
                    return data;

                }
            },{
                "targets": [17],
                "data": "vip_invest_infos[0].vip_type",//!
                "orderable": false,
                "render": function (data, type, full, meta) {
                    if (data === '1') {
                        return "长线客户";
                    }
                    else if (data === '2') {
                        return "短线客户";
                    }
                    else if (data === '3') {
                        return "中线客户";
                    }
                    else if (data === '4') {
                        return "中长线客户";
                    }
                    else {
                        return "暂无数据";
                    }
                }

            },
        ]
    });
    function retrieveData(sSource, aoData, fnCallBack) {
        var aoDataLength = aoData.length;
        var sEcho = aoData[0].value;
        var displayStart = aoData[3].value;
        var displayLength = aoData[4].value;
        var sortNameIndex = aoData[aoDataLength - 3].value * 2 + 5;
        var sortName = aoData[sortNameIndex].value;
        var sortType = aoData[aoDataLength - 2].value == "asc" ? constant.value.sort_asc : constant.value.sort_desc;
        $.ajax({
            "type": "get",
            "contentType": "application/json",
            "url": sSource,
            "data": {
                "filterType": constant.FilterType.INVEST,
            },
            "success": function (resp) {
                resp = JSON.parse(resp);
                var result = {
                    "sEcho": sEcho,
                    "aaData": resp.data
                };
                fnCallBack(result);
            }
        });
    }
    var table = $('#example').DataTable({
        "paging": false,
        // "iDisplayLength": 10,
        "lengthChange": false,
        "searching": false,
        "ordering": true,
        "info": false,
        "autoWidth": false,
        "bServerSide": true,
        "sAjaxSource": api.customAllFilter,
        "fnServerData": retrieveData,
        "columnDefs": [
            {
                "defaultContent": "",
                "targets": "_all"
            },
            {
                "targets": [0],
                "data": "name"
            },
            {
                "targets": [1],
                "data": "sort",
                "orderable": false
            },
            {
                "targets": [2],
                "data": "id",
                "orderable": false,
                "render": function (value, type, full, meta) {
                    // return '<div> <button class="btn btn-warning" data-toggle="' + value + '">编辑</button> <button class="btn btn-danger" data-toggle="' + value + '">删除 </button></div>';
                    return '<div><button class="btn btn-danger" data-toggle="' + value + '">删除</button></div>';
                }
            }
        ]
    });

    $("#example").on("click", ".btn-danger", function () {
        var id = $(this).attr("data-toggle");
        var url = api.customFilterDelete;
        $.ajax({
            "type": "get",
            "contentType": "application/json",
            "url": url,
            "data": {
                "filterId": id
            },
            "success": function (resp) {
                resp = JSON.parse(resp);
                if (resp.code === constant.value.success_code) {
                    swal({
                        title: "删除成功",
                        type: "success"
                    });
                    $("#example").DataTable().ajax.reload();
                    listAllFilter();
                }else {
                    swal({
                        title: resp.msg,
                        type: "error"
                    });
                }
            }
        });
    });
    $("#example").on("click", ".btn-warning", function () {
        var id = $(this).attr("data-toggle");
        var url = api.customAllFilterItem;
        $.ajax({
            "type": "get",
            "contentType": "application/json",
            "url": url,
            "data": {
                "filterId": id
            },
            "success": function (resp) {
                resp = JSON.parse(resp);
                if (resp.code === constant.value.success_code) {

                }else {
                    swal({
                        title: resp.msg,
                        type: "error"
                    });
                }
            }
        });
    });

    $('input[type="checkbox"].flat-red, input[type="radio"].flat-red').iCheck({
        checkboxClass: 'icheckbox_flat-green',
        radioClass: 'iradio_flat-green'
    });
    $('input[name="conditionfilter"]').on('ifChecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定
        var value = event.target.value;
        if (value === "0") {
            AllCustomer = true;
            $("#condition").hide();
        }
        else if (value === "1") {
            AllCustomer = false;
            $("#condition").show();
        }
    });
    $("#addItem_btn").click(function () {
        if (AutoFilterItem === undefined || AutoFilterItem.length === 0) {
            swal({
                title: "没有过滤条件",
                type: "error"
            });
            return;
        }
        var html = '<div class="row" style="margin-top: 10px;margin-left: 0px;">';
        html += '   <div class="col-sm-2">';
        html += '       <div class="form-group">';
        html += '           <select id="luoji_' + FilterItemAmount + '" class="form-control select2">';
        html += '               <option value="1">并且</option>';
        html += '               <option value="2">或者</option>';
        html += '           </select>';
        html += '       </div>';
        html += '   </div>';
        html += '   <div class="col-sm-2" style="margin-left: 10px;">';
        html += '       <div class="form-group">';
        html += '           <select name="" id="filterKey_' +FilterItemAmount+ '" onchange="customer.filterItemChange(this);" data-toggle="' + FilterItemAmount + '" class="form-control select2">';
        for (var i= 0 ; i < AutoFilterItem.length; i ++) {
            var item = AutoFilterItem[i];
            html += '               <option value="' + item.key+ '">'+item.name+'</option>'
        };
        html += '           </select>';
        html += '       </div>';
        html += '   </div>';
        html += '   <div class="col-sm-2" style="margin-left: 10px;">';
        html += '       <div class="form-group">';
        html += '           <select id="operation_' + FilterItemAmount + '" name="operation_' + FilterItemAmount + '" class="form-control select2">';
        html += filterutil.operationSelect(AutoFilterItem[0].dataType);
        html += '           </select>';
        html += '       </div>';
        html += '   </div>';
        html += '   <div class="col-sm-2" style="margin-left: 10px;">';
        html += '       <div class="form-group" id="valueDiv_'+FilterItemAmount+'">';
        if (AutoFilterItem[0].enumName === undefined) {
            html += '<input type="text" id="value_' + FilterItemAmount + '" name="value_' +FilterItemAmount+ '" class="form-control col-sm-2">';
        }
        else {
            html += '           <select id="value_' + FilterItemAmount + '" name="value_' +FilterItemAmount+ '" class="form-control select2">';
            html += filterutil.enumSelect(AutoFilterItem[0].enumName);
            html += '           </select>';
        }
        html += '       </div>';
        html += '   </div>';
        html += '<div class="col-sm-2"><button class="btn btn-warning" data-toggle="' + FilterItemAmount +'">删除</button></div>';
        html += '</div>';
        $("#conditionbody").append(html);
        FilterItemArray += FilterItemAmount + ","
        FilterItemAmount ++;
    });
    var filterItemChange = function (params) {
        var currentAmount = $(params).attr("data-toggle");
        var dataKey = $(params).val();
        var filterItem;
        for (var i= 0 ; i < AutoFilterItem.length; i ++) {
            if (AutoFilterItem[i].key === dataKey) {
                filterItem = AutoFilterItem[i];
                break;
            }
        }
        $("#operation_" + currentAmount).html(filterutil.operationSelect(filterItem.dataType));
        var valueHtml = "";
        if (filterItem.enumName === undefined) {
            valueHtml += '<input type="text" id="value_' + currentAmount + '" name="value_' +currentAmount+ '" class="form-control col-sm-2">';
        }
        else {
            valueHtml += '           <select id="value_' + currentAmount + '" name="value_' +currentAmount+ '" class="form-control select2">';
            valueHtml += filterutil.enumSelect(filterItem.enumName);
            valueHtml += '           </select>';
        }
        $("#value_" + currentAmount).parent().html(valueHtml);
    };
    $("#addCondition_btn").click(function () {
        $("#showcondition").hide();
        $("#saveCondition_btn").show();
        $("#addCondition_btn").hide();
        $("#editcondition").show();
    });
    $("#saveCondition_btn").click(function(){
        var url = api.customSaveFilter;
        var items = "";
        if(!$("#conditionname").val()){
            sweetAlert("名称必填");
            return;
        }
        if(!$("#conditionsort").val()){
            sweetAlert("排序号必填");
            return;
        }
        if (!AllCustomer) {
            var ids = FilterItemArray.substr(0, FilterItemArray.length-1).split(",");
            for (var i = 0 ; i < ids.length; i ++) {
                items += $("#luoji_" + ids[i]).val() + "!";
                items += $("#filterKey_" + ids[i]).val() + "!";
                items += $("#operation_" + ids[i]).val() + "!";
                items += $("#value_" + ids[i]).val() + ",";
            }
            items = items.substr(0, items.length-1);
        }
        $.ajax({
            "type": "get",
            "contentType": "application/json",
            "url": url,
            "data": {
                "filterType": constant.FilterType.INVEST,
                "filterName": $("#conditionname").val(),
                "sort": $("#conditionsort").val(),
                "items":items,
            },
            "success": function (resp) {
                resp = JSON.parse(resp);
                if (resp.code === constant.value.success_code) {
                    AutoFilterItem =resp.data;
                    $("#saveCondition_btn").hide();
                    $("#addCondition_btn").show();
                    $("#myModalDIY").modal("hide");
                    swal({
                        title: "新增成功",
                        type: "success"
                    });
                    listAllFilter();
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
    $("#clsCondition_btn").click(function () {
        $("#myModalDIY").modal("hide");
        $("#showcondition").show();
        $("#editcondition").hide();
        clearDiyModalInput();
    });
    $("#conditionbody").on("click",".btn-warning", function () {
        var temp = $(this).attr("data-toggle") + ",";
        FilterItemArray = FilterItemArray.replace(temp , "");
        $(this).parent().parent().remove();
    });
    $("#search_btn").click(function () {
        $("#myModalSearch").modal("show");
    });
    $("#clsSearch_btn").click(function () {
        $("#myModalSearch").modal("hide");
    });
    $("#add_btn").click(function () {
        placeProvinceData();
        placeCityData();
        placeCountryData();
        $("#myModalAdd").modal("show");
    });
    $("#edit_btn").click(function () {
        var checkboxIds = getCheckedData();
        if (checkboxIds === "" || checkboxIds.split(",").length != 1 ){
            swal({
                title: "请选择一条记录",
                type: "error"
            });
            return;
        }
        $.ajax({
            url: api.vipDetail,
            type: "get",
            data: {"id": checkboxIds},
            async: false,
            success: function (result) {
                result = JSON.parse(result);
                if (result.code === constant.value.success_code) {
                    console.log(result);
                    placeProvinceEditData();
                    var data = result.data;
                    $("#customNameEdit").val(data.name),
                        $("#customIdEdit").val(data.id),
                        $("#customTypeEdit").val(data.custom_type),
                        $("#customPhoneEdit").val(data.phone),
                        $("#sexEdit").val(data.sex),
                        $("#customBirthDayEdit").val(data.birthday),
                        $("#provinceSelectEdit").val(data.vip_province_id),
                        placeCityEditData();
                    $("#citySelectEdit").val(data.vip_city_id),
                        placeCountryEditData();
                    $("#countrySelectEdit").val(data.vip_district_id),
                        $("#detailAddressEdit").val(data.detail_address),
                        $("#dayInvestBalanceEdit").val(data.vip_invest_infos[0].day_invest_balance),
                        $("#dayAccountBalanceEdit").val(data.vip_invest_infos[0].day_account_balance),
                        $("#dayAddInvestAmountEdit").val(data.vip_invest_infos[0].day_add_invest_amount),
                        $("#accInvestAmountEdit").val(data.vip_invest_infos[0].acc_inv_amount),
                        $("#accInvestYearAmountEdit").val(data.vip_invest_infos[0].acc_inv_year_amount),
                        $("#accInviInvestPersonsEdit").val(data.vip_invest_infos[0].acc_invi_invest_persons),
                        $("#totalInvestTimesEdit").val(data.vip_invest_infos[0].total_invest_times),
                        $("#accInviInvestAmountEdit").val(data.vip_invest_infos[0].acc_invi_invest_amount),
                        $("#accInviInvestAmountYearEdit").val(data.vip_invest_infos[0].acc_invi_invest_amount_year);
                    $("#myModalEdit").modal("show");
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
    $("#clsEdit_btn").click(function () {
        clearEditModalInput();
        $("#myModalEdit").modal("hide");
    });
    $("#doEdit_btn").click(function () {
        var url = api.vipEdit;
        $.ajax({
            "type": "get",
            "contentType": "application/json",
            "url": url,
            "data": {
                "id": $("#customIdEdit").val(),
                "provinceId":$("#provinceSelectEdit").val(),
                "cityId": $("#citySelectEdit").val(),
                "districtId":$("#countrySelectEdit").val(),
                "detailAddress": $("#detailAddressEdit").val()
            },
            "success": function (resp) {
                resp = JSON.parse(resp);
                if (resp.code === constant.value.success_code) {
                    $("#myModalEdit").modal("hide");
                    clearEditModalInput();
                    swal({
                        title: "修改成功",
                        type: "success"
                    });
                    $("#example").DataTable().ajax.reload();
                }else {
                    swal({
                        title: resp.msg,
                        type: "error"
                    });
                }
            }
        });
    });
    $("#dele_btn").click(function () {
        var checkboxIds = getCheckedData();
        if (checkboxIds === ""){
            swal({
                title: "请至少选择一条记录",
                type: "error"
            });
            return;
        }
        $.ajax({
            url: api.vipDel,
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
    $("#clsAdd_btn").click(function () {
        clearAddModalInput();
        $("#myModalAdd").modal("hide");
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
    $("#clsMessage_btn").click(function () {
        $("#myModalMsg").modal("hide");
    });
    $("#doAdd_btn").click(function () {
        var url = api.vipAdd;
        $.ajax({
            "type": "get",
            "contentType": "application/json",
            "url": url,
            "data": {
                "userName": $("#customNameAdd").val(),
                "id": $("#customIdAdd").val(),
                "customType": $("#customTypeAdd").val(),
                "phone": $("#customPhoneAdd").val(),
                "sex": $("#sexAdd").val(),
                "birthday": $("#customBirthDayAdd").val(),
                "province":$("#provinceSelectAdd").val(),
                "city": $("#citySelectAdd").val(),
                "district":$("#countrySelectAdd").val(),
                "detailAddr": $("#detailAddressAdd").val(),
                "dayInvestBalance": $("#dayInvestBalanceAdd").val(),
                "dayAccountBalance":$("#dayAccountBalanceAdd").val(),
                "dayAddInvestAmount": $("#dayAddInvestAmountAdd").val(),
                "accInvestAmount": $("#accInvestAmountAdd").val(),
                "accInvestYearAmount": $("#accInvestYearAmountAdd").val(),
                "totalInvestTimes": $("#totalInvestTimesAdd").val(),
                "accInviInvestPersons":$("#accInviInvestPersonsAdd").val(),
                "accInviInvestAmount":$("#accInviInvestAmountAdd").val(),
                "accInviInvestAmountYear":$("#accInviInvestAmountYearAdd").val()
            },
            "success": function (resp) {
                resp = JSON.parse(resp);
                if (resp.code === constant.value.success_code) {
                    $("#myModalAdd").modal("hide");
                    clearAddModalInput();
                    swal({
                        title: "添加成功",
                        type: "success"
                    });
                    $("#example2").DataTable().ajax.reload();
                }else {
                    swal({
                        title: resp.msg,
                        type: "error"
                    });
                }
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
            "filterId":FilterId,
            "msgName":msgName,
            "msgContent":msgContent,
            "sendType":sendType,
            "date":date,
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
    $('input[name="giftSendType"]').on('ifChecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定
        var value = event.target.value;
        if (value === "0") {
            $("#giftDate").hide();
        }
        else if (value === "1") {
            $("#giftDate").show();
        }
    });

    var clearAddModalInput = function () {
        $("#customNameAdd").val("");
        $("#customIdAdd").val("");
        $("#customTypeAdd").val("");
        $("#customPhoneAdd").val("");
        $("#sexAdd").val("");
        $("#customBirthDayAdd").val("");
        $("#provinceSelectAdd").val("");
        $("#citySelectAdd").val("");
        $("#countrySelectAdd").val("");
        $("#detailAddressAdd").val("");
        $("#dayInvestBalanceAdd").val("");
        $("#dayAccountBalanceAdd").val("");
        $("#dayAddInvestAmountAdd").val("");
        $("#accInvestAmountAdd").val("");
        $("#accInvestYearAmountAdd").val("");
        $("#customTypeAdd").val("");
        $("#accInviInvestPersonsAdd").val("");
        $("#accInviInvestAmountAdd").val("");
        $("#accInviInvestAmountYearAdd").val("");
    };
    var clearEditModalInput = function () {
        $("#customNameEdit").val("");
        $("#customIdEdit").val("");
        $("#customTypeEdit").val("");
        $("#customPhoneEdit").val("");
        $("#sexEdit").val("");
        $("#customBirthDayEdit").val("");
        $("#provinceSelectEdit").val("");
        $("#citySelectEdit").val("");
        $("#countrySelectEdit").val("");
        $("#detailEditressEdit").val("");
        $("#dayInvestBalanceEdit").val("");
        $("#dayAccountBalanceEdit").val("");
        $("#dayEditInvestAmountEdit").val("");
        $("#accInvestAmountEdit").val("");
        $("#accInvestYearAmountEdit").val("");
        $("#customTypeEdit").val("");
        $("#accInviInvestPersonsEdit").val("");
        $("#accInviInvestAmountEdit").val("");
        $("#accInviInvestAmountYearEdit").val("");
    };
    var clearDiyModalInput = function () {
        $("#conditionname").val("");
        $("#conditionsort").val("");
        $("#conditionbody").html("");
        $('input:radio[name="conditionfilter"]').eq(0).iCheck("checked");
        // $('input:radio[name="conditionfilter"]').eq(1).removeAttr("checked");
        $("#addCondition_btn").show();
        $("#saveCondition_btn").hide();
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

    var clearMsgModalInput = function () {
        $("#messageName").val("");
        $("#messageContent").val("");
        $("input[name='msgSendType']").eq(0).iCheck("check");
        $("#msgdatebody").find("input").val("");
    };

    var init = function () {
        listAllFilter();


    };
    init();
    return {
        filterItemChange: filterItemChange
    };
});