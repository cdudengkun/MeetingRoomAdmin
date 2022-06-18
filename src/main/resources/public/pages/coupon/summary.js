layui.use(['form', 'table', 'util', 'baseConfig'], function () {
    var $ = layui.jquery,
        form = layui.form,
        baseConfig = layui.baseConfig,
        util = layui.util;
        table = layui.table;

    //------------公共配置
    var pageName = "coupon";

    var tableIns = table.render({
        id : "listTable",
        elem: '#currentTableId',
        url : '/' + pageName + '/summary',
        toolbar: '#toolbar',
        defaultToolbar: [],
        cols: [[
            {field: 'typeName', width: 400, title: '礼包类型'},
            {field: 'viewCount', width: 300, title: '领取次数'}
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

});