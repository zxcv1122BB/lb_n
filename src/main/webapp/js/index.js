/** index.js By Beginner Emain:zheng_jinfan@126.com HomePage:http://www.zhengjinfan.cn */

var tab;

layui.config({
    base: 'js/',
    version: new Date().getTime()
}).use(['element', 'layer', 'navbar', 'tab'], function () {
    var element = layui.element(),
        $ = layui.jquery,
        layer = layui.layer,
        navbar = layui.navbar();
    tab = layui.tab({
        elem: '.admin-nav-card' //设置选项卡容器
        ,
        //maxSetting: {
        //	max: 5,
        //	tipMsg: '只能开5个哇，不能再开了。真的。'
        //},
        contextMenu: true,
        onSwitch: function (data) {
            ////console.log(data.id); //当前Tab的Id
            ////console.log(data.index); //得到当前Tab的所在下标
            ////console.log(data.elem); //得到当前的Tab大容器

            ////console.log(tab.getCurrentTabId())
        },
        closeBefore: function (obj) { //tab 关闭之前触发的事件
            ////console.log(obj);
            //obj.title  -- 标题
            //obj.url    -- 链接地址
            //obj.id     -- id
            //obj.tabId  -- lay-id
            if (obj.title === 'BTable') {
                layer.confirm('确定要关闭' + obj.title + '吗?', {icon: 3, title: '系统提示'}, function (index) {
                    //因为confirm是非阻塞的，所以这里关闭当前tab需要调用一下deleteTab方法
                    tab.deleteTab(obj.tabId);
                    layer.close(index);
                });
                //返回true会直接关闭当前tab
                return false;
            } else if (obj.title === '表单') {
                layer.confirm('未保存的数据可能会丢失哦，确定要关闭吗?', {icon: 3, title: '系统提示'}, function (index) {
                    tab.deleteTab(obj.tabId);
                    layer.close(index);
                });
                return false;
            }
            return true;
        }
    });
    //iframe自适应
    $(window).on('resize', function () {
        var $content = $('.admin-nav-card .layui-tab-content');
        $content.height($(this).height() - 147);
        $content.find('iframe').each(function () {
            $(this).height($content.height());
        });
    }).resize();
    var data = {};
    var options = {
        type: 'get',
        url: '/sysMenu/getAuthoSysMenuList',
        data: data,
        async: false,
        dataType: 'json',
        success: function (data) {
            //alert(JSON.stringify(data.body));
            navs = util.getJsonTree(data.body, 2);
            //alert(JSON.stringify(navs));
        },
        error: function (msg) {
            //console.log(msg);
        }
    };
    base.sendRequest(options);

    //设置navbar
    navbar.set({
        spreadOne: true,
        elem: '#admin-navbar-side',
        cached: true,
        data: navs
        /*cached:true,
         url: 'datas/nav.json'*/
    });
    //渲染navbar
    navbar.render();
    //监听点击事件
    navbar.on('click(side)', function (data) {
        tab.tabAdd(data.field);
    });
    //清除缓存
    $('#clearCached').on('click', function () {
        navbar.cleanCached();
        layer.alert('清除完成!', {icon: 1, title: '系统提示'}, function () {
            location.reload();//刷新
        });
    });

    $('.admin-side-toggle').on('click', function () {
        var sideWidth = $('#admin-side').width();
        if (sideWidth === 200) {
            $('#admin-body').animate({
                left: '0'
            }); //admin-footer
            $('#admin-footer').animate({
                left: '0'
            });
            $('#admin-side').animate({
                width: '0'
            });
        } else {
            $('#admin-body').animate({
                left: '200px'
            });
            $('#admin-footer').animate({
                left: '200px'
            });
            $('#admin-side').animate({
                width: '200px'
            });
        }
    });
    $('.admin-side-full').on('click', function () {
        var docElm = document.documentElement;
        //W3C  
        if (docElm.requestFullscreen) {
            docElm.requestFullscreen();
        }
        //FireFox  
        else if (docElm.mozRequestFullScreen) {
            docElm.mozRequestFullScreen();
        }
        //Chrome等  
        else if (docElm.webkitRequestFullScreen) {
            docElm.webkitRequestFullScreen();
        }
        //IE11
        else if (elem.msRequestFullscreen) {
            elem.msRequestFullscreen();
        }
        layer.msg('按Esc即可退出全屏');
    });

    $('#setting').on('click', function () {
        tab.tabAdd({
            href: '/Manage/Account/Setting/',
            icon: 'fa-gear',
            title: '设置'
        });
    });

    //锁屏
    $(document).on('keydown', function () {
        var e = window.event;
        if (e.keyCode === 76 && e.altKey) {
            //alert("你按下了alt+l");
            lock($, layer);
        }
    });
    $('#lock').on('click', function () {
        lock($, layer);
    });

    //手机设备的简单适配
    var treeMobile = $('.site-tree-mobile'),
        shadeMobile = $('.site-mobile-shade');
    treeMobile.on('click', function () {
        $('body').addClass('site-mobile');
    });
    shadeMobile.on('click', function () {
        $('body').removeClass('site-mobile');
    });
});

var isShowLock = false;
function lock($, layer) {
    if (isShowLock)
        return;
    //自定页
    layer.open({
        title: false,
        type: 1,
        closeBtn: 0,
        anim: 6,
        content: $('#lock-temp').html(),
        shade: [0.9, '#393D49'],
        area: '40%',
        success: function (layero, lockIndex) {
            isShowLock = true;
            //给显示用户名赋值
            //layero.find('div#lockUserName').text('admin');
            //layero.find('input[name=username]').val('admin');
            layero.find('input[name=password]').on('focus', function () {
                var $this = $(this);
                if ($this.val() === '输入密码解锁..') {
                    $this.val('').attr('type', 'password');
                }
            })
                .on('blur', function () {
                    var $this = $(this);
                    if ($this.val() === '' || $this.length === 0) {
                        $this.attr('type', 'text').val('输入密码解锁..');
                    }
                });
            //在此处可以写一个请求到服务端删除相关身份认证，因为考虑到如果浏览器被强制刷新的时候，身份验证还存在的情况			
            //do something...
            //e.g. 

            $.getJSON('/Account/Logout', null, function (res) {
                if (!res.rel) {
                    layer.msg(res.msg);
                }
            }, 'json');

            //绑定解锁按钮的点击事件
            layero.find('button#unlock').on('click', function () {
                var $lockBox = $('div#lock-box');

                var userName = $lockBox.find('input[name=username]').val();
                var pwd = $lockBox.find('input[name=password]').val();
                if (pwd === '输入密码解锁..' || pwd.length === 0) {
                    layer.msg('请输入密码..', {
                        icon: 2,
                        time: 1000
                    });
                    return;
                }
                unlock(userName, pwd);
            });
            /**
             * 解锁操作方法
             * @param {String} 用户名
             * @param {String} 密码
             */
            var unlock = function (un, pwd) {
                //console.log(un, pwd);
                //这里可以使用ajax方法解锁
                $.post('/Account/UnLock', {userName: un, password: pwd}, function (res) {
                    //验证成功
                    if (res.rel) {
                        //关闭锁屏层
                        layer.close(lockIndex);
                        isShowLock = false;
                    } else {
                        layer.msg(res.msg, {icon: 2, time: 1000});
                    }
                }, 'json');
                //isShowLock = false;
                //演示：默认输入密码都算成功
                //关闭锁屏层
                //layer.close(lockIndex);
            };
        }
    });
};
//下面是点击会员管理页面的帐变按钮来添加会员帐变页面到主页tab
function test() {
    tab.tabAdd({
        href: 'finance/memAccCha.html',
        //icon: 'fa-gear',
        title: '会员帐变管理'
    });
}
//下面是点击会员管理页面的报表按钮来添加全局报表管理页面到主页tab
function associator_2() {
    tab.tabAdd({
        href: 'report/globalCount.html',
        //icon: 'fa-gear',
        title: '全局报表管理'
    });
}
//下面是点击会员管理页面的财务按钮来添加系统风险评估页面到主页tab
function associator_3() {
    tab.tabAdd({
        href: 'report/systemRiskEvaluation.html',
        //icon: 'fa-gear',
        title: '系统风险评估'
    });
}
//下面是点击会员管理页面的概况按钮来添加会员概况页面到主页tab
function associator_4() {
    tab.tabAdd({
        href: 'report/userDataSurvey.html',
        //icon: 'fa-gear',
        title: '会员数据概况'
    });
}



//下面是点击主页的在线按钮来添加在线用户页面到主页tab
function test1() {
    tab.tabAdd({
        href: 'associator/online_msg.html',
        //icon: 'fa-gear',
        title: '在线用户'
    })
}
//下面是点击主页的充值按钮来添加未处理会员充值页面到主页tab
function test2() {
    tab.tabAdd({
        href: 'finance/clientRecharge.html',
        //icon: 'fa-gear',
        title: '会员充值记录'
    })
}
//下面是点击主页的充值按钮来添加未处理会员充值页面到主页tab
function test3() {
    tab.tabAdd({
        href: 'finance/withdrawals.html',
        //icon: 'fa-gear',
        title: '会员提款记录'
    })
}
//下面是点击会员提款记录--处理申请--查看打码量 会把打码量变动记录页面添加到主页tab
function test4() {
    tab.tabAdd({
        href: 'finance/memDMLCha.html',
        title: '打码量变动记录'
    })
}



let app = new Vue({
    el: '#app',
    data: {
        online: 0,      //在线人数
        deposit: 0,     //充值未处理
        withdraw: 0,    //提款未处理
        jiange: 10000,      //间隔多久调一次上面的数据-默认为10秒

        time1: '',       //控制时间的变量
        ring_: 2,         //控制铃声类型 1:铃声  2：语音

        state_: '',        //登录认证
        secret_key: '',    //二维码密钥
        yanzhengma: '',    //绑定验证码输入框

        username: '',        //修改密码要显示的帐号
        oddPass: '',         //旧密码
        newPass: '',         //新密码
        newPass_: '',        //确认密码
        flag: false,         //控制旧密码是否正确的变量
    },
    created: function () {
        //检测是否有款未处理的信息
        // setInterval(function () {
        //this.check_msg();
        // }, 13000);
        //去后台调取在线人数、充值、提款的数据
        let _this = this;
        function search() {
            ////console.log(1);
            let obj = {
                type: 'post',
                data: {},
                dataType: 'json',
                url: '/getDepositAndWithdrawCount',
                success: function (data) {
                    ////console.log(data);
                    if (data.code == 200) {
                        _this.online = data.body.onlineCount;
                        _this.deposit = data.body.depositCount;
                        _this.withdraw = data.body.withdrawCount;
                    }else if(data.code==1001){
                        window.clearInterval(_this.time1);
                        window.location.href = base.BASE_URL+'/login.html';
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        }
        //初始化间隔几秒调一次接口
        this.time1 = setInterval(function () {
            search();
        }, parseInt(this.jiange));
    },
    methods: {
        //点击清除缓存
        clear_cache(){
            window.clearInterval(this.time1);
            localStorage.clear();
            layer.msg('缓存清除成功！');
            setTimeout(function () {
                location.href = base.BASE_URL + '/login.html';
            },500)
        },
        //有消息就播放音乐--未开启
        check_msg: function () {
            setInterval(function () {
                if (this.deposit != 0 || this.withdraw != 0) {
                    //music_1.play();
                }
            }, 1000)
        },
        //点击改变喇叭状态
        change_laba: function () {
            $('#laba').toggleClass('aa');
            if ($('#laba').hasClass('aa')) {
                $('#laba').attr('src', 'images/laba_close.png');
                music_1.muted = true;
                music_2.muted = true;
                music_3.muted = true;
            } else {
                $('#laba').attr('src', 'images/laba.png');
                music_1.muted = false;
                music_2.muted = false;
                music_3.muted = false;
            }
        },
        //点击打开在线列表，并添加到tab栏。
        href1: function () {
            test1();
        },
        //点击打开会员充值记录，并添加到tab栏
        href2: function () {
            localStorage.wcz = this.deposit;
            test2();
        },
        //点击打开会员提款记录，并添加到tab栏
        href3: function () {
            localStorage.wtk = this.withdraw;
            test3();
        },
        //点击设置按钮弹出设置二维码层
        set: function () {
            //检测是否开启登录认证
            let userName = localStorage.userName;
            let data = {
                userName: userName
            };
            let _this = this;
            _this.yanzhengma = '';
            ////console.log(data);
            let obj = {
                type: 'get',
                data: data,
                dataType: 'json',
                url: '/sysUser/getGoogleCodeStatus',
                success: function (data) {
                    ////console.log(data);
                    if (data.code == 200) {
                        _this.state_ = data.body.GOOGLECODE_STATE;
                        _this.secret_key = data.body.GOOGLECODE_SECRET;
                        // let pic_url = data.body.qrcode;
                        // let pic_urls = pic_url.split('=');
                        // //console.log(pic_urls[1]);
                        $('#qrcode').html('');
                        jQuery('#qrcode').qrcode({
                            render: "canvas", // 渲染方式有table方式和canvas方式
                            width: 150,   //默认宽度
                            height: 150, //默认高度
                            text: data.body.qrcode, //二维码内容
                            typeNumber: -1,   //计算模式一般默认为-1
                            correctLevel: 2, //二维码纠错级别
                            background: "#ffffff",  //背景颜色
                            foreground: "#000000"  //二维码颜色
                        })
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest_2(obj);
            if (this.state_ == 1) {        //如果开启则用原来的密钥和扫描码

            } else {                     //否则用新生成的密钥和扫描码
                let obj1 = {
                    type: 'get',
                    data: {},
                    dataType: 'json',
                    url: '/gooleVerifyCode',
                    success: function (data) {
                        ////console.log(data);
                        _this.secret_key = data.secret;
                        $('#qrcode').html('');
                        jQuery('#qrcode').qrcode({
                            render: "canvas", // 渲染方式有table方式和canvas方式
                            width: 150,   //默认宽度
                            height: 150, //默认高度
                            text: data.qrcode, //二维码内容
                            typeNumber: -1,   //计算模式一般默认为-1
                            correctLevel: 2, //二维码纠错级别
                            background: "#ffffff",  //背景颜色
                            foreground: "#000000"  //二维码颜色
                        })
                    },
                    error: function (msg) {
                        //console.log(msg);
                    }
                };
                base.sendRequest(obj1);
            }
            layer.open({
                title: '设置 Google Authenticator 验证',
                type: 1,
                content: $('.set'),
                area: ['37%'],
                // btn:['确定','关闭'],
                // yes:function (index) {
                //     if(_this.yanzhengma.trim()===''){
                //         layer.msg('验证码不能为空!');
                //         return;
                //     }
                //     if(_this.state_==0){
                //         var sta = 1;
                //     }else {
                //         var sta = 0;
                //     }
                //     var data = {
                //         userName:userName,
                //         googleState:sta,
                //         googleVerifycode:_this.yanzhengma,
                //         googleSecret:_this.secret_key
                //     };
                //     ////console.log(data);
                //     var obj = {
                //         type:'post',
                //         data:data,
                //         dataType:'json',
                //         url:'/sysUser/updateGoogleCodeStatus',
                //         success:function (data) {
                //             //console.log(data);
                //             if(data.code==200){
                //                 layer.close(index);
                //                 layer.msg(data.msg);
                //                 // setTimeout(function () {
                //                 //     window.location.reload();
                //                 // }, 1000);
                //             }else {
                //                 layer.msg(data.msg);
                //             }
                //         },
                //         error:function (msg) {
                //             //console.log(msg);
                //         }
                //     };
                //     base.sendRequest(obj);
                // },
                // btn2:function () {}
            })
        },
        //点击谷歌设置框关闭按钮
        close_set: function () {
            this.yanzhengma = '';
            layer.closeAll('page');
        },
        //点击谷歌设置框开启按钮
        open_set: function () {
            let userName = localStorage.userName;
            if (this.yanzhengma.trim() === '') {
                layer.msg('验证码不能为空!');
                return;
            }
            let data = {
                userName: userName,
                googleState: 1,
                googleVerifycode: this.yanzhengma,
                googleSecret: this.secret_key
            };
            //console.log(data);
            let obj = {
                type: 'post',
                data: data,
                dataType: 'json',
                url: '/sysUser/updateGoogleCodeStatus',
                success: function (data) {
                    //console.log(data);
                    if (data.code == 200) {
                        layer.closeAll('page');
                        layer.msg(data.msg);
                        // setTimeout(function () {
                        //     window.location.reload();
                        // }, 1000);
                    } else {
                        layer.msg(data.msg);
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        },
        //点击谷歌设置框关闭按钮
        shut_set: function () {
            let userName = localStorage.userName;
            if (this.yanzhengma.trim() === '') {
                layer.msg('验证码不能为空!');
                return;
            }
            let data = {
                userName: userName,
                googleState: 0,
                googleVerifycode: this.yanzhengma,
                googleSecret: this.secret_key
            };
            //console.log(data);
            let obj = {
                type: 'post',
                data: data,
                dataType: 'json',
                url: '/sysUser/updateGoogleCodeStatus',
                success: function (data) {
                    //console.log(data);
                    if (data.code == 200) {
                        layer.closeAll('page');
                        layer.msg(data.msg);
                        // setTimeout(function () {
                        //     window.location.reload();
                        // }, 1000);
                    } else {
                        layer.msg(data.msg);
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        },
        //点击注销执行
        quit: function () {
            let obj = {
                type: 'get',
                data: {},
                dataType: 'json',
                url: '/manage/loginout',
                success: function (data) {
                    ////console.log(data);
                    if (data.code == 200) {
                        window.clearInterval(this.time1);
                        layer.msg(data.msg);
                        localStorage.clear();
                        setTimeout(function () {
                            window.location.href = 'login.html';
                        }, 500);
                    } else {
                        layer.msg(data.msg);
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        },
        //检测旧密码是否正确
        check_oddpass: function () {
            let data = {
                userName: this.username,
                oldPassword: this.oddPass
            };
            let _this = this;
            let obj = {
                type: 'post',
                data: data,
                dataType: 'json',
                url: '/sysUser/isPassword',
                success: function (data) {
                    ////console.log(data);
                    if (data.code == 200) {
                        layer.msg(data.msg);
                        _this.flag = true;
                    } else {
                        layer.msg(data.msg);
                        _this.flag = false;
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        },
        //点击修改密码
        editPass: function () {
            let _this = this;
            this.oddPass = '';
            this.newPass = '';
            this.newPass_ = '';
            this.username = localStorage.userName;
            layer.open({
                title: '修改密码',
                type: 1,
                content: $('.popEditPass'),
                area: ['30%'],
                btn: ['确定', '取消'],
                yes: function (index) {
                    if (_this.oddPass.trim() === '') {
                        layer.msg('旧密码不能为空');
                        return;
                    }
                    if (_this.flag == false) {
                        layer.msg('旧密码错误');
                        return;
                    }
                    if (_this.newPass.trim().length < 6 || _this.newPass.trim().length > 12) {
                        layer.msg('请输入新密码6-12位');
                        return;
                    }
                    if (_this.newPass_ != _this.newPass) {
                        layer.msg('两次密码输入不一致');
                        return;
                    }
                    let data = {
                        userName: _this.username,
                        oldPassword: _this.oddPass,
                        password: _this.newPass
                    };
                    let obj = {
                        type: 'post',
                        data: data,
                        dataType: 'json',
                        url: '/sysUser/updatePassword',
                        success: function (data) {
                            ////console.log(data);
                            if (data.code == 200) {
                                layer.close(index);
                                layer.msg(data.msg);
                                _this.quit();
                            } else {
                                layer.msg(data.msg);
                            }
                        },
                        error: function (msg) {
                            //console.log(msg);
                        }
                    };
                    base.sendRequest(obj);
                },
                btn2: function () {
                }
            })
        }
    },
    watch: {
        //监听在线人线实时刷新页面(如果在线用户打开的情况下)
        online(){
            let lis = $('.layui-tab-title>li cite');
            for (let i = 0; i < lis.length;i++) {
                if (lis[i].innerHTML=='在线用户'){
                    //test1();
                }
            }
        },
        //监听充值未处理消息
        deposit: function () {
            ////console.log(music_2);
            music_2.load();
            music_2.play();
        },
        //监听提款未处理消息
        withdraw: function () {
            music_3.load();
            music_3.play();
        },
        //监听间隔多久去查一次在线人数、充值、提款的数据
        jiange: function () {
            window.clearInterval(this.time1);
            this.time1 = setInterval(function () {
                ////console.log(2);
                let _this = this;
                let obj = {
                    type: 'post',
                    data: {},
                    dataType: 'json',
                    url: '/getDepositAndWithdrawCount',
                    success: function (data) {
                        ////console.log(data);
                        if (data.code == 200) {
                            _this.online = data.body.onlineCount;
                            _this.deposit = data.body.depositCount;
                            _this.withdraw = data.body.withdrawCount;
                        }else if(data.code==1001){
                            window.clearInterval(_this.time1);
                        }
                    },
                    error: function (msg) {
                        //console.log(msg);
                    }
                };
                base.sendRequest(obj);
            }, parseInt(this.jiange));
        },
        //监听铃声类型  1铃声  2语音
        ring_: function () {
            if (this.ring_ == 1) {
                $('.ring_2').attr('src', 'audio/2.mp3');
                $('.ring_3').attr('src', 'audio/2.mp3');
            } else {
                $('.ring_2').attr('src', 'audio/in_money.wav');
                $('.ring_3').attr('src', 'audio/out_money.wav');
            }
        }
    }
});