import Vue from 'vue';
import VueRouter from 'vue-router';

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    redirect: '/home',
  },
  {
    path: '/home',
    component: () => import('../views/home/Home'),
    children: [
      {
        path: '',
        redirect: 'point',
      },
      {
        path: 'point',
        component: () => import('../views/point/Point'),
      },
      {
        path: 'filter-support',
        component: () => import('../views/filterSupport/FilterSupport'),
      },
      {
        path: 'trigger-support',
        component: () => import('../views/triggerSupport/TriggerSupport'),
      },
      {
        path: 'filter-info',
        component: () => import('../views/filterInfo/FilterInfo'),
      },
      {
        path: 'trigger-info',
        component: () => import('../views/triggerInfo/TriggerInfo'),
      },
    ],
  },
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
});

export default router;
