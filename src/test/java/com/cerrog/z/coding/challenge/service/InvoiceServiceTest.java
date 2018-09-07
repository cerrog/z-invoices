/*******************************************************************************
 * Copyright (C) 1997-2017 by TouchTunes Digital Jukebox, Inc. All Rights
 * Reserved.
 *
 * Disclosure or use in part or in whole without prior written consent
 * constitutes an infringement of copyright, punishable by law.
 ******************************************************************************/
package com.cerrog.z.coding.challenge.service;

import com.cerrog.z.coding.challenge.entity.InvoiceEntity;
import com.cerrog.z.coding.challenge.dto.InvoiceDto;
import com.cerrog.z.coding.challenge.dto.InvoicesDto;
import com.cerrog.z.coding.challenge.repository.InvoiceRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

/**
 * Created by gcerro on 2018-09-05.
 */
@RunWith(SpringRunner.class)
public class InvoiceServiceTest {
	@TestConfiguration
	static class InvoiceServiceTestContextConfiguration {
		@MockBean
		public InvoiceRepository repository;

		@Bean
		public InvoiceService service() {
			return new InvoiceService(repository);
		}
	}

	@Autowired
	private InvoiceService service;

	@Autowired
	private InvoiceRepository repository;

	private Random random = new Random(100);

	@Test
	public void givenValidInvoiceDto_whenCreatingInvoice_thenReturnInvoice() {
		// given
		InvoiceDto newInvoice = createDto();
		assertNull(newInvoice.getId());
		assertNull(newInvoice.getCreatedAt());

		// when
		Mockito.when(repository.save(any(InvoiceEntity.class))).thenReturn(toEntity(newInvoice));

		// then
		InvoiceDto createdInvoice = service.createInvoice(newInvoice);
		assertNotNull(createdInvoice.getId());
		assertNotNull(createdInvoice.getCreatedAt());
	}

	@Test
	public void testSearchByInvoice() {
		final long ID = 1L;
		InvoiceEntity invoice = createEntity(ID);
		Page<InvoiceEntity> page = createPage(Collections.singletonList(invoice));

		Mockito.when(repository.findAllByInvoiceId(eq(invoice.getInvoiceId()), any()))
				.thenReturn(page);

		InvoiceSearchCriteria criteria = new InvoiceSearchCriteria("INV-" + ID, null, 10, 0);

		InvoicesDto invoices = service.findInvoices(criteria);
		assertEquals(Long.valueOf(1), invoices.getPagination().getTotal());
		assertEquals(1, invoices.getInvoices().size());
		assertEquals("INV-" + ID, invoices.getInvoices().get(0).getInvoiceId());
	}

	@Test
	public void testSearchByPurchaseOrder() {
		final long ID = 2L;
		InvoiceEntity invoice = createEntity(ID);
		Page<InvoiceEntity> page = createPage(Collections.singletonList(invoice));

		Mockito.when(repository.findAllByPurchaseOrderNo(eq(invoice.getPurchaseOrderNo()), any()))
				.thenReturn(page);

		InvoiceSearchCriteria criteria = new InvoiceSearchCriteria(null, "PO-" + ID, 10, 0);

		InvoicesDto invoices = service.findInvoices(criteria);
		assertEquals(Long.valueOf(1), invoices.getPagination().getTotal());
		assertEquals(1, invoices.getInvoices().size());
		assertEquals("PO-" + ID, invoices.getInvoices().get(0).getPurchaseOrderNo());
	}

	@Test
	public void testSearchAll() {
		List<InvoiceEntity> entities = new ArrayList<>(2);
		entities.add(createEntity(1L));
		entities.add(createEntity(2L));
		Page<InvoiceEntity> page = createPage(entities);

		Mockito.when(repository.findAll(any(Pageable.class)))
				.thenReturn(page);

		InvoiceSearchCriteria criteria = new InvoiceSearchCriteria();

		InvoicesDto invoices = service.findInvoices(criteria);
		assertEquals(Long.valueOf(entities.size()), invoices.getPagination().getTotal());
		assertEquals(entities.size(), invoices.getInvoices().size());
	}

	private InvoiceEntity createEntity(Long uniqueId) {
		ZonedDateTime now = ZonedDateTime.now();
		InvoiceEntity entity = new InvoiceEntity();
		entity.setAmountInCents(uniqueId * 100);
		entity.setCreatedAt(now);
		entity.setDueDate(now.plusDays(15).toLocalDate());
		entity.setId(uniqueId);
		entity.setInvoiceId("INV-" + uniqueId);
		entity.setPurchaseOrderNo("PO-" + uniqueId);
		return entity;
	}

	private InvoiceDto createDto() {
		long value = random.nextLong();
		LocalDateTime now = LocalDateTime.now();
		InvoiceDto dto = new InvoiceDto();
		dto.setAmountInCents(value * 100);
		dto.setDueDate(now.plusDays(15).toLocalDate());
		dto.setInvoiceId("INV-" + value);
		dto.setPurchaseOrderNo("PO-" + value);
		return dto;
	}

	private Page<InvoiceEntity> createPage(List<InvoiceEntity> invoices) {
		return (Page) new PageImpl(invoices);
	}

	private InvoiceEntity toEntity(InvoiceDto dto) {
		InvoiceEntity entity = new InvoiceEntity();
		BeanUtils.copyProperties(dto, entity);
		entity.setId(1L);
		entity.setCreatedAt(ZonedDateTime.now());
		return entity;
	}
}
