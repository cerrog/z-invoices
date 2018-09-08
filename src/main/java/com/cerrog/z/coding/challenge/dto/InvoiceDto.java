/*******************************************************************************
 * Copyright (C) 1997-2017 by TouchTunes Digital Jukebox, Inc. All Rights
 * Reserved.
 *
 * Disclosure or use in part or in whole without prior written consent
 * constitutes an infringement of copyright, punishable by law.
 ******************************************************************************/
package com.cerrog.z.coding.challenge.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.ZonedDateTime;

/**
 * Created by gcerro on 2018-09-04.
 */
@Data
public class InvoiceDto {
	@JsonProperty
	private Long id;
	@JsonProperty(value = "invoice_number", required = true)
	@NotBlank
	private String invoiceId;
	@JsonProperty(value = "po_number", required = true)
	@NotBlank
	private String purchaseOrderNo;
	@JsonProperty(value = "due_date", required = true)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@NotNull
	private LocalDate dueDate;
	@JsonProperty(value = "amount_cents", required = true)
	@NotNull
	private Long amountInCents;
	@JsonProperty(value = "created_at")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	private ZonedDateTime createdDateTime;
}
