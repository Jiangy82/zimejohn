package com.zime.web.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.zime.web.config.FtpConfig;
import com.zime.web.contants.Const;
import com.zime.web.contants.Errors;
import com.zime.web.dao.entity.Category;
import com.zime.web.dao.entity.Product;
import com.zime.web.dao.mapper.CategoryMapper;
import com.zime.web.dao.mapper.ProductMapper;
import com.zime.web.service.CategoryService;
import com.zime.web.service.ProductService;
import com.zime.web.util.DateUtil;
import com.zime.web.util.ResponseEntity;
import com.zime.web.util.ResponseEntityUtil;
import com.zime.web.vo.ProductDetailVo;
import com.zime.web.vo.ProductListVo;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductMapper productMapper;
	@Autowired
	private CategoryMapper categoryMapper;
	@Autowired
	private FtpConfig ftpConfig;
	
	@Autowired
	private CategoryService categoryService;

	@Override
	public ResponseEntity saveorUpdateProduct(Product product) {
		if (product != null) {
			if (StringUtils.isNoneBlank(product.getSubImages())) {
				String[] subImageArray = product.getSubImages().split(",");
				if (subImageArray.length > 0) {
					product.setMainImage(subImageArray[0]);
				}
			}
			if (product.getId() != null) {
				int resultCount = productMapper.updateByPrimaryKey(product);
				if (resultCount > 0) {
					return ResponseEntityUtil.success("更新产品成功");
				}
				return ResponseEntityUtil.fail(Errors.SYSTEM_UPDATE_ERROR.getCode(), "更新产品失败");

			} else {
				int resultCount = productMapper.insert(product);
				if (resultCount > 0) {
					return ResponseEntityUtil.success("新增产品成功");
				}
				return ResponseEntityUtil.fail("新增产品失败");
			}
		}
		return ResponseEntityUtil.fail("新增或更新产品参数不正确");
	}

	@Override
	public ResponseEntity setSaleStatus(Integer productId, Integer status) {
		if(productId==null || status==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_REQUEST_PARAM_ERROR.getCode(), "请求参数不正确");
		}
		Product product=new Product();
		product.setId(productId);
		product.setStatus(status);
		
		int resultCount= productMapper.updateByPrimaryKeySelective(product);
		if(resultCount>0) {
			return ResponseEntityUtil.success("修改产品销售状态成功");
		}
		return ResponseEntityUtil.fail("修改产品销售状态失败");
	}

	@Override
	public ResponseEntity<ProductDetailVo> manageProductDetail(Integer productId) {
		if(productId==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_REQUEST_PARAM_ERROR.getCode(),"请求参数有误");
		}
		Product product= productMapper.selectByPrimaryKey(productId);
		if(product==null) {
			return ResponseEntityUtil.fail("该产品已删除或下架");
		}
		
		ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ResponseEntityUtil.success(productDetailVo);
	}
	
	private ProductDetailVo assembleProductDetailVo(Product product) {
		ProductDetailVo productDetailVo = new ProductDetailVo();
		
		productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());
        
        productDetailVo.setImageHost(ftpConfig.getServerHttpPrefix());
        Category category=categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(category==null) {
        	productDetailVo.setParentCategoryId(0);
        }else {
        	productDetailVo.setParentCategoryId(category.getParentId());
        }

        productDetailVo.setCreateTime(DateUtil.format(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateUtil.format(product.getUpdateTime()));
        
		return productDetailVo;
	}

	public ResponseEntity<PageInfo>  getProductList(int pageNum, int pageSize) {
		
		PageHelper.startPage(pageNum, pageSize);
		List<Product> products= productMapper.selectList();
		List<ProductListVo> productListVos= Lists.newArrayList();
		for(Product product : products) {
			ProductListVo productListVo= assembleProductListVo(product);
			productListVos.add(productListVo);
		}
		PageInfo pageInfo =new PageInfo(products);
		pageInfo.setList(productListVos);
		
		return ResponseEntityUtil.success(pageInfo);
	}
	
	private ProductListVo assembleProductListVo(Product product) {
		ProductListVo productListVo= new ProductListVo();
		productListVo.setId(product.getId());
		productListVo.setName(product.getName());
		productListVo.setCategoryId(product.getCategoryId());
		productListVo.setImageHost(ftpConfig.getServerHttpPrefix());
		productListVo.setMainImage(product.getMainImage());
		productListVo.setPrice(product.getPrice());
		productListVo.setSubtitle(product.getSubtitle());
		productListVo.setStatus(product.getStatus());
		
		return productListVo;
	}
	
	public ResponseEntity<PageInfo>  searchProducts(String productName, Integer productId ,int pageNum, int pageSize){
		
		PageHelper.startPage(pageNum, pageSize);
		if(StringUtils.isNoneBlank(productName)) {
			productName= new StringBuilder().append("%").append(productName).append("%").toString();
		}
		List<Product> products= productMapper.selectByNameAndProductId(productName, productId);
		List<ProductListVo> productListVos= Lists.newArrayList();
		for(Product product : products) {
			ProductListVo productListVo= assembleProductListVo(product);
			productListVos.add(productListVo);
		}
		PageInfo pageInfo =new PageInfo(products);
		pageInfo.setList(productListVos);
		
		return ResponseEntityUtil.success(pageInfo);
	}

	@Override
	public ResponseEntity<ProductDetailVo> getProductDetail(Integer productId) {
		if(productId==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_REQUEST_PARAM_ERROR.getCode(),"请求参数有误");
		}
		Product product= productMapper.selectByPrimaryKey(productId);
		if(product==null) {
			return ResponseEntityUtil.fail("该产品已删除或下架");
		}
		if(product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()) {
			return ResponseEntityUtil.fail("该产品已删除或下架");
		}
		ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ResponseEntityUtil.success(productDetailVo);
	}
	
	public ResponseEntity<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize,String orderBy){
		if(StringUtils.isBlank(keyword) && categoryId==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_REQUEST_PARAM_ERROR.getCode(), "请求参数错误");
		}
		
		List<Integer> categoryIdList= new ArrayList<Integer>();
		
		if(categoryId!=null) {
			Category category= categoryMapper.selectByPrimaryKey(categoryId);
			if(category ==null && StringUtils.isBlank(keyword)) {
				PageHelper.startPage(pageNum, pageSize);
				List<ProductListVo> productListVos=Lists.newArrayList();
				PageInfo pageInfo=new PageInfo<>(productListVos);
				return ResponseEntityUtil.success(pageInfo);
			}
			categoryIdList= categoryService.selectCategoryAndChildrenById(category.getId()).getData();
		}
		
	
		if(StringUtils.isNotBlank(keyword)) {
			keyword= new StringBuilder().append("%").append(keyword).append("%").toString();
		}
		//排序处理
		PageHelper.startPage(pageNum, pageSize);
		if(StringUtils.isNotBlank(orderBy)) {
			if(Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
				String[] orderByArray= orderBy.split("_");
				PageHelper.orderBy(orderByArray[0]+" "+orderByArray[1]);
			}
		}
        List<Product> products = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword)?null:keyword,categoryIdList.size()==0?null:categoryIdList);
		List<ProductListVo> productListVos= Lists.newArrayList();
		for (Product product : products) {
			ProductListVo productListVo= assembleProductListVo(product);
			productListVos.add(productListVo);
		}
		
		PageInfo pageInfo= new PageInfo<>(products);
		pageInfo.setList(productListVos);
		
		return ResponseEntityUtil.success(pageInfo);
		
	}
}
