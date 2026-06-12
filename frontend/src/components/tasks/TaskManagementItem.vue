<script setup lang="ts">
import { computed } from 'vue'
import { CalendarDays, Check, CircleSlash, Clock3, Pencil, RotateCcw } from '@lucide/vue'

import { Badge, type BadgeVariants } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import { Checkbox } from '@/components/ui/checkbox'
import type { Category, Task } from '@/types/homebase'

const props = defineProps<{
  task: Task
  category: Category | null
}>()

const emit = defineEmits<{
  complete: [taskId: number]
  edit: [task: Task]
  skip: [task: Task]
  reopen: [taskId: number]
}>()

const isDone = computed(() => props.task.status === 'done')
const isSkipped = computed(() => props.task.status === 'skipped')
const isActive = computed(() => props.task.status === 'active')

const statusLabel = computed(() => {
  if (isDone.value) return 'Done'
  if (isSkipped.value) return 'Skipped'
  return 'Active'
})

const statusBadgeVariant = computed<BadgeVariants['variant']>(() => {
  if (isDone.value) return 'secondary'
  if (isSkipped.value) return 'outline'
  return 'default'
})

const scheduleLabel = computed(() => {
  if (props.task.scheduledStart) {
    return formatDateTimeRange(props.task.scheduledStart, props.task.scheduledEnd)
  }

  if (props.task.plannedDate) {
    return `All day, ${formatDate(props.task.plannedDate)}`
  }

  if (props.task.dueAt) {
    return `Due ${formatDateTime(props.task.dueAt)}`
  }

  return 'No date set'
})

function toggleDone() {
  if (isDone.value) {
    emit('reopen', props.task.id)
    return
  }

  if (isActive.value) {
    emit('complete', props.task.id)
  }
}

function formatDate(value: string) {
  const parts = value.split('-')
  const year = Number(parts[0])
  const month = Number(parts[1])
  const day = Number(parts[2])

  if (!Number.isFinite(year) || !Number.isFinite(month) || !Number.isFinite(day)) {
    return value
  }

  const date = new Date(year, month - 1, day)

  return new Intl.DateTimeFormat('en-GB', {
    day: 'numeric',
    month: 'short',
    year: 'numeric',
  }).format(date)
}

function formatDateTime(value: string) {
  const parts = value.split('T')
  const date = parts[0] ?? ''
  const time = parts[1] ?? ''

  if (!date) {
    return value
  }

  return `${formatDate(date)}${time ? `, ${time.slice(0, 5)}` : ''}`
}

function formatDateTimeRange(start: string, end: string | null) {
  const startParts = start.split('T')
  const date = startParts[0] ?? ''
  const startTime = startParts[1] ?? ''
  const endTime = end?.split('T')[1] ?? ''

  if (!date) {
    return start
  }

  if (!endTime) {
    return `${formatDate(date)}, ${startTime.slice(0, 5)}`
  }

  return `${formatDate(date)}, ${startTime.slice(0, 5)}-${endTime.slice(0, 5)}`
}
</script>

<template>
  <li
    class="group grid gap-3 rounded-lg border bg-card p-4 text-card-foreground shadow-xs transition hover:border-ring/40 hover:bg-accent/25 sm:grid-cols-[auto_1fr_auto] sm:items-center"
    :class="{
      'opacity-75': isDone || isSkipped,
    }"
  >
    <Checkbox
      :model-value="isDone"
      :disabled="isSkipped"
      @update:model-value="toggleDone"
    />

    <div class="min-w-0">
      <div class="flex flex-wrap items-center gap-2">
        <h3
          class="truncate text-sm font-medium"
          :class="{ 'line-through text-muted-foreground': isDone }"
        >
          {{ task.title }}
        </h3>

        <Badge :variant="statusBadgeVariant">
          {{ statusLabel }}
        </Badge>

        <Badge v-if="category" variant="outline" class="gap-1">
          <span
            v-if="category.colour"
            class="size-2 rounded-full border"
            :style="{ backgroundColor: category.colour }"
          />
          {{ category.name }}
        </Badge>
      </div>

      <p v-if="task.notes" class="mt-1 text-sm text-muted-foreground">
        {{ task.notes }}
      </p>

      <div class="mt-2 flex flex-wrap gap-x-4 gap-y-1 text-xs text-muted-foreground">
        <span class="inline-flex items-center gap-1">
          <CalendarDays class="size-3.5" />
          {{ scheduleLabel }}
        </span>

        <span v-if="task.completedAt" class="inline-flex items-center gap-1">
          <Check class="size-3.5" />
          Completed {{ formatDateTime(task.completedAt) }}
        </span>

        <span v-if="task.skippedAt" class="inline-flex items-center gap-1">
          <CircleSlash class="size-3.5" />
          Skipped<span v-if="task.skipReason">: {{ task.skipReason }}</span>
        </span>
      </div>
    </div>

    <div class="flex items-center justify-end gap-1">
      <Button type="button" variant="ghost" size="icon-sm" @click="emit('edit', task)">
        <Pencil class="size-4" />
        <span class="sr-only">Edit task</span>
      </Button>

      <Button
        v-if="isActive"
        type="button"
        variant="ghost"
        size="icon-sm"
        @click="emit('skip', task)"
      >
        <Clock3 class="size-4" />
        <span class="sr-only">Skip task</span>
      </Button>

      <Button
        v-if="isSkipped"
        type="button"
        variant="ghost"
        size="icon-sm"
        @click="emit('reopen', task.id)"
      >
        <RotateCcw class="size-4" />
        <span class="sr-only">Reopen task</span>
      </Button>
    </div>
  </li>
</template>