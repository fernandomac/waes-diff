package com.waes.repository;

import java.util.Optional;

import com.waes.domain.Side;

public interface DiffDao {

	void add(Long id, String content, Side side);
	
	Optional<String> find(Long id, Side side);
}
