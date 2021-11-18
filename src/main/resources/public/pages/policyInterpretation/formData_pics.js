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
        handleContentShow( data.type);
    }else{
        handleContentShow( 0);
    }

    //关闭弹窗页面
    $( "#closeBtn").on( "click", function(){
        var index = parent.layer.getFrameIndex( window.name);
        parent.layer.close( index);
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
        url: '/file/upload?type=cooperationShoppingImg',
        multiple: true,
        done: function(res){
            if( res.code == 200){
                var filePath = res.data.filePath;
                $.ajax({
                    url: "/" + pageName + "/uploadImg?id=" + data.id + "&img=" + filePath,
                    type: "POST",
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function( res){
                        if( res.code == 200){
                            top.layer.msg( "上传成功");
                            $('#Images').prepend('<li>' +
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
                            form.render( "checkbox");
                        }else{
                            top.layer.msg( res.msg);
                        }
                    }});
            }
        }
    });

    function delImg( imgs){
        $.post( "/" + pageName + "/delImg?id=" + data.id + "&imgs=" + imgs,{},function( res){
            if( res.code == 200){
                top.layer.msg( "删除成功");
            }else{
                top.layer.msg( res.msg);
            }
        });
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