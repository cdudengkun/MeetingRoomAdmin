layui.use(['form','layer', 'baseConfig', "upload"], function () {
    var form = layui.form,
        $ = layui.jquery,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        upload = layui.upload,
        baseConfig = layui.baseConfig;

    var pageName = "enterpriseServiceType";
    var data = baseConfig.getDataFromList( pageName);
    var actionType = baseConfig.getUrlParamer( "actionType");

    //------------加载搜索表单下拉框
    var type = data ? data.type: null;
    if( type){
        var parentId = data ? data.parentId: null;
        baseConfig.loadGroupSelect( "/enterpriseServiceType/list?type=" + type + "&level=2", "parentId", parentId, "name");
    }


    /**
     * 将list页面通过url传过来的参数加载到form表单里面去
     * @param data
     */
    if( data){
        baseConfig.loadFormData( data);
        $( "#logoImg_img").attr( "src", data.logo);
        $( "#logoChosedImg_img").attr( "src", data.logoChosed);
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

    form.on('select(typeFilter)', function(data){
        var type = data.value; //得到被选中的值
        baseConfig.loadGroupSelect( "/enterpriseServiceType/list?type=" + type + "&level=2", "parentId", null, "name");
    });

    //关闭弹窗页面
    $( "#closeBtn").on( "click", function(){
        var index = parent.layer.getFrameIndex( window.name);
        parent.layer.close( index);
    });
    //上传图片
    upload.render({
        elem: '#logoImg_div',
        url: '/file/upload?type=enterpriseServiceTypeLogo',
        multiple: true,
        auto: false,
        choose: function(obj){  //上传前选择回调方法
            var flag = true;
            obj.preview( function(index, file, result){
                console.log(file);            //file表示文件信息，result表示文件src地址
                var img = new Image();
                img.src = result;
                var c_width = 128;
                var c_height = 128;
                img.onload = function () { //初始化夹在完成后获取上传图片宽高，判断限制上传图片的大小。
                    var width =  baseConfig.parseImgSize( img.width);
                    var height = baseConfig.parseImgSize( img.height);
                    if( width == c_width && height == c_height){
                        obj.upload( index, file);
                        return true;
                    }else{
                        flag = false;
                        top.layer.msg("您上传的图片必须是"+c_width+"*"+c_height+"尺寸");
                        return false;
                    }
                }
                return flag;
            });
        },
        done: function(res){
            if( res.code == 200){
                var filePath = res.data.filePath;
                $( "input[name=logo]").val( filePath);
                $( "#logoImg_img").attr( "src", filePath);
            }
        }
    });
    upload.render({
        elem: '#logoChosedImg_div',
        url: '/file/upload?type=enterpriseServiceTypeLogoChosed',
        multiple: true,
        auto: false,
        choose: function(obj){  //上传前选择回调方法
            var flag = true;
            obj.preview( function(index, file, result){
                console.log(file);            //file表示文件信息，result表示文件src地址
                var img = new Image();
                img.src = result;
                var c_width = 128;
                var c_height = 128;
                img.onload = function () { //初始化夹在完成后获取上传图片宽高，判断限制上传图片的大小。
                    var width =  baseConfig.parseImgSize( img.width);
                    var height = baseConfig.parseImgSize( img.height);
                    if( width == c_width && height == c_height){
                        obj.upload( index, file);
                        return true;
                    }else{
                        flag = false;
                        top.layer.msg("您上传的图片必须是"+c_width+"*"+c_height+"尺寸");
                        return false;
                    }
                }
                return flag;
            });
        },
        done: function(res){
            if( res.code == 200){
                var filePath = res.data.filePath;
                $( "input[name=logoChosed]").val( filePath);
                $( "#logoChosedImg_img").attr( "src", filePath);
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