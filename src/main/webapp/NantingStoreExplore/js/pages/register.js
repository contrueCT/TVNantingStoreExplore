// register.js - 注册页面组件

import auth from '../auth.js';

/**
 * 注册页面渲染函数
 * @param {HTMLElement} container - 页面容器元素
 */
export default function RegisterPage(container) {
    const isUserRegMode = localStorage.getItem('registerMode') !== 'store';
    
    // 创建注册页面HTML
    container.innerHTML = `
        <div class="page active">
            <h2>${isUserRegMode ? '用户注册' : '商铺注册'}</h2>
            
            <div class="register-tabs">
                <button id="user-register-tab" class="${isUserRegMode ? 'active' : ''}">用户注册</button>
                <button id="store-register-tab" class="${!isUserRegMode ? 'active' : ''}">商铺注册</button>
            </div>
            
            <form id="register-form" class="form">
                <!-- 用户注册字段 -->
                <div class="user-fields" ${!isUserRegMode ? 'style="display:none;"' : ''}>
                    <div class="form-group">
                        <label for="username">用户名</label>
                        <input type="text" id="username" name="username" placeholder="设置用户名" 
                            ${isUserRegMode ? 'required' : ''}>
                    </div>
                    
                    <div class="form-group">
                        <label for="password">密码</label>
                        <input type="password" id="password" name="password" placeholder="设置密码"
                            ${isUserRegMode ? 'required' : ''}>
                    </div>
                    
                    <div class="form-group">
                        <label for="phone">手机号码 *</label>
                        <input type="tel" id="phone" name="phone" placeholder="输入手机号码"
                            ${isUserRegMode ? 'required' : ''}>
                    </div>
                    
                    <div class="form-group">
                        <label for="email">邮箱</label>
                        <input type="email" id="email" name="email" placeholder="输入邮箱(选填)">
                    </div>
                    
                    <div class="form-group">
                        <label for="address">地址</label>
                        <input type="text" id="address" name="address" placeholder="输入地址(选填)">
                    </div>
                    
                    <div class="form-group">
                        <label>性别</label>
                        <div class="radio-group">
                            <label>
                                <input type="radio" name="gender" value="male"> 男
                            </label>
                            <label>
                                <input type="radio" name="gender" value="female"> 女
                            </label>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label for="age">年龄</label>
                        <input type="number" id="age" name="age" placeholder="输入年龄(选填)">
                    </div>
                </div>
                
                <!-- 商铺注册字段 -->
                <div class="store-fields" ${isUserRegMode ? 'style="display:none;"' : ''}>
                    <div class="form-group">
                        <label for="storeName">商铺名称</label>
                        <input type="text" id="storeName" name="storeName" placeholder="输入商铺名称"
                            ${!isUserRegMode ? 'required' : ''}>
                    </div>
                    
                    <div class="form-group">
                        <label for="storePassword">密码</label>
                        <input type="password" id="storePassword" name="storePassword" placeholder="设置密码"
                            ${!isUserRegMode ? 'required' : ''}>
                    </div>
                    
                    <div class="form-group">
                        <label for="storeAddress">商铺地址</label>
                        <input type="text" id="storeAddress" name="storeAddress" placeholder="输入商铺地址"
                            ${!isUserRegMode ? 'required' : ''}>
                    </div>
                    
                    <div class="form-group">
                        <label for="storePhone">联系电话</label>
                        <input type="tel" id="storePhone" name="storePhone" placeholder="输入联系电话"
                            ${!isUserRegMode ? 'required' : ''}>
                    </div>
                    
                    <div class="form-group">
                        <label for="storeIntro">商铺简介</label>
                        <input type="text" id="storeIntro" name="storeIntro" placeholder="简短介绍您的商铺"
                            ${!isUserRegMode ? 'required' : ''}>
                    </div>
                    
                    <div class="form-group">
                        <label for="storeDesc">详细描述</label>
                        <textarea id="storeDesc" name="storeDesc" rows="3" placeholder="详细描述您的商铺"></textarea>
                    </div>
                    
                    <div class="form-group">
                        <label for="roleType">商铺类型</label>
                        <select id="roleType" name="roleType" ${!isUserRegMode ? 'required' : ''}>
                            <option value="">请选择商铺类型</option>
                            <option value="restaurant">餐饮</option>
                            <option value="retail">零售</option>
                            <option value="service">服务</option>
                            <option value="other">其他</option>
                        </select>
                    </div>
                </div>
                
                <div class="form-actions">
                    <button type="submit" class="button">注册</button>
                </div>
                
                <div id="register-message" class="message"></div>
            </form>
            
            <div class="form-footer">
                <p>已有账号？<a href="#login" data-page="login">立即登录</a></p>
            </div>
        </div>
    `;
    
    // 添加动态切换表单和管理required属性的JS代码
    const userTab = container.querySelector('#user-register-tab');
    const storeTab = container.querySelector('#store-register-tab');
    const userFields = container.querySelector('.user-fields');
    const storeFields = container.querySelector('.store-fields');
    const registerForm = container.querySelector('#register-form');
    
    // 用户注册Tab点击事件
    userTab.addEventListener('click', () => {   
        userTab.classList.add('active');
        storeTab.classList.remove('active');
        
        // 显示用户注册表单
        userFields.style.display = '';
        storeFields.style.display = 'none';
        
        // 更新表单标题
        container.querySelector('h2').textContent = '用户注册';
        
        // 设置必填字段
        toggleRequiredFields(true);
        
        // 存储当前注册模式
        localStorage.setItem('registerMode', 'user');
    });
    
    // 商铺注册Tab点击事件
    storeTab.addEventListener('click', () => {
        storeTab.classList.add('active');
        userTab.classList.remove('active');
        
        // 显示商铺注册表单
        storeFields.style.display = '';
        userFields.style.display = 'none';
        
        // 更新表单标题
        container.querySelector('h2').textContent = '商铺注册';
        
        // 设置必填字段
        toggleRequiredFields(false);
        
        // 存储当前注册模式
        localStorage.setItem('registerMode', 'store');
    });
    
    // 切换必填字段函数
    function toggleRequiredFields(isUserMode) {
        // 用户表单必填字段
        const userRequiredFields = ['username', 'password', 'phone'];
        // 商铺表单必填字段
        const storeRequiredFields = ['storeName', 'storePassword', 'storeAddress', 'storePhone', 'storeIntro', 'roleType'];
        
        // 设置用户表单字段
        userRequiredFields.forEach(fieldId => {
            const field = registerForm.querySelector(`#${fieldId}`);
            if (field) {
                if (isUserMode) {
                    field.setAttribute('required', '');
                } else {
                    field.removeAttribute('required');
                }
            }
        });
        
        // 设置商铺表单字段
        storeRequiredFields.forEach(fieldId => {
            const field = registerForm.querySelector(`#${fieldId}`);
            if (field) {
                if (!isUserMode) {
                    field.setAttribute('required', '');
                } else {
                    field.removeAttribute('required');
                }
            }
        });
    }
    
    // 初始化必填字段
    toggleRequiredFields(isUserRegMode);
    
    // 处理注册表单提交逻辑
    const messageEl = container.querySelector('#register-message');

    registerForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        try {
            const isUserMode = localStorage.getItem('registerMode') !== 'store';
            let result;
            
            if (isUserMode) {
                // 用户注册
                const username = registerForm.querySelector('#username').value.trim();
                const password = registerForm.querySelector('#password').value;
                const phone = registerForm.querySelector('#phone').value.trim();
                const address = registerForm.querySelector('#address').value.trim();
                const email = registerForm.querySelector('#email').value.trim();
                const age = registerForm.querySelector('#age').value ? parseInt(registerForm.querySelector('#age').value) : 0;
                
                // 获取性别
                let gender = '';
                const genderRadios = registerForm.querySelectorAll('input[name="gender"]');
                for (const radio of genderRadios) {
                    if (radio.checked) {
                        gender = radio.value;
                        break;
                    }
                }
                
                // 表单验证
                if (!username || !password || !phone) {
                    displayMessage('请填写必要的注册信息', 'error');
                    return;
                }
                
                result = await auth.registerUser(username, password, phone, address, email, age, gender);
            } else {
                // 商铺注册
                const name = registerForm.querySelector('#storeName').value.trim();
                const password = registerForm.querySelector('#storePassword').value;
                const address = registerForm.querySelector('#storeAddress').value.trim();
                const phone = registerForm.querySelector('#storePhone').value.trim();
                const shortDescription = registerForm.querySelector('#storeIntro').value.trim();
                const description = registerForm.querySelector('#storeDesc').value.trim();
                
                // 表单验证
                if (!name || !password || !address || !phone || !shortDescription) {
                    displayMessage('请填写完整的商铺信息', 'error');
                    return;
                }
                
                result = await auth.registerStore(
                    name, password, address, phone, shortDescription, description
                );
            }
            
            if (result && result.success) {
                displayMessage(result.message, 'success');
                // 注册成功后，延迟跳转到登录页面
                setTimeout(() => {
                    window.location.hash = '#login';
                }, 1500);
            } else {
                displayMessage(result ? result.message : '注册失败', 'error');
            }
        } catch (error) {
            console.error('注册过程中发生错误:', error);
            displayMessage(error.message || '注册时发生错误，请稍后重试', 'error');
        }
    });

    function displayMessage(message, type) {
        if (messageEl) {
            messageEl.textContent = message;
            messageEl.className = `message ${type}`;
            messageEl.style.display = 'block';
        }
    }
}