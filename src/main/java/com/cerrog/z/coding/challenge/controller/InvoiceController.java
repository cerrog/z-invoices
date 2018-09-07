/*******************************************************************************
 * Copyright (C) 1997-2017 by TouchTunes Digital Jukebox, Inc. All Rights
 * Reserved.
 *
 * Disclosure or use in part or in whole without prior written consent
 * constitutes an infringement of copyright, punishable by law.
 ******************************************************************************/
package com.cerrog.z.coding.challenge.controller;

import com.cerrog.z.coding.challenge.dto.InvoiceDto;
import com.cerrog.z.coding.challenge.dto.InvoicesDto;
import com.cerrog.z.coding.challenge.service.InvoiceSearchCriteria;
import com.cerrog.z.coding.challenge.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by gcerro on 2018-09-04.
 */
@RestController
@RequestMapping("/v1/invoices")
public class InvoiceController {

	private final InvoiceService service;

	@Autowired
	public InvoiceController(InvoiceService service) {
		this.service = service;
		Assert.notNull(service, "Missing dependency: InvoiceService");
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public InvoiceDto createInvoice(@RequestBody @Valid InvoiceDto invoice) {
		return service.createInvoice(invoice);
	}

	@GetMapping
	public InvoicesDto findInvoices(@RequestParam(name = "invoice_number", required = false) String invoiceNo,
	                                @RequestParam(name = "po_number", required = false) String purchaseOrderNo,
	                                @RequestParam(required = false, defaultValue = "20") Integer limit,
	                                @RequestParam(required = false, defaultValue = "0") Integer offset)
	{
		InvoiceSearchCriteria criteria = new InvoiceSearchCriteria(invoiceNo, purchaseOrderNo, limit, offset);
		return service.findInvoices(criteria);
	}
}
