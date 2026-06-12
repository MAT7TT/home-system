<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { Plus } from '@lucide/vue'

import {
  completeTask,
  createTask,
  deleteTask,
  getCategories,
  getRoutines,
  getTags,
  getTaskRoutines,
  getTaskTags,
  getTasks,
  reopenTask,
  skipTask,
  updateTask,
} from '@/api/homebase'
import AppShell from '@/components/layout/AppShell.vue'
import { Button } from '@/components/ui/button'
import CreateTaskDialog from '@/components/tasks/CreateTaskDialog.vue'
import EditTaskDialog from '@/components/tasks/EditTaskDialog.vue'
import SkipTaskDialog from '@/components/tasks/SkipTaskDialog.vue'
import TaskManagementItem from '@/components/tasks/TaskManagementItem.vue'
import TaskManagementStats from '@/components/tasks/TaskManagementStats.vue'
import TaskManagementToolbar from '@/components/tasks/TaskManagementToolbar.vue'
import type { TaskStatusFilter } from '@/types/task-filters'
import type { Category, Routine, Tag, Task } from '@/types/homebase'
import type { TaskFormSubmitPayload } from '@/types/task-form'

const tasks = ref<Task[]>([])
const categories = ref<Category[]>([])
const tags = ref<Tag[]>([])
const routines = ref<Routine[]>([])
const loading = ref(true)
const errorMessage = ref<string | null>(null)

const search = ref('')
const statusFilter = ref<TaskStatusFilter>('active')

const createDialogOpen = ref(false)

const editingTask = ref<Task | null>(null)
const editDialogOpen = ref(false)
const selectedTagIds = ref<number[]>([])
const selectedRoutineIds = ref<number[]>([])

const skippingTask = ref<Task | null>(null)
const skipDialogOpen = ref(false)

const categoryById = computed(() => {
  return new Map(categories.value.map((category) => [category.id, category]))
})

const filteredTasks = computed(() => {
  const searchText = search.value.trim().toLowerCase()

  return tasks.value.filter((task) => {
    const matchesStatus =
      statusFilter.value === 'all' || task.status === statusFilter.value

    const category = task.categoryId ? categoryById.value.get(task.categoryId) : null

    const matchesSearch =
      !searchText ||
      task.title.toLowerCase().includes(searchText) ||
      task.notes?.toLowerCase().includes(searchText) ||
      category?.name.toLowerCase().includes(searchText)

    return matchesStatus && matchesSearch
  })
})

const taskStats = computed(() => {
  return {
    total: tasks.value.length,
    active: tasks.value.filter((task) => task.status === 'active').length,
    done: tasks.value.filter((task) => task.status === 'done').length,
    skipped: tasks.value.filter((task) => task.status === 'skipped').length,
  }
})

async function loadTasksPage() {
  loading.value = true
  errorMessage.value = null

  try {
    const [loadedTasks, loadedCategories, loadedTags, loadedRoutines] =
      await Promise.all([
        getTasks(),
        getCategories(),
        getTags(),
        getRoutines(),
      ])

    tasks.value = loadedTasks
    categories.value = loadedCategories
    tags.value = loadedTags
    routines.value = loadedRoutines
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Could not load tasks'
  } finally {
    loading.value = false
  }
}

async function handleCreateTask(payload: TaskFormSubmitPayload) {
  try {
    await createTask({
      title: payload.title,
      notes: payload.notes,
      categoryId: payload.categoryId,
      plannedDate: payload.plannedDate,
      scheduledStart: payload.scheduledStart,
      scheduledEnd: payload.scheduledEnd,
      dueAt: payload.dueAt,
      tagIds: payload.tagIds,
      routineIds: payload.routineIds,
    })

    createDialogOpen.value = false
    await loadTasksPage()
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Could not create task'
  }
}

async function handleEditTask(task: Task) {
  editingTask.value = task
  editDialogOpen.value = true
  selectedTagIds.value = []
  selectedRoutineIds.value = []

  try {
    const [taskTags, taskRoutines] = await Promise.all([
      getTaskTags(task.id),
      getTaskRoutines(task.id),
    ])

    selectedTagIds.value = taskTags.map((tag) => tag.id)
    selectedRoutineIds.value = taskRoutines.map((routine) => routine.id)
  } catch (error) {
    errorMessage.value =
      error instanceof Error ? error.message : 'Could not load task links'
  }
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
      tagIds: payload.tagIds,
      routineIds: payload.routineIds,
    })

    editDialogOpen.value = false
    editingTask.value = null
    selectedTagIds.value = []
    selectedRoutineIds.value = []
    await loadTasksPage()
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Could not update task'
  }
}

async function handleDeleteTask(taskId: number) {
  try {
    await deleteTask(taskId)

    editDialogOpen.value = false
    editingTask.value = null
    selectedTagIds.value = []
    selectedRoutineIds.value = []
    await loadTasksPage()
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Could not delete task'
  }
}

async function handleCompleteTask(taskId: number) {
  try {
    await completeTask(taskId)
    await loadTasksPage()
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Could not complete task'
  }
}

async function handleReopenTask(taskId: number) {
  try {
    await reopenTask(taskId)
    await loadTasksPage()
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Could not reopen task'
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
    await loadTasksPage()
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Could not skip task'
  }
}

onMounted(() => {
  loadTasksPage()
})
</script>

<template>
  <AppShell>
    <div class="flex flex-col gap-6">
      <header class="flex flex-col gap-2">
        <p class="text-sm font-medium text-muted-foreground">Task management</p>

        <div class="flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">
          <div>
            <h1 class="text-3xl font-semibold tracking-tight">Tasks</h1>
            <p class="mt-2 max-w-2xl text-sm text-muted-foreground">
              Search, review and tidy every task across the system.
            </p>
          </div>

          <Button type="button" class="w-full sm:w-auto" @click="createDialogOpen = true">
            <Plus class="size-4" />
            New task
          </Button>
        </div>
      </header>

      <TaskManagementStats
        :total="taskStats.total"
        :active="taskStats.active"
        :done="taskStats.done"
        :skipped="taskStats.skipped"
      />

      <TaskManagementToolbar
        v-model:search="search"
        v-model:status="statusFilter"
      />

      <p v-if="loading" class="rounded-lg border bg-card p-4 text-sm text-muted-foreground">
        Loading tasks...
      </p>

      <p
        v-else-if="errorMessage"
        class="rounded-lg border border-destructive/30 bg-card p-4 text-sm text-destructive"
      >
        {{ errorMessage }}
      </p>

      <section v-else class="rounded-lg border bg-card/50 p-3 shadow-xs">
        <div
          v-if="filteredTasks.length === 0"
          class="rounded-md border border-dashed bg-background p-8 text-center"
        >
          <h2 class="text-sm font-medium">No matching tasks</h2>
          <p class="mt-1 text-sm text-muted-foreground">
            Adjust the search or filter to find what you need.
          </p>
        </div>

        <ul v-else class="space-y-3">
          <TaskManagementItem
            v-for="task in filteredTasks"
            :key="task.id"
            :task="task"
            :category="task.categoryId ? categoryById.get(task.categoryId) ?? null : null"
            @complete="handleCompleteTask"
            @reopen="handleReopenTask"
            @edit="handleEditTask"
            @skip="handleOpenSkipTask"
          />
        </ul>
      </section>

      <CreateTaskDialog
        v-model:open="createDialogOpen"
        :default-planned-date="null"
        :categories="categories"
        :tags="tags"
        :routines="routines"
        @create-task="handleCreateTask"
      />

      <EditTaskDialog
        v-model:open="editDialogOpen"
        :task="editingTask"
        :categories="categories"
        :tags="tags"
        :routines="routines"
        :selected-tag-ids="selectedTagIds"
        :selected-routine-ids="selectedRoutineIds"
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