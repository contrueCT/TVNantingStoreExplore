package com.contrue.entity.po;

import com.contrue.annotation.Column;
import com.contrue.annotation.ForeignKey;
import com.contrue.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/**
 * 商铺表
 * @author confff
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "store")
public class Store {
    @Column(name = "id")
    private Integer id;
    @Column(name = "store_name")
    private String name;
    @Column(name = "store_password")
    private String password;
    @Column(name = "store_address")
    private String address;
    @Column(name = "store_phone")
    private String phone;
    @Column(name = "store_short_description")
    private String shortDescription;
    @Column(name = "store_description")
    private String description;
    @ForeignKey(name = "followers")
    private List<User> followers;
    @ForeignKey(name = "roles")
    private List<Role> roles;
    @ForeignKey(name = "likes")
    private List<Like> likes;
    @ForeignKey(name = "comments")
    private List<Comment> comments;

    private boolean liked;
}
