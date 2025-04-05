// stores.js - 商铺列表页面

import api from '../api.js';
import { checkAuth } from '../app.js';

/**
 * 商铺列表页面渲染函数
 * @param {HTMLElement} container - 页面容器元素
 */
export default async function StoresPage(container) {
    // 创建页面基础结构
    container.innerHTML = `
        <div class="page active">
            <h2>商铺列表</h2>
            
            <div class="stores-filter">
                <div class="sort-controls">
                    <span>排序方式：</span>
                    <button id="sort-default" class="sort-btn active">默认</button>
                    <button id="sort-likes" class="sort-btn">点赞数</button>
                    <button id="sort-comments" class="sort-btn">评论数</button>
                </div>
            </div>
            
            <div id="stores-loading" class="loading">加载中...</div>
            
            <div id="stores-container" class="stores-grid">
                <!-- 商铺列表将在这里动态生成 -->
            </div>
            
            <div id="stores-message" class="message"></div>
        </div>
    `;
    
    // 添加样式
    const styleElement = document.createElement('style');
    styleElement.textContent = `
        .stores-filter {
            margin-bottom: 20px;
            padding: 10px 0;
            border-bottom: 1px solid #eee;
        }
        .sort-controls {
            display: flex;
            align-items: center;
            gap: 10px;
        }
        .sort-btn {
            background: #f0f0f0;
            border: none;
            padding: 5px 10px;
            border-radius: 4px;
            cursor: pointer;
            transition: all 0.2s;
        }
        .sort-btn.active {
            background: #007bff;
            color: white;
        }
        .loading {
            text-align: center;
            padding: 20px;
            color: #666;
        }
        .stores-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 20px;
        }
        .store-card {
            border: 1px solid #eee;
            border-radius: 5px;
            padding: 15px;
            transition: transform 0.2s, box-shadow 0.2s;
            background-color: #fff;
            cursor: pointer;
        }
        .store-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }
        .store-card h3 {
            margin-top: 0;
            margin-bottom: 10px;
            color: #333;
        }
        .store-card p {
            color: #666;
            margin-bottom: 10px;
        }
        .store-card .store-meta {
            display: flex;
            justify-content: space-between;
            color: #888;
            font-size: 0.9em;
            margin-top: 15px;
        }
        .store-card .store-address {
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }
        .message {
            text-align: center;
            padding: 20px;
            display: none;
        }
        .message.error {
            display: block;
            color: #721c24;
        }
    `;
    document.head.appendChild(styleElement);
    
    // 获取页面元素
    const storesContainer = container.querySelector('#stores-container');
    const loadingElement = container.querySelector('#stores-loading');
    const messageElement = container.querySelector('#stores-message');
    const sortDefaultBtn = container.querySelector('#sort-default');
    const sortLikesBtn = container.querySelector('#sort-likes');
    const sortCommentsBtn = container.querySelector('#sort-comments');
    
    // 设置排序按钮事件
    sortDefaultBtn.addEventListener('click', () => loadStores());
    sortLikesBtn.addEventListener('click', () => loadStores('likes'));
    sortCommentsBtn.addEventListener('click', () => loadStores('comments'));
    
    // 设置排序按钮激活状态
    function setActiveSortButton(sortType) {
        sortDefaultBtn.classList.toggle('active', !sortType);
        sortLikesBtn.classList.toggle('active', sortType === 'likes');
        sortCommentsBtn.classList.toggle('active', sortType === 'comments');
    }
    
    /**
     * 加载商铺列表
     * @param {string} sortType - 排序方式（默认、likes、comments）
     */
    async function loadStores(sortType = null) {
        try {
            // 更新排序按钮状态
            setActiveSortButton(sortType);
            
            // 显示加载中
            loadingElement.style.display = 'block';
            storesContainer.innerHTML = '';
            messageElement.style.display = 'none';
            
            // 调用API获取商铺列表
            const stores = await api.getStores(sortType);
            console.log('获取到的商铺数据:', stores); // 调试日志
            
            // 隐藏加载中
            loadingElement.style.display = 'none';
            
            // 修改这里：检查stores是否为数组
            if (!stores || !Array.isArray(stores) || stores.length === 0) {
                messageElement.textContent = '暂无商铺数据';
                messageElement.style.display = 'block';
                messageElement.className = 'message error';
                return;
            }
            
            // 渲染商铺列表 - 直接遍历stores数组
            stores.forEach(store => {
                const storeCard = document.createElement('div');
                storeCard.className = 'store-card';
                storeCard.innerHTML = `
                    <h3>${store.name}</h3>
                    <p class="store-address">${store.address}</p>
                    <p>${store.shortDescription || '暂无简介'}</p>
                    <div class="store-meta">
                        <span>👍 ${store.likes ? store.likes.length : 0}</span>
                        <span>💬 ${store.comments ? store.comments.length : 0}</span>
                    </div>
                `;
                
                // 点击商铺卡片，跳转到详情页
                storeCard.addEventListener('click', () => {
                    window.location.hash = `#store/${store.id}`;
                });
                
                storesContainer.appendChild(storeCard);
            });
            
        } catch (error) {
            console.error('加载商铺列表失败:', error);
            loadingElement.style.display = 'none';
            messageElement.textContent = '加载商铺列表失败，请稍后重试';
            messageElement.style.display = 'block';
            messageElement.className = 'message error';
        }
    }
    
    // 初始加载商铺列表
    loadStores();
    
    // 检查登录状态，更新导航栏
    checkAuth();
}