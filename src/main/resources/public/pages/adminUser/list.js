var INTERFACE_NAME = "adminUser";
layui.extend({
    baseConfig : "/js/baseConfig"
}).use(['form','layer','laydate','table','laytpl','element','util','baseConfig'],function(){

    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        baseConfig = layui.baseConfig,
        table = layui.table;
    var util = layui.util;
    var roleId = baseConfig.getUrlParamer( "roleId");//角色id
    var tableIns = table.render({
        elem: '#list',
        url : '/' + INTERFACE_NAME + '/list?roleId=' + roleId,
        cellMinWidth : 95,
        height : "full-125",
        page: true, //开启分页
        id : "listTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: 'loginName', title: '登录帐号', align:"center"},
            {field: 'roleName', title: '角色', align:"center"},
            {field: 'name', title: '姓名', align:"center"},
            {field: 'phone', title: '电话', align:'center'},
            {field: 'email', title: '邮箱', align:"center"},
            {field: 'lastLoginTime', title: '最后登录时间', align:'center', templet : function( d){
                if( d.lastLoginTime){
                    return util.toDateString( d.lastLoginTime);
                }else{
                    return "未登录过";
                }
            }},
            {field: 'createTime', title: '添加时间', width:180, align:'center', templet : function( d){
                return util.toDateString( d.createTime);
            }},
            {title: '操作', fixed:"right", width: 200, align:"center", templet : function( d) {
                var str = "";
                str += "<a class=\"layui-btn layui-btn-xs\" lay-event=\"update\">编辑</a>";
                str += "<a class=\"layui-btn layui-btn-xs\" lay-event=\"detail\">详细</a>";
                str += "<a class=\"layui-btn layui-btn-xs\" lay-event=\"del\">删除</a>";
                return str;
            }}
        ]],
        response : {
            statusCode: 200 //规定成功的状态码，默认：0
        },
        parseData : function( res){ //res 即为原始返回的数据
            return {
                "code": res.code, //解析接口状态
                "msg": res.msg, //解析提示文本
                "count": res.data.count, //解析数据长度
                "data": res.data.data //解析数据列表
            };
        }
    });

    //添加
    $(".add_btn").click(function(){
        addOrUpdateOrDetail( null, baseConfig.ACTION.ADD);
    });

    //添加/修改/详细查看（readonly=true）
    function addOrUpdateOrDetail( data, type){
        var title = baseConfig.getTitleByType( type, "账户");
        var index = layui.layer.open({
            title : title,
            type : 2,
            area: [900 + 'px', baseConfig.getHeight( 620) + 'px'],
            shade: 0.4,
            btnAlign: 'r',
            id: "aboutUs", //设定一个id，防止重复弹出
            content : "formData.html?data=" + encodeURIComponent( JSON.stringify( data)) + "&actionType=" + type + "&roleId=" + roleId,//把操作类型传到form页面",
            success : function(){
                setTimeout( function(){
                    layui.layer.tips('点击此处返回数据列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                },500);
            }
        });
    }

    //删除
    function del( ids){
        layer.confirm('确定删除选中的数据项？', {icon: 3, title: '提示信息'}, function (index) {
            $.post("/" + INTERFACE_NAME + "/del",{
                "ids" : ids
            },function( res){
                if( res.code == 200){
                    tableIns.reload();
                    layer.close( index);
                    top.layer.msg( res.msg);
                }else{
                    top.layer.close( index);
                    top.layer.msg( res.msg);
                }
            });
        });
    }

    //批量删除
    $(".delAll_btn").click(function(){
        var checkStatus = table.checkStatus( 'listTable'),
            data = checkStatus.data, ids = "";
        if( data.length > 0) {
            for ( var i=0 ; i<data.length ; i++) {
                ids += data[i].id;
                if( i != data.length-1){
                    ids += ",";
                }
            }
            del( ids);
        }else{
            layer.msg( "请选择需要删除的数据项");
        }
    });

    //列表操作
    table.on('tool(list)', function(obj){
        var layEvent = obj.event,
            data = obj.data;
        if(layEvent === 'update'){ //编辑
            addOrUpdateOrDetail( data, baseConfig.ACTION.UPDATE);
        } else if(layEvent === 'del'){ //删除
            del( data.id);
        } else if(layEvent === 'detail'){ //预览
            addOrUpdateOrDetail( data, baseConfig.ACTION.DETAIL);
        }
    });
});
