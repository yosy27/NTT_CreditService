package com.nttdata.credit_service.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    private String id;
    private String document;
    private String fullName;
    private String type; // personal, business, etc. (si aplica)
}
