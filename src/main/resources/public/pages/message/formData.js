layui.extend({
    baseConfig : "/lib/baseConfig"
}).use(['form', 'baseConfig'], function () {
    var form = layui.form,
        layer = layui.layer,
        baseConfig = layui.baseConfig,
        $ = layui.$;

    var pageName = "message";
    var data = baseConfig.getDataFromList( pageName);
    alert( data);
    //监听提交
    form.on('submit(saveBtn)', function (data) {
        var index = layer.alert(JSON.stringify(data.field), {
            title: '最终的提交信息'
        }, function () {

            // 关闭弹出层
            layer.close(index);

            var iframeIndex = parent.layer.getFrameIndex(window.name);
            parent.layer.close(iframeIndex);

        });
        return false;
    });

});