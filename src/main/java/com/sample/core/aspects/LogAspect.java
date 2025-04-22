package com.sample.core.aspects;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
@Component
public class LogAspect {

	@Before(value = "execution(* com.sample.core.service.PostService.*(..))")
	public void beforeLog() {
		log.info("before method AOP ");
	}

	@After(value = "execution(* com.sample.core.service.PostService.*(..))")
	public void afterLog() {
		log.info("after method AOP ");
	}
}
