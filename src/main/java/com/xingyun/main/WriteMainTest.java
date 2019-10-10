package com.xingyun.main;

import com.xingyun.model.UserInfo;
import com.xingyun.util.SmartWriteBigFileUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author 星云
 * @功能
 * @日期和时间 9/10/2019 9:02 PM
 */
@Slf4j
public class WriteMainTest {
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

    private static final List<String> resultList=new ArrayList<>();
    public static void main(String[] args) {
        Long startTime= System.currentTimeMillis();
        //生成一千万条测试数据 1 GB左右数据
        int length=10000000;
        for (int i = 0; i <length ; i++) {
            UserInfo userInfo=new UserInfo();
            userInfo.setUserInfoId(Long.valueOf(i));
            userInfo.setUserInfoUUID(UUID.randomUUID().toString().replace("-",""));
            userInfo.setUserInfoName("testUser"+i);
            userInfo.setUserInfoAge(18);
            resultList.add(userInfo.toString());
        }
        SmartWriteBigFileUtils.writeDataToFileByBufferWriter(testFileName,resultList);
        Long endTime= System.currentTimeMillis();

        //测试数据生成共耗时:23592毫秒
        log.info("测试数据生成共耗时:{}毫秒",(endTime-startTime));
    }
}
