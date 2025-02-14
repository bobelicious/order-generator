package com.augusto.productsystemapi.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import com.augusto.productsystemapi.dtos.product.ProductInputDto;
import com.augusto.productsystemapi.dtos.product.ProductOutputDto;
import com.augusto.productsystemapi.dtos.product.ProductUpdateDto;
import com.augusto.productsystemapi.service.ProductService;

@Controller
@RequestMapping("/api/v1/products")
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/new")
    public ResponseEntity<ProductOutputDto> createProduct(@RequestPart ProductInputDto productDto,
            @RequestParam MultipartFile file) {
        return new ResponseEntity<ProductOutputDto>(productService.createProduct(productDto, file),
                HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<ProductOutputDto>> listAllProducts() {
        return new ResponseEntity<List<ProductOutputDto>>(productService.findAllProducts(),
                HttpStatus.OK);
    }

    @GetMapping("/{code}")
    public ResponseEntity<ProductOutputDto> findProductByCode(@PathVariable String code) {
        return new ResponseEntity<ProductOutputDto>(productService.findProductByCode(code),
                HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<ProductOutputDto> updateProduct(
            @RequestBody ProductUpdateDto productUpdateDto) {
        return new ResponseEntity<ProductOutputDto>(productService.updateProduct(productUpdateDto),
                HttpStatus.OK);
    }
}
