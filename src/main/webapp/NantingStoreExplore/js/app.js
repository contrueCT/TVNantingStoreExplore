// app.js - 应用主文件

// 路由配置
const routes = {
    '': 'home',
    '#': 'home',
    '#home': 'home',
    '#login': 'login',
    '#register': 'register',
    '#profile': 'UserProfile',
    '#stores': 'stores',
    '#store': 'storeDetail'
};

// 页面内容容器
const appContainer = document.getElementById('app');

// 检查用户是否已登录
function checkAuth() {
    const token = localStorage.getItem('accessToken');
    const authLinks = document.querySelector('.auth-links');
    const userLinks = document.querySelector('.user-links');
    
    if (token) {
        authLinks.classList.remove('active');
        userLinks.classList.add('active');
        return true;
    } else {
        authLinks.classList.add('active');
        userLinks.classList.remove('active');
        return false;
    }
}

// 路由处理函数
function handleRoute() {
    const hash = window.location.hash || '#';
    const routeName = routes[hash.split('/')[0]] || 'notFound';
    
    // 记录路由参数
    let params = {};
    if (hash.includes('/')) {
        const parts = hash.split('/');
        if (parts.length > 1) {
            params.id = parts[1];
        }
    }
    
    // 检查是否需要认证
    const isLoggedIn = checkAuth();
    const authRequiredPages = ['profile'];
    
    if (authRequiredPages.includes(routeName) && !isLoggedIn) {
        // 如果需要登录但未登录，重定向到登录页面
        window.location.hash = '#login';
        return;
    }
    
    // 动态加载对应页面模块
    loadPage(routeName, params);
}

// 动态加载页面
async function loadPage(pageName, params = {}) {
    try {
        // 清除当前内容
        appContainer.innerHTML = '<div class="loading">加载中...</div>';
        
        // 根据页面名称动态加载对应模块
        try {
            const pageModule = await import(`./pages/${pageName}.js`);
            if (pageModule && pageModule.default) {
                appContainer.innerHTML = '';
                pageModule.default(appContainer, params);
            } else {
                appContainer.innerHTML = '<div class="error">页面加载失败</div>';
            }
        } catch (err) {
            console.error('页面加载错误:', err);
            appContainer.innerHTML = `
                <div class="page active">
                    <h2>页面未找到</h2>
                    <p>抱歉，您请求的页面不存在或正在开发中。</p>
                    <a href="#" class="button">返回首页</a>
                </div>
            `;
        }
    } catch (error) {
        console.error('路由错误:', error);
        appContainer.innerHTML = '<div class="error">发生错误，请稍后再试</div>';
    }
}

// 初始化应用
function initApp() {
    // 检查认证状态
    checkAuth();
    
    // 设置路由变化监听
    window.addEventListener('hashchange', handleRoute);
    
    // 设置登出处理
    document.getElementById('logout').addEventListener('click', (e) => {
        e.preventDefault();
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        window.location.hash = '#';
        checkAuth();
    });
    
    // 处理初始路由
    handleRoute();
}

// 启动应用
document.addEventListener('DOMContentLoaded', initApp);

// 导出用于其他模块使用的工具函数
export { checkAuth };