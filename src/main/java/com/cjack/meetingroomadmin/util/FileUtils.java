package com.cjack.meetingroomadmin.util;

import java.io.File;
import java.util.Date;
import java.util.UUID;

public class FileUtils {

    /**
     * 获取文件后缀
     * @param fileName
     * @return
     */
    private static String getSuffix(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 生成新的文件名
     * @param fileOriginName 源文件名
     * @return
     */
    public static String getFileName( String fileOriginName){
        return UUID.randomUUID() + getSuffix( fileOriginName);
    }

    /**
     * 生成新的xlsx文件名
     * @return
     */
    public static String getXlsxFileName(){
   //     return UUID.randomUUID() + ".xlsx";
        return UUID.randomUUID() + ".xls";
    }

    /**
     * 生成新的随机数路径名
     * @return
     */
    public static String getRandomPath(){
        //     return UUID.randomUUID() + ".xlsx";
        return UUID.randomUUID() + "";
    }

    /**
     * 根据年月日获取目录的路径
     * @return
     */
    public static String getDirByDate(){
        return DateFormatUtil.format( new Date(), DateFormatUtil.DATE_RULE_2);
    }

    /**
     * 判断如果对应的目录不存在则自动创建
     * @param dir
     */
    public static void handleDir( String dir){
        File file = new File( dir);
        if( !file.exists()){
            file.mkdirs();
        }
    }

    /**
     * 递归删除目录及其下面的所有目录和文件
     * @param dir
     */
    public static void delDir( String dir){
        File dirFile = new File( dir);
        if( dirFile.isDirectory()){
            String[] children = dirFile.list();
            for (int i=0 ; i< children.length ; i++) {
                delDir( dir + "/" + children[i]);
            }
        }
        dirFile.delete();
    }

    /**
     * 删除文件
     * @param filePath
     */
    private static void delFile( String filePath){
        File dirFile = new File( filePath);
        dirFile.delete();
    }


    /**
     * 将客户端访问路径转换为服务器路径，需要处理地址带ip的情况
     * @param clientFileUrl
     * @return
     */
    public static String transferClientFileUrl( String clientFileUrl, String baseServerDir, String baseClientDir){
        String relationFilePath = clientFileUrl.split( baseClientDir)[1];
        return baseServerDir + relationFilePath;
    }

    /**
     * 根据客户端访问路径来删除服务器上的文件
     * @param clientFileUrl
     */
    public static void delFile( String clientFileUrl, String baseServerDir, String baseClientDir){
        String serverPath = transferClientFileUrl( clientFileUrl, baseServerDir, baseClientDir);
        delFile( serverPath);
    }
}
