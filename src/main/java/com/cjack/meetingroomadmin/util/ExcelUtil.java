package com.cjack.meetingroomadmin.util;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.util.List;

public class ExcelUtil {

    /**
     * 读取excel文件数据
     * @param fileUrl
     * @param titleRows   标题行行号
     * @param headerRows  表格头总行数
     * @param pojoClass   表格列对应字段实体
     * @param <T>
     * @return
     * @throws FileNotFoundException
     */
    public static <T> List<T> importExcel( String fileUrl, Integer titleRows, Integer headerRows, Class<T> pojoClass) throws FileNotFoundException {
        File file = new File( fileUrl);
        if( !file.exists()){
            throw new FileNotFoundException();
        }
        InputStream is = new FileInputStream( file);
        ImportParams params = new ImportParams();
        params.setTitleRows( titleRows);
        params.setHeadRows( headerRows);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel( is, pojoClass, params);
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public static void exportExcel( List<?> list, String title, String sheetName, Class<?> pojoClass, String filePath) throws IOException {
        ExportParams exportParams = new ExportParams( title, sheetName);
        Workbook workbook = ExcelExportUtil.exportExcel( exportParams, pojoClass, list);
        File file = new File( filePath);
        OutputStream os = new FileOutputStream( file);
        workbook.write( os);
        workbook.close();
        os.flush();
        os.close();
    }

}
