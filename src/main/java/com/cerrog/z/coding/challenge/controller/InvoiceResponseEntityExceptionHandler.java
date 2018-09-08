/*******************************************************************************
 * Copyright (C) 1997-2017 by TouchTunes Digital Jukebox, Inc. All Rights
 * Reserved.
 *
 * Disclosure or use in part or in whole without prior written consent
 * constitutes an infringement of copyright, punishable by law.
 ******************************************************************************/
package com.cerrog.z.coding.challenge.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ValidationException;

/**
 * Created by gcerro on 2018-09-02.
 */
@ControllerAdvice
@Slf4j
public class InvoiceResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { ValidationException.class })
	protected ResponseEntity<Object> handleValidationError(
			RuntimeException ex, WebRequest request) {
		String bodyOfResponse = ex.getMessage();
		log.error(ex.getMessage());
		return handleExceptionInternal(ex, bodyOfResponse,
				new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(value = { RuntimeException.class })
	protected ResponseEntity<Object> handleServerError(
			RuntimeException ex, WebRequest request) {
		String bodyOfResponse = "Something's wrong on the backend: " + ex.getMessage();
		log.error(bodyOfResponse);
		return handleExceptionInternal(ex, bodyOfResponse,
				new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
}
