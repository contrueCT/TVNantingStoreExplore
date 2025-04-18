package com.contrue.webapp.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author confff
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResult {
    private int code;
    private String msg;
    private String accessToken;
    private String refreshToken;
}
