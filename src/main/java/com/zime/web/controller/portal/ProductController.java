package com.zime.web.controller.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.zime.web.service.ProductService;
import com.zime.web.util.ResponseEntity;
import com.zime.web.vo.ProductDetailVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description="前端产品操作接口",produces = "application/json")
@RestController
@RequestMapping("/product/")
public class ProductController {
	@Autowired
	private ProductService productService;
	
	@ApiOperation(value = "获取产品详情操作接口",notes = "获取产品详情")
	@RequestMapping(value="detail.do")	
	@ResponseBody
	public ResponseEntity<ProductDetailVo> detail(Integer productId){
		return productService.getProductDetail(productId);
	}
	@ApiOperation(value = "产品搜索操作接口",notes = "搜索产品")
	@PostMapping(value="list.do")	
	public ResponseEntity<PageInfo> list(
			@RequestParam(value="keyword",required=false)String keyword, 
			@RequestParam(value="categoryId",required=false)int categoryId,
			@RequestParam(value="pageNum",defaultValue="1") int pageNum, 
			@RequestParam(value="pageSize",defaultValue="10")int pageSize, 
			@RequestParam(value="orderBy",defaultValue="")String orderBy){
		return productService.getProductByKeywordCategory(keyword, categoryId, pageNum, pageSize, orderBy);
	}

}
