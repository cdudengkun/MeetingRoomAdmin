layui.use(['form', 'table', 'util', 'baseConfig'], function () {
    var $ = layui.jquery,
        form = layui.form,
        baseConfig = layui.baseConfig,
        util = layui.util;
        table = layui.table;

    //------------公共配置
    var pageName = "coupon";
    var minWidth = 800;
    var minHeight = 400;
    var formTitleSuffix = "优惠券";

    var tableIns = table.render({
        id : "listTable",
        elem: '#currentTableId',
        url : '/' + pageName + '/list',
        toolbar: '#toolbar',
        defaultToolbar: [],
        cols: [[
            {field: 'name', width: 300, title: '优惠券名称'},
            {field: 'mount', width: 100, title: '金额'},
            {field: 'numberNo', width: 200, title: '编号'},
            {field: 'status', width: 100, title: '状态', templet : function( d){
                switch (d.status) {
                    case 1: return "未发布";
                    case 2: return "已发布";
                }
            }},
            {field: 'drawedCount', width: 200, title: '领取次数'},
            {field: 'startTime', width: 400, title: '可用时间', templet : function( d){
                return baseConfig.formatDateToDay( d.startTime) + "至" + baseConfig.formatDateToDay( d.endTime);
            }},
            {field: 'createTime', width: 200, title: '发布时间', templet : function( d){
                return util.toDateString( d.createTime);
            }},
            {title: '操作', minWidth: 150, align: "center", templet:function ( d) {
                var str = "";
                if( d.status == 1){
                    str = '<a class="layui-btn layui-btn-normal layui-btn-xs data-count-edit" lay-event="edit">编辑</a>\n' +
                        '<a class="layui-btn layui-btn-normal layui-btn-xs data-count-edit" lay-event="publish">发布</a>\n' +
                        '<a class="layui-btn layui-btn-xs layui-btn-danger data-count-delete" lay-event="delete">删除</a>';
                }
                str += '<a class="layui-btn layui-btn-xs" lay-event="detail">详细</a>\n';
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

    //------------加载搜索表单下拉框
    baseConfig.loadGroupSelect( "/enterpriseServiceType/list?type=3", "typeId", null, "name");

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

        } else if (obj.event === 'publish') {
            layer.confirm('确认发布？发布之后，所有用户都会收到该优惠券，本操作无法撤销！', function (index) {
                $.post("/" + pageName + "/publish", { "id": data.id}, function( res){
                    if( res.code == 200){
                        layer.close(index);
                        layer.msg( "发布优惠券成功");
                        table.reload('listTable', {
                            page: {
                                curr: 1
                            }
                            , where: result
                        }, 'data');
                    }else{
                        layer.close( index);
                        layer.msg( res.msg);
                    }
                });
            });
        }else if (obj.event === 'delete') {
            del( data.id);
        }
    });

});