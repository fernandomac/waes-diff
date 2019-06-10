package com.waes.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.waes.domain.Side;
import com.waes.exception.BusinessException;
import com.waes.exception.NotFoundException;
import com.waes.model.DiffResponse;
import com.waes.model.DiffResult;
import com.waes.repository.DiffDao;
import com.waes.test.support.TestSampleData;

@RunWith(MockitoJUnitRunner.class)
public class DiffServiceTest {

	@Mock
	private DiffDao diffDao;
	
	@Autowired
	private DiffServiceImpl service;
	
	@Before
	public void setUp() {
		service = new DiffServiceImpl(diffDao);
	}
	
	@Test
	public void shouldSaveLeftSide() {
		
		Long id = 1234L;
		String base64Value = TestSampleData.BASE64_SAMPLE_SMALL;
		Side side = Side.LEFT;
		
		service.save(id, base64Value, side);
		
		verify(diffDao).add(eq(1234L), eq(base64Value), eq(Side.LEFT));
		verifyNoMoreInteractions(diffDao);
	}
	
	@Test
	public void shouldSaveRightSide() {
		
		Long id = 1234L;
		String base64Value = TestSampleData.BASE64_SAMPLE_SMALL;
		Side side = Side.RIGHT;
		
		service.save(id, base64Value, side);
		
		verify(diffDao).add(eq(1234L), eq(base64Value), eq(Side.RIGHT));
		verifyNoMoreInteractions(diffDao);
	}
	
	@Test(expected=BusinessException.class)
	public void shouldNotSaveContentBlank() {
		
		Long id = 1234L;
		String base64Value = " ";
		Side side = Side.RIGHT;
		
		service.save(id, base64Value, side);
	}
	
	@Test(expected=BusinessException.class)
	public void shouldNotSaveInvalidContent() {
		
		Long id = 1234L;
		String base64Value = "*&^%";
		Side side = Side.RIGHT;
		
		service.save(id, base64Value, side);
	}
	
	@Test
	public void shoulGetResultDifferentSizeWhenDifferentContentAndSize() {
		
		Long id = 4355645L;
		
		when(diffDao.find(id, Side.LEFT)).thenReturn(Optional.of(TestSampleData.BASE64_SAMPLE_LARGE));
		when(diffDao.find(id, Side.RIGHT)).thenReturn(Optional.of(TestSampleData.BASE64_SAMPLE_SMALL));
		
		DiffResponse actual = service.getResult(id);
		
		assertEquals(DiffResult.DIFFERENT_SIZE, actual.getResult());
		assertNull(actual.getDetails());
		verify(diffDao).find(eq(id), eq(Side.RIGHT));
		verify(diffDao).find(eq(id), eq(Side.LEFT));
		verifyNoMoreInteractions(diffDao);
	}
	
	@Test
	public void shoulGetResultEqualsWhenSameSizeSameContent() {
		
		Long id = 4355645L;
		
		when(diffDao.find(id, Side.LEFT)).thenReturn(Optional.of(TestSampleData.BASE64_SAMPLE_LARGE));
		when(diffDao.find(id, Side.RIGHT)).thenReturn(Optional.of(TestSampleData.BASE64_SAMPLE_LARGE));
		
		DiffResponse actual = service.getResult(id);
		
		assertEquals(DiffResult.EQUALS, actual.getResult());
		assertNull(actual.getDetails());
		verify(diffDao).find(eq(id), eq(Side.RIGHT));
		verify(diffDao).find(eq(id), eq(Side.LEFT));
		verifyNoMoreInteractions(diffDao);
	}
	
	
	@Test
	public void shoulGetResultDifferentContentWhenSameSizeDifferentContent() {
		
		Long id = 4355645L;
		
		when(diffDao.find(id, Side.LEFT)).thenReturn(Optional.of(TestSampleData.BASE64_SAMPLE_SMALL));
		when(diffDao.find(id, Side.RIGHT)).thenReturn(Optional.of(TestSampleData.BASE64_SAMPLE_SMALL_OTHER_CONTENT));
		
		DiffResponse actual = service.getResult(id);
		assertEquals(DiffResult.DIFFERENT_CONTENT, actual.getResult());
		assertEquals("Offsets: 3084 3085 3086 - [ Content length: 3088 ]", actual.getDetails());
		verify(diffDao).find(eq(id), eq(Side.RIGHT));
		verify(diffDao).find(eq(id), eq(Side.LEFT));
		verifyNoMoreInteractions(diffDao);
		
	}
	
	@Test(expected=BusinessException.class)
	public void shoulNotGetResultMissingRightContent() {
		
		Long id = 4355645L;
		
		when(diffDao.find(id, Side.LEFT)).thenReturn(Optional.of(TestSampleData.BASE64_SAMPLE_LARGE));
		when(diffDao.find(id, Side.RIGHT)).thenReturn(Optional.empty());
		
		service.getResult(id);
		
	}
	
	@Test(expected=BusinessException.class)
	public void shoulNotGetResultMissingLeftContent() {
		
		Long id = 4355645L;
		
		when(diffDao.find(id, Side.RIGHT)).thenReturn(Optional.of(TestSampleData.BASE64_SAMPLE_LARGE));
		when(diffDao.find(id, Side.LEFT)).thenReturn(Optional.empty());
		
		service.getResult(id);
		
	}
	
	@Test(expected=NotFoundException.class)
	public void shoulNotGetResultMissingBothSidest() {
		
		Long id = 4355645L;
		
		when(diffDao.find(id, Side.RIGHT)).thenReturn(Optional.empty());
		when(diffDao.find(id, Side.LEFT)).thenReturn(Optional.empty());
		
		service.getResult(id);
		
	}
}
