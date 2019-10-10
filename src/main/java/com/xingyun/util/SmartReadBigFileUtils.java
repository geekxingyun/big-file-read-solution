package com.xingyun.util;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 功能: 大文件读取工具类
 * @author 星云
 * 时间: 2019/9/3 16:37
 */
@Slf4j
public final class SmartReadBigFileUtils {
    /**
     * 禁用构造方法
     */
    private SmartReadBigFileUtils(){}

    /**
     * 检查文件是否存在
     * @param testFileName
     * @return
     */
    @SuppressWarnings("unused")
    public static Boolean checkFileExist(String testFileName){
        File file=new File(testFileName);
        if(!file.exists()){
            log.error("file not found:"+file.getAbsolutePath());
            return false;
        }else{
            return true;
        }
    }
    /**
     * 通过Scanner 扫描快速扫描读取每一行
     * Scanner--->FileInputStream--->File
     * @param filePath
     * @return
     */
    @SuppressWarnings("unused")
    public static List<String> readBigFileByScanner(String filePath){
        List<String> readLineDataList=new ArrayList<>();
        FileInputStream fileInputStream=null;
        Scanner scanner=null;
        try {
            fileInputStream=new FileInputStream(filePath);
            scanner=new Scanner(fileInputStream,"UTF-8");
            while (scanner.hasNextLine()){
                String currentLineData=scanner.nextLine();
                readLineDataList.add(currentLineData);
            }
        } catch (FileNotFoundException e) {
           log.error("File not found Exception:",e);
        }finally {
            if(null!=scanner){
                scanner.close();
            }
            if(null!=fileInputStream){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    log.error("close FileInputStream Exception:"+e);
                }
            }
        }
        return readLineDataList;
    }

    /**
     *通过Common IO 快速读取文件每一行
     * @param filePath
     * @return
     */
    @SuppressWarnings("unused")
    public static List<String> readBigFileByCommonIO(String filePath){
        List<String> readLineDataList=new ArrayList<>();
        LineIterator lineIterator= null;
        try {
            lineIterator = FileUtils.lineIterator(new File(filePath),"UTF-8");
            while (lineIterator.hasNext()){
                String currentLineData=lineIterator.nextLine();
                readLineDataList.add(currentLineData);
            }
        } catch (IOException e) {
            log.error("IO Exception:",e);
        }finally {
            if(null!=lineIterator){
                try {
                    lineIterator.close();
                } catch (IOException e) {
                    log.error("IO Exception:"+e);
                }
            }
        }
        return readLineDataList;
    }
    /**
     * 在内存中通过使用Google Guava快速读取文件每一行
     * @param filePath
     * @return
     */
    @SuppressWarnings("unused")
    public static List<String> readBigFileByGuava(String filePath){
        List<String> readLineDataList=null;
        try {
            readLineDataList = Files.readLines(new File(filePath),Charsets.UTF_8);
        } catch (IOException e) {
            log.error("IO Exception:"+e);
        }
        return readLineDataList;
    }

    //////////////////////////////测试失败方法//////////////////////////////////////////////////////
    /**
     * Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
     * 使用BufferedReader--->InputStreamReader---->BufferedInputStream----->FileInputStream--->File读取一个文件中所有行
     * @param filePath
     * @return 将读取文件中的所有行保存到一个List<String>中
     */
    @SuppressWarnings("unused")
    public static List<String> readBigFileAllLine(String filePath){
        List<String> readLineDataList=new ArrayList<>();
        FileInputStream fileInputStream=null;
        BufferedInputStream bufferedInputStream=null;
        BufferedReader bufferedReader=null;
        try {
            //设置5M缓存
            int bufferSize=5*1024*1024;
            //当前行
            String readCurrentLine;
            //FileInputStream--->File
            fileInputStream=new FileInputStream(new File(filePath));
            //BufferedInputStream----->FileInputStream--->File
            bufferedInputStream=new BufferedInputStream(fileInputStream);
            //BufferedReader--->InputStreamReader---->BufferedInputStream----->FileInputStream--->File
            bufferedReader=new BufferedReader(new InputStreamReader(bufferedInputStream,"UTF-8"),bufferSize);
            //读取每一行
            while (null!= (readCurrentLine=bufferedReader.readLine())){
                //处理每一行数据
                readLineDataList.add(readCurrentLine);
            }
        } catch (FileNotFoundException e) {
            log.error("File not Found Exception:"+e);
        } catch (IOException e) {
            log.error("IO exception:"+e);
        }finally {
            if(null!=bufferedReader){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    log.error("BufferReader IO Exception:"+e);
                }
            }
            if(null!=bufferedInputStream){
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    log.error("BufferedInputStream IO Exception:"+e);
                }
            }
            if(null!=fileInputStream){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    log.error("FileInputStream IO Exception:"+e);
                }
            }
        }
        return readLineDataList;
    }

    /**
     * Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
     * 使用BufferedReader读取一个文件中所有行
     * BufferedReader--->FileReader--->File
     * @param filePath
     * @return 将读取文件中的所有行保存到一个List<String>中
     */
    @SuppressWarnings("unused")
    public static List<String> readSimpleFileAllLine(String filePath){
        List<String> readLineDataList=new ArrayList<>();
        FileReader fileReader=null;
        BufferedReader bufferedReader=null;
        try {
            //设置5M缓存
            int bufferSize=5*1024*1024;
            String readCurrentLine;
            //FileReader--->File
            fileReader=new FileReader(filePath);
            //BufferedReader--->FileReader--->File
            bufferedReader=new BufferedReader(fileReader,bufferSize);
            //读取每一行
            while (null!= (readCurrentLine=bufferedReader.readLine())){
                readLineDataList.add(readCurrentLine);
            }
        } catch (FileNotFoundException e) {
            log.error("File not Found Exception:"+e);
        } catch (IOException e) {
            log.error("IO exception:"+e);
        }finally {
            if(null!=bufferedReader){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    log.error("BufferReader IO Exception:"+e);
                }
            }
            if(null!=fileReader){
                try {
                    fileReader.close();
                } catch (IOException e) {
                    log.error("FileReader IO Exception:"+e);
                }
            }
        }
        return readLineDataList;
    }

    /**
     * FileChannel---->FileInputStream--->File
     * @param filePath
     * @return 返回字节
     */
    public static long readBigFileByFileChannel(String filePath){
        long counts=0;
        FileInputStream fileInputStream=null;
        FileChannel fileChannel=null;
        ByteBuffer byteBuffer;
        try {
            fileInputStream=new FileInputStream(new File(filePath));
            fileChannel=fileInputStream.getChannel();
            byteBuffer=ByteBuffer.allocate(2048);
            int offset;
            while ((offset=fileChannel.read(byteBuffer))!=-1){
                counts=counts+offset;
                byteBuffer.clear();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null!=fileChannel){
                try {
                    fileChannel.close();
                } catch (IOException e) {
                    log.error("Close FileChannel IO Exception:"+e);
                }
            }
            if(null!=fileInputStream){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    log.error("Close FileInputStream IO Exception:"+e);
                }
            }
        }
        return counts;
    }

    /**
     *
     * @param filePath
     * @return
     */
    public static List<String> readBigFileByMappedByteBuffer(String filePath){
        List<String> readLineDataList=new ArrayList<>();
        CharBuffer charBuffer;
        Path path= Paths.get(filePath);
        FileChannel fileChannel=null;
        try {
            fileChannel=FileChannel.open(path);
            long fileChannelSize=fileChannel.size();
            MappedByteBuffer mappedByteBuffer=fileChannel.map(FileChannel.MapMode.READ_ONLY,0,fileChannelSize);
            if(null!=mappedByteBuffer){
                charBuffer= Charset.forName("UTF-8").decode(mappedByteBuffer);
                readLineDataList.add(charBuffer.toString());
            }
        } catch (IOException e) {
            log.error("IO exception:"+e);
        }finally {
            if(null!=fileChannel){
                try {
                    fileChannel.close();
                } catch (IOException e) {
                    log.error("Close FileChannel IO Exception:"+e);
                }
            }
        }
        return readLineDataList;
    }

    public static List<String> readBigFileByFileInputStream(String filePath){
        List<String> readLineDataList=new ArrayList<>();
        FileInputStream fileInputStream=null;
        try {
            fileInputStream=new FileInputStream(new File(filePath));
            int readByteData;
            while ((readByteData=fileInputStream.read())!=-1){
                readLineDataList.add(String.valueOf(readByteData));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
           log.error("IO Exception:"+e);
        }finally {
            if(null!=fileInputStream){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    log.error("Close FileInputStream IO Exception:"+e);
                }
            }
        }
        return readLineDataList;
    }

    public static List<String> readBigFileByBufferedInputStream(String filePath){
        List<String> readLineDataList=new ArrayList<>();
        FileInputStream fileInputStream=null;
        BufferedInputStream bufferedInputStream=null;
        try {
            fileInputStream=new FileInputStream(new File(filePath));
            bufferedInputStream=new BufferedInputStream(fileInputStream,2048);
            int readByteData;
            while ((readByteData=bufferedInputStream.read())!=-1){
                readLineDataList.add(String.valueOf(readByteData));
            }
        } catch (FileNotFoundException e) {
            log.error("File Not Found Exception:"+e);
        } catch (IOException e) {
            log.error("IO Exception:"+e);
        }finally {
            if(null!=bufferedInputStream){
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    log.error("Close BufferedInputStream IO Exception:"+e);
                }
            }
            if(null!=fileInputStream){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    log.error("Close FileInputStream IO Exception:"+e);
                }
            }
        }
        return readLineDataList;
    }


    public static List<String> readBigFileByBufferReader(String filePath){
        List<String> readLineDataList=new ArrayList<>();
        FileInputStream fileInputStream=null;
        InputStreamReader inputStreamReader=null;
        BufferedReader bufferedReader=null;
        try {
            fileInputStream=new FileInputStream(new File(filePath));
            inputStreamReader=new InputStreamReader(fileInputStream,"UTF-8");
            bufferedReader=new BufferedReader(inputStreamReader,2048);
            String currentLineData;
            while (null!=(currentLineData=bufferedReader.readLine())){
                readLineDataList.add(currentLineData);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null!=bufferedReader){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    log.error("Close BufferReader IO Exception:"+e);
                }
            }
            if(null!=inputStreamReader){
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    log.error("Close InputStreamReader IO Exception:"+e);
                }
            }
            if(null!=fileInputStream){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    log.error("Close FileInputStream IO Exception:"+e);
                }
            }
        }
        return readLineDataList;
    }
}
