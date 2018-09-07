/*******************************************************************************
 * Copyright (C) 1997-2017 by TouchTunes Digital Jukebox, Inc. All Rights
 * Reserved.
 *
 * Disclosure or use in part or in whole without prior written consent
 * constitutes an infringement of copyright, punishable by law.
 ******************************************************************************/
package com.cerrog.z.coding.challenge.service;

import com.cerrog.z.coding.challenge.dto.InvoiceDto;
import com.cerrog.z.coding.challenge.dto.InvoicesDto;
import com.cerrog.z.coding.challenge.entity.InvoiceEntity;
import com.cerrog.z.coding.challenge.repository.InvoiceRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 * Created by gcerro on 2018-09-04.
 */
@Service
@Validated
public class InvoiceService {

	private final InvoiceRepository repository;

	@Autowired
	public InvoiceService(InvoiceRepository repository) {
		this.repository = repository;
	}

	public InvoiceDto createInvoice(@Valid InvoiceDto invoice) {
		InvoiceEntity saved = repository.save(fromDtoToEntity(invoice));
		return fromEntityToDto(saved);
	}

	public InvoicesDto findInvoices(@Valid InvoiceSearchCriteria searchCriteria) {
		Page<InvoiceEntity> page = null;

		//TODO Improvement: Use DslQuery to simplify and be future proof

		Pageable pageable = toPageable(searchCriteria);
		// Specification: search invoice(s) by invoice_number OR po_number criteria
		if (searchCriteria.isSearchByInvoice()) {
			page = repository.findAllByInvoiceId(searchCriteria.getInvoiceNo(), pageable);
		} else if (searchCriteria.isSearchByPurchaseNumber()) {
			page = repository.findAllByPurchaseOrderNo(searchCriteria.getPurchaseOrderNo(), pageable);
		} else {
			page = repository.findAll(pageable);
		}

		InvoicesDto invoices = new InvoicesDto();
		invoices.getPagination().setLimit(searchCriteria.getLimit());
		invoices.getPagination().setOffset(searchCriteria.getOffset());
		invoices.getPagination().setTotal(page.getTotalElements());
		page.forEach(i -> {
			invoices.getInvoices().add(this.fromEntityToDto(i));
				}
		);
		return invoices;
	}

	private InvoiceDto fromEntityToDto(InvoiceEntity entity) {
		InvoiceDto dto =  new InvoiceDto();
		BeanUtils.copyProperties(entity, dto);
		return dto;
	}

	private InvoiceEntity fromDtoToEntity(InvoiceDto dto) {
		InvoiceEntity entity = new InvoiceEntity();
		BeanUtils.copyProperties(dto, entity);
		return entity;
	}

	private Pageable toPageable(InvoiceSearchCriteria criteria) {
		return PageRequest.of(criteria.getPageNumber(),
				criteria.getPageSize(),
				Sort.by("createdAt").descending());
	}
}
