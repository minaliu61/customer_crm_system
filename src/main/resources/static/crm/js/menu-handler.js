/**
 * 菜单和用户界面处理脚本
 * 用于统一处理所有页面的菜单显示和退出登录功能
 */

$(function() {
    // 检查用户角色并显示对应菜单
    function showMenuByRole() {
        // 首先确保所有菜单项都可见，防止CSS冲突导致菜单隐藏
        $('.nav-item').show();
        $('.dropdown-menu').show();
        $('#logoutBtn').show();
        $('#logoutBtn').parent().show();
        
        // 获取当前页面URL路径以确定当前角色
        const pathname = window.location.pathname;
        
        if (pathname.includes('/admin/')) {
            // 管理员页面
            console.log("当前是管理员页面");
            $('.nav-admin').show();
            $('.nav-sales, .nav-customer, .nav-salesperson').hide();
            
            // 确保管理员菜单项有nav-admin类
            $('#navbarNav .nav-item').addClass('nav-admin');
            $('.sidebar .nav-item').addClass('nav-admin');
        } else if (pathname.includes('/sales/')) {
            // 销售人员页面 - sales目录
            console.log("当前是销售人员页面(sales)");
            $('.nav-sales').show();
            $('.nav-admin, .nav-customer, .nav-salesperson').hide();
            
            // 确保销售人员菜单项有nav-sales类
            $('#navbarNav .nav-item').addClass('nav-sales');
            $('.sidebar .nav-item').addClass('nav-sales');
        } else if (pathname.includes('/salesperson/')) {
            // 销售人员页面 - salesperson目录
            console.log("当前是销售人员页面(salesperson)");
            $('.nav-sales').show(); // 使用与sales相同的类，便于统一管理
            $('.nav-admin, .nav-customer').hide();
            
            // 确保销售人员菜单项有nav-sales类
            $('.sidebar .nav-item').addClass('nav-sales');
            $('.dropdown').closest('li').addClass('nav-sales');
        } else if (pathname.includes('/customer/')) {
            // 客户页面
            console.log("当前是客户页面");
            $('.nav-customer').show();
            $('.nav-admin, .nav-sales, .nav-salesperson').hide();
            
            // 确保客户菜单项有nav-customer类
            $('#navbarNav .nav-item').addClass('nav-customer');
            $('.sidebar .nav-item').addClass('nav-customer');
        }
        
        // 确保退出登录按钮始终可见
        if ($('#logoutBtn').length > 0) {
            console.log("找到退出登录按钮");
            $('#logoutBtn').closest('li.nav-item').show();
            $('#logoutBtn').closest('.dropdown-item').show();
        } else {
            console.error("未找到退出登录按钮");
        }
        
        // 针对Bootstrap 5版本的适配（主要用于salesperson目录）
        if ($('.dropdown-toggle[data-bs-toggle]').length > 0) {
            $('.dropdown-toggle').closest('div').show();
        }
    }
    
    // 统一处理退出登录
    function setupLogout() {
        $(document).off('click', '#logoutBtn').on('click', '#logoutBtn', function(e) {
            e.preventDefault();
            console.log("点击退出登录按钮");
            
            // 提示确认
            if (confirm('确定要退出登录吗？')) {
                if (typeof logout === 'function') {
                    logout();
                } else {
                    // 如果没有找到logout函数，直接重定向到登出页面
                    window.location.href = '/logout';
                }
            }
        });
    }
    
    // 高亮当前页面的菜单项
    function highlightCurrentMenuItem() {
        const pathname = window.location.pathname;
        $('a.nav-link').each(function() {
            const href = $(this).attr('href');
            if (href && pathname.endsWith(href)) {
                $(this).addClass('active');
                // 对于Bootstrap 5的页面，添加额外的高亮样式
                if ($(this).hasClass('text-white')) {
                    $(this).addClass('bg-primary bg-opacity-25');
                }
            } else {
                $(this).removeClass('active');
                // 移除额外的高亮样式
                if ($(this).hasClass('text-white')) {
                    $(this).removeClass('bg-primary bg-opacity-25');
                }
            }
        });
    }
    
    // 初始化
    function init() {
        console.log("初始化菜单处理...");
        showMenuByRole();
        setupLogout();
        highlightCurrentMenuItem();
        
        // 监听页面变化，重新初始化菜单
        $(window).on('popstate', function() {
            console.log("检测到页面变化，重新初始化菜单...");
            setTimeout(init, 100);
        });
    }
    
    // 监听重新初始化菜单事件
    $(document).on('reinitMenu', function() {
        console.log("接收到重新初始化菜单事件");
        init();
    });
    
    // 执行初始化
    init();
}); 