package com.waes.service;

import com.waes.domain.Side;
import com.waes.model.DiffResponse;

/**
 * DiffService interface provides methods to support add base64 content for comparison.
 * Once saved both sides with same ID, it is possible to get the diff result.
 * For result we have 3 options: Equals, Different Size and Different Content.
 */
public interface DiffService {

	 /**
     * Save the Base64 content side for the diff comparison.
     *
     * @param id: numeric identifier.
     * @param content: base64 encoded content.
     * @param side: compare side.
     */
	void save(Long id, String content, Side side);
	
	/**
     * Get the comparison result.
     *
     * @param id: numeric identifier.
     * @return DiffResponse information.
     */
	DiffResponse getResult(Long id);
	
}