import { getElectricList, switchFn} from '@/services/api';
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
        type: 'switchFn',
        payload: response,
      });
    },
  },

  reducers: {
    getElectricList(state, { payload }) {
      const switchTotal = payload.switchTotal;
      const electrifyTotal = payload.electrifyTotal;
      const powerFailure = payload.powerFailure;
      const switchRecord = payload.switchRecord;
      const emergency = payload.emergency;
      const electricList = payload.electricList;
    
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
    switchFn(state, { payload }) {
      console.log(payload);
      return {
        ...state,
        success: payload,  //true or false
      };
    },
  },
};
