import { routerRedux } from 'dva/router';
import { stringify } from 'qs';
import { fakeAccountLogin, getBaseInfo,goLogin} from '@/services/api';
import { getPageQuery } from '@/utils/utils';
import { setAuthority } from '@/utils/authority';

export default {
  namespace: 'login',

  state: {
    //用户名
    user_name: '',
    status: undefined,
    isShowLogin: true,
  },

  effects: {

    *goLogin({ payload }, { call, put }) {
      const response = yield call(goLogin, payload);
      yield put({
        type: 'goLoginSuccess',
        payload: response,
      });
      yield put(routerRedux.push('/'));

    },
    *logout(_, { put }) {

      if (document.cookie) {
        let cookies = document.cookie.split(';');
        for (let i = 0; i < cookies.length; i++) {
          let cookie = cookies[i];
          let eqPos = cookie.indexOf('=');
          let name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
          document.cookie = name + '=;expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/';
        }
        if (cookies.length > 0) {
          for (let i = 0; i < cookies.length; i++) {
            let cookie = cookies[i];
            let eqPos = cookie.indexOf('=');
            let name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
            let domain = window.location.host.substr(window.location.host.indexOf('.'));
            document.cookie =
              name + '=;expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/; domain=' + domain;
          }
        }
        localStorage.setItem('environment', 'production');
      } else {
        localStorage.setItem('environment', 'development');
      }
      sessionStorage.removeItem('user_name');
      yield put(routerRedux.push('/login'));
      window.location.reload();
      // yield put({
      //   type: 'changeLoginStatus',
      //   payload: {
      //     status: false,
      //   },
      // });
    },
    *getALLinfo({ payload }, { call, put }) {
      console.log(payload, 'getALLinfo');
      const response = yield call(getBaseInfo, payload);
      yield put({
        type: 'getALLinfoSuccess',
        payload: response,
      });
    },
  },

  reducers: {
    goLoginSuccess(state, { payload }) {
      console.log(payload, 'goLoginSuccess');
      const current_authority = payload.data.current_authority;
      const user_name = payload.data.user_name;
      sessionStorage.setItem('user_name', user_name);
      sessionStorage.setItem('time_user_name', user_name);
      sessionStorage.setItem('current_authority', current_authority);
      setAuthority(current_authority);
      return {
        user_name,
        current_authority,
      };
    },

    changeLoginStatus(state, { payload }) {
      return {
        ...state,
        status: payload.status,
      };
    },
    updateIsShowLogin(state, { payload }) {
      return {
        ...state,
        isShowLogin: payload.isShowLogin,
      };
    },
    updateUserName(state, { payload }) {
      sessionStorage.setItem('user_name', payload.user_name);
      return {
        ...state,
        time_user_name: payload.user_name,
      };
    },
    getALLinfoSuccess(state, { payload }) {
      console.log(payload, 'getALLinfoSuccess');
      const current_authority = payload.data.current_authority;
      const user_name = payload.data.user_name;
      sessionStorage.setItem('user_name', user_name);
      sessionStorage.setItem('time_user_name', user_name);
      sessionStorage.setItem('current_authority', current_authority);
      setAuthority(current_authority);

      return {
        user_name,
        current_authority,
      };


    },
  },
};
