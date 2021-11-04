package com.cjack.meetingroomadmin.service;

import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.dao.BookUnitWordDao;
import com.cjack.meetingroomadmin.dao.DictionaryWordDao;
import com.cjack.meetingroomadmin.excel.DictionaryWordExcel;
import com.cjack.meetingroomadmin.exception.DictionaryWordIsUsingException;
import com.cjack.meetingroomadmin.model.DictionaryWordModel;
import com.cjack.meetingroomadmin.table.BookUnitWordTable;
import com.cjack.meetingroomadmin.table.DictionaryWordTable;
import com.cjack.meetingroomadmin.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.zeroturnaround.zip.ZipUtil;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DictionaryService {

    private static Logger log = LoggerFactory.getLogger(DictionaryService.class);

    @Value("${file.word.engDir}")
    private String engDir;
    @Value("${file.word.usaDir}")
    private String usaDir;
    @Value("${file.upload.baseServerDir}")
    String baseServerDir;
    @Value("${file.upload.baseClientDir}")
    String baseClientDir;
    @Value("${file.upload.spelitor}")
    String spelitor;
    @Value("${file.upload.serverUrl}")
    String serverUrl;
    @Autowired
    private DictionaryWordDao dao;
    @Autowired
    private BookUnitWordDao bookUnitWordDao;

    public void list( LayPage layPage, DictionaryWordModel model){
        Sort sort = new Sort( Sort.Direction.DESC, "createTime");
        Pageable pageable = new PageRequest( layPage.getPage()-1, layPage.getLimit(), sort);
        Specification<DictionaryWordTable> specification = handleConditon( model.getWord(), model.getRootAffixes());
        Page<DictionaryWordTable> pageTable = dao.findAll( specification, pageable);
        List<DictionaryWordModel> datas = new ArrayList<>();
        for( DictionaryWordTable wordTable : pageTable.getContent()){
            DictionaryWordModel data = ModelUtils.copySignModel( wordTable, DictionaryWordModel.class);
            datas.add( data);
        }
        layPage.setData( datas);
        layPage.setCount( Long.valueOf( pageTable.getTotalElements()).intValue());
    }

    public void del( String ids) throws DictionaryWordIsUsingException {
        List<DictionaryWordTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            DictionaryWordTable table = dao.getOne( Long.valueOf( id));
            //判断该单词是否已经被课本引用，如果被引用了，不能删除
            Specification<BookUnitWordTable> specification = handleJoinConditon( table.getWord(), table.getRootAffixes());
            List<BookUnitWordTable> usedTables = bookUnitWordDao.findAll( specification);
            if( EmptyUtil.isNotEmpty( usedTables)){
                throw new DictionaryWordIsUsingException();
            }
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( DictionaryWordModel model){
        DictionaryWordTable table;
        if( EmptyUtil.isNotEmpty( model.getId())){
            table = dao.getOne( model.getId());
            ModelUtils.copySignModel( model, table);
        }else{
            table = ModelUtils.copySignModel( model, DictionaryWordTable.class);
        }
        dao.save( table);
    }

    public String exportChooseWord( String ids) throws IOException {
        if( EmptyUtil.isNotEmpty( ids)){
            String[] idArr = ids.split( ",");
            //先找到需要导出的单词
            List<DictionaryWordExcel> excelWords = new ArrayList<>( idArr.length);
            for( String id : idArr){
                DictionaryWordTable wordTable = dao.getOne( Long.valueOf( id));
                DictionaryWordExcel excelWord = ModelUtils.copySignModel( wordTable, DictionaryWordExcel.class);
                excelWord.setOrderNum( wordTable.getId() + "");
                excelWords.add( excelWord);
            }
            String fileName = "exportDictionaryWords_" + DateFormatUtil.format( new Date(), DateFormatUtil.DATE_RULE_2) + ".xls";
            String dumic =  "exportDictionaryWords" + spelitor + FileUtils.getDirByDate();//动态的这一截路径
            //服务器保存文件的目录
            String destDir = baseServerDir + dumic;
            //供浏览器客户端访问的目录
            String clientDir = baseClientDir + dumic;
            //先判断当前的目录是否存在
            FileUtils.handleDir( destDir);
            String serverFilePath = destDir  + spelitor + fileName;
            //导出单词数据到excel
            ExcelUtil.exportExcel( excelWords, "词库单词", "词库单词", DictionaryWordExcel.class, serverFilePath);
            //客户端访问文件的地址
            String clientFilePath = serverUrl + clientDir  + spelitor + fileName;
            return clientFilePath;
        }
        return "";
    }

    public String exportAudioLoseWord() throws IOException {
        List<DictionaryWordTable> audioLoseWords = dao.findAllByWordAudioUrlIsNullOrUsaAudioUrlIsNull();
        List<DictionaryWordExcel> excelWords = new ArrayList<>( audioLoseWords.size());
        for( DictionaryWordTable audioLoseWord : audioLoseWords){
            DictionaryWordExcel excelWord = ModelUtils.copySignModel( audioLoseWord, DictionaryWordExcel.class);
            excelWord.setOrderNum( audioLoseWord.getId() + "");
            if( EmptyUtil.isNotEmpty( audioLoseWord.getWordAudioUrl())){
                excelWord.setAudioUrl( "美式音频缺失");
            }else if( EmptyUtil.isNotEmpty( audioLoseWord.getUsaAudioUrl())){
                excelWord.setAudioUrl( "英式音频缺失");
            }else{
                excelWord.setAudioUrl( "全部缺失");
            }
            excelWords.add( excelWord);
        }
        String fileName = "loseAudioDictionaryWords_" + DateFormatUtil.format( new Date(), DateFormatUtil.DATE_RULE_2) + ".xls";
        String dumic =  "loseAudioDictionaryWords" + spelitor + FileUtils.getDirByDate();//动态的这一截路径
        //服务器保存文件的目录
        String destDir = baseServerDir + dumic;
        //供浏览器客户端访问的目录
        String clientDir = baseClientDir + dumic;
        //先判断当前的目录是否存在
        FileUtils.handleDir( destDir);
        String serverFilePath = destDir  + spelitor + fileName;
        //导出单词数据到excel
        ExcelUtil.exportExcel( excelWords, "词库丢失音频文件单词", "词库丢失音频文件单词", DictionaryWordExcel.class, serverFilePath);
        //客户端访问文件的地址
        String clientFilePath = serverUrl + clientDir  + spelitor + fileName;
        return clientFilePath;
    }

    public void improtWords( String fileUrl) throws FileNotFoundException {
        //需要先将 excel的浏览器端访问路径 转化为服务器端访问路径
        fileUrl = FileUtils.transferClientFileUrl( fileUrl, baseServerDir, baseClientDir);
        List<DictionaryWordExcel> excelWords = ExcelUtil.importExcel( fileUrl, 0, 1, DictionaryWordExcel.class);
        if( excelWords != null && excelWords.size() != 0){
            for( DictionaryWordExcel excelWord : excelWords){
                //先校验这个是否已经导入这个单词
                DictionaryWordTable dictionaryWordTable = dao.getByWord( excelWord.getWord());
                if( dictionaryWordTable != null){
                    continue;
                }
                //保存单词
                dictionaryWordTable = ModelUtils.copySignModel( excelWord, DictionaryWordTable.class);
                dictionaryWordTable.setCreateTime( new Date());
                dictionaryWordTable.setUpdateTime( new Date());
                dao.save( dictionaryWordTable);
            }
        }
    }

    public void uploadWordsAudio( MultipartFile audioFile, String type) throws IOException {
        String fileName = audioFile.getOriginalFilename();
        String word = fileName.substring( 0, fileName.lastIndexOf( "."));
        DictionaryWordTable dictionaryWordTable = dao.getByWord( word);
        if( dictionaryWordTable == null){
            return;
        }
        if( type.equals( "1")){//1 英式音频
            FileUtils.handleDir( engDir);
            String filePath = engDir + fileName;
            File destFile = new File( filePath);
            audioFile.transferTo( destFile);
            dictionaryWordTable.setWordAudioUrl( "audio/eng/" + fileName);
        }else{//2 美式音频
            FileUtils.handleDir( usaDir);
            String filePath = usaDir + fileName;
            File destFile = new File( filePath);
            audioFile.transferTo( destFile);
            dictionaryWordTable.setWordAudioUrl( "audio/usa/" + fileName);
        }
        dao.save( dictionaryWordTable);
    }

    /**
     * 上传单词音频文件压缩包
     * @param audioZipFilePath
     * @param type
     */
    public void uploadWordsAudioZip( String audioZipFilePath, String type){
        //文件地址转化为服务器上文件地址
        audioZipFilePath = FileUtils.transferClientFileUrl( audioZipFilePath, baseServerDir, baseClientDir);
        File zipFile = new File( audioZipFilePath);
        if( type.equals( "1")){//1 英式音频
            ZipUtil.unpack( zipFile, new File( engDir), s -> {
                //取最后的值；
                String[] st = s.split("/");
                String  filename = st[ st.length-1];
                //不是文件的情况下，过滤；
                if( filename.contains( "_") || !filename.contains( ".")){
                    return null;
                }
                String word = filename.substring( 0, filename.lastIndexOf( "."));
                //查找对应的单词
                DictionaryWordTable wordTable = dao.getByWord( word);
                if( wordTable == null){
                    return null;
                }
                //保存单词音频路径
                wordTable.setWordAudioUrl( "audio/eng/" + filename);
                dao.save( wordTable);
                return filename;
            });
        }else{//2 美式音频
            ZipUtil.unpack( zipFile, new File( usaDir), s -> {
                //取最后的值；
                String[] st = s.split("/");
                String  filename = st[ st.length-1];
                //不是文件的情况下，过滤；
                if( filename.contains( "_") || !filename.contains( ".")){
                    return null;
                }
                String word = filename.substring( 0, filename.lastIndexOf( "."));
                //查找对应的单词
                DictionaryWordTable wordTable = dao.getByWord( word);
                if( wordTable == null){
                    return null;
                }
                //保存单词音频路径
                wordTable.setWordAudioUrl( "audio/usa/" + filename);
                dao.save( wordTable);
                return filename;
            });
        }

    }

    public void scanWordAudio(){
        List<DictionaryWordTable> words = dao.findAll();
        for( DictionaryWordTable word : words){
            String engAudioUrl = engDir + word.getWord() + ".mp3";
            File engAudioFile = new File( engAudioUrl);
            if( !engAudioFile.exists()){
                log.info( engAudioUrl + "，对应文件不存在，将单词音频置空");
                word.setWordAudioUrl( null);
                dao.save( word);
            }
            String usaAudioUrl = usaDir + word.getWord() + ".mp3";
            File usaAudioFile = new File( usaAudioUrl);
            if( !usaAudioFile.exists()){
                log.info( engAudioUrl + "，对应文件不存在，将单词音频置空");
                word.setUsaAudioUrl( null);
                dao.save( word);
            }
        }
    }

    private Specification<DictionaryWordTable> handleConditon( String word, String rootAffixes){
        Specification< DictionaryWordTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if( EmptyUtil.isNotEmpty( word)){
                predicate.getExpressions().add( cb.like( root.get( "word"), "%" + word + "%"));
            }
            if( EmptyUtil.isNotEmpty( rootAffixes)){
                predicate.getExpressions().add( cb.like( root.get( "rootAffixes"), "%" + rootAffixes + "%"));
            }
            return predicate;
        };
        return specification;
    }

    private Specification<BookUnitWordTable> handleJoinConditon( String word, String rootAffixes){
        Specification< BookUnitWordTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            Join<BookUnitWordTable, DictionaryWordTable> wordJoin = root.join("dicWord", JoinType.LEFT);
            if( EmptyUtil.isNotEmpty( word)){
                predicate.getExpressions().add( cb.like( wordJoin.get( "word"), "%" + word + "%"));
            }
            if( EmptyUtil.isNotEmpty( rootAffixes)){
                predicate.getExpressions().add( cb.like( wordJoin.get( "rootAffixes"), "%" + rootAffixes + "%"));
            }

            return predicate;
        };
        return specification;
    }
}
