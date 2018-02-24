package com.zime.web.service;

import com.github.pagehelper.PageInfo;
import com.zime.web.dao.entity.Product;
import com.zime.web.util.ResponseEntity;
import com.zime.web.vo.ProductDetailVo;

public interface ProductService {

	ResponseEntity saveorUpdateProduct(Product product);
	
	ResponseEntity setSaleStatus(Integer productId, Integer status);

	ResponseEntity<ProductDetailVo> manageProductDetail(Integer productId);
	
	ResponseEntity<PageInfo>  getProductList(int pageNum, int pageSize);
	
	ResponseEntity<PageInfo>  searchProducts(String productName, Integer productId ,int pageNum, int pageSize);

	ResponseEntity<ProductDetailVo> getProductDetail(Integer productId);
	
	ResponseEntity<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize,String orderBy);
}
