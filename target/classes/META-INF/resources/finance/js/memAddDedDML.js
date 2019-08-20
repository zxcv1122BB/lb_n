"use strict";
var addDedDML = new Vue({
    el: "#container",
    data: {
        prevUserName: ''
    },
    created: function () {
    },
    mounted: function () {
    },
    methods: {
        checkData: function checkData(data) {
            for (var i = 0; i < data.length; i++) {
                if (data[i] == "" || data[i] == null) {
                    return false;
                }
            }
            return true;
        },
        getVal: function getVal(elem) {
            return elem.find("option:selected").val();
        },
        //提示框
        tipsContent: function tipsContent(str) {
            layui.use('layer', function () {
                var closeTiming = '';
                var layer = layui.layer;
                layer.msg(str, {
                    time: 1000
                });
            });
        },
        //提示确认框
        checkConfirmFn: function checkConfirmFn(send) {
            var that = this;
            let userName = sessionStorage.getItem("userName");
            let operate = $("td.addDed>select").val() == 6 ? '增加' : '扣除';
            layui.use('layer', function () {
                var layer = layui.layer;
                layer.open({
                    type: 1,
                    title: '添加提示',
                    content: "<div style='padding:15px;box-sizing: border-box;'>确认" + operate + "会员" + userName + "打码量</div>",
                    area: ['22%'],
                    btn: ['取消', '确认'],
                    yes: function yes(index, layero) {
                        layer.close(index);
                    },
                    btn2: function btn2(index, layero) {
                        $('.layui-layer-btn1').css('pointer-events','none');
                        base.sendRequest(send);
                    },
                    cancel: function cancel() {
                    }
                });
            });
        },
        //查询
        btn_select_click: function () {
            var str = {
                    userName: $('.labelList input[name="userName"]').val().trim()
                }, that = this,
                checkStr = [str.userName];
            if (that.checkData(checkStr)) {

                if (that.prevUserName != str.userName) {
                    that.prevUserName == "" && (that.prevUserName = str.userName);
                    var sendSelect = {
                        type: 'get',
                        data: str,
                        dataType: 'json',
                        url: '/calculate/queryUserByUserName',
                        success: function success(data) {
                            that.prevData = [];
                            //console.log(data);
                            if (data.body == "" || data.body == null) {
                                that.tipsContent("找不到该会员");
                            } else {
                                $('.news_table .userName').html(data.body.userName);
                                // 存储会员名称
                                sessionStorage.setItem("userName", data.body.userName);
                                $('.news_table .remainMoney').html(data.body.coin ? data.body.coin : 0);
                                $('.news_table .dml').html(data.body.betsumNeed ? data.body.betsumNeed : 0);
                                $('.news_table .nowDML').html(data.body.betsum ? data.body.betsum : 0);
                                $('.news_table .num input[name="number"]').val("");
                                $('.news_table .result').val("");
                            }
                        },
                        error: function error() {
                        }
                    };
                    base.sendRequest(sendSelect);
                } else {
                    that.tipsContent('请不要重复查询');
                }
                ;
            } else {
                that.tipsContent("查询的会员账号不能为空");
            }
        },

        //重置
        handerReset: function () {
            this.prevUserName = "";
            $('.labelList input[name="userName"]').val("");
            $('.news_table .userName').html("");
            $('.news_table .remainMoney').html("");
            $('.news_table .num input[name="number"]').val("");
            $('.news_table .result').val("");
            $('.news_table .dml').html('0');
            $('.news_table .nowDML').html('0');
        },
        //添加确认按钮
        btn_confirm_click: function () {
            var str = {
                "userName": "",
                "betsumOperateType": 0,
                "betsum": 0,
                "info": ""
            }, that = this;
            str.userName = $('.news_table .userName').html();
            str.betsumOperateType = that.getVal($('.news_table .addDed'));
            str.betsum = parseFloat($('.news_table .num input[name="number"]').val());
            str.info = $('.news_table .result').val();
            var checkStr = [str.userName, str.betsumOperateType, str.betsum, str.info];
            if (that.checkData(checkStr)) {
                if (isNaN(checkStr[2])) {
                    that.tipsContent("操作金额只能为数字，请重新填写");
                } else if (checkStr[2] < 1) {
                    that.tipsContent("操作金额要大于1，请重新填写");
                } else if (checkStr[3].length > 50) {
                    that.tipsContent("操作原因过长，请重新输入(不大于50)");
                } else if (checkStr[2] > 100000000) {
                    that.tipsContent("操作金额不能大于100000000，请重新填写");
                } else {
                    var sendAdd = {
                        type: 'post',
                        data: str,
                        dataType: 'json',
                        url: '/calculate/addAndSubtractCode',
                        success: function success(data) {
                            that.prevUserName = "";
                            if (data.code == 200) {
                                that.tipsContent(data.msg);
                                $('.labelList input[name="userName"]').val("");
                                $('.news_table .userName').html("");
                                $('.news_table .remainMoney').html("");
                                $('.news_table .num input[name="number"]').val("");
                                $('.news_table .result').val("");
                                $('.dml').text("0");
                                $('.nowDML').text("0");
                            } else {
                                that.tipsContent(data.msg);
                            }
                        }
                    };
                    that.checkConfirmFn(sendAdd);
                }
            } else {
                if (checkStr[0] == null || checkStr[0] == '') {
                    that.tipsContent('会员账号为空，请先查询');
                } else if (checkStr[2] == null || checkStr[2] == '') {
                    that.tipsContent('操作数额不能为空,请填写');
                } else {
                    that.tipsContent('操作原因不能为空,请填写');
                }
            }
        },
    },
});
