export default [
  {
    path: '/login',
    component: './User/Login',
  },
  // app
  {
    path: '/',
    component: '../layouts/BasicLayout',
    routes: [
      { path: '/', redirect: '/index' },
      { path: '/404', redirect: '/index' },
      {
        path: '/index',
        name: 'homeIndex',
        icon: 'home',
        component: './home/index',
      },
      {
        path: '/period-manage',
        name: 'periodManage',
        icon: 'control',
        component: './periodManage/index',
      },
      {
        path: '/student-info',
        name: 'studentInfo',
        icon: 'calendar',
        component: './studentInfo/index',
      },
      {
        path: '/elec-info',
        name: 'elecInfo',
        icon: 'profile',
        component: './elecinfo/index',
      },
      {
        component: '404',
      },
    ],
  },
];
