export default [
  {path: '/',name:'主页',icon:'smile',component: './Index'},
  {
    // 动态路由
    path: '/interface_info/:id',
    name: '查看接口',
    icon:'smile',
    component: './InterfaceInfo',
    // 不在菜单页显示
    hideInMenu: true
  },
  {
    path: '/user',
    layout: false,
    routes: [
      {
        name: '登录',
        path: '/user/login',
        component: './User/Login',
      },
    ],
  },
  // {
  //   path: '/welcome',
  //   name: 'welcome',
  //   icon: 'smile',
  //   component: './Index',
  // },
  {
    path: '/admin',
    name: '管理页',
    icon: 'crown',
    access: 'canAdmin',

      // {
      //   path: '/admin',
      //   redirect: '/admin/sub-page',
      // },
      // {
      //   path: '/admin/sub-page',
      //   name: 'sub-page',
      //   component: './Admin',
      // },
    routes: [
      {
        name: '接口管理',
        icon: 'table',
        path: '/admin/interface_info',
        component: './Admin/InterfaceInfo',
      },
      { name: '接口分析', icon: 'analysis', path: '/admin/interface_analysis', component: './Admin/InterfaceAnalysis' },
    ],
  },
  // {
  //   path: '/',
  //   redirect: '/welcome',
  // },
  {
    path: '*',
    layout: false,
    component: './404',
  },
];
