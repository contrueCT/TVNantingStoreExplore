package com.contrue.webapp.entity.po;

import com.contrue.Framework.annotation.Column;
import com.contrue.Framework.annotation.Table;
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
    @Column(name = "method")
    private String method;
    @Column(name = "url")
    private String url;
}
