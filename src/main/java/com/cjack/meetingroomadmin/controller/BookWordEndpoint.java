package com.cjack.meetingroomadmin.controller;

import com.cjack.meetingroomadmin.config.AjaxResult;
import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.exception.UnitAlreadyUsedException;
import com.cjack.meetingroomadmin.exception.WordNotFoundException;
import com.cjack.meetingroomadmin.model.BookUnitWordModel;
import com.cjack.meetingroomadmin.service.BookWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileNotFoundException;
import java.io.IOException;

@Controller
@RequestMapping("/bookWord")
public class BookWordEndpoint extends BaseEndpoint{

    @Autowired
    BookWordService service;

    /**
     * 列表
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult list( LayPage layPage, BookUnitWordModel model) {
        try{
            service.list( layPage, model);
            return AjaxResult.SUCCESS( layPage);
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
    public AjaxResult exportChooseWord( Long bookId, String ids) {
        try{
            String fileUrl = service.exportChooseWord( bookId, ids);
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
    @RequestMapping(value = "/deleteBookWords", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult deleteBookWords( Long bookId) {
        try{
            service.deleteBookWords( bookId);
            return AjaxResult.SUCCESS();
        } catch (UnitAlreadyUsedException e) {
            e.printStackTrace();
            return AjaxResult.ERROR( e.getCode(), e.getMessage());
        }
    }

    /**
     * 导入单词
     * @return
     */
    @RequestMapping(value = "/improtWords", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult improtWords( String fileUrl, Long bookId) {
        try{
            service.improtWords( fileUrl, bookId);
            return AjaxResult.SUCCESS();
        } catch (WordNotFoundException e) {
            e.printStackTrace();
            return AjaxResult.ERROR( e.getCode(), e.getMessage());
        } catch (UnitAlreadyUsedException e) {
            e.printStackTrace();
            return AjaxResult.ERROR( e.getCode(), e.getMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.ERROR();
        }
    }
}
