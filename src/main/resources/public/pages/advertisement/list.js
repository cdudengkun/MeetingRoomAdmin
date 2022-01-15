layui.use(['form', 'table', 'util', 'baseConfig'], function () {
    var $ = layui.jquery,
        form = layui.form,
        baseConfig = layui.baseConfig,
        util = layui.util;
        table = layui.table;

    //------------公共配置
    var pageName = "advertisement";
    var minWidth = 1100;
    var minHeight = 700;
    var formTitleSuffix = "广告banner";

    var tableIns = table.render({
        id : "listTable",
        elem: '#currentTableId',
        url : '/' + pageName + '/list',
        toolbar: '#toolbar',
        defaultToolbar: [],
        cols: [[
            {field: 'title', width: 200, title: '广告标题'},
            {field: 'urlType', width: 200, title: '跳转类型', templet : function( d){
                switch (d.urlType) {
                    case 1: return "外部网站跳转";
                    case 2: return "跳转企业服务";
                    case 3: return "跳转政策解读";
                    case 4: return "跳转礼包领取";
                    case 5: return "跳转企业助力";
                    case 6: return "跳转共享办公中心";
                }
            }},
            {field: 'url', width: 400, title: '广告点击跳转地址'},
            {field: 'sequence', width: 200, title: '广告出现顺序'},
            {field: 'position', width: 200, title: '广告位置', templet : function( d){
                switch (d.position) {
                    case 1: return "首页广告";
                }
            }},
            {field: 'createTime', width: 200, title: '发布时间', templet : function( d){
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
                "count": res.data.length, //解析数据长度
                "data": res.data //解析数据列表
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
        } else if (obj.event === 'delete') {
            del( data.id);
        } else if (obj.event === 'imgsEdit') {
            baseConfig.sendDataToForm( pageName, data);
            var index = layer.open({
                title: baseConfig.getTitleByType( 2, "图片编辑"),
                type: 2,
                shade: 0.2,
                shadeClose: true,
                area: [ baseConfig.getWidth( minWidth), baseConfig.getHeight( minHeight)],
                content: 'formData_Pics.html?actionType=2',
            });
        }
    });

});