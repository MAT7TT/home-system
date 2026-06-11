<script setup lang="ts">
import CalendarTaskChip from '@/components/calendar/CalendarTaskChip.vue'
import type { Task } from '@/types/homebase'

defineProps<{
  date: Date
  tasks: Task[]
  isCurrentMonth: boolean
  isToday: boolean
  isSelected: boolean
}>()

const emit = defineEmits<{
  selectDate: [date: Date]
  selectTask: [task: Task]
}>()
</script>

<template>
  <button
    type="button"
    class="min-h-24 border-b border-r p-2 text-left align-top transition hover:bg-accent"
    :class="{
      'bg-muted/30 text-muted-foreground': !isCurrentMonth,
      'bg-accent': isSelected,
    }"
    @click="emit('selectDate', date)"
  >
    <span
      class="inline-flex size-7 items-center justify-center rounded-full text-sm"
      :class="{
        'bg-primary text-primary-foreground': isToday,
      }"
    >
      {{ date.getDate() }}
    </span>

    <div class="mt-2 space-y-1">
      <CalendarTaskChip
        v-for="task in tasks.slice(0, 3)"
        :key="task.id"
        :task="task"
        @select="emit('selectTask', $event)"
      />

      <div v-if="tasks.length > 3" class="text-xs text-muted-foreground">
        +{{ tasks.length - 3 }} more
      </div>
    </div>
  </button>
</template>