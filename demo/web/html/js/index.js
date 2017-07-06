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
    var initMenu = function () {
        var content = '<div class="panel panel-primary leftMenu">';
        content += '<div class="panel-heading" id="collapseListGroupHeading${titleId}" data-toggle="collapse" role="tab" >';
        content += '    <h4 class="panel-title ${isClick}" ${url}>';
        content += '        ${titleName}';
        content += '        <span class="glyphicon glyphicon-chevron-right right" id="span${titleId}" style="display: none;"></span>';
        content += '    </h4>';
        content += '</div>';
        content += '<div id="collapseListGroup${titleId}" class="panel-collapse collapse" role="tabpanel" aria-labelledby="collapseListGroupHeading${titleId}">';
        content += '    <ul class="list-group" id="ul${titleId}">';
        content += '    </ul>';
        content += '</div>';
        content += '</div>';
        var powerLi = '';
        powerLi += '        <li class="list-group-item">';
        powerLi += '            <button class="menu-item-left btn-menu" data-target="${powerUrl}">';
        powerLi += '                ${powerName}';
        powerLi += '            </button>';
        powerLi += '        </li>';
        var data = JSON.parse(localStorage.getItem("user")).role.permissions;
        for(var i = 0 ; i < data.length; i ++) {
            // var temp = data[i];
            if (data[i].parent_id === 0) {
                continue;
            }
            if (data[i].parent_id === 1) {
                var titleId = "#collapseListGroupHeading" + data[i].id;
                if ($(titleId).length < 1 ) {
                    console.log("新增一级菜单：" + data[i].name);
                    console.log("newTest")
                    var tempStr = content;
                    tempStr = tempStr.replace(/\$\{titleId\}/g, data[i].id);
                    tempStr = tempStr.replace(/\$\{titleName\}/g, data[i].name);

                    if (data[i].url != "") {
                        tempStr = tempStr.replace(/\$\{url\}/g, "data-target=" + data[i].url);
                        tempStr = tempStr.replace(/\$\{isClick\}/g, "btn-menu");
                    } else{
                        tempStr = tempStr.replace(/\$\{url\}/g, "");
                        tempStr = tempStr.replace(/\$\{isClick\}/g, "");
                    }
                    $("#menuList").append(tempStr);
                }
            }
            else {
                var ulId = "#ul" + data[i].parent_id;
                var spanId = "#span" + data[i].parent_id;
                var titleId = "#collapseListGroupHeading" + data[i].parent_id;
                var tempStr = powerLi;
                tempStr = tempStr.replace(/\$\{powerUrl\}/g, data[i].url);
                tempStr = tempStr.replace(/\$\{powerName\}/g, data[i].name);
                $(spanId).show();
                $(titleId).attr("data-target", "#collapseListGroup" + data[i].parent_id);
                $(ulId).append(tempStr);
            }
        }
    };
    initMenu();
    function iFrameHeight(){
        var ifm = document.getElementById("content");
        ifm.height = document.documentElement.clientHeight;
    }
    $("#menuList").on("click", '.panel-heading', function () {
        $(this).find("span").toggleClass("glyphicon-chevron-right");
        $(this).find("span").toggleClass("glyphicon-chevron-down");
    });
    $("#menuList").on("click", '.inactive', function () {
        if($(this).siblings('ul').css('display')=='none'){
            $(this).parent('li').siblings('li').removeClass('inactives');
            $(this).addClass('inactives');
            $(this).siblings('ul').slideDown(500).children('li');
            if($(this).parents('li').siblings('li').children('ul').css('display')=='block'){
                $(this).parents('li').siblings('li').children('ul').parent('li').children('a').removeClass('inactives');
                $(this).parents('li').siblings('li').children('ul').slideUp(500);
            }
        }else{
            //控制自身变成+号
            $(this).removeClass('inactives');
            //控制自身菜单下子菜单隐藏
            $(this).siblings('ul').slideUp(500);
            //控制自身子菜单变成+号
            $(this).siblings('ul').children('li').children('ul').parent('li').children('a').addClass('inactives');
            //控制自身菜单下子菜单隐藏
            $(this).siblings('ul').children('li').children('ul').slideUp(500);
            //控制同级菜单只保持一个是展开的（-号显示）
            $(this).siblings('ul').children('li').children('a').removeClass('inactives');
        }
    });
    function loadPage(param) {
        window.frames["content"].src = "/web/html/" + param;
    }
    $("#menuList").on("click", ".btn-menu", function () {
        var page = $(this).attr("data-target");
//        console.log(page);
        loadPage(page);
//            $("#content").load("../html/" + page);
    });
    loadPage("overview.html");
    return {
        iFrameHeight:iFrameHeight
    };
});