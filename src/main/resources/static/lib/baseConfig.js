layui.define(["form","jquery"],function(exports){
    var $ = layui.jquery;
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
            formatSecond: function( second){
                if( !second){
                    return "0秒";
                }
                //把秒转化为时分秒格式化
                var hour = Math.floor(Math.floor(second/60)/60);
                var minute = Math.floor(second/60) % 60;
                var senc = second % 60;
                var str = "";
                if( hour){
                    str += hour + "小时";
                }
                if( minute){
                    str += minute + "分钟";
                }
                if( senc){
                    str += senc + "秒";
                }
                return str;
            },

            getTitleByType: function ( type, name) {
                switch ( type) {
                    case this.ACTION.ADD: return "新增" + name;
                    case this.ACTION.UPDATE: return "修改" + name;
                    case this.ACTION.DETAIL: return "查看" + name;
                    case this.ACTION.REVIEW: return "审核" + name;
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
            }
        };
    exports( "baseConfig", baseConfig);
})
