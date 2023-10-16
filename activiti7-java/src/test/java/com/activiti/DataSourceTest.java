package com.activiti;

import com.activiti.modules.entity.SysUserEntity;
import com.activiti.modules.service.SysUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author liuguofeng
 * @date 2023/10/16 11:50
 **/
@SpringBootTest
public class DataSourceTest {
    @Autowired
    private SysUserService userService;

    @Test
    public void demo01(){
        List<SysUserEntity> list = userService.list();
        System.out.println();
    }
}
