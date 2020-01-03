import React, { PureComponent } from 'react';
import { connect } from 'dva';
import router from 'umi/router';
import {
  Card,
  Table,
  Divider,
  Tag,
  Row,
  Col,
  Button,
  Input,
  Icon,
  message,
  Modal,
  DatePicker,
} from 'antd';
import { cpus } from 'os';
import moment from 'moment';

import echarts from 'echarts/lib/echarts';
import 'echarts/lib/chart/line';
import 'echarts/lib/component/tooltip';
import 'echarts/lib/component/title';
import 'echarts/lib/component/legend';
import 'echarts/lib/component/grid';

const electricOpenImg = require('./static/electricOpen.png');
const electricCloseImg = require('./static/electricClose.png');
const switchGreenOpen = require('./static/switchGreenOpen.png');
const switchGreenClose = require('./static/switchGreenClose.png');
const switchRedOpen = require('./static/switchRedOpen.png');
const switchRedClose = require('./static/switchRedClose.png');

const { confirm } = Modal;
const { RangePicker } = DatePicker;

const dateFormat = 'YYYY-MM-DD';
const date = new Date();
const earlyDate = new Date(date - 86400000 * 3);

const seperator1 = '-';

const year = date.getFullYear();
const earlyYear = earlyDate.getFullYear();

let month = date.getMonth() + 1;
let earlyMonth = earlyDate.getMonth() + 1;

let strDate = date.getDate();
let earlyStrDate = earlyDate.getDate();

if (month >= 1 && month <= 9) {
  month = '0' + month;
}
if (strDate >= 0 && strDate <= 9) {
  strDate = '0' + strDate;
}
if (earlyMonth >= 1 && earlyMonth <= 9) {
  earlyMonth = '0' + earlyMonth;
}
if (earlyStrDate >= 0 && earlyStrDate <= 9) {
  earlyStrDate = '0' + earlyStrDate;
}
const today = year + seperator1 + month + seperator1 + strDate;
const earlyToday = earlyYear + seperator1 + earlyMonth + seperator1 + earlyStrDate;

@connect(({ electricInfo }) => ({
  electricInfo,
}))
class homeIndex extends PureComponent {
  constructor(props) {
    super(props);
    //初始化页面数据
    var date = moment().format('YYYYMMDD');

    this.state = {
      switchTotal: 352, //开关总数
      electrifyTotal: 22, //通电开关数
      powerFailure: 330, //断电开关数
      switchRecord: [
        //开关记录
        {
          name: '一号开关',
          detail: '开启',
          time: '14：30',
        },
        {
          name: '一号开关',
          detail: '开启',
          time: '14：30',
        },
        {
          name: '一号开关',
          detail: '开启',
          time: '14：30',
        },
        {
          name: '一号开关',
          detail: '开启',
          time: '14：30',
        },
        {
          name: '一号开关',
          detail: '开启',
          time: '14：30',
        },
        {
          name: '一号开关',
          detail: '开启',
          time: '14：30',
        },
      ],
      emergency: [
        //告警
        {
          name: '一号开关',
          detail: '断电',
          time: '14：30',
        },
        {
          name: '一号开关',
          detail: '断电',
          time: '14：30',
        },
      ],
      electricList: [
        {
          electricBox: '一号开关',
          electricStatus: true,
          electricList: [
            {
              id: 1,
              switchName: '一号开关',
              switchVoltage: [111, 222, 333], //电压
              switchCurrent: [111, 222, 333], //电流
              switchStatus: true,
            },
            {
              id: 2,
              switchName: '二号开关',
              switchVoltage: [111, 222, 333],
              switchCurrent: [111, 222, 333],
              switchStatus: false,
            },
            {
              id: 3,
              switchName: '三号开关',
              switchVoltage: [111, 222, 333],
              switchCurrent: [111, 222, 333],
              switchStatus: true,
            },
          ],
        },
      ],
      detailMdal: false,
      detailName: '',
      detailId: '',
      detailStart: earlyToday,
      detailEnd: today,
      tableList: [],
      currentPage: 1,
    };
  }

  componentWillMount() {
    this.getElectricList();
  }

  getElectricList() {
    const { dispatch } = this.props;
    const params = {};
    dispatch({
      type: 'electricInfo/getElectricList',
      payload: params,
    }).then(response => {
      const { electricInfo } = this.props;
      console.log(electricInfo);
      const {
        switchTotal,
        electrifyTotal,
        powerFailure,
        switchRecord,
        emergency,
        electricList,
      } = electricInfo;

      this.setState({
        ...this.state,
        switchTotal,
        electrifyTotal,
        powerFailure,
        switchRecord,
        emergency,
        electricList,
      });
    });
  }

  //值改变回调方法
  onSwitchChange(type, id) {
    const { onChange } = this.props;
    if (type == 'close') {
      confirm({
        title: '确定关闭开关吗?',
        onOk: () => this.onSwitchChangeConfirm(type, id),
        onCancel() {
          // this.onChange(checked,false);
        },
      });
    } else {
      confirm({
        title: '确定开启开关吗?',
        onOk: () => this.onSwitchChangeConfirm(type, id),
        onCancel() {
          // this.onChange(checked,false);
        },
      });
    }
  }

  onSwitchChangeConfirm = (type, id) => {
    const { dispatch } = this.props;

    const params = {
      id: id,
      switch: type,
    };
    dispatch({
      type: 'electricInfo/switchFn',
      payload: params,
    }).then(response => {
      const { electricInfo } = this.props;
      const { success } = electricInfo;
      if (success) {
        //为true则为成功
        message.success('更改成功');
        this.getElectricList();
      } else {
        message.error('更改失败');
      }
    });
  };

  showDetailModal(item) {
    this.setState({
      ...this.state,
      detailMdal: true,
      detailName: item.switchName,
      detailId: item.id,
    });
    this.getDetailData();
  }

  changeDetailDate(date, dateString) {
    this.setState({
      ...this.state,
      detailStart: dateString[0],
      detailEnd: dateString[1],
    });
  }

  detailClose() {
    this.setState({
      ...this.state,
      detailMdal: false,
      detailName: '',
      detailStart: earlyToday,
      detailEnd: today,
    });
  }

  getDetailData() {
    const { dispatch } = this.props;
    const { detailStart, detailEnd, detailId, currentPage } = this.state;
    const limit = 10;
    const params = {
      start: detailStart,
      end: detailEnd,
      id: detailId,
      limit,
      offset: limit * (currentPage - 1),
    };
    dispatch({
      type: 'electricInfo/getDetailData',
      payload: params,
    }).then(response => {
      const { electricInfo } = this.props;
      const { chartsList, chartsLegend, tableList, detailTotal } = electricInfo;
      this.renderCharts(chartsList, chartsLegend);
      this.setState({
        ...this.state,
        tableList,
        detailTotal,
      });
    });
  }

  renderCharts(renderData, renderLegend) {
    let parent = document.getElementById('chartsParentDiv');
    parent.innerHTML = '';
    let divCharts = 'chartsDiv';
    let chartsD = document.getElementById(divCharts);
    let div = document.createElement('div');
    div.setAttribute('id', divCharts);
    div.style.height = '200px';
    parent.appendChild(div);

    let lineDate = [];
    let lineLegend = renderLegend;
    let lineList = [];
    if (renderLegend) {
      for (let j = 0; j < renderLegend.length; j++) {
        let seriesJson = {
          name: renderLegend[i],
          type: 'line',
          data: [],
        };
        lineList.push(seriesJson);
      }
    }

    // 图表数据接口返回例子：
    // {
    //   legend:['一号开关','二号开关','三号开关'],    //对应Y轴得每条数据得名称
    //   list:[
    //     {
    //       date:'20191230',
    //       series:[
    //         {
    //           name:'一号开关',
    //           data:'210'
    //         },
    //         {
    //           name:'二号开关',
    //           data:'240'
    //         },
    //         {
    //           name:'三号开关',
    //           data:'190'
    //         }
    //       ]
    //     },
    //     {
    //       date:'20191231',
    //       series:[
    //         {
    //           name:'一号开关',
    //           data:'210'
    //         },
    //         {
    //           name:'二号开关',
    //           data:'240'
    //         },
    //         {
    //           name:'三号开关',
    //           data:'190'
    //         }
    //       ]
    //     }
    //   ]
    // }

    if (renderData) {
      for (let i = 0; i < renderData.length; i++) {
        lineDate.push(renderData[i].date);
        for (let k = 0; k < renderData[i].series.length; k++) {
          for (let l = 0; l < lineList.length; l++) {
            if (lineList[l].name == renderData[i].series[k].name) {
              lineList[l].data.push(renderData[i].series[k].data);
            }
          }
        }
      }
    }

    let myCharts = echarts.init(document.getElementById(divCharts));
    let option = {
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'cross',
          crossStyle: {
            color: '#999',
          },
        },
      },
      legend: {
        data: lineLegend,
        textStyle: {
          fontSize: 14,
          color: '#000',
        },
        show: true,
      },
      xAxis: {
        type: 'category',
        boundaryGap: false,
        data: lineDate,
      },
      yAxis: {
        type: 'value',
      },
      series: lineList,
    };
    myCharts.setOption(option);
  }

  changePage({ current }) {
    this.setState(
      {
        ...this.state,
        currentPage: current,
      },
      () => {
        this.getDetailData();
      }
    );
  }
  render() {
    //从状态存储器里取出值
    const {
      electricList,
      switchTotal,
      electrifyTotal,
      powerFailure,
      switchRecord,
      emergency,
      detailMdal,
      detailName,
      tableList,
      detailTotal,
      currentPage,
    } = this.state;

    const infoCol = {
      background: '#fff',
      borderTop: '1px solid #e8e8e8',
      borderBottom: '1px solid #e8e8e8',
      borderLeft: '1px solid #e8e8e8',
      padding: '8px',
      height: '153px',
    };

    const infoText = {
      fontSize: '14px',
      color: 'rgba(0,0,0,0.7)',
      margin: '6px 0',
    };

    const infoTextBig = {
      fontSize: '34px',
      color: 'rgba(0,0,0,0.7)',
      fontWeight: 'bold',
      marginTop: '-6px',
    };

    const infoTextBold = {
      fontSize: '14px',
      color: 'rgba(0,0,0,0.7)',
      fontWeight: 'bold',
      marginLeft: '5px',
    };

    const infoIndex = {
      borderRadius: '50%',
      display: 'inline-block',
      border: '1px solid rgb(102, 102, 102)',
      width: '18px',
      height: '18px',
      textAlign: 'center',
      lineHeight: '16px',
    };

    const openBtn = {
      display: 'inline-block',
      width: '24px',
      height: '24px',
      borderRadius: '50%',
      background: 'green',
      boxShadow: '0px 0px 5px 2px rgba(0,0,0,0.3)',
      cursor: 'pointer',
    };

    const openBtnDisabled = {
      display: 'inline-block',
      width: '24px',
      height: '24px',
      borderRadius: '50%',
      background: '#e8e8e8',
      boxShadow: '0px 0px 5px 2px rgba(0,0,0,0.3)',
      cursor: 'default',
    };

    const closeBtn = {
      display: 'inline-block',
      width: '24px',
      height: '24px',
      borderRadius: '50%',
      background: 'red',
      marginLeft: '10px',
      boxShadow: '0px 0px 5px 2px rgba(0,0,0,0.3)',
      cursor: 'pointer',
    };

    const closeBtnDisabled = {
      display: 'inline-block',
      width: '24px',
      height: '24px',
      borderRadius: '50%',
      background: '#e8e8e8',
      marginLeft: '10px',
      boxShadow: '0px 0px 5px 2px rgba(0,0,0,0.3)',
      cursor: 'default',
    };

    const switchDiv = {
      overflow: 'hidden',
    };

    const switchInfo = {
      display: 'inline-block',
      borderTop: '1px solid #e8e8e8',
      borderBottom: '1px solid #e8e8e8',
      borderRight: '1px solid #e8e8e8',
      padding: '4px 10px',
    };

    const switchInfoFirst = {
      display: 'inline-block',
      border: '1px solid #e8e8e8',
      padding: '4px 10px',
    };

    const infoList = infoArr =>
      (infoArr || []).map((item, index) => {
        return (
          <Row>
            <Col span={2}>
              <span style={infoIndex}>{index + 1}</span>
            </Col>
            <Col span={7}>
              <span>{item.name}</span>
            </Col>
            <Col span={7}>
              <span>{item.detail}</span>
            </Col>
            <Col span={6}>
              <span>{item.time}</span>
            </Col>
          </Row>
        );
      });

    const indexCard = electricList =>
      (electricList || []).map((item, index) => {
        return (
          <Col span={8} style={{ marginBottom: '16px', borderBottom: '1px dashed #e8e8e8' }}>
            <div
              style={{
                overflow: 'hidden',
                border: '1px solid #e8e8e8',
                borderBottom: 'none',
                padding: '8px',
                background: '#fff',
                textAlign: 'center',
              }}
            >
              {/* <h3 style={{float:'left'}}>{item.electricBox}</h3> */}
              <div style={{ display: 'inline-block' }}>
                {item.electricStatus ? (
                  <img src={electricOpenImg} />
                ) : (
                  <img src={electricCloseImg} />
                )}
              </div>
            </div>
            <div
              style={{
                border: '1px solid #e8e8e8',
                height: '448px',
                overflow: 'auto',
                padding: '8px 8px 0 8px',
                background: '#fff',
              }}
            >
              {switchCard(item.electricList)}
            </div>
          </Col>
        );
      });

    const switchCard = switchList =>
      (switchList || []).map((item, index) => {
        return (
          <Card
            size="small"
            title={
              <span style={{ cursor: 'pointer' }} onClick={this.showDetailModal.bind(this, item)}>
                {item.switchName}
              </span>
            }
            style={{ marginBottom: '8px' }}
          >
            <Row>
              <Col span={6}>
                <div>
                  {item.switchStatus ? (
                    <div>
                      <img src={switchGreenOpen} style={{ width: '24px', height: '24px' }} />
                      <img
                        src={switchRedClose}
                        style={{ width: '24px', height: '24px', marginLeft: '10px' }}
                      />
                    </div>
                  ) : (
                    <div>
                      <img src={switchGreenClose} style={{ width: '24px', height: '24px' }} />
                      <img
                        src={switchRedOpen}
                        style={{ width: '24px', height: '24px', marginLeft: '10px' }}
                      />
                    </div>
                  )}
                </div>
              </Col>
              <Col span={18}>
                <div style={switchDiv}>
                  <span style={switchInfoFirst}>电压三项</span>
                  {(item.switchVoltage || []).map((ele, ind) => (
                    <span style={switchInfo}>{ele}</span>
                  ))}
                </div>
              </Col>
            </Row>
            <Row style={{ marginTop: '10px' }}>
              <Col span={6}>
                <div>
                  {item.switchStatus ? (
                    <span>
                      <span style={openBtnDisabled} />
                      <span
                        style={closeBtn}
                        onClick={this.onSwitchChange.bind(this, 'close', item.id)}
                      />
                    </span>
                  ) : (
                    <span>
                      <span
                        style={openBtn}
                        onClick={this.onSwitchChange.bind(this, 'open', item.id)}
                      />
                      <span style={closeBtnDisabled} />
                    </span>
                  )}
                </div>
              </Col>
              <Col span={18}>
                <div style={switchDiv}>
                  <span style={switchInfoFirst}>电流三项</span>
                  {(item.switchCurrent || []).map((ele, ind) => (
                    <span style={switchInfo}>{ele}</span>
                  ))}
                </div>
              </Col>
            </Row>
          </Card>
        );
      });

    const detailColumns = [
      {
        title: '序号',
        width: 48,
        render: () => <span>{index + 1}</span>,
      },
      {
        title: 'id',
        dataIndex: 'id',
        key: 'id',
      },
      {
        title: '时间',
        dataIndex: 'time',
        key: 'time',
      },
    ];

    return (
      <div>
        <Row style={{ borderRight: '1px solid #e8e8e8' }}>
          <Col span={6} style={infoCol}>
            <div style={infoText}>开关总数</div>
            <div style={infoTextBig}>{switchTotal}</div>
            <Divider dashed style={{ margin: '6px 0' }} />
            <div>
              <span style={infoText}>通电开关数</span>
              <span style={infoTextBold}>{electrifyTotal}</span>
            </div>
            <div>
              <span style={infoText}>断电开关数</span>
              <span style={infoTextBold}>{powerFailure}</span>
            </div>
          </Col>
          <Col span={9} style={infoCol}>
            <div>开关记录</div>
            <Divider dashed style={{ margin: '4px 0' }} />
            <div style={{ height: '110px', overflow: 'auto' }}>{infoList(switchRecord)}</div>
          </Col>
          <Col span={9} style={infoCol}>
            <div>告警</div>
            <Divider dashed style={{ margin: '4px 0' }} />
            <div style={{ height: '110px', overflow: 'auto' }}>{infoList(emergency)}</div>
          </Col>
        </Row>
        <Divider dashed />
        <Row gutter={16}>{indexCard(electricList)}</Row>
        <Modal
          visible={detailMdal}
          title={detailName}
          destroyOnClose={true}
          maskClosable={false}
          centered={true}
          // onOk={this.handleOk}
          onCancel={this.detailClose.bind(this)}
          footer={[
            <Button key="back" onClick={this.detailClose.bind(this)}>
              关闭
            </Button>,
          ]}
        >
          <div>
            <RangePicker
              defaultValue={[moment(earlyToday, dateFormat), moment(today, dateFormat)]}
              onChange={this.changeDetailDate.bind(this)}
            />
            <Button
              type="primary"
              style={{ marginLeft: '10px' }}
              onClick={this.getDetailData.bind(this)}
            >
              搜索
            </Button>
          </div>
          <div id="chartsParentDiv" />
          <Table
            size="small"
            columns={detailColumns}
            dataSource={tableList}
            pagination={{ current: currentPage, total: detailTotal }}
            onChange={this.changePage.bind(this)}
          />
        </Modal>
      </div>
    );
  }
}

export default homeIndex;
