import React, { PureComponent } from 'react';
import { connect } from 'dva';
import router from 'umi/router';
import { Card, Table, Divider, Tag,Button,Modal,Input,message } from 'antd';
import $ from 'jquery';
@connect(({ studentInfo }) => ({
  studentInfo,
}))
class studentInfo extends PureComponent {
  constructor(props) {
    super(props);
    //初始化页面数据
    this.state = {
      studentList: [],
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
    this.getStudentList();
  }


  getStudentList() {
    const { dispatch } = this.props;
    const params = {};
    dispatch({
      type: 'studentInfo/getStudentList',
      payload: params,
    }).then(response => {
      // this.setState({
      //     loading: false
      // })
      const { studentInfo } = this.props;
      const { studentList } = studentInfo;
      this.setState({
        studentList,
      });
    });
  }


  render() {
    //从状态存储器里取出值
    const { studentList } = this.state;

    const tableHeight = $(window).height()-280
    //定义表格表头
    const studentColumns = [
      {
        title: '所在箱',
        dataIndex: 'boxname',
        width: 100,
        key: 'boxname',
      },
      {
        title: '开关别名',
        dataIndex: 'switchname',
        width: 100,
        key: 'switchname',
      },
      {
        title: 'DO的slaveId',
        dataIndex: 'slaveid',
        width: 50,
        key: 'slaveid',
      },
      {
        title: 'DO的地址',
        dataIndex: 'address',
        width: 50,
        key: 'school_class',
      },
      {
        title: '动作',
        dataIndex: 'action',
        width: 50,
        key: 'action',
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
            columns={studentColumns}
            dataSource={studentList}
            pagination={false}
            scroll={{y: tableHeight}}
              />
        </Card>
      </div>


    );
  }
}

export default studentInfo;
