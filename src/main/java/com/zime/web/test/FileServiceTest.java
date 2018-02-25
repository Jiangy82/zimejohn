package com.zime.web.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zime.web.config.FtpConfig;
import com.zime.web.service.OrderService;
import com.zime.web.service.ProductService;
import com.zime.web.service.TestService;
import com.zime.web.util.FTPUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@EnableConfigurationProperties({FtpConfig.class})
public class FileServiceTest {
	@Autowired
	private FtpConfig ftpConfig;

	@Autowired
	private TestService testService;
	@Autowired
	private ProductService productService;
	@Autowired
	private OrderService orderService;


	@Test
	public void test() {
		System.out.println(ftpConfig.getServerIp()+ftpConfig.getServerHttpPrefix()+ftpConfig.getUser());
	}

	@Test
	public void test1() {
		System.out.println(testService.connectTest());
	}
	@Test
	public void test2() {
		System.out.println(productService.getProductByKeywordCategory("美的", 100006, 1, 2, "price_asc").getData());
	}
	@Test
	public void test3() {
		System.out.println(orderService.pay(1491753014256L, 1, "upload"));
	}
	@Test
	public void test4() {
		System.out.println(orderService.getOrderList(1, 1, 2));
	}	
}
