import { getElecList } from '@/services/api';
import { setAuthority } from '@/utils/authority';
import { reloadAuthorized } from '@/utils/Authorized';

export default {
  namespace: 'elecInfo',

  state: {
    elecList: [],
    success:false

  },

  effects: {
    *getElecList({ payload }, { call, put }) {
      const response = yield call(getElecList, payload);
      yield put({
        type: 'getElecListSuccess',
        payload: response,
      });
    },

  },

  reducers: {
    getElecListSuccess(state, { payload }) {
      return {
        ...state,
        elecList: payload,
      };
    },

  },
};
