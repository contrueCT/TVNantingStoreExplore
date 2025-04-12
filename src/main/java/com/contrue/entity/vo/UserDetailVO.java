package com.contrue.entity.vo;

import com.contrue.entity.po.Comment;
import com.contrue.entity.po.Like;

import java.util.List;

/**
 * @author confff
 */
public class UserDetailVO {
    private Integer id;
    private String username;
    private Integer likesCount;
    private Integer commentsCount;
    private List<Like> likes;
    private List<Comment> comments;
}
