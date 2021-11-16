layui.use(['form','layer', 'baseConfig', "upload"], function () {
    var form = layui.form,
        $ = layui.jquery,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        upload = layui.upload,
        baseConfig = layui.baseConfig;

    var pageName = "enterpriseSupport";
    var data = baseConfig.getDataFromList( pageName);
    var actionType = baseConfig.getUrlParamer( "actionType");


    /**
     * 将list页面通过url传过来的参数加载到form表单里面去
     * @param data
     */
    if( data){
        baseConfig.loadFormData( data);
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
        elem: '#uploadFile',
        url: '/file/upload?type=enterpriseSupportFile',
        multiple: true,
        before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
            obj.preview(function(index, file, result) {
                var size = file.size;//文件大小填写到数据表里面，这里的单位是byte
                if( parseInt( size / 1024) > 0){//转化为KB
                    if( parseInt(size / 1024 / 1024) > 0){//转化为MB
                        size = parseInt(size / 1024 /1024) + "MB";
                    }else{
                        size = parseInt(size / 1024) + "KB";
                    }
                }else{
                    size = size + "B";
                }

                $( "input[name=size]").val( size);
            });
        },
        done: function(res){
            if( res.code == 200){
                var filePath = res.data.filePath;
                $( "input[name=url]").val( filePath);
            }
        }
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