layui.use(['form','layer', 'baseConfig', "upload",'flow'], function () {
    var form = layui.form,
        $ = layui.jquery,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        upload = layui.upload,
        flow = layui.flow,
        baseConfig = layui.baseConfig;

    var pageName = "policyInterpretation";
    var data = baseConfig.getDataFromList( pageName);
    var actionType = baseConfig.getUrlParamer( "actionType");

    function handleContentShow( type){
        $( ".policyInterpretation_content").css( "display", "none");
        $( "#content" + type).css( "display", "block");
    }

    /**
     * 将list页面通过url传过来的参数加载到form表单里面去
     * @param data
     */
    if( data){
        baseConfig.loadFormData( data);
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

    //处理附件相关
    //附件上传
    upload.render({
        elem: '.uploadAttachment',
        url: '/file/upload?type=policyInterpretationAttachment',
        multiple: true,
        done: function(res){
            if( res.code == 200){
                var filePath = res.data.filePath;
                top.layer.msg( "上传成功");
                $('#attachment').prepend('<li>' +
                    '<div class="operate" style="margin-top: 3px;">' +
                    '<div class="check" style="float: left;">' +
                    '<input type="checkbox" name="img" lay-filter="choose" value="' + filePath + '" lay-skin="primary">' +
                    '</div>' +
                    '<div style="height: 16px;width:16px;float:left;margin-top: 1px;"><i class="layui-icon img_del">&#xe640;</i></div>' +
                    '</div>' +
                    '</li>');
                //设置图片的高度
                $("#Images li img").height($("#Images li img").width());
                uploadImg( filePath);
                form.render( "checkbox");
            }
        }
    });

    //处理图片相关
    //流加载图片
    var imgNums = 15;  //单页显示图片数量
    flow.load({
        elem: '#Images', //流加载容器
        done: function(page, next){ //加载下一页
            var imgList = [],pics = data && data.imgs ? data.imgs.split( ",") : [];
            var maxPage = imgNums*page < pics.length ? imgNums*page : pics.length;
            if( pics && pics.length > 0){
                setTimeout(function(){
                    for(var i = imgNums * ( page - 1); i < maxPage; i++){
                        var pic = pics[i];
                        if( !pic){
                            continue;
                        }
                        imgList.push('<li style="width: 200px;float: left;margin-right: 20px;">' +
                            '<img style="width: 200px; height:200px;" layer-src="'+ pic +'" src="'+ pic +'">' +
                            '<div class="operate" style="margin-top: 3px;">' +
                            '<div class="check" style="float: left;">' +
                            '<input type="checkbox" name="img" lay-filter="choose" value="' + pic + '" lay-skin="primary">' +
                            '</div>' +
                            '<div style="height: 16px;width:16px;float:left;margin-top: 1px;"><i class="layui-icon img_del">&#xe640;</i></div>' +
                            '</div>' +
                            '</li>');
                    }
                    next(imgList.join(''), page < (pics.length/imgNums));
                    form.render();
                    if( actionType == baseConfig.ACTION.DETAIL){
                        $( ".operate").hide();
                    }
                }, 500);
            }else{
                next( '', false);
                form.render();
            }
        }
    });

    //设置图片的高度
    $(window).resize(function(){
        $("#Images li img").height($("#Images li img").width());
    })

    //图片上传
    upload.render({
        elem: '.uploadNewImg',
        url: '/file/upload?type=policyInterpretationImg',
        multiple: true,
        done: function(res){
            if( res.code == 200){
                var filePath = res.data.filePath;
                top.layer.msg( "上传成功");
                $('#Images').prepend('<li style="width: 200px;float: left;margin-right: 20px;">' +
                    '<img style="width: 200px; height:200px;" layer-src="'+ filePath +'" src="'+ filePath +'"  class="layui-upload-img">' +
                    '<div class="operate" style="margin-top: 3px;">' +
                    '<div class="check" style="float: left;">' +
                    '<input type="checkbox" name="img" lay-filter="choose" value="' + filePath + '" lay-skin="primary">' +
                    '</div>' +
                    '<div style="height: 16px;width:16px;float:left;margin-top: 1px;"><i class="layui-icon img_del">&#xe640;</i></div>' +
                    '</div>' +
                    '</li>');
                //设置图片的高度
                $("#Images li img").height($("#Images li img").width());
                uploadImg( filePath);
                form.render( "checkbox");
            }
        }
    });

    function delImg( imgs){
        var imgsArr = imgs.split( ",");
        var exsitsImgs = $( "input[name=imgs]").val();
        for( var i = 0; i < imgsArr.length; i++){
            exsitsImgs = exsitsImgs.replace( imgsArr[i], "");
        }
        $( "input[name=imgs]").val( exsitsImgs);
    }

    function uploadImg( img){
        var exsitsImgs = $( "input[name=imgs]").val();
        if( !exsitsImgs){
            exsitsImgs = img;
        }else{
            if( !exsitsImgs.contain( img)){
                exsitsImgs += img + ",";
            }
        }
        $( "input[name=imgs]").val( exsitsImgs);
    }

    //删除单张图片
    $("body").on("click",".img_del",function(){
        var _this = $(this);
        layer.confirm('确定删除图片吗？',{icon:3, title:'提示信息'},function(index){
            _this.parents("li").hide(1000);
            delImg( _this.parents("li").find( "input[name=img]").val());
            setTimeout(function(){_this.parents("li").remove();},950);
            layer.close(index);
        });
    })

    //全选
    form.on('checkbox(selectAll)', function(data){
        var child = $("#Images li input[type='checkbox']");
        child.each(function(index, item){
            item.checked = data.elem.checked;
        });
        form.render('checkbox');
    });

    //通过判断是否全部选中来确定全选按钮是否选中
    form.on("checkbox(choose)",function(data){
        var child = $(data.elem).parents('#Images').find('li input[type="checkbox"]');
        var childChecked = $(data.elem).parents('#Images').find('li input[type="checkbox"]:checked');
        if(childChecked.length == child.length){
            $(data.elem).parents('#Images').siblings("blockquote").find('input#selectAll').get(0).checked = true;
        }else{
            $(data.elem).parents('#Images').siblings("blockquote").find('input#selectAll').get(0).checked = false;
        }
        form.render('checkbox');
    })

    //批量删除
    $(".batchDel").click(function(){
        var $checkbox = $('#Images li input[type="checkbox"]');
        var $checked = $('#Images li input[type="checkbox"]:checked');
        if($checkbox.is(":checked")){
            layer.confirm('确定删除选中的图片？',{icon:3, title:'提示信息'},function(index){
                var index = layer.msg('删除中，请稍候',{icon: 16,time:false,shade:0.8});
                setTimeout(function(){
                    var imgs = [];
                    //删除数据
                    $checked.each(function(){
                        $(this).parents("li").hide(1000);
                        setTimeout(function(){$(this).parents("li").remove();},950);
                        ids.push( $(this).parents("li").find( "input[name=img]").val());
                    })
                    delImg( imgs.join(','));
                    $('#Images li input[type="checkbox"],#selectAll').prop("checked",false);
                    form.render();
                    layer.close(index);
                    layer.msg("删除成功");
                },2000);
            })
        }else{
            layer.msg("请选择需要删除的图片");
        }
    });
});