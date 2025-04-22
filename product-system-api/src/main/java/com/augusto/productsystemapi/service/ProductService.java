package com.augusto.productsystemapi.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.augusto.productsystemapi.client.FileUploadServiceClient;
import com.augusto.productsystemapi.dtos.image.ImageMetadata;
import com.augusto.productsystemapi.dtos.product.ProductInputDto;
import com.augusto.productsystemapi.dtos.product.ProductOutputDto;
import com.augusto.productsystemapi.dtos.product.ProductUpdateDto;
import com.augusto.productsystemapi.exceptions.ProductException;
import com.augusto.productsystemapi.model.Product;
import com.augusto.productsystemapi.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    FileUploadServiceClient fileClient;

    public ProductOutputDto createProduct(ProductInputDto productInput, MultipartFile file) {
        var newProduct = toProduct(productInput);
        callUploadImgClient(newProduct, file);
        productRepository.save(newProduct);
        return toProductOutputDto(newProduct);
    }

    private String callUploadImgClient(Product product, MultipartFile file) {
        var metadaDto = new ImageMetadata("product", file.getOriginalFilename(), product.getCode());
        try {
            var stringMetadaJson = objectMapper.writeValueAsString(metadaDto);
            return fileClient.createNewImage(stringMetadaJson, file);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<ProductOutputDto> findAllProducts() {
        var allProducts = productRepository.findAll();
        return allProducts.stream().map(p -> toProductOutputDto(p)).toList();
    }

    public ProductOutputDto updateProduct(ProductUpdateDto productUpdateDto) {
        var product = productRepository.findProductByCode(productUpdateDto.code()).orElseThrow(
                () -> new ProductException(HttpStatus.BAD_REQUEST, "Produto nao encontrado"));
        product.setName(productUpdateDto.name());
        product.setPrice(productUpdateDto.price());
        product.setQuantity(productUpdateDto.quantity());
        product.getUpdatedDate().add(LocalDateTime.now().toString());
        productRepository.save(product);
        return toProductOutputDto(product);
    }

    public ProductOutputDto findProductByCode(String code) {
        var product = productRepository.findProductByCode(code).orElseThrow(
                () -> new ProductException(HttpStatus.BAD_REQUEST, "Produto nao encontrado"));
        return toProductOutputDto(product);
    }

    private String generateCode() {
        char[] alphanumeric =
                "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        StringBuilder random = new StringBuilder();

        for (int i = 0; i <= 5; i++) {
            int index = (int) (ThreadLocalRandom.current().nextDouble() * alphanumeric.length);
            random.append(alphanumeric[index]);
        }
        return random.toString();
    }

    private Product toProduct(ProductInputDto productInput) {
        var product = new Product();
        product.setName(productInput.name());
        product.setPrice(productInput.price());
        product.setQuantity(productInput.quantity());
        product.setAddedDate(LocalDateTime.now().toString());
        product.setUpdatedDate(new ArrayList<String>());
        product.getUpdatedDate().add(LocalDateTime.now().toString());
        product.setCode(generateCode());
        product.setCnpjOwner(productInput.cnpjOwner());
        product.setDescription(productInput.description());
        return product;
    }

    protected ProductOutputDto toProductOutputDto(Product product) {
        return new ProductOutputDto(product.getName(), product.getAddedDate(),
                product.getUpdatedDate(), product.getQuantity(), product.getCode(),
                product.getPrice(), product.getCnpjOwner(), product.getDescription());
    }
}
