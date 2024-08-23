package com.example.gamehubbackend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class GamehubBackendApplicationTests {


    @Autowired
    private BeanFactoryPostProcessor forceAutoProxyCreatorToUseClassProxying;

	@Test
	void contextLoads() {
		int actual = 1;
		assertEquals(1,actual);
	}

}
