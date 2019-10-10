package com.xingyun.util;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

/**
 * 功能 写入大文件有问题 1138029行数据 最多只能写入1137498行数据
 * @author 星云
 * 时间 2019/9/5 9:04
 */
public class SmartWriteBigFileUtils {

    private SmartWriteBigFileUtils(){}

    /**
     *   按照行写入             按照流写入               按照流写入        文件
     *  BufferedWriter---->OutputStreamWriter----->FileOutputStream--->File
     * @param newFilePath
     * @param dataList
     * @return
     */
    public static Boolean writeDataToFileByBufferWriter(String newFilePath,List<String> dataList){
        FileOutputStream fileOutputStream=null;
        OutputStreamWriter outputStreamWriter=null;
        try {
            fileOutputStream=new FileOutputStream(new File(newFilePath));
            outputStreamWriter=new OutputStreamWriter(fileOutputStream);
            BufferedWriter bufferedWriter=new BufferedWriter(outputStreamWriter);
            for (String currentLineData:dataList
                 ) {
                bufferedWriter.write(currentLineData);
                bufferedWriter.newLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 读取将一个文件中的内容后全部写入到另外一个文件中
     * @param originFilePath
     * @param newFilePath
     */
    public static void writeBigFileByMappedByteBuffer(String originFilePath,String newFilePath){
        FileInputStream fileInputStream;
        FileOutputStream fileOutputStream;
        FileChannel originFileChannel;
        FileChannel newFileChannel;
        try {
            fileInputStream=new FileInputStream(new File(originFilePath));
            originFileChannel=fileInputStream.getChannel();
            fileOutputStream=new FileOutputStream(new File(newFilePath));
            newFileChannel=fileOutputStream.getChannel();
            long fileChannelSize=originFileChannel.size();
            MappedByteBuffer mappedByteBuffer=originFileChannel.map(FileChannel.MapMode.READ_ONLY,0,fileChannelSize);
            newFileChannel.write(mappedByteBuffer);
        } catch (IOException e) {
            System.err.println("IO exception:"+e);
        }
    }
}
