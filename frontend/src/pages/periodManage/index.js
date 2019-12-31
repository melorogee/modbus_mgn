import React, { PureComponent } from 'react';
import { connect } from 'dva';
import router from 'umi/router';
import { Card, Table, Divider, Tag, Row, Col, Button, Input, Icon, message, Modal,Calendar ,Select,Switch} from 'antd';
import { cpus } from 'os';
import moment from 'moment';

const { confirm } = Modal;
const { Option } = Select;

@connect(({ courseInfo }) => ({
  courseInfo,
}))
class studentCourseInfo extends PureComponent {
  showModal (operator,item) {


    this.setState({
      // visible: true,
      // op: operator,
      // tempCourseId : item.courseId,
      // tempStudentId : item.studentId
    });
  };



  handleCancel = () => {
    this.setState({
      visible: false,
    });
  };


  constructor(props) {
    super(props);
    //初始化页面数据
    var  date = moment().format('YYYYMMDD');

    this.state = {
      studentClassVisible :false,
      detailVisible :false,
      visible: false,
      op:"",
      nowDate:date,
      tempCourseId:"",
      tempStudentId:"",
      defaultNum:1,
      detailStudentId:"",
      detailCourseId:"",
      courseList: [

      ],

      studentClassNum : "",
      tempClass:"",
      tempStudent:""

    };
  }

  componentWillMount() {
    this.getCourseList();

  }


  getCourseList() {
    const { dispatch } = this.props;
    const params = {};
    dispatch({
      type: 'courseInfo/getStudentCourse',
      payload: params,
    }).then(response => {
      const { courseInfo } = this.props;
      console.log(courseInfo);

      const { courseList } = courseInfo;

      console.log(courseList);
      this.setState({
        courseList,
      });
    });
  }





  //值改变回调方法
  onSwitchChange = (record,checked) => {
    const {onChange} = this.props;

    confirm({
      title: '确定开启或者关闭开关吗?',
      onOk:() => this.onSwitchChangeConfirm(record,checked),
      onCancel() {
          // this.onChange(checked,false);
      },
    });


  };


  onSwitchChangeConfirm = (record,checked) => {
    this.setState({
      currentValue: checked ? '1' : '0',
      checked: checked
    }, () => {
      // alert(record.oncode)
      // alert(this.state.checked)
      // if (onChange && typeof onChange === 'function') {
      //   onChange(checked, this.state.currentValue);
      // }
      const { dispatch } = this.props;

      const params = {
        code: this.state.checked == true?record.oncode:record.offcode,
      };
      // alert(params.code);
      dispatch({
        type: 'courseInfo/addClass',
        payload: params,
      }).then(response => {
        const { courseInfo } = this.props;
        const { success } = courseInfo;
        this.setState({
          visible: false,
        });
        if(success){
          //为true则为成功
          message.success('更改成功')
          this.getCourseList();

        }else{
          message.error('更改失败')
          this.getCourseList();

        }
      });
    });  }


  render() {

    //从状态存储器里取出值
    const { courseList } = this.state;
    //定义表格表头

    const courseColumns = [
      {
        title: '开关名称',
        dataIndex: 'switchname',
        key: 'switchname',
        width: 180,
      },


      {
        title: '控制',
        width: 100,
        render: (text, record) => (
          <div>
            <Switch checkedChildren="开" unCheckedChildren="关" defaultUnChecked checked={record.state == '1' ? true : false}  onChange={this.onSwitchChange.bind(this,record)} />
          </div>
        ),
      },
    ];
    {/*<div>*/}
    {/*  /!* <span style={courseBtn} onClick={this.changeCourse.bind(this,'minus', record)}>*/}
    {/*    <Icon type="minus" />*/}
    {/*  </span> *!/*/}

    {/*  <span style={courseBtn} onClick={this.showModal.bind(this,'minus',record)}>*/}
    {/*    <Icon type="minus" />*/}
    {/*  </span>*/}
    {/*  <span style={courseBtn} onClick={this.showModal.bind(this,'plus',record)}>*/}
    {/*    <Icon type="plus" />*/}
    {/*  </span>*/}
    {/*</div>*/}



    const courseTable = courseList =>
      courseList.map((item, index) => {
        return (
          <Col span={8} style={{ marginBottom: '16px',height:"450px",overflow:'scroll',borderBottom:'1px dashed #e8e8e8' }}>
            <Table
              size="small"
              title={() => item.boxname}
              columns={courseColumns}
              dataSource={item.boxdetail}
              pagination={false}
              scroll={{ y: 350 }}
            />
          </Col>
        );
      });

    return (
      <div>
        <Card title="开关管理">
          <Row gutter={16}>{courseTable(courseList)}</Row>
        </Card>

      </div>

    );
  }
}

export default studentCourseInfo;
