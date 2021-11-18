layui.use(['form', 'table', 'util', 'baseConfig',"miniTab"], function () {
    var $ = layui.jquery,
        form = layui.form,
        baseConfig = layui.baseConfig,
        util = layui.util,
        miniTab = layui.miniTab,
        table = layui.table;

    //------------公共配置
    var pageName = "policyInterpretation";
    var minWidth = 1300;
    var minHeight = 800;
    var formTitleSuffix = "政策解读";
    miniTab.listen();

    table.render({
        id : "listTable",
        elem: '#currentTableId',
        url : '/' + pageName + '/list',
        toolbar: '#toolbar',
        defaultToolbar: [],
        cols: [[
            {field: 'title', width: 300, title: '标题'},
            {field: 'type', width: 150, title: '内容类型', templet : function( d){
                switch (d.type) {
                    case 1: return "视频讲解";
                    case 2: return "图文";
                    case 3: return "纯图片";
                    case 4: return "优惠券";
                }
            }},
            {field: 'createTime', width: 200, title: '创建时间', templet : function( d){
                return util.toDateString( d.createTime);
            }},
            {title: '操作', minWidth: 150, align: "center", templet:function ( d) {
                var str = '<a class="layui-btn layui-btn-normal layui-btn-xs data-count-edit" lay-event="edit">编辑</a>\n' +
                    '<a class="layui-btn layui-btn-xs" lay-event="detail">详细</a>\n' +
                    '<a class="layui-btn layui-btn-xs layui-btn-danger data-count-delete" lay-event="delete">删除</a>';
                switch ( d.type) {
                    case 1:
                        str += '<a class="layui-btn layui-btn-normal layui-btn-xs data-count-edit" lay-event="editVideo">编辑视频文件</a>\n';
                       break;
                    case 2:
                        break;
                    case 3:
                        str += '<a class="layui-btn layui-btn-normal layui-btn-xs data-count-edit" lay-event="editImg">编辑图片内容</a>\n';
                        break;
                    case 4:
                        str += '<a class="layui-btn layui-btn-normal layui-btn-xs data-count-edit" lay-event="editCoupon">编辑优惠券</a>\n';
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
        } else if (obj.event === 'editImg') {
            baseConfig.sendDataToForm( pageName, data);
            var index = layer.open({
                title: baseConfig.getTitleByType( 2, formTitleSuffix),
                type: 2,
                shade: 0.2,
                shadeClose: true,
                area: [ baseConfig.getWidth( minWidth), baseConfig.getHeight( minHeight)],
                content: 'formData_pics.html?actionType=2',
            });
        } else if (obj.event === 'editVideo') {
            miniTab.create({
                title: "政策解读[" + data.name + "]视频详情",
                href: "/pages/policyInterpretationVideo/list.html?policyInterpretationId=" + data.id,
                tabId: "policyInterpretationVideo" + data.id,
                isIframe : true
            });
            miniTab.changeIframe( "policyInterpretationVideo" + data.id);

        } else if (obj.event === 'delete') {
            layer.confirm('确认删除？', function (index) {
                obj.del();
                layer.close(index);
            });
        }
    });

});