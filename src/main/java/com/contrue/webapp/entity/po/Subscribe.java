package com.contrue.webapp.entity.po;

import com.contrue.Framework.annotation.Column;
import com.contrue.Framework.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author confff
 */
@Table(name = "subscribes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subscribe {
    @Column(name = "subscribes_user_id")
    private Integer userId;
    @Column(name = "subscribes_target_id")
    private Integer targetId;
    @Column(name = "subscribes_target_type")
    private String targetType;
    @Column(name = "subscribes_target_name")
    private String targetName;
    @Column(name = "subscribes_create_time")
    private String createTime;

}
