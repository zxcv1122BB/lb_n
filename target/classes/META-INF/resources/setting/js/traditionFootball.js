//文本框禁用空格
function keyUp() {
    if (event.keyCode == 32) {
        event.returnValue = false;
    }
}

new Vue({
    el: "#main",
    data: {
        tradData: [],       // 奖金信息数据
        banner_store: [],   // 期数存储数组
        playTypeNum: "",    // 记录玩法
        bannerNum: "",      // 记录期号
    },
    created(){
        this.getlayer();
        this.getBannerData(this.getBonusData);
    },
    methods: {
        //加载layer
        getlayer(){
            layui.use('layer', function () {
                let layer = layui.layer;
            })
        },
        // 获取奖金信息
        getBonusData: function () {
            let _this = this;
            _this.tradData = [];
            let playTypeId = this.playTypeNum;
            // let playTypeId = "14"
            let banner = $("#dateSelect").val();
            //console.log(banner);
            if (banner === "{{item}}") {
                //console.log(1111);
                banner = this.banner_store[0];
            }
            _this.bannerNum = banner;
            // banner = 17177
            let data = {
                playTypeId: playTypeId,
                banner: banner
            };
            let obj = {
                type: 'get',
                data: data,
                dataType: 'json',
                url: '/sysPrize/selectData',
                success: function (data) {
                    if (data.code == 200) {
                        //console.log(data);
                        if (data.body.length) {
                            //取到后台传递多来的Body下面的List
                            _this.tradData = data.body;
                            if (_this.playTypeNum == "") {
                                let resl = [];
                                let tempArr = ["14", "15", "16", "17"];
                                let resArr = [];
                                _this.tradData.map(item => {
                                    resArr.push(item.playTypeId)
                                });
                                //console.log("返回给我的数据中的id集合");
                                //console.log(resArr);
                                tempArr.map(item => {
                                    if (resArr.indexOf(+item) == -1) {
                                        //console.log("当前遍历的元素" + item);
                                        resl.push(item);
                                    }
                                });
                                //console.log("传递给筛选方法的id集合");
                                //console.log(resl);
                                _this.filterTableInfo(resl);
                            }
                        } else {
                            //console.log(playTypeId);
                            switch (playTypeId) {
                                case "14":
                                    _this.filterTableInfo(["14"]);
                                    break;
                                case "15":
                                    _this.filterTableInfo(["15"]);
                                    break;
                                case "16":
                                    _this.filterTableInfo(["16"]);
                                    break;
                                case "17":
                                    _this.filterTableInfo(["17"]);
                                    break;
                                default:
                                    _this.filterTableInfo(["14", "15", "16", "17"]);
                                    break;
                            }
                        }
                        //console.log(_this.tradData);
                    } else {
                        //console.log(playTypeId);
                        switch (playTypeId) {
                            case "14":
                                _this.filterTableInfo(["14"]);
                                break;
                            case "15":
                                _this.filterTableInfo(["15"]);
                                break;
                            case "16":
                                _this.filterTableInfo(["16"]);
                                break;
                            case "17":
                                _this.filterTableInfo(["17"]);
                                break;
                            default:
                                _this.filterTableInfo(["14", "15", "16", "17"]);
                                break;
                        }
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        },
        // 修改开奖信息
        changeData: function (obj) {
            let _this = this;
            for (let key in obj) {
                if (key != "playTypeId") {
                    if (obj[key] != "")
                        obj[key] = +obj[key];
                }
            }
            if (obj.playTypeId != "14") {
                delete obj.secondPrizeBonus;
                delete obj.secondPrizeNumber;
            }
            //console.log(obj);
            let reqObj = {
                type: 'get',
                data: obj,
                dataType: 'json',
                url: '/sysPrize/save',
                success: function (data) {
                    layer.msg("community");
                    //判断是否操作成功
                    if (data.code == 200) {
                        layer.msg("操作成功");
                        setTimeout(function () {
                            _this.getBonusData();
                        },1000)
                    } else {
                        layer.msg("操作失败");
                    }
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(reqObj);
        },
        //获取期数信息
        getBannerData: function (callback) {
            let _this = this;
            let data = {};
            let obj = {
                type: 'get',
                data: data,
                dataType: 'json',
                url: '/sysPrize/mod',
                success: function (data) {
                    if (data.code == 200) {
                        if (data.body.length != 0) {
                            _this.banner_store = data.body;
                            _this.bannerNum = _this.banner_store[0];
                        }
                    }
                    callback();
                },
                error: function (msg) {
                    //console.log(msg);
                }
            };
            base.sendRequest(obj);
        },
        //筛选页面显示表格信息
        filterTableInfo(arr) {
            let _this = this;
            //console.log(arr)
            let tempObj = {};
            arr.map(item => {
                tempObj = {};
                //console.log(item.id);
                if (item == '14') {
                    //console.log("item==14")
                    tempObj.name = "胜负彩";
                    tempObj.mod = _this.bannerNum;
                    tempObj.firstPrizeBonus = '';
                    tempObj.firstPrizeNumber = '';
                    tempObj.secondPrizeBonus = '';
                    tempObj.secondPrizeNumber = '';
                    tempObj.playTypeId = item;
                    tempObj.id = '';
                } else {
                    tempObj.mod = _this.bannerNum;
                    tempObj.firstPrizeBonus = '';
                    tempObj.firstPrizeNumber = '';
                    tempObj.secondPrizeBonus = '';
                    tempObj.secondPrizeNumber = '';
                    tempObj.playTypeId = item;
                    tempObj.id = '';
                    switch (item) {
                        case "15":
                            tempObj.name = "任选9场";
                            break;
                        case "16":
                            tempObj.name = "四场进球";
                            break;
                        case "17":
                            tempObj.name = "六场半全场";
                            break;
                        default:
                            break;
                    }
                }
                _this.tradData.push(tempObj)
            })
        }
    },
});