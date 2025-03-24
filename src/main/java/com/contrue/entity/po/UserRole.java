package com.contrue.entity.po;

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
@Table(name = "user_role")
public class UserRole {
    @Column(name = "user_id")
    private int userId;
    @Column(name = "role_id")
    private int roleId;

}
