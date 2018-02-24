package com.zime.web.service;

import java.util.List;

import com.zime.web.util.ResponseEntity;

public interface CategoryService {
	
	ResponseEntity<String> addCategory(String categoryName, Integer parentId);
	
	ResponseEntity<String> updateCategoryName(Integer categoryId, String categoryName);

	ResponseEntity getChildParallelCategory(Integer categoryId);
	
	ResponseEntity<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);

}
