package com.kh.demo.web;

import com.kh.demo.dao.Product;
import com.kh.demo.svc.ProductSVC;
import com.kh.demo.web.api.ApiResponse;
import com.kh.demo.web.api.product.AddReq;
import com.kh.demo.web.api.product.EditReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j  // log.info()
//@RestController //Controller+ResponseBody
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiProductControllerV2 {

  private final ProductSVC productSVC;

        //  등록	POST	/api/products
        //@ResponseBody 위에서 전체 적용하여 메소드 레벨은 없어도 됨
        @PostMapping(value = "/products")
        public ApiResponse<Long> add(@RequestBody AddReq addReq){
          log.info("reqMsg={}",addReq);
          //검증

          //AddReq->Product 변환
          Product product = new Product();
          BeanUtils.copyProperties(addReq, product );

          //상품등록
          Long id = productSVC.save(product);

          //응답메세지
          return ApiResponse.createApiResMsg("00","성공",id);
        }

        //  조회	GET	/api/products/{id}
        //@ResponseBody
        @GetMapping("/products/{id}")
        public  ApiResponse<Product> findById(@PathVariable("id") Long id){

          //상품조회
          Optional<Product> findedProduct = productSVC.findByProductId(id);

          //응답메세지
          ApiResponse<Product> response = null;
          if(findedProduct.isPresent()){
            response =  ApiResponse.createApiResMsg("00", "성공", findedProduct.get());
          }else{
            response =  ApiResponse.createApiResMsg("01", "찾고자 하는 상품이 없습니다.", null);
          }
          return response;
        }

      //  수정	PATCH	/api/products/{id}
      @PatchMapping("/products/{id}")
      public ApiResponse<Product> edit(@PathVariable("id") Long id, @RequestBody EditReq editReq){

        //검증
        Optional<Product> findedProduct = productSVC.findByProductId(id);
        if(findedProduct.isEmpty()){
          return ApiResponse.createApiResMsg("01", "수정 하고자 상품이 없습니다.", null);
        }

        //EditReq=>Product 변환
        Product product = new Product();
        BeanUtils.copyProperties(editReq,product);

        //수정
        productSVC.update(id, product);

        //응답메세지
        return ApiResponse.createApiResMsg("01", "수정 하고자 상품이 없습니다.", productSVC.findByProductId(id).get());
      }


        //  삭제	DELETE	/api/products/{id}
        //@ResponseBody
        @DeleteMapping("/products/{id}")
        public ApiResponse<Product> del(@PathVariable("id") Long id){
          //검증
          ApiResponse<Product> response = null;
          Optional<Product> findedProduct = productSVC.findByProductId(id);
          if(findedProduct.isEmpty()){
            return ApiResponse.createApiResMsg("01", "삭제하고자 하는 정보가 없습니다.", null);
          }

          //삭제
         productSVC.deleteByProductId(id);

          //응답메세지
          return ApiResponse.createApiResMsg("00", "성공", null);
        }


        //  목록	GET	/api/products
        // @ResponseBody
        @GetMapping("/products")
        public ApiResponse<List<Product>> findAll(){

          List<Product> list = productSVC.findAll();

          //api응답 메세지
          return ApiResponse.createApiResMsg("00", "성공", list);
        }
}