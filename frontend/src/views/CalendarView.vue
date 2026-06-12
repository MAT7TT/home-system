<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'

import {
  createTask,
  deleteTask,
  getCategories,
  getRoutines,
  getTags,
  getTaskRoutines,
  getTaskTags,
  getTasks,
  updateTask,
} from '@/api/homebase'
import CalendarToolbar from '@/components/calendar/CalendarToolbar.vue'
import DayPanel from '@/components/calendar/DayPanel.vue'
import MonthCalendar from '@/components/calendar/MonthCalendar.vue'
import AppShell from '@/components/layout/AppShell.vue'
import CreateTaskDialog from '@/components/tasks/CreateTaskDialog.vue'
import EditTaskDialog from '@/components/tasks/EditTaskDialog.vue'
import type { Category, Routine, Tag, Task } from '@/types/homebase'
import type { TaskFormSubmitPayload } from '@/types/task-form'

type CalendarMode = 'list' | 'day' | 'week' | 'month'

const mode = ref<CalendarMode>('month')
const currentDate = ref(new Date())
const selectedDate = ref<Date | null>(new Date())
const createTaskPlannedDate = ref<string | null>(null)
const createDialogOpen = ref(false)
const editingTask = ref<Task | null>(null)
const editDialogOpen = ref(false)
const selectedTagIds = ref<number[]>([])
const selectedRoutineIds = ref<number[]>([])
const tasks = ref<Task[]>([])
const categories = ref<Category[]>([])
const tags = ref<Tag[]>([])
const routines = ref<Routine[]>([])
const loading = ref(true)
const errorMessage = ref<string | null>(null)

const calendarTitle = computed(() => {
  return new Intl.DateTimeFormat('en-GB', {
    month: 'long',
    year: 'numeric',
  }).format(currentDate.value)
})

const selectedDateTasks = computed(() => {
  if (!selectedDate.value) {
    return []
  }

  const dateKey = formatDateKey(selectedDate.value)

  return tasks.value.filter((task) => task.plannedDate === dateKey)
})

async function loadCalendar() {
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
    errorMessage.value = error instanceof Error ? error.message : 'Could not load calendar'
  } finally {
    loading.value = false
  }
}

function formatDateKey(date: Date) {
  return [
    date.getFullYear(),
    String(date.getMonth() + 1).padStart(2, '0'),
    String(date.getDate()).padStart(2, '0'),
  ].join('-')
}

function changeMode(nextMode: CalendarMode) {
  mode.value = nextMode
}

function goPrevious() {
  const nextDate = new Date(currentDate.value)

  if (mode.value === 'month' || mode.value === 'list') {
    nextDate.setMonth(nextDate.getMonth() - 1)
  } else if (mode.value === 'week') {
    nextDate.setDate(nextDate.getDate() - 7)
  } else {
    nextDate.setDate(nextDate.getDate() - 1)
  }

  currentDate.value = nextDate
}

function goNext() {
  const nextDate = new Date(currentDate.value)

  if (mode.value === 'month' || mode.value === 'list') {
    nextDate.setMonth(nextDate.getMonth() + 1)
  } else if (mode.value === 'week') {
    nextDate.setDate(nextDate.getDate() + 7)
  } else {
    nextDate.setDate(nextDate.getDate() + 1)
  }

  currentDate.value = nextDate
}

function selectDate(date: Date) {
  selectedDate.value = date
}

function createTaskDialog(date: Date | null) {
  createTaskPlannedDate.value = date ? formatDateKey(date) : null
  createDialogOpen.value = true
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

    createTaskPlannedDate.value = null
    createDialogOpen.value = false
    await loadCalendar()
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Could not create task'
  }
}

async function selectTask(task: Task) {
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
    await loadCalendar()
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
    await loadCalendar()
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Could not delete task'
  }
}

onMounted(() => {
  loadCalendar()
})
</script>

<template>
  <AppShell>
    <div class="flex flex-col gap-6">
      <header>
        <p class="text-sm text-muted-foreground">Planning</p>
        <h1 class="text-3xl font-semibold tracking-tight">Calendar</h1>
      </header>

      <CalendarToolbar
        :title="calendarTitle"
        :mode="mode"
        @previous="goPrevious"
        @next="goNext"
        @mode-change="changeMode"
      />

      <p v-if="loading" class="text-sm text-muted-foreground">Loading calendar...</p>

      <p v-else-if="errorMessage" class="text-sm text-destructive">
        {{ errorMessage }}
      </p>

      <div v-else class="grid gap-6 lg:grid-cols-[1fr_320px]">
        <MonthCalendar
          v-if="mode === 'month'"
          :current-date="currentDate"
          :selected-date="selectedDate"
          :tasks="tasks"
          @select-date="selectDate"
          @select-task="selectTask"
        />

        <section v-else class="rounded-lg border bg-card p-4 text-card-foreground">
          {{ mode }} view will go here.
        </section>

        <DayPanel
          :date="selectedDate"
          :tasks="selectedDateTasks"
          @select-task="selectTask"
          @create-task="createTaskDialog"
        />
      </div>
    </div>

    <CreateTaskDialog
      v-model:open="createDialogOpen"
      :default-planned-date="createTaskPlannedDate"
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
  </AppShell>
</template>