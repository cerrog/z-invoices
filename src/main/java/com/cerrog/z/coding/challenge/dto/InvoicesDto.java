/*******************************************************************************
 * Copyright (C) 1997-2017 by TouchTunes Digital Jukebox, Inc. All Rights
 * Reserved.
 *
 * Disclosure or use in part or in whole without prior written consent
 * constitutes an infringement of copyright, punishable by law.
 ******************************************************************************/
package com.cerrog.z.coding.challenge.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gcerro on 2018-09-04.
 *
 * Note: A framework level more sophisticated generic class would be an improvement.
 */
@Data
public class InvoicesDto {
	private List<InvoiceDto> invoices = new ArrayList<>();
	private Pagination pagination = new Pagination();
}
