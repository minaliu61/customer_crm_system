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
function showMessage(message, type = 'success') {
    toastr.options = {
        closeButton: true,
        progressBar: true,
        positionClass: 'toast-top-center',
        timeOut: 3000
    };
    
    if (type === 'success') {
        toastr.success(message);
    } else if (type === 'error') {
        toastr.error(message);
    } else if (type === 'warning') {
        toastr.warning(message);
    } else if (type === 'info') {
        toastr.info(message);
    }
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
    $('.nav-item').hide();
    
    // 1-管理员，2-销售人员，3-客户
    if (role === 1) {
        // 管理员菜单
        $('.nav-admin').show();
    } else if (role === 2) {
        // 销售人员菜单
        $('.nav-sales').show();
    } else if (role === 3) {
        // 客户菜单
        $('.nav-customer').show();
    }
}

// 退出登录
function logout() {
    get('/user/logout').then(() => {

        setTimeout(() => {
            window.location.href = '/crm/login.html';
        }, 1500);
    });
}

// 页面加载完成时执行
$(function() {
    // 如果当前页面不是登录或注册页面，则检查登录状态
    if (window.location.pathname !== '/crm/login.html' && window.location.pathname !== '/crm/register.html') {
        checkLogin();
    }
    
    // 退出登录按钮事件
    $('#logoutBtn').click(function() {
        logout();
    });
}); 