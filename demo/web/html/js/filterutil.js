/**
 * Created by HeartZeus on 2017/2/23.
 */
define(['/web/html/js/constant.js'], function (constant) {
    var operationSelect = function (dataType) {
        var html = "";
        if (dataType == constant.DataType.STRING) {
            html += '<option value="7">包含</option>';
            html += '<option value="1">等于</option>';
        } else if (dataType == constant.DataType.INT) {
            html += '<option value="1">==</option>';
            html += '<option value="4">>=</option>';
            html += '<option value="3"><=</option>';
            html += '<option value="2">!=</option>';
        } else if (dataType == constant.DataType.DATE) {
            html += '<option value="1">==</option>';
            html += '<option value="4">>=</option>';
            html += '<option value="3"><=</option>';
            html += '<option value="2">!=</option>';
        } else {
            html += '<option value="1">等于</option>';
        };
        return html;
    };
    var enumSelect = function (enumName) {
        var enums = constant[enumName];
        var html = "";
        for (var i = 0 ; i < enums.length; i ++) {
            html += '<option value="' +enums[i].value+ '">' +enums[i].name+ '</option>';
        };
        return html;
    };

    var genaratorHtml = function(currentAmount, filterItem, isMinModel) {
        var dataType = filterItem.dataType;
        var html = '';
        if (dataType == "T_AREA") {
            html += '<div id = "value_' + currentAmount + '" style="margin-left: 0px;margin-right: 0px;">';
            html += '    <div class="col-sm-3">';
            html += '       <div class="form-group">';
            html += '           <select class="form-control select2" id="provinceSelectAdd' + currentAmount + '">';
            html += '               </select>';
            html += '       </div>';
            html += '    </div>';
            html += '    <div class="col-sm-3 col-sm-offset-1" style="">';
            html += '       <div class="form-group">';
            html += '           <select class="form-control select2" id="citySelectAdd' + currentAmount + '">';
            html += '           </select>';
            html += '       </div>';
            html += '    </div>';
            html += '    <div class="col-sm-4 col-sm-offset-1" style="">';
            html += '       <div class="form-group">';
            html += '           <select class="form-control select2" id="countrySelectAdd' + currentAmount + '">';
            html += '           </select>';
            html += '       </div>';
            html += '    </div>';
            html += '</div>';
        }
        else {
            if (dataType == "INT") {
                html += '<input type="number" id="value_' + currentAmount + '" name="value_' + currentAmount + '" class="form-control col-sm-2" >';
            }
            else if (dataType == "BOOLEAN") {
                html += '           <select id="value_' + currentAmount + '" name="value_' + currentAmount + '" class="form-control select2">';
                html += '<option value="1">是</option>';
                html += '<option value="0">否</option>';
                html += '           </select>';
            }
            else if (dataType == "DATE") {
                html += '<input type="date" id="value_' + currentAmount + '" name="value_' + currentAmount + '" class="form-control col-sm-2">';
            }
            else {
                html += '<input type="text" id="value_' + currentAmount + '" name="value_' + currentAmount + '" class="form-control col-sm-2">';
            }
        }
        return html;

    };

    return {
        operationSelect: operationSelect,
        enumSelect: enumSelect,
        genaratorHtml:genaratorHtml,
    };
});