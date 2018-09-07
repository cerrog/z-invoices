/*******************************************************************************
 * Copyright (C) 1997-2017 by TouchTunes Digital Jukebox, Inc. All Rights
 * Reserved.
 *
 * Disclosure or use in part or in whole without prior written consent
 * constitutes an infringement of copyright, punishable by law.
 ******************************************************************************/
package com.cerrog.z.coding.challenge.repository;

import com.cerrog.z.coding.challenge.entity.InvoiceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by gcerro on 2018-09-04.
 */
@Repository
public interface InvoiceRepository extends PagingAndSortingRepository<InvoiceEntity, Long> {
	Page<InvoiceEntity> findAllByInvoiceId(String invoiceId, Pageable pageRequest);
	Page<InvoiceEntity> findAllByPurchaseOrderNo(String purchaseOrderNo, Pageable pageRequest);
}
