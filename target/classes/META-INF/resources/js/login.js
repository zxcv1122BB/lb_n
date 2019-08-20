/**
 * Created by ASUS on 2017/9/12.
 */

//定义验证码值为假
let code_flag = false;
//定义帐号密码为假
let login_pass = false;
// 定义谷歌验证码变量这假
let check_code = false;

localStorage.clear();

$(function () {
    
    // setTimeout(function () {
    //     $('.beg-login-bg').css({'background':'url(images/bg-2.jpg) no-repeat center center fixed','background-size':'100%'});
        $('.beg-login-box').slideDown();
    // },4300);
    //onchange事件-检测用户是否开户谷歌认证
    // $('#userName').on('input propertychange', function () {
    $('#userName').change(function () {
        let username = $('#userName').val();
        let obj = {
            type: 'get',
            data: {userName: username},
            dataType: 'json',
            url: '/sysUser/getGoogleCodeStatus',
            success: function (data) {
                //console.log(data);
                if (data.code == 200) {
                    if (data.body.GOOGLECODE_STATE == 1) {
                        check_code = true;
                        $('.check_').show();
                    } else {
                        check_code = false;
                        $('.check_').hide();
                    }
                } else {
                    check_code = false;
                    $('.check_').hide();
                }
            },
            error: function (msg) {
                //console.log(msg);
            }
        };
        base.sendRequest_2(obj);
    });
    //按回车键触发登录
    $('body').keyup(function () {
        // if(code_flag==true){
        if (event.keyCode == '13') {
            $('#btn').click();
        }
        // }
    });
    //点击登录按钮执行
    $("#btn").click(function () {
        $('#prompt').hide();
//        debugger;
        let userName = $("#userName").val();
        let userPassword = $("#passWord").val();
        let code = $('#code').val();
        if (userName.trim() === '') {
            $("#userName").css('background-color', 'peachpuff');
            $('#userName').focus();
            $('#prompt').html('登录名不能为空！');
            $('#prompt').show();
            return;
        }
        if (userPassword.trim() === '') {
            $('#passWord').css('background-color', 'peachpuff');
            $('#passWord').focus();
            $('#prompt').html('密码不能为空！');
            $('#prompt').show();
            return;
        }
        if (code.trim() === '') {
            $('#code').css('background-color', 'peachpuff');
            $('#code').focus();
            $('#prompt').html('验证码不能为空！');
            $('#prompt').show();
            return;
        }
        if (check_code == true) {
            let check_ = $('#check_').val();
            if (check_.trim() === '') {
                $('#check_').css('background-color', 'peachpuff');
                $('#check_').focus();
                $('#prompt').html('谷歌验证码不能为空！');
                $('#prompt').show();
                return;
            }
        }
        $.getJSON(base.BASE_URL + "/keyPair?username=" + userName, function (data) {
            //将加密的模数,基数存储到localStorage里面
            localStorage.modulus = data.modulus;
            localStorage.exponent = data.exponent;
            logsubmit();
        });
    });
    // 登录名密码加密
    function logsubmit() {
        let userName = $("#userName").val();
        let userPassword = $("#passWord").val();
        let code = $("#code").val();
        let googlecode = $('#check_').val();
        let options = {
            type: "post",
            data: {
                'userName': userName,
                'userPassword': userPassword,
                'verifycode': code,
                //谷歌身份验证码
                'googleVerifycode': googlecode

            },
            url: '/manage/login',
            success: function (data) {
                $('#code').blur();
                if (data.code == 200 && code_flag == true) {
                    //login_pass = true;
                    localStorage.userName = userName;
                    localStorage.userChName = data.body.userChName;
                    localStorage.roleId = data.body.roleId;
                    localStorage.acessToken = data.body.acessToken;
                    window.location.href = base.BASE_URL + '/index.html';
                } else {
                    login_pass = false;
                    if (data.code == 202) {
                        $('#prompt').html(data.msg);
                        $('#prompt').show();
                    } else if (data.code == 201) {
                        $('#prompt').html(data.msg);
                        $('#prompt').show();
                    } else if (data.code == 203) {
                        code_flag = false;
                        $('#prompt').html(data.msg);
                        $('#prompt').show();
                    } else if (data.code == 204) {
                        code_flag = false;
                        $('#prompt').html(data.msg);
                        $('#prompt').show();
                    } else if (data.code == 205 || data.code == 206) {
                        $('.check_').show();
                        $('#prompt').html(data.msg);
                        $('#prompt').show();
                    } else if (data.code == 207) {  //密码输入5次锁住
                        $('#prompt').html(data.msg);
                        $('#prompt').show();
                    } else if (data.code == 600) {  //单点登录
                        code_flag = false;
                        alert(data.msg);
                    }else if(data.code==208){   //帐号被禁用
                        $('#prompt').html(data.msg);
                        $('#prompt').show();
                    }
                }
            },
            error: function (msg) {
                //console.log(msg);
            }

        };
        base.sendRequest(options);
    }


    //离开登录名框非空检验
    // $('#userName').blur(function () {
    //     if ($(this).val().trim() != '') {
    //         $(this).css('background-color', '#fff');
    //         $('#prompt').hide();
    //     } else {
    //         $(this).css('background-color', 'peachpuff');
    //     }
    // });
    // //离开密码框非空检验
    // $('#passWord').blur(function () {
    //     if ($(this).val().trim() != '') {
    //         $(this).css('background-color', '#fff');
    //         $('#prompt').hide();
    //     } else {
    //         $(this).css('background-color', 'peachpuff');
    //     }
    // });
    //点击重置
    $('#resetting').click(function () {
        $('#userName').val('');
        $('#passWord').val('');
        $('#code').val('');
    });
    let app = new Vue({
        el: "#setImg",
        data: {imgSrc: base.BASE_URL + '/code/image',},
        mounted: function () {

        },
        methods: {
            //点击更换验证码
            changeCode: function (event) {
                $(event.currentTarget).attr('src', base.BASE_URL + '/code/image?' + new Date().getTime());
            },
            //验证码失焦发送后台比对
            isReadCode: function (event) {
                let _this = event.currentTarget;
                if ($(_this).val() == '') {
                    $(_this).css('background-color', 'peachpuff');
                    $(_this).focus();
                    $('#prompt').html('验证码不能为空！');
                    $('#prompt').show();
                    return;
                } else {
                    $(_this).css('background-color', '#fff');
                    let code = $('#code').val();
                    let options = {
                        type: "post",
                        data: {'verifycode': code},
                        url: '/manage/checkVerifycode',
                        success: function (data) {
                            ////console.log(data);
                            if (data.code == 200) {
                                code_flag = true;
                                $('#prompt').html(data.msg);
                                //$('#btn').click();
                            } else {
                                code_flag = false;
                                $('#prompt').html(data.msg);
                                $('#verifycode').attr('src', base.BASE_URL + '/code/image?' + new Date().getTime());

                            }
                        },
                        error: function (msg) {
                            //console.log(msg);
                        }
                    };
                    base.sendRequest_2(options);
                }
            },
        }
    })
});