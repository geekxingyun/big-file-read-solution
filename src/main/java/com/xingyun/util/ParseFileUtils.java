package com.xingyun.util;

import com.xingyun.model.UserInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能:解析文件工具类
 * @author 星云
 * 时间: 2019/9/5 14:46
 */
@Slf4j
public class ParseFileUtils {
    private ParseFileUtils() { }

    /**
     * 解析文件
     *
     * @param readLineData
     */
    public static List<UserInfo> doProcessorFile(List<String> readLineData) {
        log.info("------对象解析开始------");
        log.info("当前要解析的总行数:" + readLineData.size());
        List<UserInfo> userInfoList=new ArrayList<>();
        UserInfo userInfo;
        for (String currentLineData : readLineData
        ) {
            // 第一行信息，为标题信息，不用,如果需要，注释掉
            String line = currentLineData;
            // CSV格式文件为逗号分隔符文件，这里根据逗号切分
            String[] item = line.split(",");
            String last = item[item.length - 1];
            //处理每一行数据
            if (last != null) {
                //处理解析保存每一行数据
                userInfo = new UserInfo();
                //当前处理统计
                //数据文件每行数据中以;分割
                String[] datas = last.split(";");
                //查看每一列
                for (int i = 0; i < datas.length; i++) {
                    //获取每一列每个单元格的值
                    String value = datas[i];
                    if (null != value && !"".equals(value)) {
                        //处理每一列数据
                        switch (i) {
                            case 0:
                                //用户Id
                                userInfo.setUserInfoId(Long.valueOf(value));
                                break;
                            case 1:
                                //用户UUID
                                userInfo.setUserInfoUUID(value);
                                break;
                            case 2:
                                //用户姓名
                                userInfo.setUserInfoName(value);
                                break;
                            case 3:
                                //用户年龄
                                userInfo.setUserInfoAge(Integer.valueOf(value));
                                break;
                            default:
                                break;
                        }
                    }
                }//end for
                userInfoList.add(userInfo);
            }//end if last!=null
        }
        log.info("------对象解析结束------");
        return userInfoList;
    }
}
