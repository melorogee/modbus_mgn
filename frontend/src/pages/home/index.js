import React, { PureComponent } from 'react';
import { connect } from 'dva';
import router from 'umi/router';
import { Card, Table, Divider, Tag, Row, Col, Button, Input, Icon, message, Modal,Calendar ,Select,Switch} from 'antd';
import { cpus } from 'os';
import moment from 'moment';

const electricOpenImg = require('./static/electricOpen.png');
const electricCloseImg = require('./static/electricClose.png');
const switchGreenOpen = require('./static/switchGreenOpen.png');
const switchGreenClose = require('./static/switchGreenClose.png');
const switchRedOpen = require('./static/switchRedOpen.png');
const switchRedClose = require('./static/switchRedClose.png');

const { confirm } = Modal;
const { Option } = Select;

@connect(({ electricInfo }) => ({
  electricInfo,
}))
class homeIndex extends PureComponent {

  constructor(props) {
    super(props);
    //初始化页面数据
    var  date = moment().format('YYYYMMDD');

    this.state = {
      switchTotal:352,     //开关总数
      electrifyTotal:22,   //通电开关数
      powerFailure:330,    //断电开关数
      switchRecord:[       //开关记录
        {
          name:'一号开关',
          detail:'开启',
          time:'14：30'
        },
        {
          name:'一号开关',
          detail:'开启',
          time:'14：30'
        },
        {
          name:'一号开关',
          detail:'开启',
          time:'14：30'
        },
        {
          name:'一号开关',
          detail:'开启',
          time:'14：30'
        },
        {
          name:'一号开关',
          detail:'开启',
          time:'14：30'
        },
        {
          name:'一号开关',
          detail:'开启',
          time:'14：30'
        }
      ],     
      emergency:[          //告警
        {
          name:'一号开关',
          detail:'断电',
          time:'14：30'
        },
        {
          name:'一号开关',
          detail:'断电',
          time:'14：30'
        }
      ],        
      electricList:[
        {
          electricBox:'一号开关',
          electricStatus:true,
          electricList:[
            {
              id:1,
              switchName:'一号开关',
              switchVoltage:[111,222,333],    //电压
              switchCurrent:[111,222,333],    //电流
              switchStatus:true
            },
            {
              id:2,
              switchName:'二号开关',
              switchVoltage:[111,222,333],
              switchCurrent:[111,222,333],
              switchStatus:false
            },
            {
              id:3,
              switchName:'三号开关',
              switchVoltage:[111,222,333],
              switchCurrent:[111,222,333],
              switchStatus:true
            }
          ]
        }, 
      ],
    };
  }

  componentWillMount() {
    // this.getElectricList();

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
        electricList
      } = electricInfo;

      this.setState({
        switchTotal,
        electrifyTotal,
        powerFailure,
        switchRecord,
        emergency,
        electricList
      });
    });
  }


  //值改变回调方法
  onSwitchChange(type,id) {
    const {onChange} = this.props;
    if(type == 'close'){
      confirm({
        title: '确定关闭开关吗?',
        onOk:() => this.onSwitchChangeConfirm(type,id),
        onCancel() {
            // this.onChange(checked,false);
        },
      });
    }else{
      confirm({
        title: '确定开启开关吗?',
        onOk:() => this.onSwitchChangeConfirm(type,id),
        onCancel() {
            // this.onChange(checked,false);
        },
      });
    }
  };


  onSwitchChangeConfirm = (type,id) => {
      const { dispatch } = this.props;

      const params = {
        id: id,
        switch: type
      };
      dispatch({
        type: 'electricInfo/switchFn',
        payload: params,
      }).then(response => {
        const { electricInfo } = this.props;
        const { success } = electricInfo;
        if(success){
          //为true则为成功
          message.success('更改成功')
          this.getElectricList();

        }else{
          message.error('更改失败')
          // this.getCourseList();
        }
      });
    };  


  render() {

    //从状态存储器里取出值
    const { electricList, switchTotal, electrifyTotal, powerFailure, switchRecord, emergency} = this.state;

    const infoCol = {
      background:'#fff',
      borderTop:'1px solid #e8e8e8',
      borderBottom:'1px solid #e8e8e8',
      borderLeft:'1px solid #e8e8e8',
      padding:'8px',
      height:'153px',
    };

    const infoText = {
      fontSize:'14px',
      color:'rgba(0,0,0,0.7)',
      margin:'6px 0'
    };

    const infoTextBig = {
      fontSize:'34px',
      color:'rgba(0,0,0,0.7)',
      fontWeight:'bold',
      marginTop:'-6px'
    };

    const infoTextBold = {
      fontSize:'14px',
      color:'rgba(0,0,0,0.7)',
      fontWeight:'bold',
      marginLeft:'5px'
    };

    const infoIndex = {
      borderRadius:'50%',
      display:'inline-block',
      border:'1px solid rgb(102, 102, 102)',
      width:'18px',
      height:'18px',
      textAlign:'center',
      lineHeight:'16px'
    };

    const openBtn = {
      display:'inline-block',
      width:'24px',
      height:'24px',
      borderRadius:'50%',
      background:'green',
      boxShadow:'0px 0px 5px 2px rgba(0,0,0,0.3)',
      cursor:'pointer'
    };

    const openBtnDisabled = {
      display:'inline-block',
      width:'24px',
      height:'24px',
      borderRadius:'50%',
      background:'#e8e8e8',
      boxShadow:'0px 0px 5px 2px rgba(0,0,0,0.3)',
      cursor:'default'
    };

    const closeBtn = {
      display:'inline-block',
      width:'24px',
      height:'24px',
      borderRadius:'50%',
      background:'red',
      marginLeft:'10px',
      boxShadow:'0px 0px 5px 2px rgba(0,0,0,0.3)',
      cursor:'pointer'
    };

    const closeBtnDisabled = {
      display:'inline-block',
      width:'24px',
      height:'24px',
      borderRadius:'50%',
      background:'#e8e8e8',
      marginLeft:'10px',
      boxShadow:'0px 0px 5px 2px rgba(0,0,0,0.3)',
      cursor:'default'
    };

    const switchDiv = {
      overflow:'hidden',
    };

    const switchInfo = {
      display:'inline-block',
      borderTop:'1px solid #e8e8e8',
      borderBottom:'1px solid #e8e8e8',
      borderRight:'1px solid #e8e8e8',
      padding:'4px 10px',
    };

    const switchInfoFirst = {
      display:'inline-block',
      border:'1px solid #e8e8e8',
      padding:'4px 10px',
    };


    const infoList = infoArr =>
      infoArr.map((item, index) => {
        return (
          <Row>
            <Col span={2}>
              <span style={infoIndex}>{index+1}</span>
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
        )
      });
    
    const indexCard = electricList =>
      electricList.map((item, index) => {
        return (
          <Col span={8} style={{ marginBottom: '16px',borderBottom:'1px dashed #e8e8e8' }}>
            <div style={{overflow:'hidden',border:'1px solid #e8e8e8',borderBottom:'none',padding:'8px',background:'#fff',textAlign:'center'}}>
              {/* <h3 style={{float:'left'}}>{item.electricBox}</h3> */}
              <div style={{display:'inline-block'}}>
                {
                  item.electricStatus?
                  <img src={electricOpenImg}></img>:<img src={electricCloseImg}></img>
                }
              </div>
            </div>
            <div style={{border:'1px solid #e8e8e8',height:"448px",overflow:'auto',padding:'8px 8px 0 8px',background:'#fff'}}>
              {switchCard(item.electricList)}
            </div>
          </Col>
        );
      });

      const switchCard = switchList =>
        switchList.map((item, index) => {
          return (
            <Card size="small" title={item.switchName} style={{marginBottom:'8px'}}>
              <Row>
                <Col span={6}>
                  <div>
                    {
                      item.switchStatus?
                      <div>
                        <img src={switchGreenOpen} style={{width:'24px',height:'24px'}}></img><img src={switchRedClose} style={{width:'24px',height:'24px',marginLeft:'10px'}}></img>
                      </div>:
                      <div>
                        <img src={switchGreenClose} style={{width:'24px',height:'24px'}}></img><img src={switchRedOpen} style={{width:'24px',height:'24px',marginLeft:'10px'}}></img>
                      </div>
                    }
                  </div>
                </Col>
                <Col span={18}>
                  <div style={switchDiv}>
                    <span style={switchInfoFirst}>电压三项</span>
                    {
                      (item.switchVoltage || []).map((ele, ind) => (
                      <span style={switchInfo}>{ele}</span>
                      ))
                    }
                  </div> 
                </Col>
              </Row>
              <Row style={{marginTop:'10px'}}>
                <Col span={6}>
                  <div>
                    {
                      item.switchStatus?
                      <span>
                        <span style={openBtnDisabled}></span>
                        <span style={closeBtn} onClick={this.onSwitchChange.bind(this,'close',item.id)}></span>
                      </span>:
                      <span>
                        <span style={openBtn} onClick={this.onSwitchChange.bind(this,'open',item.id)}></span>
                        <span style={closeBtnDisabled}></span>
                      </span>
                    }
                    
                  </div>
                </Col>
                <Col span={18}>
                <div style={switchDiv}>
                    <span style={switchInfoFirst}>电流三项</span>
                      {
                        (item.switchCurrent || []).map((ele, ind) => (
                        <span style={switchInfo}>{ele}</span>
                        ))
                      }
                  </div>
                </Col>
              </Row>
            </Card>
          );
        });
    return (
      <div>
          <Row style={{borderRight:'1px solid #e8e8e8'}}>
            <Col span={6} style={infoCol}>
              <div style={infoText}>
                开关总数
              </div>
              <div style={infoTextBig}>{switchTotal}</div>
              <Divider dashed style={{margin:'6px 0'}}/>
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
              <Divider dashed style={{margin:'4px 0'}}/>
              <div style={{height:'110px',overflow:'auto'}}>
                {infoList(switchRecord)}
              </div>
            </Col>
            <Col span={9} style={infoCol}>
              <div>告警</div>
              <Divider dashed style={{margin:'4px 0'}}/>
              <div style={{height:'110px',overflow:'auto'}}>
                {infoList(emergency)}
              </div>
            </Col>
          </Row>
          <Divider dashed />
          <Row gutter={16}>{indexCard(electricList)}</Row>
      </div>

    );
  }
}

export default homeIndex;
