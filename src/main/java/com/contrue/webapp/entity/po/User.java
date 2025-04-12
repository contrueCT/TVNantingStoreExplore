package com.contrue.webapp.entity.po;

import com.contrue.Framework.annotation.Column;
import com.contrue.Framework.annotation.ForeignKey;
import com.contrue.Framework.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户表
 * @author confff
 */
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    //用户id
    @Column(name = "id")
    private Integer id;
    //用户名
    @Column(name = "user_name")
    private String username;
    @Column(name = "password")
    private String password;
    //邮箱选填
    @Column(name = "email")
    private String email;
    //电话号码必填
    @Column(name = "user_phone")
    private String phone;
    //地址选填
    @Column(name = "user_address")
    private String address;
    //年龄选填
    @Column(name = "age")
    private Integer age;
    //性别选填
    @Column(name = "gender")
    private String gender;
    //关注的数量
    @Column(name = "user_subscribes_count")
    private Integer subscribesCount;
    //粉丝的数量
    @Column(name = "user_followers_count")
    private Integer followersCount;

    @ForeignKey(name = "likes")
    private List<Like> likes;
    @ForeignKey(name = "role")
    private List<Role> roles;
    @ForeignKey(name = "comment")
    private List<Comment> comments;
    //多表查寻获得用户权限
    @ForeignKey(name = "permission")
    private List<Permission> permissions;
    //多表查寻获得用户关注的商铺/用户
    @ForeignKey(name = "subscribes")
    private List<Object> subscribes;
}
