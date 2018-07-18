package com.ccbuluo;

import com.ccbuluo.business.constants.CodePrefixEnum;
import com.ccbuluo.business.platform.projectcode.service.GenerateProjectCodeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServicePlatformApplicationTests {

    @Autowired
    private GenerateProjectCodeService codeService;

    @Test
    public void contextLoads() {
        codeService.grantCode(CodePrefixEnum.FC);

    }

}
