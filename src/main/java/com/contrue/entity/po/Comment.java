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
@Table(name = "comment")
public class Comment {
    @Column(name = "comment_id")
    private Integer id;
    @Column(name = "comment_user_id")
    private Integer userId;
    @Column(name = "comment_target_id")
    private Integer targetId;
    @Column(name = "comment_user_name")
    private String userName;
    @Column(name = "comment_target_name")
    private String targetName;
    @Column(name = "content")
    private String content;
    @Column(name = "comment_create_time")
    private LocalDateTime createTime;

    private boolean liked;
}
