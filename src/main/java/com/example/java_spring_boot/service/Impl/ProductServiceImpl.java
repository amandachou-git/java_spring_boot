package com.example.java_spring_boot.service.Impl;

import com.example.java_spring_boot.converter.ProductConverter;
import com.example.java_spring_boot.dao.repository.MockProductDAO;
import com.example.java_spring_boot.dao.repository.ProductRepository;
import com.example.java_spring_boot.dto.request.ProductListRequest;
import com.example.java_spring_boot.dto.request.ProductRequest;
import com.example.java_spring_boot.dto.response.Product;
import com.example.java_spring_boot.dto.response.ProductResponse;
import com.example.java_spring_boot.service.MailService;
import com.example.java_spring_boot.service.ProductService;
import com.example.java_spring_boot.util.exception.NotFoundException;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ProductServiceImpl implements ProductService {
    private MockProductDAO productDAO;
    private ProductRepository productRepository;
    private MailService mailService;

    public ProductServiceImpl(ProductRepository productRepository, MockProductDAO productDAO, MailService mailService) {
        this.productRepository = productRepository;
        this.productDAO = productDAO;
        this.mailService = mailService;
    }

    @Override
    public ProductResponse getProduct(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find product."));
        return ProductConverter.toProductResponse(product);
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        Product product = ProductConverter.toProduct(request);
        Product newProduct = productRepository.insert(product);
        mailService.sendNewProductMail(newProduct.getId());
        return ProductConverter.toProductResponse(newProduct);
    }

    @Override
    public ProductResponse updateProduct(String id, ProductRequest request) {
        Product oldProduct = ProductConverter.toProduct(request);
        Product newProduct = ProductConverter.toProduct(request);
        newProduct.setId(oldProduct.getId());
        Product product = productRepository.save(newProduct);
        return ProductConverter.toProductResponse(product);
    }

    @Override
    public void deleteProductById(String id) {
        productRepository.deleteById(id);
        mailService.sendDeleteProductMail(id);
    }

    @Override
    public List<ProductResponse> getProducts(ProductListRequest request) {

        String keyword = Optional.ofNullable(request.getKeyword()).orElse("");
        int priceFrom = Optional.ofNullable(request.getPriceFrom()).orElse(0);
        int priceTo = Optional.ofNullable(request.getPriceTo()).orElse(Integer.MAX_VALUE);
        Sort sort = genSortingStrategy(request.getOrderBy(), request.getSortRule());

        List<Product> products = productRepository.findByPriceBetweenAndNameLikeIgnoreCase(priceFrom, priceTo, keyword, sort);

        return products.stream()
                .map(ProductConverter::toProductResponse)
                .toList();
    }

    private Sort genSortingStrategy(String orderBy, String sortRule) {

        Sort sort = Sort.unsorted();
        if (Objects.nonNull(orderBy) && Objects.nonNull(sortRule)) {
            Sort.Direction direction = Sort.Direction.fromString(sortRule);
            sort = Sort.by(direction, orderBy);
        }

        return sort;
    }
}
