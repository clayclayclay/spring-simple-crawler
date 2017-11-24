package com.max;

import com.max.constants.CityConstants;
import com.max.service.MainService;
import com.max.util.RequestUrlUtil;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import us.codecraft.webmagic.Spider;

@SpringBootApplication
public class SpringSimpleCrawlerApplication implements CommandLineRunner, ApplicationContextAware
{

	private ApplicationContext applicationContext;

	public static void main(String[] args)
	{
		SpringApplication.run(SpringSimpleCrawlerApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception
	{
		MainService mainService = applicationContext.getBean("mainService", MainService.class);
		if (mainService != null)
		{
			mainService.setCityId(CityConstants.CITY_BEIJING_ID);
            mainService.setCityName(CityConstants.CITY_BEIJING_NAME);
//			mainService.setCityId(CityConstants.CITY_CHENGDU_ID);
//            mainService.setCityName(CityConstants.CITY_CHENGDU_NAME);
//			mainService.setCityId(CityConstants.CITY_SHANGHAI_ID);
//            mainService.setCityName(CityConstants.CITY_SHANGHAI_NAME);
//			mainService.setCityId(CityConstants.CITY_GUANGZHOU_ID);
//            mainService.setCityName(CityConstants.CITY_GUANGZHOU_NAME);
//			mainService.setCityId(CityConstants.CITY_SHENZHEN_ID);
//            mainService.setCityName(CityConstants.CITY_SHENZHEN_NAME);

			//logic run
			Spider.create(mainService).addUrl(
					RequestUrlUtil.getHotelListRequestForGet(
							0, 20, mainService.getCityId())
					).thread(5).run();
		}

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
	{
		this.applicationContext = applicationContext;
	}
}
