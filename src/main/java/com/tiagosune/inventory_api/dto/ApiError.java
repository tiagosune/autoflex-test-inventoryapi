package com.tiagosune.inventory_api.dto;

import lombok.Data;

@Data
public class ApiError {

    private String timestamp;
    private String message;
    private int status;
    private String error;
    private String path;
}
