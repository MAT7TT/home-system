<script setup lang="ts">
import { computed } from 'vue'

import MonthDayCell from '@/components/calendar/MonthDayCell.vue'
import type { Task } from '@/types/homebase'

const props = defineProps<{
  currentDate: Date
  selectedDate: Date | null
  tasks: Task[]
}>()

const emit = defineEmits<{
  selectDate: [date: Date]
  selectTask: [task: Task]
}>()

const weekDays = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']

function startOfDay(date: Date) {
  return new Date(date.getFullYear(), date.getMonth(), date.getDate())
}

function formatDateKey(date: Date) {
  return [
    date.getFullYear(),
    String(date.getMonth() + 1).padStart(2, '0'),
    String(date.getDate()).padStart(2, '0'),
  ].join('-')
}

function isSameDay(a: Date, b: Date) {
  return (
    a.getFullYear() === b.getFullYear() &&
    a.getMonth() === b.getMonth() &&
    a.getDate() === b.getDate()
  )
}

function tasksForDate(date: Date) {
  const dateKey = formatDateKey(date)
  return props.tasks.filter((task) => task.plannedDate === dateKey)
}

const calendarDays = computed(() => {
  const year = props.currentDate.getFullYear()
  const month = props.currentDate.getMonth()

  const firstOfMonth = new Date(year, month, 1)
  const firstDayIndex = (firstOfMonth.getDay() + 6) % 7

  const gridStart = new Date(firstOfMonth)
  gridStart.setDate(firstOfMonth.getDate() - firstDayIndex)

  return Array.from({ length: 42 }, (_, index) => {
    const date = new Date(gridStart)
    date.setDate(gridStart.getDate() + index)

    return {
      date: startOfDay(date),
      tasks: tasksForDate(date),
      isCurrentMonth: date.getMonth() === month,
      isToday: isSameDay(date, new Date()),
      isSelected: props.selectedDate ? isSameDay(date, props.selectedDate) : false,
    }
  })
})
</script>

<template>
  <section class="rounded-lg border bg-card text-card-foreground">
    <div class="grid grid-cols-7 border-b text-xs font-medium text-muted-foreground">
      <div v-for="day in weekDays" :key="day" class="px-2 py-2">
        {{ day }}
      </div>
    </div>

    <div class="grid grid-cols-7">
      <MonthDayCell
        v-for="day in calendarDays"
        :key="day.date.toISOString()"
        :date="day.date"
        :tasks="day.tasks"
        :is-current-month="day.isCurrentMonth"
        :is-today="day.isToday"
        :is-selected="day.isSelected"
        @select-date="emit('selectDate', $event)"
        @select-task="emit('selectTask', $event)"
      />
    </div>
  </section>
</template>