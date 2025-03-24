package com.contrue.entity.po;

import com.contrue.annotation.Column;
import com.contrue.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 权限表
 * @author confff
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "permission")
public class Permission {
    @Column(name = "permission_id")
    private Integer id;
    @Column(name = "permission_name")
    private String name;
    @Column(name = "permission_description")
    private String description;
}
