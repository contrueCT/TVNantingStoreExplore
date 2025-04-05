package com.contrue.entity.po;

import com.contrue.annotation.Column;
import com.contrue.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author confff
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "likes")
public class Like {
    @Column(name = "like_id")
    private Integer id;
    //点赞者的id
    @Column(name = "like_user_id")
    private Integer userId;
    //被点赞的目标
    @Column(name = "like_target_id")
    private Integer targetId;
    //商铺或评论
    @Column(name = "like_target_type")
    //目标类型（商铺或评论）
    private String targetType;
    @Column(name = "like_target_name")
    private String targetName;
    @Column(name = "like_user_name")
    private String userName;
    //点赞创建时间
    @Column(name = "like_create_time")
    private LocalDateTime createTime;
}
