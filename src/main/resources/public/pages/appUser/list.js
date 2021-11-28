layui.use(['form', 'table', 'util', 'baseConfig'], function () {
    var $ = layui.jquery,
        form = layui.form,
        baseConfig = layui.baseConfig,
        util = layui.util;
        table = layui.table;

    //------------公共配置
    var pageName = "appUser";
    var minWidth = 800;
    var minHeight = 400;
    var formTitleSuffix = "消息";

    table.render({
        id : "listTable",
        elem: '#currentTableId',
        url : '/' + pageName + '/list',
        toolbar: '#toolbar',
        defaultToolbar: [],
        cols: [[
            {field: 'name', width: 200, title: '昵称'},
            {field: 'loginName', width: 200, title: '登录账号'},
            {field: 'phone', width: 200, title: '电话'},
            {field: 'status', width: 300, title: '状态', templet : function( d){
                switch (d.status) {
                    case 1: return "启用";
                    case 2: return "禁用";
                }
            }},
            {field: 'isVip', width: 200, title: '是否vip', templet : function( d){
                switch (d.accountModel.isVip) {
                    case 1: return "是";
                    case 2: return "否";
                }
            }},
            {field: 'lastLoginTime', width: 200, title: '最近登录时间', templet : function( d){
                return util.toDateString( d.lastLoginTime);
            }},
            {field: 'createTime', width: 200, title: '注册时间', templet : function( d){
                return util.toDateString( d.createTime);
            }},
            {title: '操作', minWidth: 150, align: "center", templet:function ( d) {
                var str = '<a class="layui-btn layui-btn-xs" lay-event="detail">详细</a>\n';
                switch ( d.status) {
                    case 1:
                        str += '<a class="layui-btn layui-btn-normal layui-btn-xs data-count-edit" lay-event="disableUser">禁用</a>\n';
                        break;
                    case 2:
                        str += '<a class="layui-btn layui-btn-normal layui-btn-xs data-count-edit" lay-event="enableUser">启用</a>\n';
                        break;
                }
                return str;
            }}
        ]],
        limits: [10, 15, 20, 25, 50, 100],
        limit: 15,
        page: true,
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

    //------------表单搜索
    form.on('submit(data-search-btn)', function (data) {
        var result = data.field;
        //执行搜索重载
        table.reload('listTable', {
            page: {
                curr: 1
            }
            , where: result
        }, 'data');

        return false;
    });

    //------------表格头部工具栏，一般放新增等按钮
    table.on('toolbar(currentTableFilter)', function (obj) {
        if (obj.event === 'add') {  // 监听添加操作
            baseConfig.sendDataToForm( pageName, null);
            var index = layer.open({
                title: baseConfig.getTitleByType( 1, formTitleSuffix),
                type: 2,
                shade: 0.2,
                shadeClose: true,
                area: [ baseConfig.getWidth( minWidth), baseConfig.getHeight( minHeight)],
                content: 'formData.html',
            });
        }
    });

    //------------表格数据工具栏，一般放表格行数据的编辑、删除等按钮
    table.on('tool(currentTableFilter)', function (obj) {
        var data = obj.data;
        if (obj.event === 'detail') {
            baseConfig.sendDataToForm( pageName, data);
            var index = layer.open({
                title: baseConfig.getTitleByType( 3, formTitleSuffix),
                type: 2,
                shade: 0.2,
                shadeClose: true,
                area: [ baseConfig.getWidth( minWidth), baseConfig.getHeight( minHeight)],
                content: 'formData.html?actionType=3',
            });
        } else if (obj.event === 'edit') {
            baseConfig.sendDataToForm( pageName, data);
            var index = layer.open({
                title: baseConfig.getTitleByType( 2, formTitleSuffix),
                type: 2,
                shade: 0.2,
                shadeClose: true,
                area: [ baseConfig.getWidth( minWidth), baseConfig.getHeight( minHeight)],
                content: 'formData.html?actionType=2',
            });
        } else if (obj.event === 'disableUser') {
            layer.confirm('确认禁用用户？', function (index) {
                updateStatus( data.id, 2, index);
            });
        } else if (obj.event === 'enableUser') {
            layer.confirm('确认启用用户？', function (index) {
                updateStatus( data.id, 1, index);
            });
        } else if (obj.event === 'delete') {
            del( data.id);
        }
    });

    function updateStatus( id, status, index){
        $.post("/" + pageName + "/update", { "id": id, "status": status}, function( res){
            if( res.code == 200){
                layer.close(index);
                layer.msg( res.msg);
                //刷新父页面
                table.reload('listTable', {
                    page: {
                        curr: 1
                    }
                    , where: {}
                }, 'data');
            }else{
                layer.close(index);
                layer.msg( res.msg);
            }
        });
    }
});