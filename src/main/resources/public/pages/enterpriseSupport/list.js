layui.use(['form', 'table', 'util', 'baseConfig'], function () {
    var $ = layui.jquery,
        form = layui.form,
        baseConfig = layui.baseConfig,
        util = layui.util;
        table = layui.table;

    //------------公共配置
    var pageName = "enterpriseSupport";
    var minWidth = 900;
    var minHeight = 500;
    var formTitleSuffix = "企业助力";

    table.render({
        id : "listTable",
        elem: '#currentTableId',
        url : '/' + pageName + '/list',
        toolbar: '#toolbar',
        defaultToolbar: [],
        cols: [[
            {field: 'name', width: 300, title: '名称'},
            {field: 'typeName', width: 150, title: '服务类别'},
            {field: 'url', width: 500, title: '下载地址'},
            {field: 'size', width: 150, title: '文件大小'},
            {field: 'downloadCount', width: 150, title: '下载次数'},
            {field: 'createTime', width: 200, title: '创建时间', templet : function( d){
                return util.toDateString( d.createTime);
            }},
            {title: '操作', minWidth: 150, toolbar: '#currentTableBar', align: "center"}
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

    //------------加载搜索表单下拉框
    baseConfig.loadSelect( "/enterpriseServiceType/list?type=4", "typeId", null, "name");

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
                    table.reload();
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
        } else if (obj.event === 'delete') {
            del( data.id);
        }
    });

});