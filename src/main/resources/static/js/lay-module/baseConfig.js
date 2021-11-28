layui.define(["form","jquery",'echarts'],function(exports){
    var $ = layui.jquery,
        echarts = layui.echarts;
    var baseConfig = {
            ROLE:{
                SUPER_ID : 1,//超级管理员
                ADMIN_ID : 2,//管理员
                DEALER_ID : 3,//经销商
                MASTER_ID : 4,//校长
                TEACHER_ID : 5,//教师
            },
            ACTION:{
                ADD: 1,//新增操作
                UPDATE: 2,//修改操作
                DETAIL: 3,//详细操作
                REVIEW: 4,//审核操作
            },
            FORM_ITEM:{
                INPUT: "input",
                SELECT: "select",
                RADIO: "radio",
                TEXTAREA: "textarea",
                IMG: "img",
            },
            ALL: "_ALL_",

            checkPrivalege: function( name){
                var userInfo = JSON.parse( window.sessionStorage.getItem( "userInfo"));
                var roleContent = userInfo.roleContent;
                if( !roleContent){
                    return false;
                }
                if( roleContent == baseConfig.ALL){
                    return true;
                }
                var privalegeArr = roleContent.split( ",");
                for( var i = 0; i < privalegeArr.length; i++){
                    if( privalegeArr[i] == name){
                        return true;
                    }
                }
                return false;
            },
            sendDataToForm: function( key, data){
                window.sessionStorage.setItem( key, JSON.stringify( data));
            },
            getDataFromList: function( key){
                return JSON.parse( window.sessionStorage.getItem( key));
            },

            getTitleByType: function ( type, name) {
                switch ( type) {
                    case this.ACTION.ADD: return "新增" + name;
                    case this.ACTION.UPDATE: return "修改" + name;
                    case this.ACTION.DETAIL: return "查看" + name;
                    case this.ACTION.REVIEW: return "审核" + name;
                }
            },
            //获取url参数，先暂时使用此方法，后续找找layui有没有提供更优雅的写法
            getUrlParamer: function( name){
                var url = window.location.href.split( "?" )[1];            /*获取url里"?"后面的值*/
                if( !url){
                    return "";
                }
                if( url.indexOf( "&") > 0){                                      /*判断是否是一个参数还是多个参数*/
                    var urlParamArry = url.split( "&");                            /*分开每个参数，并放到数组里*/
                    for(var i=0; i < urlParamArry.length ; i++){
                        var paramerName = urlParamArry[i].split( "=");   /*把每个参数名和值分开，并放到数组里*/
                        if( name == paramerName[0]){                     /*匹配输入的参数和数组循环出来的参数是否一样*/
                            return paramerName[1];                           /*返回想要的参数值*/
                        }
                    }
                }else{                                                              /*判断只有个参数*/
                    var paramerValue = url.split( "=")[1];
                    if( name ==  url.split( "=")[0]){
                        return paramerValue;
                    }
                }
            },
            getHeight: function( h){
                //获取浏览器最大高度，以防页面显示不完全
                var maxHeight = $(window).height();
                if( h < maxHeight){
                    res = h;
                }else{
                    res = maxHeight - 10;
                }
                return res + "px";
            },
            getWidth: function( w){
                //获取浏览器最大宽度，以防页面显示不完全
                var maxWidth = $(window).width();
                var res;
                if( w < maxWidth){
                    res = w;
                }else{
                    res = maxWidth - 100;
                }
                return res + "px";
            },
            setFormReadOnly: function ( elementId, readOnley) {
                var readForm = layui.$("#" + elementId);
                readForm.find( 'input,textarea,select').prop( 'disabled', readOnley);
                readForm.find( '.layui-layedit iframe').contents().find( 'body').prop( 'contenteditable', !readOnley);
                layui.form.render();
            },
            loadSelect: function ( url, selectName, val, valueName){
                $.get( url, function( res){
                    if( res.code == 200){
                        var datas = res.data;
                        var sel = $("select[name=" + selectName + "]");
                        sel.empty();
                        sel.append( "<option value='' selected>请选择</option>");
                        for( var i = 0 ; i < datas.length ; i++){
                            var data = datas[i];
                            sel.append( "<option value='" + data.id + "'>" + data[valueName] + "</option> ")
                        }
                        //更新渲染
                        layui.form.render( "select");
                        sel.val( val);
                        layui.form.render();
                    }else{
                        top.layer.msg( res.msg);
                    }
                });
            },
            loadFormData: function ( data){
                if( data && data.id){
                    layui.form.val( "formFilter", data);
                }
            },
            //获取登录的用户信息
            getUserInfo: function getUserInfo( callback){
                $.ajaxSettings.async = false;
                $.get("/adminUser/getUserInfo",function( res){
                    try{
                        res = JSON.parse( res);
                    }catch (e) {

                    }
                    if( res.code == 200){
                        callback( res.data);
                    }else if( res.code == 304){
                        top.layer.confirm( "用户尚未登录，点击跳转到登录页面", function(){
                            window.location.href = "/pages/login.html";
                        });
                    }else{
                        top.layer.alert( res.msg);
                    }
                });
            },
            arrIdToStr: function ( datas){
                var str = "";
                if( datas){
                    for( var i = 0; i < datas.length; i++){
                        str += datas[i].id + ",";
                    }
                }
                return str;
            },
            diffToHour: function (start, end) {
                var hour = (end - start)/ 1000 / 60 / 60;
                return hour + "小时";
            },
            formatDateToDay: function( dateStr){
                var date = new Date( dateStr);
                var y = date.getFullYear().toString();        // 年
                var m = (date.getMonth() + 1).toString();     // 月
                var d = date.getDate().toString();            // 日
                if( m < 10){
                    m = "0" + m;
                }
                if( d < 10){
                    d = "0" + d;
                }
                return y + "-" + m + "-" + d;
            },
            loadLineEchart: function( eId, dataUrl, title){
                $.get( dataUrl, function( res){
                    try{
                        res = JSON.parse( res);
                    }catch (e) {

                    }
                    if( res.code == 200){
                        var data = res.data;
                        var echartsRecords = echarts.init(document.getElementById( eId), 'walden');
                        var optionRecords = {
                            tooltip: {
                                trigger: 'axis'
                            },
                            legend: {
                                data:[title]
                            },
                            grid: {
                                left: '3%',
                                right: '4%',
                                bottom: '3%',
                                containLabel: true
                            },
                            toolbox: {
                                feature: {
                                    saveAsImage: {}
                                }
                            },
                            xAxis: {
                                type: 'category',
                                boundaryGap: false,
                                data: data.xseries
                            },
                            yAxis: {
                                type: 'value'
                            },
                            series: [{
                                name: title,
                                type: 'line',
                                data: data.yseries
                            },]

                        };
                        echartsRecords.setOption( optionRecords);
                    }
                });
            },
            loadBarEchart: function( eId, dataUrl, title){
                $.get( dataUrl, function( res){
                    try{
                        res = JSON.parse( res);
                    }catch (e) {

                    }
                    if( res.code == 200){
                        var data = res.data;
                        var echartsRecords = echarts.init(document.getElementById( eId), 'walden');
                        var optionRecords = {
                            tooltip: {
                                trigger: 'axis'
                            },
                            legend: {
                                data:[title]
                            },
                            grid: {
                                left: '3%',
                                right: '4%',
                                bottom: '3%',
                                containLabel: true
                            },
                            toolbox: {
                                feature: {
                                    saveAsImage: {}
                                }
                            },
                            xAxis: {
                                type: 'category',
                                boundaryGap: false,
                                data: data.xseries
                            },
                            yAxis: {
                                type: 'value'
                            },
                            series: [{
                                name: title,
                                type: 'bar',
                                data: data.yseries
                            },]

                        };
                        echartsRecords.setOption( optionRecords);
                    }
                });
            }
        };
    exports( "baseConfig", baseConfig);
})
