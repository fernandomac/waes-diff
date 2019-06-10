package com.waes.web.endpoint;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.waes.domain.Side;
import com.waes.exception.BusinessException;
import com.waes.exception.NotFoundException;
import com.waes.model.DiffResponse;
import com.waes.model.DiffResult;
import com.waes.service.DiffService;
import com.waes.test.support.TestSampleData;
import com.waes.web.endpoint.DiffController;

public class DiffControllerTest {
	 
	@InjectMocks
	private DiffController controller;
	
	@Mock
	private DiffService service;
	
	protected MockMvc mockMvc;
	protected HttpHeaders httpHeaders;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}
	
	@Test
    public void shouldSaveLeft() throws Exception {
		Long id = 12345L;
		
		mockMvc.perform(
				post("/v1/diff/{id}/left", id)
					.headers(httpHeaders)
					.content(TestSampleData.JSON_SAMPLE_SMALL))
		
				.andExpect(status().isCreated());
		
		
		verify(service).save(eq(id), eq(TestSampleData.BASE64_SAMPLE_SMALL), eq(Side.LEFT));
		verifyNoMoreInteractions(service);
    }
	
	@Test
    public void shouldSaveRight() throws Exception {
		Long id = 12345L;
		
		mockMvc.perform(
				post("/v1/diff/{id}/right", id)
					.headers(httpHeaders)
					.content(TestSampleData.JSON_SAMPLE_SMALL))
		
				.andExpect(status().isCreated());
		
		
		verify(service).save(eq(id), eq(TestSampleData.BASE64_SAMPLE_SMALL), eq(Side.RIGHT));
		verifyNoMoreInteractions(service);
    }
	
	@Test
    public void shouldGetDiffResult() throws Exception {
		Long id = 12345L;
		
		DiffResponse response = new DiffResponse();
		response.setResult(DiffResult.DIFFERENT_CONTENT);
		response.setDetails(" 2035 2037 2038");
		
		when(service.getResult(id)).thenReturn(response);
		
		mockMvc.perform(
				get("/v1/diff/{id}", id)
				.headers(httpHeaders).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result", containsString("DIFFERENT_CONTENT")))
				.andExpect(jsonPath("$.details", containsString(" 2035 2037 2038")));
		
		
		verify(service).getResult(id);
		verifyNoMoreInteractions(service);
    }
	
	@Test
    public void shouldNotSaveInvalidRequest() throws Exception {
		Long id = 12345L;
		
		doThrow(new BusinessException("validation error")).when(service).save(eq(id), anyString(), eq(Side.RIGHT));
		
		mockMvc.perform(
				post("/v1/diff/{id}/right", id)
					.headers(httpHeaders)
					.content(TestSampleData.JSON_SAMPLE_SMALL))
		
				.andExpect(status().isBadRequest());
		
		verify(service).save(eq(id), eq(TestSampleData.BASE64_SAMPLE_SMALL), eq(Side.RIGHT));
		verifyNoMoreInteractions(service);
    }
	
	@Test
    public void shouldNotSaveInvalidIdFormat() throws Exception {
		String invalidId = "invalid";
		
		mockMvc.perform(
				post("/v1/diff/{id}/right", invalidId)
					.headers(httpHeaders)
					.content(TestSampleData.JSON_SAMPLE_SMALL))
		
				.andExpect(status().isBadRequest());
		
		verifyNoMoreInteractions(service);
    }
	
	@Test
    public void shouldNotGetResultNotFoundID() throws Exception {
		Long id = 12345L;
		
		doThrow(new NotFoundException("not found error")).when(service).getResult(eq(id));
		
		mockMvc.perform(
				get("/v1/diff/{id}", id)
				.headers(httpHeaders).accept(MediaType.APPLICATION_JSON_UTF8))
		
				.andExpect(status().isNotFound());
		
		verify(service).getResult(id);
		verifyNoMoreInteractions(service);
    }
	
	@Test
    public void shouldNotGetResultMissingAComparisonSide() throws Exception {
		Long id = 12345L;
		
		doThrow(new BusinessException("missing side error")).when(service).getResult(eq(id));
		
		mockMvc.perform(
				get("/v1/diff/{id}", id)
				.headers(httpHeaders).accept(MediaType.APPLICATION_JSON_UTF8))
		
				.andExpect(status().isBadRequest());
		
		verify(service).getResult(id);
		verifyNoMoreInteractions(service);
    }
	
}
