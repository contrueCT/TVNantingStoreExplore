// api.js - API调用封装

const API_BASE_URL = '/NantingStoreExplore'; // 根据实际API地址修改

/**
 * 封装的API请求方法
 * @param {string} endpoint - API端点路径
 * @param {string} method - HTTP方法 (GET, POST, PUT, DELETE)
 * @param {object} data - 请求体数据
 * @param {boolean} requiresAuth - 是否需要认证
 * @returns {Promise<any>} 响应数据
 */
async function apiRequest(endpoint, method = 'GET', data = null, requiresAuth = false) {
    try {
        const url = `${API_BASE_URL}${endpoint}`;
        const headers = {
            'Content-Type': 'application/json'
        };
        
        // 如果需要认证，添加令牌
        if (requiresAuth) {
            const token = localStorage.getItem('accessToken');
            if (!token) {
                throw new Error('需要认证，但未找到令牌');
            }
            headers['Authorization'] = `Bearer ${token}`;
        }
        
        // 配置请求选项
        const options = {
            method,
            headers,
            credentials: 'include'
        };
        
        // 如果有数据，添加到请求体
        if (data && (method === 'POST' || method === 'PUT')) {
            options.body = JSON.stringify(data);
        }
        
        // 发送请求
        const response = await fetch(url, options);
        
        // 处理401错误（未授权），可能是令牌过期
        if (response.status === 401 && requiresAuth) {
            const refreshed = await refreshToken();
            if (refreshed) {
                // 重新获取新令牌并重试请求
                return apiRequest(endpoint, method, data, requiresAuth);
            } else {
                // 刷新令牌失败，可能需要重新登录
                window.location.hash = '#login';
                throw new Error('会话已过期，请重新登录');
            }
        }
        
        // 检查其他错误
        if (!response.ok) {
            const errorData = await response.json().catch(() => null);
            throw new Error(errorData?.message || `请求失败: ${response.status}`);
        }
        
        // 解析JSON响应 - 修改只在这里
        const result = await response.json();
        
        // 处理包装的响应格式
        if (result && typeof result.code === 'number') {
            if (result.code === 200) {
                // 返回data字段内容
                return result.data;
            } else {
                // 业务逻辑错误
                throw new Error(result.message || `业务处理失败，错误码: ${result.code}`);
            }
        }
        
        // 兜底返回原始结果
        return result;
        
    } catch (error) {
        console.error('API请求错误:', error);
        throw error;
    }
}

/**
 * 尝试刷新认证令牌
 * @returns {Promise<boolean>} 是否成功刷新令牌
 */
async function refreshToken() {
    try {
        const refreshTokenValue = localStorage.getItem('refreshToken');
        if (!refreshTokenValue) return false;
        
        console.log('准备刷新令牌，使用refreshToken:', refreshTokenValue.substring(0, 15) + '...');
        
        const response = await fetch(`${API_BASE_URL}/api/auth/refresh`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ refreshToken: refreshTokenValue })
        });
        
        if (!response.ok) {
            console.error('刷新令牌HTTP错误:', response.status);
            return false;
        }
        
        const result = await response.json();
        console.log('刷新令牌响应:', result);
        
        // 正确处理嵌套结构
        if (result && result.code === 200 && result.data) {
            // 保存新的token
            localStorage.setItem('accessToken', result.data.accessToken);
            localStorage.setItem('refreshToken', result.data.refreshToken);
            console.log('成功更新令牌');
            return true;
        } else {
            console.error('刷新令牌失败，无效的响应格式:', result);
            return false;
        }
    } catch (error) {
        console.error('刷新令牌异常:', error);
        return false;
    }
}

// 导出API请求方法
export default {
    // 认证相关
    registerUser: (userData) => apiRequest('/api/auth/register/user', 'PUT', userData),
    loginUser: (credentials) => apiRequest('/api/auth/login/user', 'POST', credentials),
    registerStore: (storeData) => apiRequest('/api/auth/register/store', 'PUT', storeData),
    loginStore: (credentials) => apiRequest('/api/auth/login/store', 'POST', credentials),
    refreshToken: () => apiRequest('/api/auth/refresh', 'POST', { refreshToken: localStorage.getItem('refreshToken') }),
    
    // 用户相关
    getUserInfo: () => apiRequest('/api/users/me', 'GET', null, true),
    getUserLikes: () => apiRequest('/api/users/me/likes', 'GET', null, true),
    getUserComments: () => apiRequest('/api/users/me/comments', 'GET', null, true),
    
    // 商铺相关
    getStores: (sort) => apiRequest(`/api/stores${sort ? `?sort=${sort}` : ''}`, 'GET'),
    getStoreDetails: (storeId) => apiRequest(`/api/stores/${storeId}`, 'GET', null, true),
    updateStore: (storeId, data) => apiRequest(`/api/stores/${storeId}/update`, 'POST', data, true),
    
    // 点赞相关
    likeStore: (storeId, data) => apiRequest(`/api/stores/${storeId}/like`, 'POST', data, true),
    unlikeStore: (storeId) => apiRequest(`/api/stores/${storeId}/like`, 'DELETE', null, true),
    likeComment: (commentId, storeId, data) => apiRequest(`/api/comments/${commentId}/like?storeId=${storeId}`, 'POST', data, true),
    unlikeComment: (commentId, storeId) => apiRequest(`/api/comments/${commentId}/like?storeId=${storeId}`, 'DELETE', null, true),    
    
    // 评论相关
    commentStore: (storeId, data) => apiRequest(`/api/comments?storeId=${storeId}`, 'POST', data, true),
    deleteComment: (commentId, storeId) => apiRequest(`/api/comments/${commentId}?storeId=${storeId}`, 'DELETE', null, true)
};