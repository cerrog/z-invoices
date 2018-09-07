/*******************************************************************************
 * Copyright (C) 1997-2017 by TouchTunes Digital Jukebox, Inc. All Rights
 * Reserved.
 *
 * Disclosure or use in part or in whole without prior written consent
 * constitutes an infringement of copyright, punishable by law.
 ******************************************************************************/
package com.cerrog.z.coding.challenge.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;

/**
 * Created by gcerro on 2018-09-04.
 */
@Entity
@Table(name = "invoices")
@Data
public class InvoiceEntity {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	public Long id;
	@Column(name = "invoice_number")
	public String invoiceId;
	@Column(name = "po_number")
	public String purchaseOrderNo;
	@Column(name = "due_date")
	public LocalDate dueDate;
	@Column(name = "amount_cents")
	public Long amountInCents;
	@Column(name = "created_at")
	@CreationTimestamp
	public ZonedDateTime createdAt;
}
