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
        path: 'mapper-support',
        component: () => import('../views/mapperSupport/MapperSupport'),
      },
      {
        path: 'filter-info',
        component: () => import('../views/filterInfo/FilterInfo'),
      },
      {
        path: 'trigger-info',
        component: () => import('../views/triggerInfo/TriggerInfo'),
      },
      {
        path: 'realtime-value',
        component: () => import('../views/realtimeValue/RealtimeValue'),
      },
      {
        path: 'persistence-value',
        component: () => import('../views/persistenceValue/PersistenceValue'),
      },
      {
        path: 'filtered-value',
        component: () => import('../views/filteredValue/FilteredValue'),
      },
      {
        path: 'triggered-value',
        component: () => import('../views/triggeredValue/TriggeredValue'),
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
