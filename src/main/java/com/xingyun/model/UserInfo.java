package com.xingyun.model;

import com.sleepycat.persist.model.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * 功能: 用户信息
 * @Getter 通过注解生成Setter方法
 * @Setter 通过注解生成Setter方法
 * @ToString 通过注解生成ToString方法
 * @Entity 标识为
 * @author: 星云
 * 时间: 2019/9/5 15:04
 */
@Getter
@Setter
public class UserInfo {
   /**
    * 用户信息Id
    */
   @PrimaryKey(sequence = "userInfoBerkeleySequenceId")
   Long userInfoId;
   /**
    * 用户信息UUID
    */
   String userInfoUUID;
   /**
    * 用户信息Name
    */
   String userInfoName;
   /**
    * 用户信息Age
    */
   Integer userInfoAge;
   /**
    * 用户信息数据库时间戳
    */
   Timestamp operaTimeStamp;
   @Override
   public String toString() {
      return userInfoId+","+userInfoUUID+","+userInfoName+","+userInfoAge;
   }
}

