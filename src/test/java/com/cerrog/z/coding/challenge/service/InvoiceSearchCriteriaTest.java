/*******************************************************************************
 * Copyright (C) 1997-2017 by TouchTunes Digital Jukebox, Inc. All Rights
 * Reserved.
 *
 * Disclosure or use in part or in whole without prior written consent
 * constitutes an infringement of copyright, punishable by law.
 ******************************************************************************/
package com.cerrog.z.coding.challenge.service;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.*;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by gcerro on 2018-09-04.
 */
public class InvoiceSearchCriteriaTest {

	@Test
	public void testDefaultConstructor() {
		InvoiceSearchCriteria criteria = new InvoiceSearchCriteria();
		assertNull(criteria.getInvoiceNo());
		assertNull(criteria.getPurchaseOrderNo());
		assertEquals(Integer.valueOf(20), criteria.getLimit());
		assertEquals(Integer.valueOf(0), criteria.getOffset());
	}

	@Test
	public void testValidationOnlyOneSearchCriteria() {
		InvoiceSearchCriteria criteria = new InvoiceSearchCriteria(null, "PUR-123", 20, 0);
		assertTrue(criteria.isSearchByPurchaseNumber());

		criteria = new InvoiceSearchCriteria("INV-123", null, 20, 0);
		assertTrue(criteria.isSearchByInvoice());
	}

	@Test
	public void testValidationNoSearchCriteria() {
		InvoiceSearchCriteria criteria = new InvoiceSearchCriteria(null, null, 20, 0);
		assertFalse(criteria.isSearchByPurchaseNumber());
		assertFalse(criteria.isSearchByInvoice());
	}

	@Test(expected = ValidationException.class)
	public void testValidationOnlyTwoSearchCriterias() {
		InvoiceSearchCriteria criteria = new InvoiceSearchCriteria("INV-123", "PUR-123", 20, 0);
	}

	@Test
	public void testValidationInvalidLimit() {
		InvoiceSearchCriteria criteria = new InvoiceSearchCriteria("INV-123", null, 0, 0);
		triggerValidations(criteria);
	}

	@Test
	public void testValidationInvalidOffset() {
		InvoiceSearchCriteria criteria = new InvoiceSearchCriteria("INV-123", null, 1, -1);
		triggerValidations(criteria);
	}

	private void triggerValidations(InvoiceSearchCriteria criteria) {
		// Dummy method used only to trigger validations
		Set<ConstraintViolation<InvoiceSearchCriteria>> violations
				= validator.validate(criteria);

		//then:
		assertFalse(violations.isEmpty());
	}

	private static ValidatorFactory validatorFactory;
	private static Validator validator;

	@BeforeClass
	public static void createValidator() {
		validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	@AfterClass
	public static void close() {
		validatorFactory.close();
	}
}
