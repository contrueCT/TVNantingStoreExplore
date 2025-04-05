// userProfile.js - 个人中心页面

import api from '../api.js';
import auth from '../auth.js';

/**
 * 个人中心页面渲染函数
 * @param {HTMLElement} container - 页面容器元素
 */
export default async function UserProfilePage(container) {
    // 检查用户是否已登录
    if (!auth.isLoggedIn()) {
        container.innerHTML = `
            <div class="page active">
                <div class="error-message">您需要登录才能访问个人中心</div>
                <a href="#login" class="button">前往登录</a>
            </div>
        `;
        return;
    }
    
    // 创建页面基础结构
    container.innerHTML = `
        <div class="page active">
            <div class="profile-header">
                <h2>个人中心</h2>
            </div>
            
            <div id="profile-loading" class="loading">加载中...</div>
            
            <div id="profile-info" class="profile-info" style="display:none;">
                <!-- 用户基本信息将在这里动态生成 -->
            </div>
            
            <div class="profile-actions">
                <button id="view-likes" class="action-button active">我的点赞</button>
                <button id="view-comments" class="action-button">我的评论</button>
            </div>
            
            <div id="records-loading" class="loading" style="display:none;">加载记录中...</div>
            
            <div id="records-container" class="records-container">
                <!-- 记录列表将在这里动态生成 -->
            </div>
        </div>
    `;
    
    // 添加样式
    const styleElement = document.createElement('style');
    styleElement.textContent = `
        .profile-header {
            padding: 15px 0;
            border-bottom: 1px solid #eee;
            margin-bottom: 20px;
        }
        .profile-header h2 {
            margin: 0;
            font-size: 24px;
            color: #333;
        }
        .profile-info {
            background: #fff;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
            margin-bottom: 20px;
            text-align: center;
        }
        .profile-info .user-name {
            font-size: 22px;
            font-weight: bold;
            margin-bottom: 15px;
        }
        .profile-info .user-stats {
            display: flex;
            justify-content: center;
            gap: 30px;
        }
        .profile-info .stat-item {
            text-align: center;
        }
        .profile-info .stat-value {
            font-size: 24px;
            font-weight: bold;
            color: #ff6b6b;
        }
        .profile-info .stat-label {
            color: #666;
            font-size: 14px;
        }
        .profile-actions {
            display: flex;
            justify-content: center;
            margin-bottom: 20px;
            gap: 10px;
        }
        .action-button {
            padding: 10px 20px;
            background: #f0f0f0;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            transition: all 0.2s;
        }
        .action-button:hover {
            background: #e0e0e0;
        }
        .action-button.active {
            background: #ff6b6b;
            color: white;
        }
        .records-container {
            background: #fff;
            border-radius: 5px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
            overflow: hidden;
        }
        .record-list {
            list-style: none;
            padding: 0;
            margin: 0;
        }
        .record-item {
            padding: 15px 20px;
            border-bottom: 1px solid #f0f0f0;
            transition: background-color 0.2s;
        }
        .record-item:last-child {
            border-bottom: none;
        }
        .record-item:hover {
            background-color: #f9f9f9;
        }
        .record-header {
            display: flex;
            justify-content: space-between;
            margin-bottom: 5px;
        }
        .record-target {
            font-weight: bold;
            font-size: 16px;
        }
        .record-date {
            color: #999;
            font-size: 14px;
        }
        .record-content {
            margin-top: 8px;
            color: #555;
            line-height: 1.5;
        }
        .record-meta {
            display: flex;
            align-items: center;
            gap: 10px;
            margin-top: 5px;
            color: #888;
            font-size: 14px;
        }
        .record-type {
            background: #f0f0f0;
            padding: 2px 8px;
            border-radius: 12px;
            font-size: 12px;
        }
        .loading {
            text-align: center;
            padding: 20px;
            color: #666;
        }
        .error-message {
            padding: 20px;
            background-color: #f8d7da;
            color: #721c24;
            border-radius: 5px;
            margin-bottom: 20px;
            text-align: center;
        }
        .empty-records {
            padding: 30px;
            text-align: center;
            color: #888;
        }
    `;
    document.head.appendChild(styleElement);
    
    // 获取页面元素
    const profileInfo = container.querySelector('#profile-info');
    const profileLoading = container.querySelector('#profile-loading');
    const recordsContainer = container.querySelector('#records-container');
    const recordsLoading = container.querySelector('#records-loading');
    const viewLikesBtn = container.querySelector('#view-likes');
    const viewCommentsBtn = container.querySelector('#view-comments');
    
    // 当前视图状态
    let currentView = 'likes';
    
    // 加载基本用户信息
    try {
        profileLoading.style.display = 'block';
        profileInfo.style.display = 'none';
        
        // 获取用户信息
        const userData = await api.getUserInfo();
        // 从返回的数组中获取长度
        const likesCount = userData.likes ? userData.likes.length : 0;
        const commentsCount = userData.comments ? userData.comments.length : 0;
        const userName = userData.username || localStorage.getItem('userName') || auth.getCurrentUser() || 'contrueCT';
        
        // 渲染用户信息
        profileInfo.innerHTML = `
            <div class="user-name">${userName}</div>
            <div class="user-stats">
                <div class="stat-item">
                    <div class="stat-value">${likesCount}</div>
                    <div class="stat-label">点赞</div>
                </div>
                <div class="stat-item">
                    <div class="stat-value">${commentsCount}</div>
                    <div class="stat-label">评论</div>
                </div>
            </div>
        `;
        
        profileLoading.style.display = 'none';
        profileInfo.style.display = 'block';
        
        // 默认加载点赞记录
        loadLikesRecords();
        
        // 设置按钮点击事件
        viewLikesBtn.addEventListener('click', function() {
            if (currentView !== 'likes') {
                currentView = 'likes';
                viewLikesBtn.classList.add('active');
                viewCommentsBtn.classList.remove('active');
                loadLikesRecords();
            }
        });
        
        viewCommentsBtn.addEventListener('click', function() {
            if (currentView !== 'comments') {
                currentView = 'comments';
                viewCommentsBtn.classList.add('active');
                viewLikesBtn.classList.remove('active');
                loadCommentsRecords();
            }
        });
        
    } catch (error) {
        console.error('加载用户信息失败:', error);
        profileLoading.style.display = 'none';
        profileInfo.innerHTML = `
            <div class="error-message">加载用户信息失败，请稍后重试</div>
        `;
        profileInfo.style.display = 'block';
    }    
    
    /**
     * 加载用户点赞记录
     */
    async function loadLikesRecords() {
        try {
            recordsContainer.innerHTML = '';
            recordsLoading.style.display = 'block';
            
            let likesArray;
            
            try {
                // 直接获取点赞数据数组
                likesArray = await api.getUserLikes();
                console.log('获取到的点赞数据:', likesArray);
            } catch (apiError) {
                console.error('API错误:', apiError);
                // 如果API调用失败，显示空记录
                recordsLoading.style.display = 'none';
                recordsContainer.innerHTML = `
                    <div class="empty-records">
                        <p>暂无点赞记录</p>
                        <p>去发现好吃的食物，点赞你最爱的美食吧！</p>
                    </div>
                `;
                return;
            }
            
            recordsLoading.style.display = 'none';
            
            // 检查是否有点赞数据
            if (!likesArray || !Array.isArray(likesArray) || likesArray.length === 0) {
                recordsContainer.innerHTML = `
                    <div class="empty-records">
                        <p>暂无点赞记录</p>
                        <p>去发现好吃的食物，点赞你最爱的美食吧！</p>
                    </div>
                `;
                return;
            }
            
            // 创建点赞记录列表
            const likesList = document.createElement('ul');
            likesList.className = 'record-list';
            
            likesArray.forEach(like => {
                const likeItem = document.createElement('li');
                likeItem.className = 'record-item';
                
                // 处理日期格式，适配后端返回的嵌套日期对象
                let formattedDate = '未知时间';
                if (like.createTime) {
                    const date = like.createTime.date;
                    const time = like.createTime.time;
                    if (date && time) {
                        formattedDate = `${date.year}-${String(date.month).padStart(2, '0')}-${String(date.day).padStart(2, '0')} ${String(time.hour).padStart(2, '0')}:${String(time.minute).padStart(2, '0')}`;
                    }
                }
                
                // 根据targetType显示不同的图标
                const typeIcon = like.targetType === 'Store' ? '🏪' : (like.targetType === 'comment' ? '💬' : '👍');
                const typeLabel = like.targetType === 'Store' ? '商铺' : (like.targetType === 'comment' ? '评论' : '其他');
                
                likeItem.innerHTML = `
                    <div class="record-header">
                        <span class="record-target">${typeIcon} ${like.targetName}</span>
                        <span class="record-date">${formattedDate}</span>
                    </div>
                    <div class="record-meta">
                        <span class="record-type">${typeLabel}</span>
                        <span>点赞了该${typeLabel}</span>
                    </div>
                `;
                
                likesList.appendChild(likeItem);
            });
            
            recordsContainer.innerHTML = '';
            recordsContainer.appendChild(likesList);
            
        } catch (error) {
            console.error('处理点赞记录失败:', error);
            recordsLoading.style.display = 'none';
            recordsContainer.innerHTML = `
                <div class="empty-records">
                    <p>暂无点赞记录</p>
                    <p>去发现好吃的食物，点赞你最爱的美食吧！</p>
                </div>
            `;
        }
    }  

    /**
     * 加载用户评论记录
     */
    async function loadCommentsRecords() {
        try {
            recordsContainer.innerHTML = '';
            recordsLoading.style.display = 'block';
            
            let commentsArray;
            
            try {
                // 直接获取评论数据数组
                commentsArray = await api.getUserComments();
                console.log('获取到的评论数据:', commentsArray);
            } catch (apiError) {
                console.error('API错误:', apiError);
                recordsLoading.style.display = 'none';
                recordsContainer.innerHTML = `
                    <div class="empty-records">
                        <p>暂无评论记录</p>
                        <p>去表达你的想法，分享你对美食的看法吧！</p>
                    </div>
                `;
                return;
            }
            
            recordsLoading.style.display = 'none';
            
            // 检查是否有评论数据
            if (!commentsArray || !Array.isArray(commentsArray) || commentsArray.length === 0) {
                recordsContainer.innerHTML = `
                    <div class="empty-records">
                        <p>暂无评论记录</p>
                        <p>去表达你的想法，分享你对美食的看法吧！</p>
                    </div>
                `;
                return;
            }
            
            // 创建评论记录列表
            const commentsList = document.createElement('ul');
            commentsList.className = 'record-list';
            
            commentsArray.forEach(comment => {
                const commentItem = document.createElement('li');
                commentItem.className = 'record-item';
                
                // 处理日期格式，适配后端返回的嵌套日期对象
                let formattedDate = '未知时间';
                if (comment.createTime) {
                    if (comment.createTime.date && comment.createTime.time) {
                        // 处理嵌套日期对象格式
                        const date = comment.createTime.date;
                        const time = comment.createTime.time;
                        formattedDate = `${date.year}-${String(date.month).padStart(2, '0')}-${String(date.day).padStart(2, '0')} ${String(time.hour).padStart(2, '0')}:${String(time.minute).padStart(2, '0')}`;
                    } else {
                        // 尝试标准日期格式
                        try {
                            const commentDate = new Date(comment.createTime);
                            formattedDate = formatDate(commentDate);
                        } catch (e) {
                            console.warn('日期格式化失败:', e);
                        }
                    }
                }
                
                commentItem.innerHTML = `
                    <div class="record-header">
                        <span class="record-target">🏪 ${comment.targetName}</span>
                        <span class="record-date">${formattedDate}</span>
                    </div>
                    <div class="record-content">${comment.content}</div>
                    <div class="record-meta">
                        <span class="record-type">评论</span>
                    </div>
                `;
                
                commentsList.appendChild(commentItem);
            });
            
            recordsContainer.innerHTML = '';
            recordsContainer.appendChild(commentsList);
            
        } catch (error) {
            console.error('加载评论记录失败:', error);
            recordsLoading.style.display = 'none';
            recordsContainer.innerHTML = `
                <div class="empty-records">
                    <p>暂无评论记录或加载失败</p>
                    <p>请稍后再试</p>
                </div>
            `;
        }
    }
    
    /**
     * 格式化日期
     * @param {Date} date - 日期对象
     * @returns {string} - 格式化后的日期字符串 
     */
    function formatDate(date) {
        if (!(date instanceof Date) || isNaN(date)) {
            return 'Unknown Date';
        }
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        
        return `${year}-${month}-${day} ${hours}:${minutes}`;
    }
}