package net.guides.springboot2.springboot2externalizingconfproperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PropertyController {
	
	@Autowired
	private DataSourceConfig dataSourceConfig;

	private static final Logger logger = LoggerFactory.getLogger(PropertyController.class);
	
	@GetMapping("/properties")
	public void printProperties() {
		logger.debug(dataSourceConfig.getUsername());
		logger.debug(dataSourceConfig.getPassword());
		logger.debug(dataSourceConfig.getDriver());
		logger.debug(dataSourceConfig.getUrl());
		logger.debug(dataSourceConfig.toString());
	}
}
