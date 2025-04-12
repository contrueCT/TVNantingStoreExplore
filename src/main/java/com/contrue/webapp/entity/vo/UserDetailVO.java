package com.contrue.webapp.entity.vo;

import com.contrue.webapp.entity.po.Comment;
import com.contrue.webapp.entity.po.Like;

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
