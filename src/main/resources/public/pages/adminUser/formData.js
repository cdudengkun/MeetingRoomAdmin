var INTERFACE_NAME = "adminUser";
layui.extend({
    baseConfig : "/js/baseConfig"
}).use(['form','layer','laydate','table','laytpl','element','util','baseConfig','upload'],function(){
    var form = layui.form,
    layer = parent.layer === undefined ? layui.layer : top.layer,
        laypage = layui.laypage,
        upload = layui.upload,
        baseConfig = layui.baseConfig,
        layedit = layui.layedit,
        laydate = layui.laydate,
        $ = layui.jquery;

    var data = baseConfig.getUrlJsonParamer( "data");
    var actionType = baseConfig.getUrlParamer( "actionType");
    var roleId = baseConfig.getUrlParamer( "roleId");
    var provinceId = data.provinceId;
    var cityId = data.cityId;
    var countryId = data.countryId;
    var schoolId = data.schoolId;
    var userInfo = JSON.parse( window.sessionStorage.getItem( "userInfo"));

    //绑定校验
    form.verify({
        name : function(val){
            if( !val || val == ''){
                return "姓名为必填";
            }
        },
        phone : function(val){
            if( !val || val == ''){
                return "电话为必填";
            }
            if( !(/^1[3456789]\d{9}$/.test( val))){
                alert("电话号码有误，请重填");
                return false;
            }
        },
        loginName : function(val){
            if( !val || val == ''){
                return "登录帐号为必填";
            }
        },
        passWord : function(val){
            if( !val || val == ''){
                if( actionType == baseConfig.ACTION.ADD){
                    return "点登录密码为必填";
                }
            }
        },
        schoolId : function(val){
            if( roleId != baseConfig.ROLE.DEALER_ID){
                if( !val || val == ''){
                    return "学校为必填";
                }
            }
        }
    });

    //处理省市县下拉列表的展示
    function handleCity() {
        if( provinceId){
            loadCity( "provinceId", -1, provinceId);
            if( cityId){
                loadCity(  "cityId", provinceId, cityId);
                if( countryId){
                    loadCity( "countryId", cityId, countryId);
                }
            }
        }else{//初始只展示省的下拉
            loadCity( "provinceId", -1, null);
        }
    }

    function loadCity( name, parentId, value) {
        console.log( { "name": name, "parentId": parentId, "value": value});
        $.get( "/city/list/" + parentId, function( res){
            if( res.code == 200){
                var citys = res.data;
                var sel = $("select[name=" + name + "]");
                sel.empty();
                sel.append( "<option value='' selected>请选择</option>");
                var selected = 0;
                for( var i = 0 ; i < citys.length ; i++){
                    var city = citys[i];
                    if( value){
                        //修改的时候，根据城市id去设置选中并加载它的下一级
                        if( value == city.id){
                            selected = city.id;
                        }
                    }
                    sel.append( "<option value='" + city.id + "'>" + city.name + "</option> ")
                }
                //更新渲染
                form.render( "select");
                sel.val( selected);
                form.render( "select");
            }else{
                top.layer.msg( res.msg);
            }
        });
    }

    handleCity();//加载省市县
    //监听省市下拉的选择
    form.on('select(provinceIdFilter)', function(data){
        loadCity(  "cityId", data.value, null);
    });
    form.on('select(cityIdFilter)', function(data){
        loadCity(  "countryId", data.value, null);
    });

    upload.render({
        elem: '#avatar_div',
        url: '/file/upload?type=adminUserAvatar',
        multiple: true,
        done: function(res){
            if( res.code == 200){
                var filePath = res.data.filePath;
                $( "input[name=avatar]").val( filePath);
                $( "#avatar_img").attr( "src", filePath);
            }
        }
    });

    //生日
    laydate.render({
        elem: '#birthday_input',
        type: 'date',
        max: baseConfig.formatDateToDay( new Date())
    });

    //加载 选择学校的下拉框
    function loadSchools() {
        var url = "";
        if( roleId == baseConfig.ROLE.MASTER_ID) {//添加校长
            url = "/school/listForAddMaster?editUserId=" + ( data && data.id ? data.id : "");
        }else if( roleId == baseConfig.ROLE.TEACHER_ID){//添加教师
            url = "/school/listForAddTeacher";
        }else{//添加经销商
            url = "/school/list";
        }
        $.get( url, function( res){
            if( res.code == 200){
                var schools = res.data;
                var sel = $("select[name=schoolId]");
                sel.empty();
                sel.append( "<option value='' selected>请选择</option>");
                for( var i = 0 ; i < schools.length ; i++){
                    var school = schools[i];
                    sel.append( "<option value='" + school.id + "'>" + school.name + "</option> ")
                }
                //更新渲染
                form.render( "select");
                sel.val( schoolId);
                form.render( "select");
            }else{
                top.layer.msg( res.msg);
            }
        });
    }
    loadSchools();

    /**
     * 将list页面通过url传过来的参数加载到form表单里面去
     * @param data
     */
    function loadFormData( data){
        if( data && data.id){
            $( "input[name=avatar]").val( data.avatar);
            $( "#avatar_img").attr( "src", data.avatar);
            data.passWord = null;
            form.val( "formFilter", data);
        }
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
        //如果是经销商，则屏蔽学校选项
        if( roleId == baseConfig.ROLE.DEALER_ID){
            $( "#schoolId_div").hide();
        }
        //查看详细的时候，内容区域和审核区域都只读
        if( actionType == baseConfig.ACTION.DETAIL){
            baseConfig.setFormReadOnly( "content_div", true);
            baseConfig.setFormReadOnly( "review_div", true);
            $( "#submitDiv").hide();
            //查看详细的时候，隐藏密码
            $( ".passWord_div").hide();
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
    form.on("submit(submitBtn)",function( data){
        //弹出loading
        var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        var jsonBody = data.field;
        jsonBody.roleId = roleId;
        //提交数据
        $.post("/" + INTERFACE_NAME + "/addOrUpdate", jsonBody, function( res){
            if( res.code == 200){
                setTimeout(function(){
                    top.layer.close( index);
                    top.layer.msg( res.msg);
                    layer.closeAll( "iframe");
                    //刷新父页面
                    parent.location.reload();
                },500);
            }else{
                top.layer.close( index);
                top.layer.msg( res.msg);
            }
        });
    });
});
