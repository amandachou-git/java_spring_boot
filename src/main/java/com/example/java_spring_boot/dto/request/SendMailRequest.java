package com.example.java_spring_boot.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SendMailRequest {
    @NotEmpty
    private String subject;

    @NotEmpty
    private String content;

    @NotEmpty
    private String receivers;
}
