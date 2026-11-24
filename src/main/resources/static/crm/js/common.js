// 全局API地址前缀
const API_BASE_URL = '/crm/api';

// 通用请求函数
function request(url, method, data) {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: API_BASE_URL + url,
            method: method,
            data: method.toLowerCase() === 'get' ? data : JSON.stringify(data),
            contentType: method.toLowerCase() === 'get' ? 'application/x-www-form-urlencoded' : 'application/json',
            dataType: 'json',
            success: function(res) {
                if (res.code === 200) {
                    resolve(res);
                } else {

                    reject(res);
                }
            },
            error: function(err) {
                if (err.status === 401) {

                    setTimeout(() => {
                        window.location.href = '/crm/login.html';
                    }, 1500);
                } else {

                }
                reject(err);
            }
        });
    });
}

// GET请求
function get(url, params) {
    return request(url, 'GET', params);
}

// POST请求
function post(url, data) {
    return request(url, 'POST', data);
}

// PUT请求
function put(url, data) {
    return request(url, 'PUT', data);
}

// DELETE请求
function del(url, data) {
    return request(url, 'DELETE', data);
}

// 显示消息提示
// function showMessage(message, type = 'success') {
//     toastr.options = {
//         closeButton: true,
//         progressBar: true,
//         positionClass: 'toast-top-center',
//         timeOut: 3000
//     };
//
//     // if (type === 'success') {
//     //     toastr.success(message);
//     // } else if (type === 'error') {
//     //     toastr.error(message);
//     // } else if (type === 'warning') {
//     //     toastr.warning(message);
//     // } else if (type === 'info') {
//     //     toastr.info(message);
//     // }
// }

// // 显示成功提示
// function showSuccess(message) {
//     showMessage(message, 'success');
// }

// 显示错误提示
function showError(message) {
    showMessage(message, 'error');
}

// 显示警告提示
function showWarning(message) {
    showMessage(message, 'warning');
}

// 显示信息提示
function showInfo(message) {
    showMessage(message, 'info');
}

// 获取URL参数
function getUrlParam(name) {
    const reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)');
    const r = window.location.search.substr(1).match(reg);
    if (r != null) return decodeURIComponent(r[2]);
    return null;
}

// 日期格式化
function formatDate(date, fmt = 'yyyy-MM-dd') {
    if (!date) return '';
    if (typeof date === 'string') {
        date = new Date(date);
    }
    
    const o = {
        'M+': date.getMonth() + 1,
        'd+': date.getDate(),
        'h+': date.getHours(),
        'm+': date.getMinutes(),
        's+': date.getSeconds(),
        'q+': Math.floor((date.getMonth() + 3) / 3),
        'S': date.getMilliseconds()
    };
    
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));
    }
    
    for (let k in o) {
        if (new RegExp('(' + k + ')').test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)));
        }
    }
    
    return fmt;
}

// 检查登录状态
function checkLogin() {
    get('/user/current').then(res => {
        if (res.code === 200 && res.data) {
            // 已登录，获取用户信息
            const user = res.data;
            $('#currentUsername').text(user.realName || user.username);
            
            // 根据角色显示不同的菜单
            showMenuByRole(user.role);
        } else {
            // 未登录，跳转到登录页
            window.location.href = '/crm/login.html';
        }
    }).catch(() => {
        window.location.href = '/crm/login.html';
    });
}

// 根据角色显示不同的菜单
function showMenuByRole(role) {
    // 先确保所有菜单项都显示出来，防止CSS冲突导致菜单隐藏
    $('.nav-item').show();
    
    // 确保导航栏和退出登录按钮总是显示
    $('.navbar').show();
    $('.dropdown-menu').show();
    $('#logoutBtn').show();
    $('#logoutBtn').parent().show();
    
    // 针对特定角色的处理
    console.log("当前用户角色: " + role);
    
    // 处理菜单显示
    if (role === 1) {
        // 显示管理员菜单，隐藏其他角色菜单
        $('.nav-admin').show();
        $('.nav-sales, .nav-customer').hide();
        console.log("显示管理员菜单");
    } else if (role === 2) {
        // 显示销售人员菜单，隐藏其他角色菜单
        $('.nav-sales').show();
        $('.nav-admin, .nav-customer').hide();
        console.log("显示销售人员菜单");
    } else if (role === 3) {
        // 显示客户菜单，隐藏其他角色菜单
        $('.nav-customer').show();
        $('.nav-admin, .nav-sales').hide();
        console.log("显示客户菜单");
    }
    
    // 确保退出登录按钮始终可见
    if ($('#logoutBtn').length > 0) {
        console.log("找到退出登录按钮");
        $('#logoutBtn').closest('li.nav-item').show();
    } else {
        console.error("未找到退出登录按钮");
    }
}

// 退出登录
function logout() {
    console.log("执行退出登录");
    // 直接清除cookie和本地存储
    document.cookie.split(";").forEach(function(c) {
        document.cookie = c.replace(/^ +/, "").replace(/=.*/, "=;expires=" + new Date().toUTCString() + ";path=/");
    });
    localStorage.clear();
    sessionStorage.clear();
    
    // 调用后端退出接口
    get('/user/logout').then(() => {

        setTimeout(() => {
            window.location.href = '/crm/login.html';
        }, 1500);
    }).catch(() => {
        // 即使后端出错，也强制退出

        setTimeout(() => {
            window.location.href = '/crm/login.html';
        }, 1500);
    });
}

// 加载菜单处理脚本
function loadMenuHandler() {
    // 检查是否已经加载过脚本
    if (document.querySelector('script[src="/crm/js/menu-handler.js"]')) {
        console.log("菜单处理脚本已加载，重新初始化...");
        // 如果已加载，则直接触发一个自定义事件以重新初始化菜单
        $(document).trigger('reinitMenu');
        return;
    }
    
    console.log("加载菜单处理脚本...");
    const script = document.createElement('script');
    script.src = '/crm/js/menu-handler.js';
    script.async = true;
    script.onload = function() {
        console.log("菜单处理脚本加载完成");
    };
    document.body.appendChild(script);
}

// 页面加载完成时执行
$(function() {
    // 如果当前页面不是登录或注册页面，则检查登录状态
    if (window.location.pathname !== '/crm/login.html' && window.location.pathname !== '/crm/register.html') {
        checkLogin();
    }
    
    // 确保退出登录按钮事件绑定正确，使用事件委托确保动态加载的元素也能正确绑定
    $(document).off('click', '#logoutBtn').on('click', '#logoutBtn', function(e) {
        e.preventDefault();
        console.log("点击退出登录按钮");
        logout();
    });
    
    // 加载菜单处理脚本
    loadMenuHandler();
}); 