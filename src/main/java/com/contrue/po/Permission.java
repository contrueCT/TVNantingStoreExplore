package com.contrue.po;

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
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
}
