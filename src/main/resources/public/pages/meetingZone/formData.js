layui.use(['form','layer', 'baseConfig', "upload", 'layarea'], function () {
    var form = layui.form,
        $ = layui.jquery,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        upload = layui.upload,
        baseConfig = layui.baseConfig,
        layarea = layui.layarea;

    var pageName = "meetingZone";
    var data = baseConfig.getDataFromList( pageName);
    var actionType = baseConfig.getUrlParamer( "actionType");

    //------------加载下拉框
    var facilityIds = data ? data.facilityIds: "";
    //------------加载多选下拉框  声明->加载数据->加载选中值
    var facilityIdsSelect = xmSelect.render({
        el: '#facilityIdsDiv',
        data: [
        ]
    });
    $.get( "/singleKey/list/4", function( res){
        if( res.code == 200){
            //转一下
            var datas = res.data;
            for( var i = 0; i < datas.length; i++){
                var data = datas[i];
                data.name = data.dataKey;
                data.value = data.id;
            }
            facilityIdsSelect.update({
                data: datas,
                autoRow: true,
            });
            facilityIdsSelect.setValue( facilityIds.split( ","));
        }else{
            top.layer.msg( res.msg);
        }
    });
    //省市区下拉框
    layarea.render({
        elem: '#area-picker',
        data: {
            provinceId: data ? data.provinceId : null,
            cityId: data ? data.cityId : null,
            countyId: data ? data.countyId : null,
        },
        change: function (res) {
            //选择结果
            console.log(res);
        }
    });
    /**
     * 将list页面通过url传过来的参数加载到form表单里面去
     * @param data
     */
    if( data){
        baseConfig.loadFormData( data);
        $( "#coverImg_img").attr( "src", data.cover);
    }

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
        var index = parent.layer.getFrameIndex( window.name);
        parent.layer.close( index);
    });
    //上传封面
    upload.render({
        elem: '#coverImg_div',
        url: '/file/upload?type=cooperationShoppingCover',
        multiple: true,
        done: function(res){
            if( res.code == 200){
                var filePath = res.data.filePath;
                $( "input[name=cover]").val( filePath);
                $( "#coverImg_img").attr( "src", filePath);
            }
        }
    });


    //监听提交
    form.on('submit(saveBtn)', function (data) {
        $.ajaxSettings.async = false;
        //弹出loading
        var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        var jsonBody = data.field;
        jsonBody.facilityIds = baseConfig.arrIdToStr( facilityIdsSelect.getValue());
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