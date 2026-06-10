<script setup lang="ts">
import { onMounted, ref } from 'vue'

import {
  completeTask,
  createTask,
  getActiveTasks,
  getRoutines,
  getRoutineProgress,
  getTodayTasks,
} from '@/api/homebase'
import AppShell from '@/components/layout/AppShell.vue'
import RoutineProgressList from '@/components/routines/RoutineProgressList.vue'
import QuickAddTask from '@/components/tasks/QuickAddTask.vue'
import TaskList from '@/components/tasks/TaskList.vue'
import type { RoutineProgress, Task } from '@/types/homebase'

const todayTasks = ref<Task[]>([])
const activeTasks = ref<Task[]>([])
const routineProgress = ref<RoutineProgress[]>([])
const loading = ref(true)
const errorMessage = ref<string | null>(null)

async function loadDashboard() {
  loading.value = true
  errorMessage.value = null

  try {
    const [today, active, routines] = await Promise.all([
      getTodayTasks(),
      getActiveTasks(),
      getRoutines(),
    ])

    todayTasks.value = today
    activeTasks.value = active

    routineProgress.value = await Promise.all(
      routines.map((routine) => getRoutineProgress(routine.id)),
    )
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Something went wrong'
  } finally {
    loading.value = false
  }
}

async function handleCompleteTask(taskId: number) {
  try {
    await completeTask(taskId)
    await loadDashboard()
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Could not complete task'
  }
}

async function handleCreateTask(payload: { title: string; plannedDate: string | null }) {
  try {
    await createTask({
      title: payload.title,
      plannedDate: payload.plannedDate,
    })

    await loadDashboard()
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Could not create task'
  }
}

onMounted(() => {
  loadDashboard()
})
</script>

<template>
  <AppShell>
    <div class="flex flex-col gap-6">
      <header>
        <p class="text-sm text-muted-foreground">Dashboard</p>
        <h1 class="text-3xl font-semibold tracking-tight">Today</h1>
      </header>

      <p v-if="loading" class="text-sm text-muted-foreground">Loading...</p>

      <p v-else-if="errorMessage" class="text-sm text-destructive">
        {{ errorMessage }}
      </p>

      <div v-else class="grid gap-6 lg:grid-cols-[1fr_320px]">
        <section class="space-y-6">
          <QuickAddTask @create="handleCreateTask" />

          <TaskList
            title="Today's Tasks"
            empty-text="Nothing planned for today."
            :tasks="todayTasks"
            @complete="handleCompleteTask"
          />

          <TaskList
            title="Active Tasks"
            empty-text="No active tasks."
            :tasks="activeTasks"
            @complete="handleCompleteTask"
          />
        </section>

        <RoutineProgressList :routines="routineProgress" />
      </div>
    </div>
  </AppShell>
</template>