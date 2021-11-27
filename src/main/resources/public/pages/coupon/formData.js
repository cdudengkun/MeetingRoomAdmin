layui.use(['form', 'layer', 'baseConfig', 'laydate'], function () {
    var form = layui.form,
        $ = layui.jquery,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        baseConfig = layui.baseConfig,
        laydate = layui.laydate;

    var pageName = "coupon";
    var data = baseConfig.getDataFromList( pageName);
    var actionType = baseConfig.getUrlParamer( "actionType");

    /**
     * 将list页面通过url传过来的参数加载到form表单里面去
     * @param data
     */
    function loadFormData( data){
        if( data && data.id){
            data.startTime = baseConfig.formatDateToDay( data.startTime);
            data.endTime = baseConfig.formatDateToDay( data.endTime);
            form.val( "formFilter", data);
        }
    }
    loadFormData( data);

    //广告开始时间
    laydate.render({
        elem: '#startTime_input',
        type: 'date' //默认，可不填
    });
    //广告结束时间
    laydate.render({
        elem: '#endTime_input',
        type: 'date' //默认，可不填
    });

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
        jsonBody.startTime = new Date( jsonBody.startTime).getTime();
        jsonBody.endTime = new Date( jsonBody.endTime).getTime();
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