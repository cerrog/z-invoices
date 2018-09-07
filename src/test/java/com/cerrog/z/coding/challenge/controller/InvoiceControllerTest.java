/*******************************************************************************
 * Copyright (C) 1997-2017 by TouchTunes Digital Jukebox, Inc. All Rights
 * Reserved.
 *
 * Disclosure or use in part or in whole without prior written consent
 * constitutes an infringement of copyright, punishable by law.
 ******************************************************************************/
package com.cerrog.z.coding.challenge.controller;

import com.cerrog.z.coding.challenge.service.InvoiceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.cerrog.z.coding.challenge.dto.InvoiceDto;
import com.cerrog.z.coding.challenge.dto.InvoicesDto;
import com.cerrog.z.coding.challenge.service.InvoiceSearchCriteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by gcerro on 2018-09-02.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(InvoiceController.class)
public class InvoiceControllerTest {
	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper mapper;

	@MockBean
	private InvoiceService service;

	private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

	@Test
	public void givenNoInvoicesAvailable_whenGetAllInvoices_thenEmpty()
			throws Exception {

		InvoicesDto dto = new InvoicesDto();

		given(service.findInvoices(any(InvoiceSearchCriteria.class))).willReturn(dto);

		mvc.perform(get("/v1/invoices")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.invoices", hasSize(0)))
				.andExpect(jsonPath("$.pagination.total", is(0)));
	}

	@Test
	public void givenValidInvoice_whenPost_thenCreated()
			throws Exception {

		InvoiceDto dto = createValidDto();
		InvoiceDto createdDto = createdDto(dto);

		given(service.createInvoice(any(InvoiceDto.class))).willReturn(createdDto);

		mvc.perform(post("/v1/invoices")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(dto)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id", is(createdDto.getId())))
				.andExpect(jsonPath("$.created_at", is(createdDto.getCreatedAt().format(FORMATTER))));
	}

	@Test
	public void givenInvalidInvoice_whenPost_thenClientError()
			throws Exception {

		InvoiceDto dto = createValidDto();
		InvoiceDto createdDto = createdDto(dto);
		dto.setInvoiceId("");

		given(service.createInvoice(any(InvoiceDto.class))).willReturn(createdDto);

		String dtoAsJson = mapper.writeValueAsString(dto);

		mvc.perform(post("/v1/invoices")
				.contentType(MediaType.APPLICATION_JSON)
				.content(dtoAsJson))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void givenException_whenPost_thenServerError()
			throws Exception {

		InvoiceDto dto = createValidDto();
		InvoiceDto createdDto = createdDto(dto);

		given(service.createInvoice(any(InvoiceDto.class))).willThrow(new RuntimeException());

		mvc.perform(post("/v1/invoices")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(dto)))
				.andExpect(status().is5xxServerError());
	}

	@Test
	public void whenInvalidPath_then404()
			throws Exception {

		mvc.perform(get("/v2/invoices")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void givenException_whenGet_thenReturnError()
			throws Exception {

		given(service.findInvoices(any(InvoiceSearchCriteria.class))).willThrow(new RuntimeException());

		mvc.perform(get("/v1/invoices")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is5xxServerError());
	}

	private InvoiceDto createValidDto() {
		Random random = new Random();
		long value = random.nextLong();
		LocalDateTime now = LocalDateTime.now();
		InvoiceDto dto = new InvoiceDto();
		dto.setAmountInCents(value * 100);
		dto.setDueDate(now.plusDays(15).toLocalDate());
		dto.setInvoiceId("INV-" + value);
		dto.setPurchaseOrderNo("PO-" + value);
		return dto;
	}

	private InvoiceDto createdDto(InvoiceDto dto) {
		Random random = new Random();
		InvoiceDto createdDto = new InvoiceDto();
		BeanUtils.copyProperties(dto, createdDto);
		createdDto.setId(random.nextLong());
		createdDto.setCreatedAt(ZonedDateTime.now());

		return createdDto;
	}
}
