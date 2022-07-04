layui.use(['form', 'table', 'util', 'laydate', 'baseConfig'], function () {
    var $ = layui.jquery,
        form = layui.form,
        baseConfig = layui.baseConfig,
        util = layui.util,
        laydate = layui.laydate,
        table = layui.table;

    //------------公共配置
    var pageName = "order";
    var minWidth = 1100;
    var minHeight = 700;
    var formTitleSuffix = "订单信息";

    table.render({
        id : "listTable",
        elem: '#currentTableId',
        url : '/' + pageName + '/list',
        toolbar: '#toolbar',
        defaultToolbar: [],
        cols: [[
            {field: 'orderNo', width: 200, title: '订单号'},
            {field: 'type', width: 100, title: '订单类型', templet : function( d){
                switch (d.type) {
                    case 1: return "工位预定";
                    case 2: return "会议室预定";
                    case 3: return "Vip预定";
                }
            }},
            {field: 'status', width: 120, title: '订单状态', templet : function( d){
                switch (d.status) {
                    case 1: return "待付款";
                    case 2: return "进行中";
                    case 3: return "已完成";
                    case 4: return "用户申请取消";
                    case 5: return "取消审核通过";
                    case 6: return "取消审核未通过";
                }
            }},
            {field: 'name', width: 100, title: '预订人', templet : function( d){
                switch (d.type) {
                    case 1:
                        return d.workStationReservationModel.name;
                    case 2:
                        return d.meetingRoomReservationModel.name;
                    case 3:
                        return d.appUserModel.name;
                }
            }},
            {field: 'phone', width: 150, title: '电话', templet : function( d){
                switch (d.type) {
                    case 1:
                        return d.workStationReservationModel.phone;
                    case 2:
                        return d.meetingRoomReservationModel.phone;
                    case 3:
                        return d.appUserModel.phone;
                }
            }},
            {field: 'recommender', width: 200, title: '推荐人'},/*
            {field: '', width: 250, title: '订单内容', templet : function( d){
                    switch (d.type) {
                        case 1:
                            var str = "预定工位数：" + d.workStationReservationModel.count + "<br/>";
                                str += "预定日期：" + baseConfig.formatDateToDay( d.workStationReservationModel.reservationTime) + "";
                            return str;
                        case 2:
                            var str = "预定会议室：" + d.meetingRoomReservationModel.addrDetail + "<br/>";
                            str += "开始时间：" + util.toDateString( d.meetingRoomReservationModel.startTime) + "<br/>";
                            str += "结束时间：" + util.toDateString( d.meetingRoomReservationModel.endTime) + "<br/>";
                            return str;
                        case 3:
                            return "开通会员";
                    }
            }},*/
            {field: 'payTime', width: 200, title: '完成支付时间', templet : function( d){
                return util.toDateString( d.payTime);
            }},
            {field: 'vipExpireTime', width: 200, title: '服务完成时间', templet : function( d){
                return util.toDateString( d.vipExpireTime);
            }},
            {title: '操作', minWidth: 150, align: "center", templet : function( d){
                var str = '<a class="layui-btn layui-btn-xs" lay-event="detail">详细</a>';
                if( d.status == 3){
                    str += "<a class=\"layui-btn layui-btn-normal layui-btn-xs data-count-edit\" lay-event=\"review\">审核</a>";
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

    //广告开始时间
    laydate.render({
        elem: '#payTimeStart_input',
        type: 'date' //默认，可不填
    });
    //广告结束时间
    laydate.render({
        elem: '#payTimeEnd_input',
        type: 'date' //默认，可不填
    });

    //------------表单搜索
    form.on('submit(data-search-btn)', function (data) {
        var result = data.field;
        result.payTimeStart = new Date( result.payTimeStart).getTime();
        result.payTimeEnd = new Date( result.payTimeEnd).getTime();
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
        } else if (obj.event === 'review') {
            baseConfig.sendDataToForm( pageName, data);
            var index = layer.open({
                title: baseConfig.getTitleByType( 4, formTitleSuffix),
                type: 2,
                shade: 0.2,
                shadeClose: true,
                area: [ baseConfig.getWidth( minWidth), baseConfig.getHeight( minHeight)],
                content: 'formData.html?actionType=4',
            });
        } else if (obj.event === 'delete') {
            layer.confirm('确认删除？', function (index) {
                obj.del();
                layer.close(index);
            });
        }
    });

});