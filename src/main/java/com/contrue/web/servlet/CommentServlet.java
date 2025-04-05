package com.contrue.web.servlet;

import com.contrue.entity.po.Comment;
import com.contrue.entity.po.Like;
import com.contrue.entity.vo.Result;
import com.contrue.service.CommentService;
import com.contrue.service.Impl.CommentServiceImpl;
import com.contrue.service.Impl.LikeServiceImpl;
import com.contrue.service.LikesService;
import com.contrue.util.JsonParser;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @author confff
 */
@WebServlet("/api/comments/*")
public class CommentServlet extends BaseServlet {
    @Override
    protected String getServletRegistration() {
        return "/api/comments/{commentId}/like";
    }

    CommentService commentService = CommentServiceImpl.getInstance();
    LikesService likesService = LikeServiceImpl.getInstance();

    //新增评论
    public void postComments(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String userId = request.getAttribute("subjectId").toString();
        String userName = request.getAttribute("subjectName").toString();
        Comment comment = JsonParser.parseJson(request, Comment.class);
        comment.setUserId(Integer.parseInt(userId));
        comment.setUserName(userName);
        if(commentService.commentStore(comment)){
            writeJson(response, Result.success());
        }else{
            writeJson(response, Result.error("评论失败"));
        }
    }

    //删除评论
    public void deleteComments(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String userId = request.getAttribute("subjectId").toString();
        String userName = request.getAttribute("subjectName").toString();
        String commentId = request.getAttribute("commentId").toString();

        Comment comment = new Comment();
        comment.setId(Integer.parseInt(commentId));

        if(commentService.deleteComment(comment)){
            writeJson(response, Result.success());
        }else{
            writeJson(response, Result.error("删除评论失败"));
        }
    }

    //评论点赞
    public void postLike(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String userId = request.getAttribute("subjectId").toString();
        String userName = request.getAttribute("subjectName").toString();
        String commentId = request.getAttribute("commentId").toString();

        Like like = new Like();
        like.setUserId(Integer.parseInt(userId));
        like.setUserName(userName);
        like.setTargetType("comment");
        like.setTargetId(Integer.parseInt(commentId));
        like.setTargetName("comment");
        if(likesService.likeComment(like)){
            writeJson(response, Result.success());
        }else{
            writeJson(response, Result.error("评论点赞失败"));
        }
    }

    //取消评论点赞
    public void deleteLike(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String userId = request.getAttribute("subjectId").toString();
        String userName = request.getAttribute("subjectName").toString();
        String commentId = request.getAttribute("commentId").toString();

        Like like = new Like();
        like.setTargetId(Integer.parseInt(commentId));
        like.setTargetType("comment");
        if(likesService.unlikeComment(like)){
            writeJson(response, Result.success());
        }else{
            writeJson(response, Result.error("取消评论点赞失败"));
        }
    }
}
