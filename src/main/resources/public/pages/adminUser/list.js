layui.use(['form', 'table', 'util', 'baseConfig'], function () {
    var $ = layui.jquery,
        form = layui.form,
        baseConfig = layui.baseConfig,
        util = layui.util;
        table = layui.table;

    //------------公共配置
    var pageName = "adminUser";
    var minWidth = 800;
    var minHeight = 500;
    var formTitleSuffix = "后台账户";

    table.render({
        id : "listTable",
        elem: '#currentTableId',
        url : '/' + pageName + '/list',
        toolbar: '#toolbar',
        defaultToolbar: [],
        cols: [[
            {field: 'loginName', width: 150, title: '账号名称'},
            {field: 'name', width: 150, title: '姓名'},
            {field: 'phone', width: 150, title: '电话'},
            {field: 'email', width: 200, title: '邮箱'},
            {field: 'lastLoginTime', width: 200, title: '最后登录时间', templet : function( d){
                return util.toDateString( d.lastLoginTime);
            }},
            {field: 'roleName', width: 150, title: '所属角色'},
            {field: 'createTime', width: 200, title: '创建时间', templet : function( d){
                return util.toDateString( d.createTime);
            }},
            {title: '操作', minWidth: 150, toolbar: '#currentTableBar', align: "center"}
        ]],
        response : {
            statusCode: 200 //规定成功的状态码，默认：0
        },
        parseData : function( res){ //res 即为原始返回的数据
            return {
                "code": res.code, //解析接口状态
                "msg": res.msg, //解析提示文本
                "data": res.data //解析数据列表
            };
        }
    });

    //------------加载搜索表单下拉框
    baseConfig.loadSelect( "/role/list", "roleId", null, "roleName");

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

    //删除
    function del( id){
        layer.confirm('确定删除选中的数据项？', {icon: 3, title: '提示信息'}, function (index) {
            $.post("/" + pageName + "/del",{
                "ids" : id
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
        }else if (obj.event === 'updateAppConfig') {  // 监听添加操作
            $.get( '/appConfig/getConfig', function( res) {
                baseConfig.sendDataToForm( "appConfig", res.data);
                var index = layer.open({
                    title: baseConfig.getTitleByType(2, "小程序参数配置"),
                    type: 2,
                    shade: 0.2,
                    shadeClose: true,
                    area: [baseConfig.getWidth(minWidth), baseConfig.getHeight(minHeight)],
                    content: 'updateAppConfig.html',
                });
            });
        }
    })

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
        } else if (obj.event === 'delete') {
            del( data.id);
        }
    });

});