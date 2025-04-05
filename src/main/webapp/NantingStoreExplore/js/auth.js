// auth.js - 认证相关功能

import api from './api.js';

/**
 * 用户注册
 * @param {string} username - 用户名
 * @param {string} password - 密码
 * @param {string} phone - 手机号码
 * @param {string} address - 地址
 * @param {string} email - 邮箱
 * @param {number} age - 年龄
 * @param {string} gender - 性别
 * @returns {Promise<object>} 注册结果
 */
async function registerUser(username, password, phone, address, email, age, gender) {
    try {
        // 创建与后端模型精确匹配的对象
        const userData = {
            username,
            password,
            phone,
            address,
            email,
            age,
            gender
        };
        
        const result = await api.registerUser(userData);
        return {
            success: true,
            userId: result.userId,
            message: '注册成功！'
        };
    } catch (error) {
        console.error('用户注册失败:', error);
        return {
            success: false,
            message: error.message || '注册失败，请稍后重试'
        };
    }
}

/**
 * 用户登录
 * @param {string} username - 用户名
 * @param {string} password - 密码
 * @returns {Promise<object>} 登录结果
 */
async function loginUser(username, password) {
    try {
        const credentials = { username, password };
        const result = await api.loginUser(credentials);
        
        // 存储令牌
        if (result.accessToken && result.refreshToken) {
            localStorage.setItem('accessToken', result.accessToken);
            localStorage.setItem('refreshToken', result.refreshToken);
            localStorage.setItem('userType', 'user');
            
            return {
                success: true,
                message: '登录成功！'
            };
        } else {
            throw new Error('登录响应缺少令牌');
        }
    } catch (error) {
        console.error('用户登录失败:', error);
        return {
            success: false,
            message: error.message || '登录失败，请检查用户名和密码'
        };
    }
}

/**
 * 商铺注册
 * @param {string} name - 商铺名称
 * @param {string} password - 密码
 * @param {string} address - 地址
 * @param {string} phone - 电话
 * @param {string} shortDescription - 简介
 * @param {string} description - 详细描述
 * @returns {Promise<object>} 注册结果
 */
async function registerStore(name, password, address, phone, shortDescription, description) {
    try {
        // 创建与后端模型精确匹配的对象
        const storeData = {
            name,
            password,
            address,
            phone,
            shortDescription,
            description
        };
        
        const result = await api.registerStore(storeData);
        return {
            success: true,
            message: '商铺注册成功！'
        };
    } catch (error) {
        console.error('商铺注册失败:', error);
        return {
            success: false,
            message: error.message || '注册失败，请稍后重试'
        };
    }
}

/**
 * 商铺登录
 * @param {string} storeName - 商铺名称
 * @param {string} password - 密码
 * @returns {Promise<object>} 登录结果
 */
async function loginStore(name, password) {
    try {
        const credentials = { name, password };
        const result = await api.loginStore(credentials);
        
        // 存储令牌
        if (result.accessToken && result.refreshToken) {
            localStorage.setItem('accessToken', result.accessToken);
            localStorage.setItem('refreshToken', result.refreshToken);
            localStorage.setItem('userType', 'store');
            
            return {
                success: true,
                message: '商铺登录成功！'
            };
        } else {
            throw new Error('登录响应缺少令牌');
        }
    } catch (error) {
        console.error('商铺登录失败:', error);
        return {
            success: false,
            message: error.message || '登录失败，请检查商铺名和密码'
        };
    }
}

/**
 * 检查用户是否已登录
 * @returns {boolean} 是否已登录
 */
function isLoggedIn() {
    return !!localStorage.getItem('accessToken');
}

/**
 * 获取用户类型
 * @returns {string|null} 用户类型 ('user' 或 'store')
 */
function getUserType() {
    return localStorage.getItem('userType');
}

/**
 * 退出登录
 */
function logout() {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('userType');
    window.location.hash = '#';
}

// 导出认证功能
export default {
    registerUser,
    loginUser,
    registerStore,
    loginStore,
    isLoggedIn,
    getUserType,
    logout
};