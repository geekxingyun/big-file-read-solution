package com.xingyun.main;

import com.xingyun.model.UserInfo;
import com.xingyun.util.ParseFileUtils;
import com.xingyun.util.SmartReadBigFileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.util.RamUsageEstimator;

import java.io.File;
import java.util.List;

/**
 * 功能: 探究大文件读取解决方案
 * @author: 星云
 * 时间: 2019/9/4 13:24
 */
@Slf4j
public class ReadMainTest {
    /**
     * 当前项目根目录
     */
    private static final String projectBasePath=new File("").getAbsolutePath();
    /**
     * src/test/resources
     */
    private static final String testResourcePath=projectBasePath+File.separator+"src"+File.separator+"test"+File.separator+"resources";
    /**
     * src/test/resources/test.csv
     */
    private static final String testFileName=testResourcePath+File.separator+"test.csv";
    /**
     * 大文件读取解决方案切换开关
     */
    private static final int bigFileReadSolutionSwitch=1;

    /**
     * 存放读取数据到List<String>
     */
    private static List<String> readLineDataList= null;
    /**
     * 存放解析后的数据存放到List<User>
     */
    private static List<UserInfo> userInfoList=null;

    public static void main(String[] args) {
        //开始时间
        Long startTime=System.currentTimeMillis();
        switch (bigFileReadSolutionSwitch){
            case 1:
                //第一种解决方案 可用 不在内存中读取 使用Common IO库提供的方法 耗时5500 默认UTF-8
                readLineDataList= SmartReadBigFileUtils.readBigFileByCommonIO(testFileName);
                break;
            case 2:
                //1138029行数据 1137498
                //第二种解决方案 可用 在内存中读取 使用Google Guava库提供的方法 耗时5969 默认UTF-8
                readLineDataList= SmartReadBigFileUtils.readBigFileByGuava(testFileName);
                break;
            case 3:
                //第三种解决方案 可用 不在内存中读取 Scanner-->FileInputStream---> File 6704 默认UTF-8
                readLineDataList = SmartReadBigFileUtils.readBigFileByScanner(testFileName);
                break;
            case 4:
                //第四种解决方案 可用 带缓冲的字符流 BufferedReader---->InputStreamReader---->FileInputStream--->File 6094毫秒  默认UTF-8
                readLineDataList= SmartReadBigFileUtils.readBigFileByBufferReader(testFileName);
                break;
            case 5:
                //第五种解决方案 不可用 不带缓冲的字节流 FileInputStream---->File 超级慢 无法按照读取
                readLineDataList= SmartReadBigFileUtils.readBigFileByFileInputStream(testFileName);
                break;
            case 6:
                //第六种解决方案 不可用 带缓冲的字节流 BufferedInputStream---->FileInputStream--->File 很慢 无法按照行读取
                readLineDataList= SmartReadBigFileUtils.readBigFileByBufferedInputStream(testFileName);
                break;
            case 7:
                //第七种解决方案 不可用 NIO方式 FileChannel----->FileInputStream---->File 耗时78秒 无法按照行读取,无字符编码,无友好显示内容
                long byteContent= SmartReadBigFileUtils.readBigFileByFileChannel(testFileName);
                log.info("byteContent:{}",byteContent);
                break;
            case 8:
                //第八种解决方案 不可用 内存映射文件 读取速度超快,但是只能一下子返回所有数据 耗时1937 默认UTF-8 无法按照行读取
                readLineDataList= SmartReadBigFileUtils.readBigFileByMappedByteBuffer(testFileName);
                break;
                default:
                    log.error("无效的解决方案开关参数");
                    break;
        }
        Long endTime=System.currentTimeMillis();
        log.info("读取csv文件到List<String>集合执行耗时:{}毫秒",(endTime-startTime));

        if(null!=readLineDataList){
            log.info("------读取List<String>集合存储情况start------");
            //打印对象信息
            showFileDataInfo(readLineDataList);
            //显示对象在内存中的使用情况
            showObjectJVMInfo(readLineDataList);
            log.info("------读取List<String>集合存储情况end------");
        }

        //解析List<String> 到List<User>
        startTime=System.currentTimeMillis();
        userInfoList=ParseFileUtils.doProcessorFile(readLineDataList);
        endTime=System.currentTimeMillis();
        log.info("解析成对象执行耗时:"+(endTime-startTime)+"毫秒");

        if(null!=userInfoList){
            log.info("------读取List<UserInfo>集合存储情况start------");
            //打印对象信息
            showFileDataInfo(readLineDataList);
            //显示对象在内存中的使用情况
            showObjectJVMInfo(readLineDataList);
            log.info("------读取List<UserInfo>集合存储情况end------");
        }
        //使用完毕销毁读取的数据List<String>
        cleanDataFreeJVM(readLineDataList);
        //使用完毕销毁解析的数据List<UserInfo>
        cleanDataFreeJVM(userInfoList);
    }

    /**
     * 显示List<Object>对象
     * @param readLineDataList
     */
    private static void showFileDataInfo(List<? extends Object> readLineDataList){
        Boolean debug=false;
        long count=0;
        for (Object object:readLineDataList
             ) {
            if(debug){
                log.info("当前第"+(count)+"行:"+object);
            }
            count++;
        }
        log.info("当前List<String>中一共有"+count+"行数据");
    }

    /**
     * 显示对象内存占用情况
     * @param object
     */
    private static void showObjectJVMInfo(Object object){
       log.info("计算指定对象及其引用树上的所有对象的综合大小，单位字节:"+RamUsageEstimator.sizeOf(object));
       log.info("计算指定对象本身在堆空间的大小，单位字节:"+RamUsageEstimator.shallowSizeOf(object));
       log.info("计算指定对象及其引用树上的所有对象的综合大小，返回可读的结果:"+RamUsageEstimator.humanSizeOf(object));
    }
    /**
     * 清理释放空间
     * @param readLineDataList
     */
    private static void cleanDataFreeJVM(List<? extends Object> readLineDataList){
        readLineDataList.clear();
        readLineDataList=null;
        if(readLineDataList==null){
           log.info("\r\n数据清理成功");
        }else{
            log.error("\r\n数据清理失败,集合不为空:"+readLineDataList.size());
        }
    }
}
