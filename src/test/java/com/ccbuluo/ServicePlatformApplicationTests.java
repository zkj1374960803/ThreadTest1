package com.ccbuluo;

import com.ccbuluo.date.DateUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class ServicePlatformApplicationTests {

    @Test
    public void contextLoads() {
        ArrayList<String> ids = Lists.newArrayList();
        ids.add("1");
        ids.add("2");
        ids.add("3");
        ArrayList<String> data = Lists.newArrayList();
        data.add("3");
        data.add("2");
        data.add("6");
        ids.removeAll(data);
        System.out.print("asdasd");
    }
//    StringUtils.isNoneBlank
    @Test
    public void isNoneBlank() {
        boolean flag = StringUtils.isNoneBlank("asd", "asdas");
        System.out.print(flag);
    }
//    1534262400
    @Test
    public void TestTime() {
        Long i = 1534262400L;

        Date date = new Date(1533052800000L);
        Timestamp parse = DateUtils.parse(date);
        System.out.print(parse);
    }

}
