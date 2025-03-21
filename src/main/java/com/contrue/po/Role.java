package com.contrue.po;

import com.contrue.annotation.Column;
import com.contrue.annotation.ForeignKey;
import com.contrue.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * 角色表
 * @author confff
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "role")
public class Role {
    //角色名
    @Column(name = "role_name")
    private String name;
    //角色id
    @Column(name = "id")
    private Integer id;
    //角色拥有的权限
    @ForeignKey(name = "permissions")
    private List<Permission> permissions;
    @Column(name = "role_description")
    private String description;
}
