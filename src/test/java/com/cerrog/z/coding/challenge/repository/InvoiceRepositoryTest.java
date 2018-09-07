/*******************************************************************************
 * Copyright (C) 1997-2017 by TouchTunes Digital Jukebox, Inc. All Rights
 * Reserved.
 *
 * Disclosure or use in part or in whole without prior written consent
 * constitutes an infringement of copyright, punishable by law.
 ******************************************************************************/
package com.cerrog.z.coding.challenge.repository;

import com.cerrog.z.coding.challenge.entity.InvoiceEntity;
import com.cerrog.z.coding.challenge.service.InvoiceSearchCriteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZonedDateTime;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by gcerro on 2018-09-05.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class InvoiceRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private InvoiceRepository invoiceRepository;

	private Random random = new Random();

	@Test
	public void givenInvoices_whenFindAll_thenReturnInvoices() {
		// given
		persistEntities(2);
		Pageable pageable = createPageable(10, 0);

		// when
		Page<InvoiceEntity> found = invoiceRepository.findAll(pageable);

		// then
		assertPagination(found, 2, 1, 2);
	}

	@Test
	public void givenInvoices_whenFindAll_thenReturnInvoicesOrdered() throws InterruptedException {
		// given
		InvoiceEntity invoice1 = entityManager.persist(createEntity());
		Thread.sleep(1000); // Delay so we can compare createdAt
		InvoiceEntity invoice2 = entityManager.persist(createEntity());
		entityManager.flush();
		Pageable pageable = createPageable(10, 0);

		// when
		Page<InvoiceEntity> found = invoiceRepository.findAll(pageable);

		// then
		assertPagination(found, 2, 1, 2);
		assertThat(found.getContent().get(0).getId()).isEqualTo(invoice2.getId());
		assertThat(found.getContent().get(1).getId()).isEqualTo(invoice1.getId());
	}

	@Test
	public void givenInvoices_whenFindByInvoiceId_thenReturnInvoice() {
		// given
		persistEntities(2);
		String invoiceIdToLookup = entityManager.persist(createEntity()).getInvoiceId();
		entityManager.flush();
		Pageable pageable = createPageable(10, 0);

		// when
		Page<InvoiceEntity> found = invoiceRepository.findAllByInvoiceId(invoiceIdToLookup, pageable);

		// then
		assertPagination(found, 1, 1, 1);
		assertThat(found.getContent().get(0).getInvoiceId()).isEqualTo(invoiceIdToLookup);
	}

	@Test
	public void givenInvoices_whenFindByPurchaseOrderNo_thenReturnInvoice() {
		// given
		persistEntities(2);
		String purchaseOrderNoToLookup = entityManager.persist(createEntity()).getPurchaseOrderNo();
		entityManager.flush();
		Pageable pageable = createPageable(10, 0);

		// when
		Page<InvoiceEntity> found = invoiceRepository.findAllByPurchaseOrderNo(purchaseOrderNoToLookup, pageable);

		// then
		assertPagination(found, 1, 1, 1);
		assertThat(found.getContent().get(0).getPurchaseOrderNo()).isEqualTo(purchaseOrderNoToLookup);
	}


	@Test
	public void givenInvoicesAndPage_whenFindAll_thenReturnInvoicesInMultiplePages() {
		// given
		persistEntities(6);
		Pageable pageable = createPageable(5, 0);

		// when
		Page<InvoiceEntity> found = invoiceRepository.findAll(pageable);

		// then
		assertPagination(found, 6, 2, 5);
		assertThat(found.isFirst()).isTrue();
		assertThat(found.hasNext()).isTrue();

		// next page
		pageable = createPageable(5, 5);
		found = invoiceRepository.findAll(pageable);
		assertPagination(found, 6, 2, 1);
		assertThat(found.isFirst()).isFalse();
		assertThat(found.hasNext()).isFalse();
		assertThat(found.hasPrevious()).isTrue();
		assertThat(found.isLast()).isTrue();

		// next page
		pageable = createPageable(5, 10);
		found = invoiceRepository.findAll(pageable);
		assertPagination(found, 6, 2, 0);
		assertThat(found.isFirst()).isFalse();
		assertThat(found.hasNext()).isFalse();
		assertThat(found.hasPrevious()).isTrue();
		assertThat(found.isLast()).isTrue();
	}

	private void assertPagination(Page page, int totalElements, int totalPages, int contentSize) {
		assertThat(page.getTotalElements()).isEqualTo(totalElements);
		assertThat(page.getTotalPages()).isEqualTo(totalPages);
		assertThat(page.getContent().size()).isEqualTo(contentSize);
	}

	private Pageable createPageable(int limit, int offset) {
		InvoiceSearchCriteria criteria = new InvoiceSearchCriteria(null, null, limit, offset);
		return PageRequest.of(criteria.getPageNumber(),
				criteria.getPageSize(),
				Sort.by("createdAt").descending());
	}

	private void persistEntities(int count) {
		for (int c = 0; c < count; c++) {
			entityManager.persist(createEntity());
		}
		entityManager.flush();
	}

	private InvoiceEntity createEntity() {
		long id  = random.nextInt(100);
		ZonedDateTime now = ZonedDateTime.now();
		InvoiceEntity entity = new InvoiceEntity();
		entity.setAmountInCents(id * 100);
		entity.setCreatedAt(now);
		entity.setDueDate(now.plusDays(15).toLocalDate());
		entity.setInvoiceId("INV-" + id);
		entity.setPurchaseOrderNo("PO-" + id);
		return entity;
	}
}
