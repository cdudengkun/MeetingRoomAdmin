layui.extend({
    baseConfig : "/lib/baseConfig"
}).use(['form', 'table', 'baseConfig'], function () {
    var $ = layui.jquery,
        form = layui.form,
        baseConfig = layui.baseConfig,
        table = layui.table;

    //------------公共配置
    var pageName = "message";
    var minWidth = 1000;
    var minHeight = 600;
    var formTitleSuffix = "消息";

    table.render({
        elem: '#currentTableId',
        url: '/api/table.json',
        toolbar: '#toolbarDemo',
        defaultToolbar: ['filter', 'exports', 'print', {
            title: '提示',
            layEvent: 'LAYTABLE_TIPS',
            icon: 'layui-icon-tips'
        }],
        cols: [[
            {type: "checkbox", width: 50},
            {field: 'id', width: 80, title: 'ID', sort: true},
            {field: 'username', width: 80, title: '用户名'},
            {field: 'sex', width: 80, title: '性别', sort: true},
            {field: 'city', width: 80, title: '城市'},
            {field: 'sign', title: '签名', minWidth: 150},
            {field: 'experience', width: 80, title: '积分', sort: true},
            {field: 'score', width: 80, title: '评分', sort: true},
            {field: 'classify', width: 80, title: '职业'},
            {field: 'wealth', width: 135, title: '财富', sort: true},
            {title: '操作', minWidth: 150, toolbar: '#currentTableBar', align: "center"}
        ]],
        limits: [10, 15, 20, 25, 50, 100],
        limit: 15,
        page: true
    });

    //------------表单搜索
    form.on('submit(data-search-btn)', function (data) {
        var result = JSON.stringify(data.field);
        layer.alert(result, {
            title: '最终的搜索信息'
        });

        //执行搜索重载
        table.reload('currentTableId', {
            page: {
                curr: 1
            }
            , where: {
                searchParams: result
            }
        }, 'data');

        return false;
    });

    //------------表格头部工具栏，一般放新增等按钮
    table.on('toolbar(currentTableFilter)', function (obj) {
        if (obj.event === 'add') {  // 监听添加操作
            var index = layer.open({
                title: baseConfig.getTitleByType( 1, formTitleSuffix),
                type: 2,
                shade: 0.2,
                shadeClose: true,
                area: [ baseConfig.getWidth( minWidth), baseConfig.getWidth( minHeight)],
                content: 'formData.html',
            });
        }
    });

    //------------表格数据工具栏，一般放表格行数据的编辑、删除等按钮
    table.on('tool(currentTableFilter)', function (obj) {
        var data = obj.data;
        if (obj.event === 'edit') {
            baseConfig.sendDataToForm( "message", data);
            var index = layer.open({
                title: baseConfig.getTitleByType( 2, formTitleSuffix),
                type: 2,
                shade: 0.2,
                shadeClose: true,
                area: [ baseConfig.getWidth(1000), baseConfig.getWidth(800)],
                content: 'formData.html',
            });
        } else if (obj.event === 'delete') {
            layer.confirm('真的删除行么', function (index) {
                obj.del();
                layer.close(index);
            });
        }
    });

});