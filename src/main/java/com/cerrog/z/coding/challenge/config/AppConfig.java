/*******************************************************************************
 * Copyright (C) 1997-2017 by TouchTunes Digital Jukebox, Inc. All Rights
 * Reserved.
 *
 * Disclosure or use in part or in whole without prior written consent
 * constitutes an infringement of copyright, punishable by law.
 ******************************************************************************/
package com.cerrog.z.coding.challenge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * Created by gcerro on 2018-09-06.
 */
@Configuration
public class AppConfig {

	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
		return new MethodValidationPostProcessor();
	}

}
