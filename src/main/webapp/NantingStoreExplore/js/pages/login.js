// login.js - 登录页面组件

import auth from '../auth.js';

/**
 * 登录页面渲染函数
 * @param {HTMLElement} container - 页面容器元素
 */
export default function LoginPage(container) {
    const isUserLoginMode = localStorage.getItem('loginMode') !== 'store';
    
    // 创建登录页面HTML
    container.innerHTML = `
        <div class="page active">
            <h2>${isUserLoginMode ? '用户登录' : '商铺登录'}</h2>
            
            <div class="login-tabs">
                <button id="user-login-tab" class="${isUserLoginMode ? 'active' : ''}">用户登录</button>
                <button id="store-login-tab" class="${!isUserLoginMode ? 'active' : ''}">商铺登录</button>
            </div>
            
            <form id="login-form" class="form">
                <div class="form-group">
                    <label for="username">${isUserLoginMode ? '用户名' : '商铺名'}</label>
                    <input type="text" id="username" name="username" required placeholder="${isUserLoginMode ? '输入用户名' : '输入商铺名'}">
                </div>
                
                <div class="form-group">
                    <label for="password">密码</label>
                    <input type="password" id="password" name="password" required placeholder="输入密码">
                </div>
                
                <div class="form-actions">
                    <button type="submit" class="button">登录</button>
                </div>
                
                <div id="login-message" class="message"></div>
            </form>
            
            <div class="form-footer">
                <p>还没有账号？<a href="#register" data-page="register">立即注册</a></p>
            </div>
        </div>
    `;
    
    // 添加样式
    const styleElement = document.createElement('style');
    styleElement.textContent = `
        .login-tabs {
            display: flex;
            margin-bottom: 20px;
        }
        .login-tabs button {
            flex: 1;
            padding: 10px;
            background: #f0f0f0;
            border: none;
            cursor: pointer;
        }
        .login-tabs button.active {
            background: #007bff;
            color: white;
        }
        .form {
            margin-bottom: 20px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        .form-group label {
            display: block;
            margin-bottom: 5px;
        }
        .form-group input {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        .form-actions {
            margin-top: 20px;
        }
        .message {
            margin-top: 15px;
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
        .form-footer {
            margin-top: 20px;
            text-align: center;
        }
    `;
    document.head.appendChild(styleElement);
    
    // 切换用户/商铺登录模式
    const userTab = container.querySelector('#user-login-tab');
    const storeTab = container.querySelector('#store-login-tab');
    
    userTab.addEventListener('click', () => {
        userTab.classList.add('active');
        storeTab.classList.remove('active');
        localStorage.setItem('loginMode', 'user');
        container.querySelector('h2').textContent = '用户登录';
        container.querySelector('label[for="username"]').textContent = '用户名';
        container.querySelector('#username').placeholder = '输入用户名';
    });
    
    storeTab.addEventListener('click', () => {
        storeTab.classList.add('active');
        userTab.classList.remove('active');
        localStorage.setItem('loginMode', 'store');
        container.querySelector('h2').textContent = '商铺登录';
        container.querySelector('label[for="username"]').textContent = '商铺名';
        container.querySelector('#username').placeholder = '输入商铺名';
    });
    
    // 处理登录表单提交
    const loginForm = container.querySelector('#login-form');
    const messageEl = container.querySelector('#login-message');
    
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        // 获取表单数据
        const username = loginForm.username.value.trim();
        const password = loginForm.password.value;
        
        // 表单验证
        if (!username || !password) {
            displayMessage('请填写完整的登录信息', 'error');
            return;
        }
        
        try {
            // 根据当前模式调用不同的登录方法
            const isUserMode = localStorage.getItem('loginMode') !== 'store';
            const result = isUserMode 
                ? await auth.loginUser(username, password)
                : await auth.loginStore(username, password);
            
            if (result.success) {
                displayMessage(result.message, 'success');
                // 登录成功后跳转到首页或上一页面
                setTimeout(() => {
                    window.location.hash = isUserMode ? '#profile' : '#stores';
                }, 1000);
            } else {
                displayMessage(result.message, 'error');
            }
        } catch (error) {
            displayMessage(error.message || '登录时发生错误，请稍后重试', 'error');
        }
    });
    
    /**
     * 显示消息
     * @param {string} message - 消息内容
     * @param {string} type - 消息类型 (success/error)
     */
    function displayMessage(message, type) {
        messageEl.textContent = message;
        messageEl.className = `message ${type}`;
    }
}