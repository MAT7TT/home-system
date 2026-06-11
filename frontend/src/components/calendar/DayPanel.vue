<script setup lang="ts">
import CalendarTaskChip from '@/components/calendar/CalendarTaskChip.vue'
import type { Task } from '@/types/homebase'

defineProps<{
  date: Date | null
  tasks: Task[]
}>()

const emit = defineEmits<{
    'select-task': [task: Task]
    'create-task': [date: Date | null]
  }>()
</script>

<template>
  <aside class="rounded-lg border bg-card p-4 text-card-foreground">
    <h2 class="text-lg font-medium">Selected day</h2>

    <p class="mt-2 text-sm text-muted-foreground">
      {{
        date
          ? new Intl.DateTimeFormat('en-GB', {
              weekday: 'long',
              day: 'numeric',
              month: 'long',
              year: 'numeric',
            }).format(date)
          : 'No day selected'
      }}
    </p>

    <div class="mt-4">
      <h3 class="text-sm font-medium">Tasks</h3>

      <div v-if="tasks.length === 0" class="mt-2 text-sm text-muted-foreground">
        No tasks planned.
      </div>

      <div v-else class="mt-2 space-y-2">
        <CalendarTaskChip
          v-for="task in tasks"
          :key="task.id"
          :task="task"
          @select="emit('select-task', $event)"
        />
      </div>

      <button
            type="button"
            class="mt-4 inline-flex w-full items-center justify-center rounded-md border border-dashed border-muted-foreground/30 bg-background px-3 py-2 text-sm font-medium text-muted-foreground transition hover:border-primary/50 hover:bg-accent hover:text-accent-foreground"
            @click="emit('create-task', date)"
        >
            Create new task
        </button>
    </div>
  </aside>
</template>