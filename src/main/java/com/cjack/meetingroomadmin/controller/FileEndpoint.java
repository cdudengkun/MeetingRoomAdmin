package com.cjack.meetingroomadmin.controller;

import com.alibaba.fastjson.JSONObject;
import com.cjack.meetingroomadmin.config.AjaxResult;
import com.cjack.meetingroomadmin.util.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
@RequestMapping("/file")
public class FileEndpoint extends BaseEndpoint{

    @Value("${file.upload.baseServerDir}")
    String baseServerDir;
    @Value("${file.upload.baseClientDir}")
    String baseClientDir;
    @Value("${file.upload.spelitor}")
    String spelitor;
    @Value("${file.upload.serverUrl}")
    String serverUrl;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult upload(@RequestParam("file") MultipartFile file, @RequestParam("type") String type) {

        if (file.isEmpty()) {
            return AjaxResult.ERROR();
        }

        String fileName = FileUtils.getFileName( file.getOriginalFilename());
        String dumic =  type + spelitor + FileUtils.getDirByDate();//动态的这一截路径
        //服务器保存文件的目录
        String destDir = baseServerDir + dumic;
        //供浏览器客户端访问的目录
        String clientDir = baseClientDir + dumic;
        //先判断当前的目录是否存在
        FileUtils.handleDir( destDir);
        File dest = new File( destDir  + spelitor + fileName);
        try {
            file.transferTo( dest);
            JSONObject fileInfo = new JSONObject();
            fileInfo.put( "filePath", serverUrl + clientDir  + spelitor + fileName);
            return AjaxResult.SUCCESS( fileInfo);
        } catch ( IOException e) {
            e.printStackTrace();
        }
        return AjaxResult.ERROR();
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(HttpServletResponse response, @RequestParam("filePath") String filePath) {

        if ( StringUtils.isEmpty( filePath)) {
            return;
        }
        // 设置信息给客户端不解析
        String type = new MimetypesFileTypeMap().getContentType( filePath);
        // 设置contenttype，即告诉客户端所发送的数据属于什么类型
        response.setHeader( "Content-type", type);
        // 读取filename
        String fileName = filePath.split( spelitor)[4];
        // 设置扩展头，当Content-Type 的类型为要下载的类型时 , 这个信息头会告诉浏览器这个文件的名字和类型。
        response.setHeader( "Content-Disposition", "attachment;filename=" + fileName);
        try{
            // 发送给客户端的数据
            OutputStream outputStream = response.getOutputStream();
            byte[] buff = new byte[1024];
            BufferedInputStream bis;
            //需要先将 excel的浏览器端访问路径 转化为服务器端访问路径
            filePath = FileUtils.transferClientFileUrl( filePath, baseServerDir, baseClientDir);
            bis = new BufferedInputStream( new FileInputStream( new File( filePath)));
            int i = bis.read(buff);
            while (i != -1) {
                outputStream.write(buff, 0, buff.length);
                outputStream.flush();
                i = bis.read(buff);
            }
        }catch ( Exception e){
            e.printStackTrace();
        }
    }
}
