'use strict';

function _typeof(obj) { if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

var dps = new Vue({
  el: "#container",
  data: {
    dataStore: {
      pageIndex: 1,
      pageNum: 50
    },
    showDatalist: [],
    //显示数据
    amount_add: 1,
    //赠送方式--添加
    amount_amend: 1,
    //赠送方式--修改
    amountUp_add: "",
    //充值金额上限
    amountDown_add: "",
    //充值金额下限
    amountUp_amend: "",
    //充值金额上限
    amountDown_amend: "",
    //充值金额下限
    givingProportion_add: '0%',
    //赠送比例
    givingProportion_amend: '0%',
    //赠送比例
    givingCeiling_add: "",
    //赠送上限
    givingCeiling_amend: "",
    //赠送上限
    givingCredits_amend: ""
  },
  created: function created() {},
  mounted: function mounted() {
    var that = this;
    that.setDateTime();
    that.clickfn();
    
  },
  methods: {
    //select选中值
    getVal: function getVal(elem) {
      return elem.find("option:selected").val();
    },
    //提示框
    tipsContent: function tipsContent(str, times) {
      layui.use('layer', function () {
        var closeTiming = '';
        var layer = layui.layer;
        layer.msg(str, {
          time: times
        });
      });
    },
    //提示确认框
    checkConfirmFn: function checkConfirmFn(send) {
      var that = this;
      layui.use('layer', function () {
        var layer = layui.layer;
        layer.open({
          type: 1,
          title: '提示信息',
          content: "<div style='padding:15px;box-sizing: border-box;'>确定删除此信息?</div>",
          area: ['20%'],
          btn: ['取消', '确定'],
          yes: function yes(index, layero) {
            layer.close(index);
          },
          btn2: function btn2(index, layero) {
            base.sendRequest(send);
          },
          cancel: function cancel() {}
        });
      });
    },
    clickfn: function clickfn() {
      var that = this;
      var id = -1; //添加

      $('#add').on('click', function () {
        layui.use('layer', function () {
          var layer = layui.layer;
          layer.open({
            type: 1,
            title: '添加存款赠送活动',
            area: ['60%', '80%'],
            content: $('#dealApplication'),
            btn: ['关闭', '保存'],
            yes: function yes(index, layero) {
              layer.close(index);
            },
            btn2: function btn2() {
              var str = {};
              str.givingWay = parseInt(that.getVal($('#amount'))); //增送方式

              str.rechargeFq = parseInt($('#frequency>input[name="rechargeFq"]').val()); //存款频率

              str.info = $('#remarks>input[name="info"]').val(); //备注

              str.startTime = $('#startDate').val(); //开始时间

              str.endTime = $('#endDate').val(); //结束时间

              str.amountUp = that.amountUp_add;
              str.amountDown = that.amountDown_add;
              str.givingCeiling = that.givingCeiling_add;
              
              var checkdata = [str.givingWay, //增送方式
              str.rechargeFq, //存款频率
              str.info, //备注
              str.startTime, //开始时间
              str.amountUp,//充值上限
              str.amountDown,//充值下限

              str.endTime];
              if(str.givingWay==2){
                checkdata.push(str.givingCeiling);
              }

              if (that.amount_add == 1) {
                str.givingCredits = $('#presentAmount>input[name="givingCredits"]').val(); //赠送额度

                str.givingProportion = '';
              } else {
                str.givingCredits = '';
                str.givingProportion = that.givingProportion_add; //赠送比例
              }

              if (checkdata[1] == 0) {
                checkdata.splice(1, 1);
              }

              if (str.amountDown == null || str.amountDown == "" || str.amountUp == null || str.amountUp == "") {
                str.amountFlag = 0;
              } else {
                if (str.amountUp <= str.amountDown) {
                  str.amountFlag = 1;
                } else {
                  str.amountFlag = 0;
                }
              }

              if (!that.checkData(checkdata)) {
                if (isNaN(str.rechargeFq) || str.rechargeFq === "") {
                  that.tipsContent("存款频率不能为空", 1000);
                }else if (str.givingCredits <= 0 && that.amount_add == 1) {
                  that.tipsContent("赠送额度不能小于或等于0", 1000);
                } else if(str.givingWay==2&&str.givingCeiling==""){
                  that.tipsContent("赠送上限不能为空", 1000);
                } else if( str.amountDown == null || str.amountDown == ""){
                  that.tipsContent("充值下限不能为空", 1000);
                }else if( str.amountUp == null || str.amountUp == ""){
                  that.tipsContent("充值上限不能为空", 1000);
                } //								else if(str.givingCeiling == null || str.givingCeiling == "") {
                //									that.tipsContent("赠送上限不能为空", 1000);
                //								} 
                else if (str.startTime == null || str.startTime == "") {
                    that.tipsContent("开始时间不能为空", 1000);
                  } else if (str.endTime == null || str.endTime == "") {
                    that.tipsContent("结束时间不能为空", 1000);
                  } else if (str.info == null || str.info == "") {
                    that.tipsContent("备注不能为空", 1000);
                  }
                  //								else if(str.amountDown == null || str.amountDown == "") {
                //									that.tipsContent("充值金额下限不能为空", 1000);
                //								} else if(str.amountUp == null || str.amountUp == "") {
                //									that.tipsContent("充值金额上限不能为空", 1000);
                //								}

              } else {
                if ((str.givingCredits == null || str.givingCredits == "" || str.givingCredits == 0) && that.amount_add == 1) {
                  that.tipsContent("赠送额度不能为空或为0", 1000);
                } else if ((str.givingProportion === null || str.givingProportion === "") && that.amount_add == 2) {
                  that.tipsContent("赠送比例不能为空", 1000);
                } else if (str.rechargeFq < 0) {
                  that.tipsContent("存款频率不能小于0", 1000);
                } else if (str.givingCredits <= 0 && that.amount_add == 1) {
                  that.tipsContent("赠送额度不能小于或等于0", 1000);
                } else if (parseInt(str.givingProportion) > 100 && that.amount_add == 2) {
                  that.tipsContent("赠送比例不能大于100%", 1000);
                } //								else if(str.amountDown < 100) {
                //									that.tipsContent("充值金额下限不能小于100", 1000);
                //								} else if(str.amountUp < 100) {
                //									that.tipsContent("充值金额上限不能小于100", 1000);
                //								} 
                else if (str.amountFlag == 1) {
                    that.tipsContent("充值金额上限不能小于或等于充值金额下限", 1000);
                  } else if (str.startTime == str.endTime) {
                    that.tipsContent("开始时间与结束时间不能相同", 1000);
                  } else {
                    var save = {
                      type: "post",
                      data: str,
                      url: "/depositGivingStrategy/addDepositGivingStrategy",
                      success: function success(data) {
                        if (data.code == 200) {
                          that.load();
                          $('#frequency>input[name="rechargeFq"]').val(""); //存款频率

                          $('#presentAmount>input[name="givingCredits"]').val(''); //赠送额度

                          $('#remarks>input[name="info"]').val(''); //备注

                          $('#startDate').val(''); //开始时间

                          $('#endDate').val(''); //结束时间

                          layer.closeAll('page');
                          that.tipsContent('添加成功', 1500);
                        } else if (data.code = "300") {
                          that.tipsContent('添加失败', 1500);
                        }
                      },
                      error: function error(msg) {//console.log(msg);
                      }
                    };
                    base.sendRequest(save);
                  }
              }

              return false;
            }
          });
        });
      }); //弹出修改框

      $('#dataList').on('click', '.amend', function (e) {
        e.preventDefault();
        var str = {};
        str.strategyId = parseInt($(this).attr('data-index'));
        id = str.strategyId;
        var amendSet = {
          type: 'get',
          data: str,
          url: '/depositGivingStrategy/queryDepositGivingStrategyById',
          success: function success(data) {
            if (data.code == 200) {
              var _this = data.body;
              $('.amount').val(_this.givingWay); //增送方式

              that.amount_amend = _this.givingWay;
              $('.frequency>input[name="rechargeFq"]').val(_this.rechargeFq); //存款频率
              //							$('.presentAmount>input[name="givingCredits"]').val(_this.givingCredits); //赠送额度

              that.givingCredits_amend = _this.givingCredits;
              $('.remarks>input[name="info"]').val(_this.info); //备注

              that.amountUp_amend = _this.amountUp; //充值金额上限

              that.amountDown_amend = _this.amountDown; //充值金额下限

              that.givingProportion_amend = _this.givingProportion; //赠送比例

              that.givingCeiling_amend = _this.givingCeiling;
              $('.startDate').val(_this.startTime); //开始时间

              $('.endDate').val(_this.endTime); //结束时间

              layui.use('layer', function () {
                var layer = layui.layer;
                layer.open({
                  type: 1,
                  title: '修改存款赠送活动',
                  area: ['60%', '80%'],
                  content: $('#amendData'),
                  btn: ['关闭', '修改'],
                  yes: function yes(index, layero) {
                    layer.close(index);
                  },
                  btn2: function btn2(index, layero) {
                    var str = {};
                    str.id = id;
                    str.givingWay = parseInt(that.getVal($('.amount'))); //增送方式

                    str.rechargeFq = parseInt($('.frequency>input[name="rechargeFq"]').val()); //存款频率

                    str.info = $('.remarks>input[name="info"]').val(); //备注

                    str.startTime = $('.startDate').val(); //开始时间

                    str.endTime = $('.endDate').val(); //结束时间

                    str.amountUp = that.amountUp_amend;
                    str.amountDown = that.amountDown_amend;
                    str.givingCeiling = that.givingCeiling_amend;
                    var checkdata = [str.givingWay, //增送方式
                    str.rechargeFq, //存款频率
                    str.info, //备注
                    
                    str.amountUp,//充值上限
                    str.amountDown,//充值下限
                    str.startTime, //开始时间
                    str.endTime];

                    if(str.givingWay==2){
                      checkdata.push(str.givingCeiling);
                    }

                    if (that.amount_amend == 1) {
                      //										$('.presentAmount>input[name="givingCredits"]').val(_this.givingCredits);
                      str.givingCredits = that.givingCredits_amend; //赠送额度

                      str.givingProportion = '';
                    } else {
                      str.givingCredits = '';
                      str.givingProportion = that.givingProportion_amend; //赠送比例
                    }

                    if (checkdata[1] == 0) {
                      checkdata.splice(1, 1);
                    }

                    if (str.amountDown == null || str.amountDown == "" || str.amountUp == null || str.amountUp == "") {
                      str.amountFlag = 0;
                    } else {
                      if (str.amountUp <= str.amountDown) {
                        str.amountFlag = 1;
                      } else {
                        str.amountFlag = 0;
                      }
                    }

                    if (!that.checkData(checkdata)) {
                      if (isNaN(str.rechargeFq) || str.rechargeFq === "") {
                        that.tipsContent("存款频率不能为空", 1000);
                      }else if (str.givingCredits <= 0 && that.amount_add == 1) {
                        that.tipsContent("赠送额度不能小于或等于0", 1000);
                      } else if(str.givingWay==2&&str.givingCeiling==""){
                        that.tipsContent("赠送上限不能为空", 1000);
                      } else if( str.amountDown == null || str.amountDown == ""){
                        that.tipsContent("充值下限不能为空", 1000);
                      }else if( str.amountUp == null || str.amountUp == ""){
                        that.tipsContent("充值上限不能为空", 1000);
                      }  //											else if(str.givingCeiling == null || str.givingCeiling == "") {
                      //												that.tipsContent("赠送上限不能为空", 1000);
                      //											} 
                      else if (str.startTime == null || str.startTime == "") {
                          that.tipsContent("开始时间不能为空", 1000);
                        } else if (str.endTime == null || str.endTime == "") {
                          that.tipsContent("结束时间不能为空", 1000);
                        } else if (str.info == null || str.info == "") {
                          that.tipsContent("备注不能为空", 1000);
                        } //											else if(str.amountDown == null || str.amountDown == "") {
                      //												that.tipsContent("充值金额下限不能为空", 1000);
                      //											} else if(str.amountUp == null || str.amountUp == "") {
                      //												that.tipsContent("充值金额上限不能为空", 1000);
                      //											}

                    } else {
                      if ((str.givingCredits == null || str.givingCredits == "" || str.givingCredits == 0) && that.amount_amend == 1) {
                        that.tipsContent("赠送额度不能为空或为0", 1000);
                      } else if ((str.givingProportion === null || str.givingProportion === "") && that.amount_amend == 2) {
                        that.tipsContent("赠送比例不能为空", 1000);
                      } else if (str.rechargeFq < 0) {
                        that.tipsContent("存款频率不能小于0", 1000);
                      } else if (str.givingCredits <= 0 && that.amount_amend == 1) {
                        that.tipsContent("赠送额度不能小于或等于0", 1000);
                      } else if (parseInt(str.givingProportion) > 100 && that.amount_amend == 2) {
                        that.tipsContent("赠送比例不能大于100%", 1000);
                      } //											else if(str.amountDown < 100) {
                      //												that.tipsContent("充值金额下限不能小于100", 1000);
                      //											} else if(str.amountUp < 100) {
                      //												that.tipsContent("充值金额上限不能小于100", 1000);
                      //											}
                      else if (str.amountFlag == 1) {
                          that.tipsContent("充值金额上限不能小于或等于充值金额下限", 1000);
                        } else if (str.startTime == str.endTime) {
                          that.tipsContent("开始时间与结束时间不能相同", 1000);
                        } else {
                          var amend = {
                            type: "post",
                            data: str,
                            dataType: 'json',
                            url: "/depositGivingStrategy/updateDepositGivingStrategy",
                            success: function success(data) {
                              if (data.code == 200) {
                                layer.close(index);
                                console.log("后台返回数据");
                                console.log(data);
                                that.load();
                                that.tipsContent('修改成功', 500);
                              } else {
                                layer.close(index);
                                that.tipsContent('修改失败', 500);
                              }

                              $('.frequency>input[name="rechargeFq"]').val(""); //存款频率

                              $('.presentAmount>input[name="givingCredits"]').val(''); //赠送额度

                              $('.remarks>input[name="info"]').val(''); //备注

                              $('.startDate').val(''); //开始时间

                              $('.endDate').val(''); //结束时间
                            },
                            error: function error(msg) {
                              console.log(msg);
                            }
                          };
                          base.sendRequest(amend);
                        }
                    }

                    return false;
                  }
                });
              });
            } else if (data.code == 301) {
              alert('更新失败');
            }
          },
          error: function error(msg) {//console.log(msg);
          }
        };
        base.sendRequest(amendSet);
      }); //删除

      $('#dataList').on('click', '.delete', function (e) {
        e.preventDefault();
        var str = {};
        str.strategyId = parseInt($(this).attr('data-index'));
        var send = {
          type: 'post',
          data: str,
          dataType: 'json',
          url: '/depositGivingStrategy/deleteDepositGivingStrategy',
          success: function success(data) {
            if (data.code == 200) {
              that.tipsContent('修改成功', 1000);
              that.load();
            } else if (data.code == 302) {
              alert('删除失败', 1000);
            }
          },
          error: function error(msg) {//console.log(msg);
          }
        };
        that.checkConfirmFn(send);
      }); //启用禁用按钮设置

      $('#dataList').on('click', '.state', function () {
        var str = {};
        str.strategyId = parseInt($(this).attr('data-index'));
        str.state = parseInt($(this).find('.hide').attr('data-index'));
        $(this).find('span').toggleClass('hide');
        var changeState = {
          type: 'post',
          data: str,
          url: '/depositGivingStrategy/isStartDepositGivingStrategy',
          dataType: 'json',
          success: function success(data) {
            if (data.code == 200) {
              that.tipsContent('状态切换成功', 500);
              that.load();
            }
          }
        };
        layui.use('layer', function () {
          var layer = layui.layer;
          layer.open({
            type: 1,
            skin: 'confirm-class',
            title: '确认操作',
            area: ['20%'],
            content: '<div style="padding: 10px 20px;color: #e4393c;">' + (str.state == 1 ? '是否启用' : '是否停用') + '</div>',
            btn: ['取消', '确定'],
            yes: function yes() {
              layer.closeAll('page');
            },
            btn2: function btn2() {
              base.sendRequest(changeState);
            }
          });
        });
      }); //刷新按钮

      $('#refrech').on('click', function () {
        that.tipsContent("刷新中...", 1000);
        that.load();
      }); //页面显示数据条数

      $('#paging_record .pageNum').on('change', function () {
        $('#page').jqPaginator('option', {
          currentPage: 1
        });
        that.dataStore.pageIndex = 1;
        that.dataStore.pageNum = parseInt(that.getVal($(this)));
        that.load();
      });
    },
    //
    changeAmount: function changeAmount(index) {
      var that = this;

      if (index == 1) {
        //赠送方式--添加
        that.amount_add = parseInt(that.getVal($('#amount')));
      } else {
        //赠送方式--修改
        that.amount_amend = parseInt(that.getVal($('.amount')));
      }
    },
    //检查数据
    checkData: function checkData(data) {
      for (var i = 0; i < data.length; i++) {
        if (data[i] == '' || data[i] == null || _typeof(data[i]) == undefined) {
          return false;
        }
      }

      return true;
    },
    setDateTime: function setDateTime() {
      //日期设置
      laydate.render({
        elem: '#startDate',
        //指定元素
        format: 'yyyy-MM-dd HH:mm:ss',
        trigger: 'click',
        type: 'datetime',
        // position: 'fixed'
      });
      laydate.render({
        elem: '#endDate',
        //指定元素
        format: 'yyyy-MM-dd HH:mm:ss',
        trigger: 'click',
        type: 'datetime',
        position: 'fixed'
      });
      laydate.render({
        elem: '#amendData .startDate',
        trigger: 'click',
        //指定元素
        format: 'yyyy-MM-dd HH:mm:ss',
        type: 'datetime',
        position: 'fixed'
      });
      laydate.render({
        elem: '#amendData .endDate',
        trigger: 'click',
        //指定元素
        format: 'yyyy-MM-dd HH:mm:ss',
        type: 'datetime',
        position: 'fixed'
      });
      // $('#startDate').removeAttr('lay-key') 
    },
    //页面数据加载
    load: function load() {
      var that = this;
      var str = {
        "pageIndex": that.dataStore.pageIndex,
        "pageNum": that.dataStore.pageNum,
        "pageSize": 10
      }; //console.log(str);

      var loading = {
        type: "get",
        data: str,
        dataType: 'json',
        url: "/depositGivingStrategy/queryDepositGivingStrategyList",
        success: function success(data) {
          if (data.code == 200) {
            that.temp(data);
          }
        }
      };
      base.sendRequest(loading);
    },
    temp: function temp(data) {
      var html = "",
          that = this;

      if (data.code == 300) {
        $('#page').jqPaginator('option', {
          totalPages: 1,
          currentPage: 1
        });
        $('#paging_record .sumRecord').html(0);
        that.showDatalist = []; //				html += '<tr><td colspan="13"><span>\u6CA1\u6709\u627E\u5230\u5339\u914D\u7684\u8BB0\u5F55</span></td></tr>';
        //				$('#dataList').html(html);
      } else if (data.code == 200) {
        /**********/
        if (data.body.list.length == 0) {
          if (that.dataStore.pageIndex > 1) {
            that.dataStore.pageIndex = that.dataStore.pageIndex - 1;
            $('#page').jqPaginator('option', {
              currentPage: that.dataStore.pageIndex
            });
            that.load();
          } else {
            $('#page').jqPaginator('option', {
              totalPages: 1,
              currentPage: 1
            });
            $('#paging_record .sumRecord').html(0);
            that.showDatalist = []; //						html += '<td colspan="13"> <span>\u6CA1\u6709\u627E\u5230\u5339\u914D\u7684\u8BB0\u5F55</span> </td>';
            //						$('#dataList').html(html);
          }
        } else {
          $('#page').jqPaginator('option', {
            totalPages: data.body.pages
          });
          $('#paging_record .sumRecord').html(data.body.total);
          that.showDatalist = data.body.list; //					for(var i = 0; i < data.body.list.length; i++) {
          //						var obj = data.body.list[i];
          //						html += '<tr>  <td>' + (obj.givingWayName ? obj.givingWayName : "-") + '</td> <td>' + (obj.rechargeFq ? obj.rechargeFq : "-") + '</td> <td>' + (obj.givingCredits ? obj.givingCredits : "-") + '</td> <td>' + (obj.givingProportion ? obj.givingProportion : "-") + '</td> <td>' + (obj.GivingCeiling ? obj.GivingCeiling : "-") + '</td> <td><div class="layui-form"><div class="layui-form-item">    <div class="layui-input-block state" data-index="' + obj.id + '" style="margin-left: 0;"> <div data-index="1" class="layui-unselect layui-form-switch layui-form-onswitch ' + (obj.state == 1 ? 'able' : 'able hide') + '">     <em>\u542F\u7528</em>     <i></i> </div> <div  data-index="0" class="layui-unselect layui-form-switch ' + (obj.state == 0 ? 'disabled' : 'disabled hide') + '">     <em>\u505C\u7528</em>     <i></i> </div>    </div></div></div></td> <td>' + (obj.startTime ? obj.startTime : "-") + '</td> <td>' + (obj.endTime ? obj.endTime : "-") + '</td> <td>' + (obj.info ? obj.info : "-") + '</td> <td> <a href="#" class="amend layui-btn layui-btn-mini" data-index="' + obj.id + '">\u4FEE\u6539</a> <a href="#" class="delete layui-btn layui-btn-mini layui-btn-danger" data-index="' + obj.id + '">\u5220\u9664</a> </td> </tr>';
          //					}
          //					$('#dataList').html(html);
        }
        /*********/

      }
    },
    get: function get() {
      var str = {};
      str.rechargeType = parseInt(that.getVal($('.depositType'))); //存款类型

      str.givingWay = parseInt(that.getVal($('.amount'))); //增送方式

      str.givingType = parseInt(that.getVal($('.presentType'))); //赠送类型

      str.rechargeFq = parseInt($('.frequency>input[name="rechargeFq"]').val()); //存款频率

      str.givingCredits = parseInt($('.presentAmount>input[name="givingCredits"]').val()); //赠送额度

      str.givingProportion = parseInt($('.multiple>input[name="givingProportion"]').val()); //流水倍数

      str.info = $('.remarks>input[name="info"]').val(); //备注

      str.startTime = $('.startDate').val(); //开始时间

      str.endTime = $('.endDate').val(); //结束时间

      return str;
    }
  },
  watch: {
    givingProportion_add: function givingProportion_add() {
      var givingProportionNum = parseInt(this.givingProportion_add);

      if (givingProportionNum <= 0 || isNaN(givingProportionNum)) {
        this.givingProportion_add = 0;
      } else if (givingProportionNum > 100) {
        this.givingProportion_add = 100;
      }
    },
    givingProportion_amend: function givingProportion_amend() {
      var givingProportionNum = parseInt(this.givingProportion_amend);

      if (givingProportionNum <= 0 || isNaN(givingProportionNum)) {
        this.givingProportion_amend = 0;
      } else if (givingProportionNum > 100) {
        this.givingProportion_amend = 100;
      }
    },
    givingCeiling_add: function givingCeiling_add() {
      var givingCeiling_add = parseInt(this.givingCeiling_add);

      if (givingCeiling_add < 0) {
        this.givingCeiling_add = '';
      }
    },
    givingCeiling_amend: function givingCeiling_amend() {
      var givingCeiling_amend = parseInt(this.givingCeiling_amend);

      if (givingCeiling_amend < 0) {
        this.givingCeiling_amend = '';
      }
    },
    amountDown_add: function amountDown_add() {
      var amountDown_add = parseInt(this.amountDown_add);

      if (amountDown_add < 0) {
        this.amountDown_add = '';
      }
    },
    amountDown_amend: function amountDown_amend() {
      var amountDown_amend = parseInt(this.amountDown_amend);

      if (amountDown_amend < 0) {
        this.amountDown_amend = '';
      }
    },
    amountUp_add: function amountUp_add() {
      var amountUp_add = parseInt(this.amountUp_add);

      if (amountUp_add < 0) {
        this.amountUp_add = '';
      }
    },
    amountUp_amend: function amountUp_amend() {
      var amountUp_amend = parseInt(this.amountUp_amend);

      if (amountUp_amend < 0) {
        this.amountUp_amend = '';
      }
    },
    amount: function amount() {//console.log(this.amount);
    }
  }
});
/*****状态编写******/

$(function () {
  // 加载分页功能
  $.jqPaginator('#page', {
    totalPages: 1,
    //多少页数据
    visiblePages: 10,
    //最多显示几页
    currentPage: 1,
    //当前页
    wrapper: '<ul class="pagination"></ul>',
    first: '<li class="first"><a href="javascript:void(0);">首页</a></li>',
    prev: '<li class="prev"><a href="javascript:void(0);">上一页</a></li>',
    next: '<li class="next"><a href="javascript:void(0);">下一页</a></li>',
    last: '<li class="last"><a href="javascript:void(0);">尾页</a></li>',
    page: '<li class="page"><a href="javascript:void(0);">{{page}}</a></li>',
    onPageChange: function onPageChange(num) {
      dps.dataStore.pageIndex = num;
      dps.load();
    }
  });
});