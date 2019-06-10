package com.waes.test.integration;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.waes.application.WebDiffApplication;
import com.waes.test.support.TestSampleData;
import com.waes.web.endpoint.DiffController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=WebDiffApplication.class)
public class DiffFeatureIntegrationTest {
	
	@Autowired
	private DiffController controller;
	
	protected MockMvc mockMvc;
	protected HttpHeaders httpHeaders;
	
	@Before
	public void setUp() {
		httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}
	
	@Test
    public void shouldSaveBothSidesAndCompareEquals() throws Exception {
		Long id = 111111L;
		
		mockMvc.perform(
				post("/v1/diff/{id}/left", id)
					.headers(httpHeaders)
					.content(TestSampleData.JSON_SAMPLE_SMALL))
				.andExpect(status().isCreated());
		
		mockMvc.perform(
				post("/v1/diff/{id}/right", id)
					.headers(httpHeaders)
					.content(TestSampleData.JSON_SAMPLE_SMALL))
				.andExpect(status().isCreated());
		
		mockMvc.perform(
				get("/v1/diff/{id}", id)
				.headers(httpHeaders).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result", containsString("EQUALS")))
				.andExpect(jsonPath("$.details", nullValue()));
    }
	
	@Test
    public void shouldSaveBothSidesAndCompareDifferentSize() throws Exception {
		Long id = 22222L;
		
		mockMvc.perform(
				post("/v1/diff/{id}/left", id)
					.headers(httpHeaders)
					.content(TestSampleData.JSON_SAMPLE_SMALL))
				.andExpect(status().isCreated());
		
		mockMvc.perform(
				post("/v1/diff/{id}/right", id)
					.headers(httpHeaders)
					.content(TestSampleData.JSON_SAMPLE_LARGE))
				.andExpect(status().isCreated());
		
		mockMvc.perform(
				get("/v1/diff/{id}", id)
				.headers(httpHeaders).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result", containsString("DIFFERENT_SIZE")))
				.andExpect(jsonPath("$.details", nullValue()));
    }
	
	@Test
    public void shouldSaveBothSidesAndCompareSameSizeDiffenteContent() throws Exception {
		Long id = 333333L;
		
		mockMvc.perform(
				post("/v1/diff/{id}/left", id)
					.headers(httpHeaders)
					.content(TestSampleData.JSON_SAMPLE_SMALL))
				.andExpect(status().isCreated());
		
		mockMvc.perform(
				post("/v1/diff/{id}/right", id)
					.headers(httpHeaders)
					.content(TestSampleData.JSON_SAMPLE_SMALL_OTHER_CONTENT))
				.andExpect(status().isCreated());
		
		mockMvc.perform(
				get("/v1/diff/{id}", id)
				.headers(httpHeaders).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result", containsString("DIFFERENT_CONTENT")))
				.andExpect(jsonPath("$.details", containsString("Offsets: 3084 3085 3086 - [ Content length: 3088 ]")));
    }
	
	@Test
    public void shouldSaveWrongDataAndFailComparison() throws Exception {
		Long id = 444444L;
		
		mockMvc.perform(
				post("/v1/diff/{id}/left", id)
					.headers(httpHeaders)
					.content(TestSampleData.JSON_SAMPLE_SMALL))
				.andExpect(status().isCreated());
		
		mockMvc.perform(
				post("/v1/diff/{id}/right", id)
					.headers(httpHeaders)
					.content(TestSampleData.JSON_SAMPLE_INVALID))
				.andExpect(status().isBadRequest());
		
		mockMvc.perform(
				get("/v1/diff/{id}", id)
				.headers(httpHeaders).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isBadRequest());
    }
	
	@Test
    public void shouldNotCompareNoSideProvided() throws Exception {
		Long id = 555555L;
		
		mockMvc.perform(
				get("/v1/diff/{id}", id)
				.headers(httpHeaders).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound());
    }

}
