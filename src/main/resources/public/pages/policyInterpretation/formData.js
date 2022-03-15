layui.use(['form','layer', 'baseConfig', "upload",'flow','wangEditor'], function () {
    var form = layui.form,
        $ = layui.jquery,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        upload = layui.upload,
        flow = layui.flow,
        baseConfig = layui.baseConfig,
        wangEditor = layui.wangEditor;

    var pageName = "policyInterpretation";
    var data = baseConfig.getDataFromList( pageName);
    var actionType = baseConfig.getUrlParamer( "actionType");

    var typeId = data ? data.typeId: null;
    baseConfig.loadSelect( "/enterpriseServiceType/list?type=2", "typeId", typeId, "name");

    function handleContentShow( type){
        $( ".policyInterpretation_content").css( "display", "none");
        $( "#content" + type).css( "display", "block");
    }

    //处理富文本编辑器
    var editor = new wangEditor('#contentEditor');
    editor.customConfig.uploadImgServer = "/file/upload?type=wangEditor";
    editor.customConfig.uploadFileName = 'file';
    editor.customConfig.pasteFilterStyle = false;
    editor.customConfig.uploadImgMaxLength = 5;
    editor.customConfig.zIndex=0;
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
                });
            } else {
                layer.msg(result.msg);
            }
        }
    };
    editor.customConfig.customAlert = function (info) {
        layer.msg(info);
    };
    editor.customConfig.onchange = function (html) {
        $("input[name=content]").val( html);
    };
    editor.create();

    /**
     * 将list页面通过url传过来的参数加载到form表单里面去
     * @param data
     */
    if( data){
        baseConfig.loadFormData( data);
        editor.txt.html( data.content);
        $( "#coverImg_img").attr( "src", data.cover);
        handleContentShow( data.type);
    }else{
        handleContentShow( 0);
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
        url: '/file/upload?type=policyInterpretationCover',
        multiple: true,
        auto: false,
        choose: function(obj){  //上传前选择回调方法
            var flag = true;
            obj.preview( function(index, file, result){
                console.log(file);            //file表示文件信息，result表示文件src地址
                var img = new Image();
                img.src = result;
                var c_width = 247;
                img.onload = function () { //初始化夹在完成后获取上传图片宽高，判断限制上传图片的大小。
                    var width =  baseConfig.parseImgSize( img.width);
                    if( width == c_width){
                        obj.upload( index, file);
                        return true;
                    }else{
                        flag = false;
                        top.layer.msg("您上传的图片宽度必须是"+c_width+"尺寸");
                        return false;
                    }
                }
                return flag;
            });
        },
        done: function(res){
            if( res.code == 200){
                var filePath = res.data.filePath;
                $( "input[name=cover]").val( filePath);
                $( "#coverImg_img").attr( "src", filePath);
            }
        }
    });
    form.on('select(typeFilter)', function(data){
        var type = data.value;
        handleContentShow( type);
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