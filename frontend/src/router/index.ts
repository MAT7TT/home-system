import { createRouter, createWebHistory } from 'vue-router'

import RoutinesView from '@/views/RoutinesView.vue'
import DashboardView from '@/views/DashboardView.vue'
import SettingsView from '@/views/SettingsView.vue'
import TasksView from '@/views/TasksView.vue'
import CalendarView from '@/views/CalendarView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'dashboard',
      component: DashboardView,
    },
    {
      path: '/tasks',
      name: 'tasks',
      component: TasksView,
    },
    {
      path: '/routines',
      name: 'routines',
      component: RoutinesView,
    },
    {
      path: '/settings',
      name: 'settings',
      component: SettingsView,
    },
    {
      path: '/calendar',
      name: 'calendar',
      component: CalendarView,
    },
  ],
})

export default router
