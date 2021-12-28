layui.use(['form','layer', 'baseConfig','wangEditor'], function () {
    var form = layui.form,
        $ = layui.jquery,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        baseConfig = layui.baseConfig;

    var pageName = "appConfig";
    var data = baseConfig.getDataFromList( pageName);/*
    data.menmberPrice1 = data.menmberPrice1 / 100;
    data.menmberPrice3 = data.menmberPrice3 / 100;
    data.menmberPrice12 = data.menmberPrice12 / 100;*/
    var actionType = baseConfig.getUrlParamer( "actionType");

    //------------加载搜索表单下拉框

    //处理富文本编辑器
    var editor = new wangEditor('#memberRightEditor');
    editor.customConfig.uploadImgServer = "/file/upload?type=wangEditor";
    editor.customConfig.uploadFileName = 'file';
    editor.customConfig.pasteFilterStyle = false;
    editor.customConfig.zIndex=0;
    editor.customConfig.uploadImgMaxLength = 5;
    editor.customConfig.uploadImgHooks = {
        // 上传超时
        timeout: function (xhr, editor) {
            layer.msg('上传超时！')
        },
        // 如果服务器端返回的不是 {errno:0, data: [...]} 这种格式，可使用该配置
        customInsert: function (insertImg, result, editor) {
            console.log(result);
            if (result.code == 200) {
                var url = result.data.filePath;
                var urls = url.split( ",");
                urls.forEach(function (e) {
                    insertImg(e);
                })
            } else {
                layer.msg(result.msg);
            }
        }
    };
    editor.customConfig.customAlert = function (info) {
        layer.msg(info);
    };
    editor.customConfig.onchange = function (html) {
        $("input[name=memberRight]").val( html);
    };
    editor.create();

    /**
     * 将list页面通过url传过来的参数加载到form表单里面去
     * @param data
     */
    baseConfig.loadFormData( data);
    editor.txt.html( data.memberRight);
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
        var jsonBody = data.field;/*
        jsonBody.menmberPrice1 = parseInt( jsonBody.menmberPrice1 * 100);
        jsonBody.menmberPrice3 = parseInt( jsonBody.menmberPrice3 * 100);
        jsonBody.menmberPrice12 = parseInt( jsonBody.menmberPrice12 * 100);*/
        //提交数据
        $.post("/" + pageName + "/saveConfig", jsonBody, function( res){
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