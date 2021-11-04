package com.cjack.meetingroomadmin.controller;

import com.cjack.meetingroomadmin.config.AjaxResult;
import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.exception.DictionaryWordIsUsingException;
import com.cjack.meetingroomadmin.model.DictionaryWordModel;
import com.cjack.meetingroomadmin.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Controller
@RequestMapping("/dictionary")
public class DictionaryEndpoint extends BaseEndpoint{

    @Autowired
    DictionaryService service;

    /**
     * 列表
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult list( LayPage layPage, DictionaryWordModel model) {
        try{
            service.list( layPage, model);
            return AjaxResult.SUCCESS( layPage);
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }

    /**
     * 新增
     * @return
     */
    @RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult addOrUpdate( DictionaryWordModel model) {
        try{
            if( isAdd( model.getId())){
                model.setCreateTime( new Date());
            }
            model.setUpdateTime( new Date());
            service.save( model);
            return AjaxResult.SUCCESS();
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult del( @RequestParam("ids") String ids) {
        try{
            service.del( ids);
            return AjaxResult.SUCCESS( "删除成功");
        }catch ( DictionaryWordIsUsingException e) {
            return AjaxResult.ERROR( e.getCode(), e.getMessage());
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }

    /**
     * 导出选中的单词
     * @return
     */
    @RequestMapping(value = "/exportChooseWord", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult exportChooseWord( String ids) {
        try{
            String fileUrl = service.exportChooseWord( ids);
            return AjaxResult.SUCCESS( (Object)fileUrl);
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }

    /**
     * 导出缺失音频文件的单词
     * @return
     */
    @RequestMapping(value = "/exportAudioLoseWord", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult exportAudioLoseWord() {
        try{
            String fileUrl = service.exportAudioLoseWord();
            return AjaxResult.SUCCESS( (Object)fileUrl);
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }

    /**
     * 导入单词
     * @return
     */
    @RequestMapping(value = "/improtWords", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult improtWords( String fileUrl) {
        try{
            service.improtWords( fileUrl);
            return AjaxResult.SUCCESS();
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }


    /**
     * 上传单词音频文件 这里是前端控件选择多个文件，再一个个调用此接口上传的
     * @return
     */
    @RequestMapping(value = "/uploadWordsAudio", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult uploadWordsAudio( MultipartFile file, String type) {
        try{
            service.uploadWordsAudio( file, type);
            return AjaxResult.SUCCESS();
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }

    /**
     * 上传单词音频文件
     * @return
     */
    @RequestMapping(value = "/uploadWordsAudioZip", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult uploadWordsAudioZip( String audioZipFilePath, String type) {
        try{
            service.uploadWordsAudioZip( audioZipFilePath, type);
            return AjaxResult.SUCCESS();
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }

    /**
     * 在服务器扫描缺少音频文件的单词，如果单词缺少音频文件，则把数据库对应的字段置空
     * @return
     */
    @RequestMapping(value = "/scanWordAudio", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult scanWordAudio() {
        try{
            service.scanWordAudio();
            return AjaxResult.SUCCESS();
        }catch ( Exception e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }
}
