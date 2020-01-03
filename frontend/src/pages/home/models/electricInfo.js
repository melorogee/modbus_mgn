import { getElectricList, switchFn, getDetailData} from '@/services/api';
import { setAuthority } from '@/utils/authority';
import { reloadAuthorized } from '@/utils/Authorized';

export default {
  namespace: 'electricInfo',

  state: {
    switchTotal:0,     //开关总数
    electrifyTotal:0,   //通电开关数
    powerFailure:0,    //断电开关数
    switchRecord:[],   //开关记录
    emergency:[],      //告警
    electricList:[],   //底部开关列表，格式见页面mock
    chartsList:[],     //图表列表
    chartsLegend:[],   //图表图例
    tableList:[],      //表格数据
    detailTotal:0,
    // 图表数据返回例子：
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
    //   ],
    //  detailTotal:100,
    //  tableList:[
    //    {
    //      id:1,
    //      time:'20191230',
    //      ...
    //    }
    //  ]
    // }
  },

  effects: {
    *getElectricList({ payload }, { call, put }) {
      const response = yield call(getElectricList, payload);
      yield put({
        type: 'getElectricListSuccess',
        payload: response,
      });
    },
    *switchFn({ payload }, { call, put }) {
      const response = yield call(switchFn, payload);
      yield put({
        type: 'switchFnSuccess',
        payload: response,
      });
    },
    *getDetailData({ payload }, { call, put }) {
      const response = yield call(getDetailData, payload);
      yield put({
        type: 'getDetailDataSuccess',
        payload: response,
      });
    },
  },

  reducers: {
    getElectricListSuccess(state, { payload }) {
      const switchTotal = payload.switchTotal;
      const electrifyTotal = payload.electrifyTotal;
      const powerFailure = payload.powerFailure;
      const switchRecord = payload.switchRecord;
      const emergency = payload.emergency;
      const electricList = payload.electricList;
    console.log(payload);
      return {
        ...state,
        switchTotal,
        electrifyTotal,
        powerFailure,
        switchRecord,
        emergency,
        electricList
      };
    },
    switchFnSuccess(state, { payload }) {
      console.log(payload);
      return {
        ...state,
        success: payload,  //true or false
      };
    },
    getDetailDataSuccess(state, { payload }) {
      const chartsList = payload.list?payload.list:[];
      const chartsLegend = payload.legend?payload.legend:[];
      const tableList = payload.tableList?payload.tableList:[];
      const detailTotal = payload.detailTotal?payload.detailTotal:0;
      return {
        ...state,
        chartsList,
        chartsLegend,
        tableList,
        detailTotal
      };
    }
  }
};
