package com.contrue.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author confff
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthToken {
    String accessToken;
    String refreshToken;
}
