package com.waes.repository;

import java.util.Optional;

import com.waes.domain.Side;

/**
 * DiffDao interface provides methods to persist and retrieve base64 contents 
 * by a numeric ID and a Side key (LEFT/RIGHT)
 */
public interface DiffDao {

	/**
     * Persists the Base64 content side indexed by 
     * a numeric id and Side key (LEFT/RIGHT).
     *
     * @param id: numeric identifier.
     * @param content: base64 encoded content.
     * @param side: Side key (LEFT/RIGHT).
     */
	void add(Long id, String content, Side side);
	
	/**
     * Retrieves a content by a numeric id and a Side key (LEFT/RIGHT).
     *
     * @param id: numeric identifier.
     * @param side: Side key (LEFT/RIGHT).
     * @return String Base64 encoded content.
     */
	Optional<String> find(Long id, Side side);
}
