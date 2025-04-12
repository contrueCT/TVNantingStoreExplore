package com.contrue.webapp.entity.po;

import com.contrue.annotation.Column;
import com.contrue.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author confff
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "store_role")
public class StoreRole {
    @Column(name = "role_id")
    private int roleId;
    @Column(name = "store_id")
    private int storeId;
}
