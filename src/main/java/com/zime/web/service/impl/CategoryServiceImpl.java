package com.zime.web.service.impl;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.zime.web.contants.Errors;
import com.zime.web.dao.entity.Category;
import com.zime.web.dao.mapper.CategoryMapper;
import com.zime.web.service.CategoryService;
import com.zime.web.util.ResponseEntity;
import com.zime.web.util.ResponseEntityUtil;

@Service
public class CategoryServiceImpl implements CategoryService {
	private Logger logger= LoggerFactory.getLogger(CategoryServiceImpl.class);
	
	@Autowired
	private CategoryMapper categoryMapper;

	@Override
	public ResponseEntity<String> addCategory(String categoryName, Integer parentId) {
        if(parentId==null || StringUtils.isBlank(categoryName)) {
        	return ResponseEntityUtil.fail(Errors.SYSTEM_REQUEST_PARAM_ERROR.getCode(),"参数错误");
        }
        
        Category category=new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);

        int resultCount= categoryMapper.insert(category);
        if(resultCount>0) {
        	return ResponseEntityUtil.success("添加分类操作成功");
        }
		return ResponseEntityUtil.fail("添加分类操作失败");
	}

	@Override
	public ResponseEntity<String> updateCategoryName(Integer categoryId, String categoryName) {
        if(categoryId==null || StringUtils.isBlank(categoryName)) {
        	return ResponseEntityUtil.fail(Errors.SYSTEM_REQUEST_PARAM_ERROR.getCode(),"参数错误");
        }
        
        Category category=new Category();
        category.setName(categoryName);
        category.setId(categoryId);

        int resultCount= categoryMapper.updateByPrimaryKeySelective(category);
        if(resultCount>0) {
        	return ResponseEntityUtil.success("更新分类名称成功");
        }
		return ResponseEntityUtil.fail("更新分类名称失败");
	}

	@Override
	public ResponseEntity<List<Category>> getChildParallelCategory(Integer categoryId) {
		List<Category> categories= categoryMapper.selectCategoryChildrenByParentId(categoryId);
		if(CollectionUtils.isEmpty(categories)) {
			logger.info("未找到当前分类的子分类");
		}
		return ResponseEntityUtil.success(categories);
	}
	
	//递归算法，算出子节点
	private Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId){
		Category category= categoryMapper.selectByPrimaryKey(categoryId);
		if(category!=null) {
			categorySet.add(category);
		}
		//查找子节点，递归算法一定要有一个退出的条件
		List<Category> categories= categoryMapper.selectCategoryChildrenByParentId(categoryId);
		for(Category categoryItem: categories) {
			findChildCategory(categorySet, categoryItem.getId());
		}
		return categorySet;
	}

	@Override
	public ResponseEntity<List<Integer>> selectCategoryAndChildrenById(Integer categoryId) {
		Set<Category> categories= Sets.newHashSet();
		findChildCategory(categories, categoryId);
		
		List<Integer> categoryIdList= Lists.newArrayList();
		if(categoryId!=null) {
			for(Category category: categories) {
				categoryIdList.add(category.getId());
			}
		}
		return ResponseEntityUtil.success(categoryIdList);
		
		
		
	}

}
