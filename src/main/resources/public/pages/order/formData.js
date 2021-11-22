layui.use(['form', 'layer', 'baseConfig', 'util'], function () {
    var form = layui.form,
        $ = layui.jquery,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        util = layui.util,
        baseConfig = layui.baseConfig;

    var pageName = "order";
    var data = baseConfig.getDataFromList( pageName);
    var actionType = baseConfig.getUrlParamer( "actionType");

    /**
     * 将list页面通过url传过来的参数加载到form表单里面去
     * @param data
     */
    function loadFormData( data){
        $(".order_content").css( "display", "none");
        switch ( data.type) {
            case 1:
                $("#workStation").css( "display", "block");
                $( "input[name=count]").val( data.workStationReservationModel.count);
                $( "input[name=reservationTime]").val( util.toDateString( data.workStationReservationModel.reservationTime));
                $( "input[name=name]").val( data.workStationReservationModel.name);
                $( "input[name=phone]").val( data.workStationReservationModel.phone);
                break;
            case 2:
                $("#meetingRoom").css( "display", "block");
                $( "input[name=meetingRoomName]").text( data.meetingRoomReservationModel.addrDetail);
                $( "input[name=startTime]").val( util.toDateString( data.meetingRoomReservationModel.startTime));
                $( "input[name=endTime]").val( util.toDateString( data.meetingRoomReservationModel.endTime));
                $( "input[name=name]").val( data.meetingRoomReservationModel.name);
                $( "input[name=phone]").val( data.meetingRoomReservationModel.phone);
                break;
        }

        $( "input[name=name]").val( data.meetingZoneModel.name);
        $( "#coverImg_img").attr( "src", data.meetingZoneModel.cover);

        switch ( data.type) {
            case 1:
                $( "input[name=totalTime]").val( "8小时");
                break;
            case 2:
                $( "input[name=totalTime]").val( baseConfig.diffToHour( data.meetingRoomReservationModel.startTime, data.meetingRoomReservationModel.endTime));
                break;
        }

        $( "input[name=couponName]").val( data.couponModel ? data.couponModel.name : "");
        $( "input[name=vipHour]").val( data.vipHour);
        $( "input[name=orderNo]").val( data.orderNo);
        $( "input[name=payType]").val( "微信支付");
        $( "input[name=createTime]").val( util.toDateString( data.createTime));
        $( "input[name=totalMoney]").val( data.amount);
    }
    loadFormData( data);
    /**
     * 处理表单元素的 禁用、隐藏、显示灯操作
     */
    function handleFormItem(){
        //只有查看详细或者审核的时候，才显示审核区域
        if( actionType != baseConfig.ACTION.REVIEW && actionType != baseConfig.ACTION.DETAIL){
            $( "#review_div").hide();
        }
        //审核的时候，只有内容区域是只读
        if( actionType == baseConfig.ACTION.REVIEW){
            baseConfig.setFormReadOnly( "content_div", true);
        }
        //查看详细的时候，内容区域和审核区域都只读
        if( actionType == baseConfig.ACTION.DETAIL){
            baseConfig.setFormReadOnly( "content_div", true);
            baseConfig.setFormReadOnly( "review_div", true);
            $( "#submitDiv").hide();
        }else{
            $( "#closeDiv").hide();
        }

    }
    handleFormItem();

    //关闭弹窗页面
    $( "#closeBtn").on( "click", function(){
        //  parent.location.reload();
        var index = parent.layer.getFrameIndex( window.name);
        parent.layer.close( index);
    });


    //监听提交
    form.on('submit(saveBtn)', function (data) {
        $.ajaxSettings.async = false;
        //弹出loading
        var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        var jsonBody = data.field;
        //提交数据
        $.post("/" + pageName + "/addOrUpdate", jsonBody, function( res){
            if( res.code == 200){
                top.layer.close( index);
                top.layer.msg( res.msg);
                layer.closeAll( "iframe");
                //刷新父页面
                parent.location.reload();
            }else{
                top.layer.close( index);
                top.layer.msg( res.msg);
            }
        });
        return false;
    });

});