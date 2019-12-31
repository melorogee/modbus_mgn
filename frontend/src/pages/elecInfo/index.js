import React, { PureComponent } from 'react';
import { connect } from 'dva';
import router from 'umi/router';
import { Card, Table, Divider, Tag,Button,Modal,Input,message } from 'antd';
import $ from 'jquery';
@connect(({ elecInfo }) => ({
  elecInfo,
}))
class elecInfo extends PureComponent {
  constructor(props) {
    super(props);
    //初始化页面数据
    this.state = {
      elecList: [],
      visible: false,
      boxname : "",
      switchname:"",
      slaveid:"",
      addres:"",
      action:"",
      time:"",

    };
  }

  componentWillMount() {
    this.getElecList();
  }


  getElecList() {
    const { dispatch } = this.props;
    const params = {};
    dispatch({
      type: 'elecInfo/getElecList',
      payload: params,
    }).then(response => {
      // this.setState({
      //     loading: false
      // })
      const { elecInfo } = this.props;
      const { elecList } = elecInfo;
      this.setState({
        elecList,
      });
    });
  }


  render() {
    //从状态存储器里取出值
    const { elecList } = this.state;

    const tableHeight = $(window).height()-280
    //定义表格表头
    const elecColumns = [
      {
        title: '三项线电压 A',
        dataIndex: 'uab',
        width: 70,
        key: 'uab',
      },
      {
        title: '三项线电压 B',
        dataIndex: 'ubc',
        width: 70,
        key: 'ubc',
      },
      {
        title: '三项线电压 C',
        dataIndex: 'uca',
        width: 70,
        key: 'uca',
      },
      {
        title: '三项线电流 A',
        dataIndex: 'ia',
        width: 70,
        key: 'ia',
      },
      {
        title: '三项线电流 B',
        dataIndex: 'ib',
        width: 70,
        key: 'ib',
      },
      {
        title: '三项线电流 C',
        dataIndex: 'ic',
        width: 70,
        key: 'ic',
      },
      {
        title: '合相功率',
        dataIndex: 'pt',
        width: 70,
        key: 'pt',
      },
      {
        title: '合相无功功',
        dataIndex: 'qt',
        width: 70,
        key: 'qt',
      },
      {
        title: '合相功率因数',
        dataIndex: 'pft',
        width: 70,
        key: 'pft',
      },
      {
        title: '频率',
        dataIndex: 'freq',
        width: 70,
        key: 'freq',
      },
      {
        title: '时间',
        dataIndex: 'time',
        width: 100,
        key: 'time',
      }
    ];

    return (
      <div>
        <Card title="开关操作日志信息">
          <Table
            size="small"
            columns={elecColumns}
            dataSource={elecList}
            pagination={false}
            scroll={{y: tableHeight}}
              />
        </Card>
      </div>


    );
  }
}

export default elecInfo;
