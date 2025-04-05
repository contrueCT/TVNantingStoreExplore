// storeDetail.js - 商铺详情页面

import api from '../api.js';
import auth from '../auth.js';

/**
 * 商铺详情页面渲染函数
 * @param {HTMLElement} container - 页面容器元素
 * @param {Object} params - 路由参数，包含商铺ID
 */
export default async function StoreDetailPage(container, params) {
    // 检查是否有商铺ID
    if (!params || !params.id) {
        container.innerHTML = `
            <div class="page active">
                <div class="error-message">没有指定商铺ID，无法加载详情</div>
                <a href="#stores" class="button">返回商铺列表</a>
            </div>
        `;
        return;
    }
    
    const storeId = params.id;
    
    // 创建页面基础结构
    container.innerHTML = `
        <div class="page active">
            <div class="store-detail-header">
                <a href="#stores" class="back-button">返回列表</a>
                <h2>商铺详情</h2>
            </div>
            
            <div id="store-loading" class="loading">加载中...</div>
            
            <div id="store-detail-container" class="store-detail">
                <!-- 商铺详情将在这里动态生成 -->
            </div>
            
            <div id="store-comments-section" class="comments-section" style="display:none;">
                <h3>用户评论</h3>
                
                <div id="comments-container">
                    <!-- 评论列表将在这里动态生成 -->
                </div>
                
                <div id="comment-form-container" style="display:none;">
                    <h4>发表评论</h4>
                    <form id="comment-form" class="comment-form">
                        <textarea id="comment-content" placeholder="请输入您的评论..." rows="3" required></textarea>
                        <button type="submit" class="button">提交评论</button>
                    </form>
                    <div id="comment-message" class="message"></div>
                </div>
            </div>
        </div>
    `;
    
    // 添加样式
    const styleElement = document.createElement('style');
    styleElement.textContent = `
        .store-detail-header {
            display: flex;
            align-items: center;
            margin-bottom: 20px;
        }
        .back-button {
            background: #f0f0f0;
            border: none;
            padding: 5px 10px;
            border-radius: 4px;
            margin-right: 15px;
            text-decoration: none;
            color: #333;
        }
        .error-message {
            padding: 20px;
            background-color: #f8d7da;
            color: #721c24;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        .store-detail {
            background: #fff;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }
        .store-info {
            margin-bottom: 20px;
        }
        .store-info h2 {
            margin-top: 0;
            margin-bottom: 10px;
        }
        .store-info .store-address {
            color: #666;
            margin-bottom: 10px;
        }
        .store-info .store-phone {
            color: #666;
            margin-bottom: 15px;
        }
        .store-description {
            line-height: 1.6;
            color: #444;
            border-top: 1px solid #eee;
            padding-top: 15px;
        }
        .store-actions {
            display: flex;
            align-items: center;
            gap: 15px;
            margin: 20px 0;
        }
        .like-button {
            display: flex;
            align-items: center;
            gap: 5px;
            background: #f0f0f0;
            border: none;
            padding: 5px 12px;
            border-radius: 4px;
            cursor: pointer;
            transition: all 0.2s;
        }
        .like-button.liked {
            background: #ff6b6b;
            color: white;
        }
        .like-button:hover {
            background: #e0e0e0;
        }
        .like-button.liked:hover {
            background: #ff5252;
        }
        .comments-section {
            background: #fff;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
        }
        .comments-section h3 {
            margin-top: 0;
            margin-bottom: 20px;
            border-bottom: 1px solid #eee;
            padding-bottom: 10px;
        }
        .comment-list {
            list-style: none;
            padding: 0;
        }
        .comment-item {
            padding: 15px 0;
            border-bottom: 1px solid #f0f0f0;
        }
        .comment-item:last-child {
            border-bottom: none;
        }
        .comment-header {
            display: flex;
            justify-content: space-between;
            margin-bottom: 5px;
        }
        .comment-author {
            font-weight: bold;
        }
        .comment-date {
            color: #999;
            font-size: 0.9em;
        }
        .comment-content {
            line-height: 1.5;
            margin-bottom: 10px;
        }
        .comment-actions {
            display: flex;
            align-items: center;
            gap: 10px;
        }
        .comment-like {
            display: flex;
            align-items: center;
            gap: 3px;
            color: #888;
            cursor: pointer;
            font-size: 0.9em;
            background: transparent;
            border: none;
        }
        .comment-like.liked {
            color: #ff6b6b;
        }
        .comment-delete {
            color: #dc3545;
            cursor: pointer;
            font-size: 0.9em;
            background: transparent;
            border: none;
        }
        .comment-form {
            margin-top: 15px;
        }
        .comment-form textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            margin-bottom: 10px;
            resize: vertical;
        }
        .no-comments {
            color: #888;
            text-align: center;
            padding: 20px 0;
        }
        .loading {
            text-align: center;
            padding: 20px;
            color: #666;
        }
        .message {
            margin-top: 10px;
            padding: 10px;
            border-radius: 4px;
            display: none;
        }
        .message.error {
            display: block;
            background-color: #f8d7da;
            color: #721c24;
        }
        .message.success {
            display: block;
            background-color: #d4edda;
            color: #155724;
        }
    `;
    document.head.appendChild(styleElement);
    
    // 获取页面元素
    const storeDetailContainer = container.querySelector('#store-detail-container');
    const loadingElement = container.querySelector('#store-loading');
    const commentsSection = container.querySelector('#store-comments-section');
    const commentsContainer = container.querySelector('#comments-container');
    const commentFormContainer = container.querySelector('#comment-form-container');
    const commentForm = container.querySelector('#comment-form');
    const commentMessage = container.querySelector('#comment-message');
    
    // 加载商铺详情
    try {
        // 加载中
        loadingElement.style.display = 'block';
        storeDetailContainer.innerHTML = '';
        commentsSection.style.display = 'none';
        
        // 调用API获取商铺详情
        const storeDetails = await api.getStoreDetails(storeId);
        
        // 隐藏加载中
        loadingElement.style.display = 'none';
        
        if (!storeDetails) {
            storeDetailContainer.innerHTML = `
                <div class="error-message">无法加载商铺详情，商铺可能不存在</div>
            `;
            return;
        }
        
        // 检查用户是否已登录
        const isLoggedIn = auth.isLoggedIn();
        const hasLiked = storeDetails.userHasLiked || false; // 假设API会返回当前用户是否已点赞
        
        // 渲染商铺详情
        storeDetailContainer.innerHTML = `
            <div class="store-info">
                <h2>${storeDetails.name}</h2>
                <div class="store-address">📍 地址：${storeDetails.address}</div>
                <div class="store-phone">📞 电话：${storeDetails.phone || '未提供'}</div>
                <div class="store-meta">
                    <span>👍 ${storeDetails.likes || 0} 人点赞</span> · 
                    <span>💬 ${storeDetails.comments ? storeDetails.comments.length : 0} 条评论</span>
                </div>
            </div>
            
            <div class="store-actions">
                <button id="like-button" class="like-button ${hasLiked ? 'liked' : ''}" 
                    ${!isLoggedIn ? 'disabled' : ''}>
                    ${hasLiked ? '❤️ 已点赞' : '🤍 点赞'}
                </button>
                ${isLoggedIn ? '' : '<span class="login-hint">登录后可点赞评论</span>'}
            </div>
            
            <div class="store-description">
                <h3>商铺简介</h3>
                <p>${storeDetails.description || storeDetails.intro || '暂无详细描述'}</p>
            </div>
        `;
        
        // 设置点赞按钮事件
        const likeButton = container.querySelector('#like-button');
        if (likeButton && isLoggedIn) {
            likeButton.addEventListener('click', async () => {
                try {
                    if (hasLiked) {
                        // 已点赞，取消点赞
                        await api.unlikeStore(storeId);
                        likeButton.classList.remove('liked');
                        likeButton.textContent = '🤍 点赞';
                    } else {
                        // 未点赞，添加点赞
                        await api.likeStore(storeId, {
                            targetName: storeDetails.name,
                            targetId: storeId,
                            targetType: 'store'
                        });
                        likeButton.classList.add('liked');
                        likeButton.textContent = '❤️ 已点赞';
                    }
                } catch (error) {
                    console.error('点赞操作失败:', error);
                    alert('点赞操作失败，请稍后重试');
                }
            });
        }
        
        // 显示评论区域
        commentsSection.style.display = 'block';
        
        // 渲染评论列表
        renderComments(storeDetails.comments || []);
        
        // 如果已登录，显示评论表单
        if (isLoggedIn) {
            commentFormContainer.style.display = 'block';
            
            // 设置评论表单提交事件
            commentForm.addEventListener('submit', async (e) => {
                e.preventDefault();
                
                const commentContent = commentForm.querySelector('#comment-content').value.trim();
                if (!commentContent) {
                    displayCommentMessage('评论内容不能为空', 'error');
                    return;
                }
                
                try {
                    displayCommentMessage('正在提交评论...', 'info');
                    
                    await api.commentStore(storeId, {
                        content: commentContent,
                        targetName: storeDetails.name,
                        targetId: storeId
                    });
                    
                    displayCommentMessage('评论发表成功！', 'success');
                    commentForm.reset();
                    
                    // 重新加载页面以更新评论列表
                    setTimeout(() => {
                        window.location.reload();
                    }, 1000);
                } catch (error) {
                    console.error('评论提交失败:', error);
                    displayCommentMessage('评论提交失败，请稍后重试', 'error');
                }
            });
        }
        
    } catch (error) {
        console.error('加载商铺详情失败:', error);
        loadingElement.style.display = 'none';
        storeDetailContainer.innerHTML = `
            <div class="error-message">加载商铺详情失败，请稍后重试</div>
        `;
    }
    
    /**
     * 渲染评论列表
     * @param {Array} comments - 评论列表数据
     */
    function renderComments(comments) {
        if (!comments || comments.length === 0) {
            commentsContainer.innerHTML = `
                <div class="no-comments">暂无评论，成为第一个评论的人吧！</div>
            `;
            return;
        }
        
        // 检查用户是否已登录
        const isLoggedIn = auth.isLoggedIn();
        const currentUserId = localStorage.getItem('userId'); // 假设登录时存储了用户ID
        
        let commentsList = document.createElement('ul');
        commentsList.className = 'comment-list';
        
        comments.forEach(comment => {
            const commentItem = document.createElement('li');
            commentItem.className = 'comment-item';
            commentItem.dataset.commentId = comment.id;
            
            // 格式化时间日期
            const commentDate = new Date(comment.createTime || comment.createdAt);
            const formattedDate = `${commentDate.getFullYear()}-${String(commentDate.getMonth() + 1).padStart(2, '0')}-${String(commentDate.getDate()).padStart(2, '0')} ${String(commentDate.getHours()).padStart(2, '0')}:${String(commentDate.getMinutes()).padStart(2, '0')}`;
            
            // 检查当前用户是否已经点赞该评论
            const hasLikedComment = comment.userHasLiked || false;
            
            // 检查该评论是否是当前用户发表的
            const isUserComment = currentUserId && comment.userId === currentUserId;
            
            commentItem.innerHTML = `
                <div class="comment-header">
                    <span class="comment-author">${comment.username || '匿名用户'}</span>
                    <span class="comment-date">${formattedDate}</span>
                </div>
                <div class="comment-content">${comment.content}</div>
                <div class="comment-actions">
                    <button class="comment-like ${hasLikedComment ? 'liked' : ''}" 
                        ${!isLoggedIn ? 'disabled' : ''}>
                        ${hasLikedComment ? '❤️' : '🤍'} ${comment.likes || 0}
                    </button>
                    ${isUserComment ? '<button class="comment-delete">删除</button>' : ''}
                </div>
            `;
            
            // 设置评论点赞事件
            const likeButton = commentItem.querySelector('.comment-like');
            if (likeButton && isLoggedIn) {
                likeButton.addEventListener('click', async () => {
                    try {
                        if (hasLikedComment) {
                            // 已点赞，取消点赞
                            await api.unlikeComment(storeId, comment.id);
                            likeButton.classList.remove('liked');
                            likeButton.innerHTML = `🤍 ${(comment.likes || 1) - 1}`;
                        } else {
                            // 未点赞，添加点赞
                            await api.likeComment(storeId, comment.id, {
                                targetName: '评论',
                                targetId: comment.id,
                                targetType: 'comment'
                            });
                            likeButton.classList.add('liked');
                            likeButton.innerHTML = `❤️ ${(comment.likes || 0) + 1}`;
                        }
                    } catch (error) {
                        console.error('评论点赞操作失败:', error);
                        alert('点赞操作失败，请稍后重试');
                    }
                });
            }
            
            // 设置评论删除事件
            const deleteButton = commentItem.querySelector('.comment-delete');
            if (deleteButton) {
                deleteButton.addEventListener('click', async () => {
                    if (confirm('确定要删除这条评论吗？')) {
                        try {
                            await api.deleteComment(storeId, comment.id);
                            commentItem.remove();
                            
                            // 如果删除后没有评论了，显示无评论提示
                            if (commentsList.children.length === 0) {
                                commentsContainer.innerHTML = `
                                    <div class="no-comments">暂无评论，成为第一个评论的人吧！</div>
                                `;
                            }
                        } catch (error) {
                            console.error('评论删除失败:', error);
                            alert('删除评论失败，请稍后重试');
                        }
                    }
                });
            }
            
            commentsList.appendChild(commentItem);
        });
        
        commentsContainer.innerHTML = '';
        commentsContainer.appendChild(commentsList);
    }
    
    /**
     * 显示评论表单消息
     * @param {string} message - 消息内容
     * @param {string} type - 消息类型 (success/error/info)
     */
    function displayCommentMessage(message, type) {
        commentMessage.textContent = message;
        commentMessage.className = `message ${type}`;
    }
}