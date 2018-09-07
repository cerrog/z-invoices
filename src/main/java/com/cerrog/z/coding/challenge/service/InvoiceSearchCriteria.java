/*******************************************************************************
 * Copyright (C) 1997-2017 by TouchTunes Digital Jukebox, Inc. All Rights
 * Reserved.
 *
 * Disclosure or use in part or in whole without prior written consent
 * constitutes an infringement of copyright, punishable by law.
 ******************************************************************************/
package com.cerrog.z.coding.challenge.service;

import lombok.Data;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.validation.ValidationException;
import javax.validation.constraints.Min;

/**
 * Created by gcerro on 2018-09-04.
 */
@Data
public class InvoiceSearchCriteria {
	private String invoiceNo;
	private String purchaseOrderNo;
	@Min(1)
	private Integer limit = 20;
	@Min(0)
	private Integer offset = 0;

	public InvoiceSearchCriteria() {
	}

	public InvoiceSearchCriteria(String invoiceNo, String purchaseOrderNo, @Min(1) Integer limit, @Min(0) Integer offset) {
		// Business rule: searches can be done with only one of two criteria
		if (!StringUtils.isEmpty(invoiceNo) && !StringUtils.isEmpty(purchaseOrderNo)) {
			throw new ValidationException("Only one(1) search criteria is supported: either 'invoice_number' or 'po_number'");
		}
		this.invoiceNo = invoiceNo;
		this.purchaseOrderNo = purchaseOrderNo;
		this.limit = limit;
		this.offset = offset;
	}

	public Integer getPageSize() {
		Assert.isTrue(limit > 0, "Limit parameter must be greater than 0");
		return limit;
	}

	public Integer getPageNumber() {
		//FIXME This will be inaccurate when offset is not a multiple of limit.
		return offset / getPageSize();
	}

	public boolean isSearchByInvoice() {
		return !StringUtils.isEmpty(invoiceNo);
	}

	public boolean isSearchByPurchaseNumber() {
		return !StringUtils.isEmpty(purchaseOrderNo);
	}
}
