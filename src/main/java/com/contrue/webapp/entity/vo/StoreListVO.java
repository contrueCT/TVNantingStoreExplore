package com.contrue.webapp.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author confff
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreListVO {
    private Integer id;
    private String name;
    private Integer likesCount;
    private Integer commentsCount;
    private String shortDescription;
    private String address;

}
