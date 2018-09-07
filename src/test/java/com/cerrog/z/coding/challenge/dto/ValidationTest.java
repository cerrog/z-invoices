/*******************************************************************************
 * Copyright (C) 1997-2017 by TouchTunes Digital Jukebox, Inc. All Rights
 * Reserved.
 *
 * Disclosure or use in part or in whole without prior written consent
 * constitutes an infringement of copyright, punishable by law.
 ******************************************************************************/
package com.cerrog.z.coding.challenge.dto;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by gcerro on 2018-09-06.
 */
public class ValidationTest {

	private static ValidatorFactory validatorFactory;
	private static Validator validator;

	private Random random = new Random(100);

	@BeforeClass
	public static void createValidator() {
		validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	@AfterClass
	public static void close() {
		validatorFactory.close();
	}

	@Test
	public void shouldBeValid() {
		InvoiceDto dto = createValidDto();
		Set<ConstraintViolation<InvoiceDto>> violations = validator.validate(dto);
		assertTrue(violations.isEmpty());
	}

	@Test
	public void shouldHaveInvalidInvoiceId() {
		InvoiceDto dto = createValidDto();
		dto.setInvoiceId("");
		Set<ConstraintViolation<InvoiceDto>> violations = validator.validate(dto);
		assertEquals(1, violations.size());
	}

	@Test
	public void shouldHaveInvalidPurchaseOrderNo() {
		InvoiceDto dto = createValidDto();
		dto.setPurchaseOrderNo("");
		Set<ConstraintViolation<InvoiceDto>> violations = validator.validate(dto);
		assertEquals(1, violations.size());
	}

	@Test
	public void shouldHaveInvalidDueDate() {
		InvoiceDto dto = createValidDto();
		dto.setDueDate(null);
		Set<ConstraintViolation<InvoiceDto>> violations = validator.validate(dto);
		assertEquals(1, violations.size());
	}

	@Test
	public void shouldHaveInvalidAmount() {
		InvoiceDto dto = createValidDto();
		dto.setAmountInCents(null);
		Set<ConstraintViolation<InvoiceDto>> violations = validator.validate(dto);
		assertEquals(1, violations.size());
	}

	private InvoiceDto createValidDto() {
		long value = random.nextLong();
		LocalDateTime now = LocalDateTime.now();
		InvoiceDto dto = new InvoiceDto();
		dto.setAmountInCents(value * 100);
		dto.setDueDate(now.plusDays(15).toLocalDate());
		dto.setInvoiceId("INV-" + value);
		dto.setPurchaseOrderNo("PO-" + value);
		return dto;
	}
}
