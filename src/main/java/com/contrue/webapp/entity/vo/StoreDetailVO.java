package com.contrue.webapp.entity.vo;

import com.contrue.webapp.entity.po.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author confff
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreDetailVO {
    private Integer id;
    private String name;
    private String description;
    private String address;
    private String phone;
    private String email;
    private String shortDescription;
    private Integer likesCount;
    private Integer commentsCount;

    private List<Comment> comments;

}
