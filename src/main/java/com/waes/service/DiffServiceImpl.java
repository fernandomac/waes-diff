package com.waes.service;

import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.waes.domain.Side;
import com.waes.exception.BusinessException;
import com.waes.exception.NotFoundException;
import com.waes.model.DiffResponse;
import com.waes.model.DiffResult;
import com.waes.repository.DiffDao;

@Service
public class DiffServiceImpl implements DiffService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DiffServiceImpl.class);
	
	private final DiffDao diffDao;
	
	@Autowired
	public DiffServiceImpl(DiffDao diffDao) {
		this.diffDao = diffDao;
	}

	public void save(Long id, String content, Side side) {
		validateContent(content);
		diffDao.add(id, content, side);
	}
	
	public DiffResponse getResult(Long id) {
		Optional<String> left = diffDao.find(id, Side.LEFT);
		Optional<String> right = diffDao.find(id, Side.RIGHT);
		validateEnoughDataToComparare(left, right);
		
		DiffResponse response = new DiffResponse();
		
		final byte[] leftContent = left.get().getBytes();
		final byte[] rightContent = right.get().getBytes();
		
		if (Arrays.equals(leftContent, rightContent)) {
			response.setResult(DiffResult.EQUALS);
		} else if (leftContent.length != rightContent.length) {
			response.setResult(DiffResult.DIFFERENT_SIZE);
		} else {
			String details = buildDiffDetails(leftContent, rightContent);
			response.setResult(DiffResult.DIFFERENT_CONTENT);
			response.setDetails(details);
		}
		
		return response;
	}
	
	private String buildDiffDetails(byte[] left, byte[] right) {
		StringBuilder sb = new StringBuilder("Offsets: ");
		
		for (int i = 0; i < right.length; i++) {
			if (left[i] != right[i]) {
				sb.append(String.format("%d ", i));
			}
        }
		
		sb.append(String.format("- [ Content length: %d ]", right.length));
		
		return sb.toString();
	}

	private void validateEnoughDataToComparare(Optional<String> left, Optional<String> right) {
		if (!left.isPresent() && !right.isPresent()) {
			throw new NotFoundException("No comparison side has been provided");
		}
		
		if (!left.isPresent()) {
			throw new BusinessException("Left side not provided");
		}
		
		if (!right.isPresent()) {
			throw new BusinessException("Right side not provided");
		}
	}
	
	private void validateContent(String content) {
		if (StringUtils.isBlank(content) || !isValidBase64(content)) {
			throw new BusinessException("Invalid Base64 content");
		}
	}
	
	private boolean isValidBase64(String content) {
        try {
            Base64.getDecoder().decode(content);
            return true;
        } catch (IllegalArgumentException e) {
    		LOGGER.debug("Exception decoding base64 [{}] - {}", content, e.getMessage());   
    		return false;
        }
    }

}
