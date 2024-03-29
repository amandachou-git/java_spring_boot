package com.example.java_spring_boot.util;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ObjectMapperTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSerializeBookToJson() throws Exception {
        Book book = new Book();
        book.setId("B0001");
        book.setName("Computer Science");
        book.setPrice(350);
        book.setIsbn("978-986-123-456-7");
        book.setCreatedTime(new Date());

        String bookJSONStr = mapper.writeValueAsString(book);
        JSONObject bookJSON = new JSONObject(bookJSONStr);

        Assert.assertEquals(book.getId(), bookJSON.getString("id"));
        Assert.assertEquals(book.getName(), bookJSON.getString("name"));
        Assert.assertEquals(book.getPrice(), bookJSON.getInt("price"));
        Assert.assertEquals(book.getIsbn(), bookJSON.getString("isbn"));
        Assert.assertEquals(book.getCreatedTime().getTime(), bookJSON.getLong("createdTime"));
    }

    @Test
    public void testDeserializeJSONToPublisher() throws Exception {
        JSONObject publisherJSON = new JSONObject()
                .put("companyName", "Teipei Company")
                .put("address", "Tqipei")
                .put("tel", "02-9876-5432");

        String publisherJSONStr = publisherJSON.toString();
        Publisher publisher = mapper.readValue(publisherJSONStr, Publisher.class);

        Assert.assertEquals(publisherJSON.getString("companyName"), publisher.getCompanyName());
        Assert.assertEquals(publisherJSON.getString("address"), publisher.getAddress());
        Assert.assertEquals(publisherJSON.getString("tel"), publisher.getTel());
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL) // 傳入特定參數，可在序列化時納入符合條件的欄位
    private static class Book {
        private String id;
        private String name;
        private int price;
        @JsonIgnore // 序列化時忽略
        private String isbn;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createdTime;
        @JsonUnwrapped // 序列化時展開
        private Publisher publisher;
    }

    @Data
    private static class Publisher {
        private String companyName;
        private String address;
        @JsonProperty("telephone") // 調整欄位名稱
        private String tel;
    }
}
