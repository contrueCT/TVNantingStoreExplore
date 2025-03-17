package com.contrue.po;

import com.contrue.annotation.Column;
import com.contrue.annotation.ForeignKey;
import com.contrue.annotation.Table;
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
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    //邮箱选填
    @Column(name = "email")
    private String email;
    //电话号码必填
    @Column(name = "phone")
    private String phone;
    //地址选填
    @Column(name = "address")
    private String address;
    //年龄选填
    @Column(name = "age")
    private Integer age;
    //性别选填
    @Column(name = "gender")
    private String gender;
    @ForeignKey(name = "role")
    private List<Role> roles;
    @ForeignKey(name = "user")
    private List<User> followers;
    //多表查寻获得用户权限
    private List<Permission> permissions;
}
