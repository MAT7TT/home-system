<script setup lang="ts">
import CalendarTaskChip from '@/components/calendar/CalendarTaskChip.vue'
import type { Task } from '@/types/homebase'

defineProps<{
  date: Date | null
  tasks: Task[]
}>()

const emit = defineEmits<{
  selectTask: [task: Task]
  'create-task': []
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
          @select="emit('selectTask', $event)"
        />

        <button
            type="button"
            class="block w-full truncate rounded-sm bg-primary/10 px-1.5 py-0.5 text-left text-xs text-primary transition hover:bg-primary/20"
            @click="emit('create-task')"
        >
            Create new task
        </button>
      </div>
    </div>
  </aside>
</template>