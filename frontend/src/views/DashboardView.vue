<script setup lang="ts">
import { onMounted, ref } from 'vue'

import {
  completeTask,
  createTask,
  deleteTask,
  getActiveTasks,
  getCategories,
  getRoutines,
  getRoutineProgress,
  getTodayTasks,
  reopenTask,
  skipTask,
  updateTask,
} from '@/api/homebase'
import AppShell from '@/components/layout/AppShell.vue'
import RoutineProgressList from '@/components/routines/RoutineProgressList.vue'
import EditTaskDialog from '@/components/tasks/EditTaskDialog.vue'
import QuickAddTask from '@/components/tasks/QuickAddTask.vue'
import SkipTaskDialog from '@/components/tasks/SkipTaskDialog.vue'
import TaskList from '@/components/tasks/TaskList.vue'
import type { Category, RoutineProgress, Task } from '@/types/homebase'
import type { TaskFormSubmitPayload } from '@/types/task-form'

const todayTasks = ref<Task[]>([])
const activeTasks = ref<Task[]>([])
const routineProgress = ref<RoutineProgress[]>([])
const categories = ref<Category[]>([])
const loading = ref(true)
const errorMessage = ref<string | null>(null)

const editingTask = ref<Task | null>(null)
const editDialogOpen = ref(false)

const skippingTask = ref<Task | null>(null)
const skipDialogOpen = ref(false)

async function loadDashboard() {
  loading.value = true
  errorMessage.value = null

  try {
    const [today, active, routines, loadedCategories] = await Promise.all([
      getTodayTasks(),
      getActiveTasks(),
      getRoutines(),
      getCategories(),
    ])

    todayTasks.value = today
    activeTasks.value = active
    categories.value = loadedCategories

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

async function handleReopenTask(taskId: number) {
  try {
    await reopenTask(taskId)
    await loadDashboard()
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Could not reopen task'
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

function handleEditTask(task: Task) {
  editingTask.value = task
  editDialogOpen.value = true
}

async function handleSaveTask(payload: TaskFormSubmitPayload & { id: number }) {
  try {
    await updateTask(payload.id, {
      title: payload.title,
      notes: payload.notes,
      categoryId: payload.categoryId,
      plannedDate: payload.plannedDate,
      scheduledStart: payload.scheduledStart,
      scheduledEnd: payload.scheduledEnd,
      dueAt: payload.dueAt,
    })

    editDialogOpen.value = false
    editingTask.value = null
    await loadDashboard()
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Could not update task'
  }
}

function handleOpenSkipTask(task: Task) {
  skippingTask.value = task
  skipDialogOpen.value = true
}

async function handleSkipTask(payload: { id: number; reason: string | null }) {
  try {
    await skipTask(payload.id, {
      reason: payload.reason,
    })

    skipDialogOpen.value = false
    skippingTask.value = null
    await loadDashboard()
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Could not skip task'
  }
}

async function handleDeleteTask(id: number) {
  try {
    await deleteTask(id)

    editDialogOpen.value = false
    editingTask.value = null
    await loadDashboard()
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Could not delete task'
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
        <h1 class="text-3xl font-semibold tracking-tight">Dashboard</h1>
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
            @reopen="handleReopenTask"
            @edit="handleEditTask"
            @skip="handleOpenSkipTask"
          />

          <TaskList
            title="Active Tasks"
            empty-text="No active tasks."
            :tasks="activeTasks"
            @complete="handleCompleteTask"
            @reopen="handleReopenTask"
            @edit="handleEditTask"
            @skip="handleOpenSkipTask"
          />
        </section>

        <RoutineProgressList :routines="routineProgress" />
      </div>

      <EditTaskDialog
        v-model:open="editDialogOpen"
        :task="editingTask"
        :categories="categories"
        @save="handleSaveTask"
        @delete="handleDeleteTask"
      />

      <SkipTaskDialog
        v-model:open="skipDialogOpen"
        :task="skippingTask"
        @skip="handleSkipTask"
      />
    </div>
  </AppShell>
</template>