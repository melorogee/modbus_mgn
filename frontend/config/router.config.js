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
      { path: '/', redirect: '/period-manage' },
      { path: '/404', redirect: '/period-manage' },

      {
        path: '/period-manage',
        name: 'periodManage',
        icon: 'calendar',
        component: './periodManage/index',
      },
      {
        path: '/student-info',
        name: 'studentInfo',
        icon: 'team',
        component: './studentInfo/index',
      },
      {
        path: '/elec-info',
        name: 'elecInfo',
        icon: 'account-book',
        component: './elecinfo/index',
      },
      {
        component: '404',
      },
    ],
  },
];
