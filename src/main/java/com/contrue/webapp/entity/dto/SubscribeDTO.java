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
public class SubscribeDTO {
    private Integer userId;
    private Integer targetId;
    private String targetType;
    private String targetName;

}
