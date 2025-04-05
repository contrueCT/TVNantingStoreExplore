// home.js - 首页组件

/**
 * 首页渲染函数
 * @param {HTMLElement} container - 页面容器元素
 */
export default function HomePage(container) {
    container.innerHTML = `
        <div class="page active">
            <h2>欢迎使用商铺点评系统</h2>
            
            <div class="home-banner">
                <p>发现城市中的美味与服务</p>
            </div>
            
            <div class="features-section">
                <div class="feature">
                    <h3>浏览商铺</h3>
                    <p>探索附近的商铺，查看评分和评论</p>
                    <a href="#stores" class="button">立即浏览</a>
                </div>
                
                <div class="feature">
                    <h3>分享体验</h3>
                    <p>点赞喜欢的商铺，分享您的真实体验</p>
                    <a href="#login" class="button">立即参与</a>
                </div>
                
                <div class="feature">
                    <h3>商铺入驻</h3>
                    <p>拥有一家商铺？立即注册并展示给用户</p>
                    <a href="#register" class="button">注册商铺</a>
                </div>
            </div>
            
            <div class="home-about">
                <h3>关于我们</h3>
                <p>商铺点评系统致力于帮助用户发现优质商铺，同时为商家提供展示平台。</p>
            </div>
        </div>
    `;
    
    // 添加内联样式
    const styleElement = document.createElement('style');
    styleElement.textContent = `
        .home-banner {
            background-color: #f8f9fa;
            padding: 40px 20px;
            text-align: center;
            border-radius: 5px;
            margin-bottom: 30px;
        }
        .home-banner p {
            font-size: 1.2em;
            color: #555;
        }
        .features-section {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }
        .feature {
            background: white;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            text-align: center;
        }
        .feature h3 {
            margin-bottom: 10px;
        }
        .feature p {
            margin-bottom: 15px;
            color: #666;
        }
        .home-about {
            background: white;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .home-about h3 {
            margin-bottom: 10px;
        }
    `;
    document.head.appendChild(styleElement);
    
    console.log('首页加载成功');
}