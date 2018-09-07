/*******************************************************************************
 * Copyright (C) 1997-2017 by TouchTunes Digital Jukebox, Inc. All Rights
 * Reserved.
 *
 * Disclosure or use in part or in whole without prior written consent
 * constitutes an infringement of copyright, punishable by law.
 ******************************************************************************/
package com.cerrog.z.coding.challenge.dto;

import lombok.Data;

/**
 * Created by gcerro on 2018-09-04.
 */
@Data
public class Pagination {
	private Integer offset = 0;
	private Integer limit = 0;
	private Long total = 0L;
}
