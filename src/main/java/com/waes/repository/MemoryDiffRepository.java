package com.waes.repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.waes.domain.Side;

@Repository
public class MemoryDiffRepository implements DiffDao {

	private final Map<Long, String> leftRepo = new ConcurrentHashMap<>();
	private final Map<Long, String> rightRepo = new ConcurrentHashMap<>();
	
	@Override
	public void add(Long id, String content, Side side) {
		if (side.isLeft()) {
			leftRepo.put(id, content);
		} else {
			rightRepo.put(id, content);
		} 
	}

	@Override
	public Optional<String> find(Long id, Side side) {
		String result = null;
		
		if (side.isLeft()) {
			result = leftRepo.get(id);
		} else {
			result = rightRepo.get(id);
		}
		
		return Optional.ofNullable(result) ;
	}

}
