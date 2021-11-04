package com.cjack.meetingroomadmin.service;

import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.dao.BookDao;
import com.cjack.meetingroomadmin.dao.BookUnitDao;
import com.cjack.meetingroomadmin.dao.BookUnitWordDao;
import com.cjack.meetingroomadmin.dao.DictionaryWordDao;
import com.cjack.meetingroomadmin.excel.BookWordExcel;
import com.cjack.meetingroomadmin.excel.LoseWordExcel;
import com.cjack.meetingroomadmin.exception.UnitAlreadyUsedException;
import com.cjack.meetingroomadmin.exception.WordNotFoundException;
import com.cjack.meetingroomadmin.model.BookUnitWordModel;
import com.cjack.meetingroomadmin.table.*;
import com.cjack.meetingroomadmin.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BookWordService {

    @Value("${file.upload.baseServerDir}")
    String baseServerDir;
    @Value("${file.upload.baseClientDir}")
    String baseClientDir;
    @Value("${file.upload.spelitor}")
    String spelitor;
    @Value("${file.upload.serverUrl}")
    String serverUrl;
    @Autowired
    private BookUnitWordDao dao;
    @Autowired
    private BookUnitService bookUnitService;
    @Autowired
    private BookDao bookDao;
    @Autowired
    private DictionaryWordDao dictionaryWordDao;
    @Autowired
    private BookUnitDao bookUnitDao;

    public void list( LayPage layPage, BookUnitWordModel model){
        Sort sort = new Sort( Sort.Direction.DESC, "createTime");
        Pageable pageable = new PageRequest( layPage.getPage()-1, layPage.getLimit(), sort);
        Specification<BookUnitWordTable> specification = handleConditon( model.getBookId(), model.getWord());
        Page<BookUnitWordTable> pageTable = dao.findAll( specification, pageable);
        List< BookUnitWordModel> datas = new ArrayList<>();
        for( BookUnitWordTable bookWordTable : pageTable.getContent()){
            BookUnitWordModel data = ModelUtils.copySignModel( bookWordTable.getDicWord(), BookUnitWordModel.class);
            data.setBookName( bookWordTable.getBook().getBookName());
            data.setUnitName( bookWordTable.getUnit().getName());
            data.setCreateTime( bookWordTable.getCreateTime());
            data.setUpdateTime( bookWordTable.getUpdateTime());
            data.setWordId( bookWordTable.getDicWord().getId());
            data.setId( bookWordTable.getId());//id 是bookUnitWord表的id
            datas.add( data);
        }
        layPage.setData( datas);
        layPage.setCount( Long.valueOf( pageTable.getTotalElements()).intValue());
    }

    public String exportChooseWord( Long bookId, String ids) throws IOException {
        if( EmptyUtil.isNotEmpty( ids)){
            String[] idArr = ids.split( ",");
            //先找到需要导出的单词
            List<BookWordExcel> excelWords = new ArrayList<>( idArr.length);
            for( String id : idArr){
                BookUnitWordTable bookUnitWordTable = dao.getOne( Long.valueOf( id));
                BookTable book = bookUnitWordTable.getBook();
                BookUnitTable bookUnit = bookUnitWordTable.getUnit();

                BookWordExcel excelWord = ModelUtils.copySignModel( bookUnitWordTable.getDicWord(), BookWordExcel.class);
                excelWord.setStage( book.getVersion().getStageName());
                excelWord.setVerison( book.getVersion().getName());
                excelWord.setBookName( book.getBookName());
                excelWord.setUnitName( bookUnit.getName());
                excelWord.setOrderNum( bookUnitWordTable.getId() + "");
                excelWords.add( excelWord);
            }
            String fileName = "exportBookWords_" + DateFormatUtil.format( new Date(), DateFormatUtil.DATE_RULE_2) + ".xls";
            String dumic =  "exportChooseWord" + spelitor + FileUtils.getDirByDate();//动态的这一截路径
            //服务器保存文件的目录
            String destDir = baseServerDir + dumic;
            //供浏览器客户端访问的目录
            String clientDir = baseClientDir + dumic;
            //先判断当前的目录是否存在
            FileUtils.handleDir( destDir);
            String serverFilePath = destDir  + spelitor + fileName;
            //导出单词数据到excel
            ExcelUtil.exportExcel( excelWords, "课本单词", "课本单词", BookWordExcel.class, serverFilePath);
            //客户端访问文件的地址
            String clientFilePath = serverUrl + clientDir  + spelitor + fileName;
            return clientFilePath;
        }
        return "";
    }

    private String exportLoseWord( List<String> words, BookTable book) throws IOException {
        //先找到需要导出的单词
        List<LoseWordExcel> excelWords = new ArrayList<>( words.size());
        for( String word : words){
            LoseWordExcel excelWord = new LoseWordExcel();
            excelWord.setWord( word);
            excelWords.add( excelWord);
        }
        String fileName = "LoseWords_" + DateFormatUtil.format( new Date(), DateFormatUtil.DATE_RULE_2) + ".xls";
        String dumic =  "exportLoseWord" + spelitor + FileUtils.getDirByDate();//动态的这一截路径
        //服务器保存文件的目录
        String destDir = baseServerDir + dumic;
        //供浏览器客户端访问的目录
        String clientDir = baseClientDir + dumic;
        //先判断当前的目录是否存在
        FileUtils.handleDir( destDir);
        String serverFilePath = destDir  + spelitor + fileName;
        //导出单词数据到excel
        ExcelUtil.exportExcel( excelWords, null, "词库缺失单词", LoseWordExcel.class, serverFilePath);
        //客户端访问文件的地址
        String clientFilePath = serverUrl + clientDir  + spelitor + fileName;
        return clientFilePath;
    }

    public void improtWords( String fileUrl, Long bookId) throws IOException, WordNotFoundException, UnitAlreadyUsedException {
        BookTable book = bookDao.getOne( bookId);
        //需要先将 excel的浏览器端访问路径 转化为服务器端访问路径
        fileUrl = FileUtils.transferClientFileUrl( fileUrl, baseServerDir, baseClientDir);
        List<BookWordExcel> excelWords = ExcelUtil.importExcel( fileUrl, 0, 1, BookWordExcel.class);
        if( excelWords != null && excelWords.size() != 0){
            List<String> notFoundWords = new ArrayList<>();
            //先校验单词是否存在
            List<DictionaryWordTable> words = new ArrayList<>();
            for( BookWordExcel excelWord : excelWords) {
                if( !EmptyUtil.isNotEmpty( excelWord.getWord())){
                    continue;
                }
                //保存单词
                DictionaryWordTable dictionaryWordTable = dictionaryWordDao.getByWord(excelWord.getWord());
                if (dictionaryWordTable == null) {
                    notFoundWords.add( excelWord.getWord());
                }else{
                    words.add( dictionaryWordTable);
                }
            }
            if( EmptyUtil.isNotEmpty( notFoundWords)){
                String excelUrl = exportLoseWord( notFoundWords, book);
                throw new WordNotFoundException( excelUrl);
            }
            if( words == null){
                return;
            }
            BookUnitTable unitTable = bookUnitService.addUnit( book, "Unit " + 1);
            for( int i = 0; i < words.size(); i++){
                DictionaryWordTable word = words.get( i);
                if( i % 30 == 0 && i > 0){
                    //修改上一个单元的单词数量
                    bookUnitService.updateUnitWordsNum( unitTable, 30);
                    //创建单元
                    unitTable = bookUnitService.addUnit( book, "Unit " + ((i / 30) + 1));
                }else if( i == ( words.size() - 1)){
                    bookUnitService.updateUnitWordsNum( unitTable,  ( i % 30) + 1);
                }

                BookUnitWordTable importTable = new BookUnitWordTable();
                importTable.setDicWord( word);
                importTable.setBook( book);
                importTable.setUnit( unitTable);
                importTable.setCreateTime( new Date());
                importTable.setUpdateTime( new Date());
                dao.save( importTable);
            }
            book.setWordsNum( words.size());
            bookDao.save( book);
        }

    }

    public void deleteBookWords( Long bookId) throws UnitAlreadyUsedException {
        BookTable book = bookDao.getOne( bookId);
        //先检查，不能一遍删除一边检查，会让数据不完整
        //课本被学习过
        if ( book.getLearnBooks() != null && book.getLearnBooks().size() > 0) {
            throw new UnitAlreadyUsedException( "课本[" + book.getBookName() + "]已被学习，无法删除");
        }
        for( BookUnitTable bookUnitTable : book.getBookUnits()){
            dao.deleteInBatch( bookUnitTable.getWords());
        }
        bookUnitDao.delete( book.getBookUnits());
        dao.flush();
        bookUnitDao.flush();
    }


    private Specification<BookUnitWordTable> handleConditon( Long bookId, String word){
        Specification< BookUnitWordTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            Join<BookUnitWordTable, DictionaryWordTable> wordJoin = root.join("dicWord", JoinType.LEFT);
            if( EmptyUtil.isNotEmpty( word)){
                predicate.getExpressions().add( cb.like( wordJoin.get( "word"), "%" + word + "%"));
            }
            Join<BookUnitWordTable, BookTable> join = root.join("book", JoinType.LEFT);
            predicate.getExpressions().add( cb.equal( join.get("id"), bookId));
            return predicate;
        };
        return specification;
    }
}
