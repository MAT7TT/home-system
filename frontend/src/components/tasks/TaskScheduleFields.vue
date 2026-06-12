<script setup lang="ts">
import { computed } from 'vue'
import { CalendarDays, Clock3 } from '@lucide/vue'

import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'

const props = defineProps<{
  idPrefix: string
  plannedDate: string
  scheduledStart: string
  scheduledEnd: string
  dueAt: string
}>()

const emit = defineEmits<{
  'update:plannedDate': [value: string]
  'update:scheduledStart': [value: string]
  'update:scheduledEnd': [value: string]
  'update:dueAt': [value: string]
}>()

const isTimeBlock = computed(() => Boolean(props.scheduledStart || props.scheduledEnd))
const startTime = computed(() => getTimeFromDateTime(props.scheduledStart))
const endTime = computed(() => getTimeFromDateTime(props.scheduledEnd))

function getTodayDateKey() {
  const today = new Date()

  return [
    today.getFullYear(),
    String(today.getMonth() + 1).padStart(2, '0'),
    String(today.getDate()).padStart(2, '0'),
  ].join('-')
}

function getTimeFromDateTime(value: string) {
  return value ? value.slice(11, 16) : ''
}

function buildDateTime(date: string, time: string) {
  return date && time ? `${date}T${time}` : ''
}

function toInputValue(value: string | number) {
  return String(value)
}

function setPlannedDate(value: string | number) {
  const plannedDate = toInputValue(value)

  emit('update:plannedDate', plannedDate)

  if (!plannedDate) {
    emit('update:scheduledStart', '')
    emit('update:scheduledEnd', '')
    return
  }

  if (props.scheduledStart) {
    emit('update:scheduledStart', buildDateTime(plannedDate, startTime.value))
  }

  if (props.scheduledEnd) {
    emit('update:scheduledEnd', buildDateTime(plannedDate, endTime.value))
  }
}

function setAllDay() {
  emit('update:scheduledStart', '')
  emit('update:scheduledEnd', '')
}

function setTimeBlock() {
  const plannedDate = props.plannedDate || getTodayDateKey()

  if (!props.plannedDate) {
    emit('update:plannedDate', plannedDate)
  }

  emit('update:scheduledStart', props.scheduledStart || `${plannedDate}T09:00`)
  emit('update:scheduledEnd', props.scheduledEnd || `${plannedDate}T10:00`)
}

function setStartTime(value: string | number) {
  const plannedDate = props.plannedDate || getTodayDateKey()

  if (!props.plannedDate) {
    emit('update:plannedDate', plannedDate)
  }

  emit('update:scheduledStart', buildDateTime(plannedDate, toInputValue(value)))
}

function setEndTime(value: string | number) {
  const plannedDate = props.plannedDate || getTodayDateKey()

  if (!props.plannedDate) {
    emit('update:plannedDate', plannedDate)
  }

  emit('update:scheduledEnd', buildDateTime(plannedDate, toInputValue(value)))
}
</script>

<template>
  <section class="rounded-lg border bg-card text-card-foreground shadow-xs">
    <div class="flex items-center gap-3 border-b px-4 py-3">
      <div class="flex size-9 items-center justify-center rounded-md bg-primary/10 text-primary">
        <CalendarDays class="size-4" />
      </div>

      <div>
        <h3 class="text-sm font-medium">Schedule</h3>
        <p class="text-xs text-muted-foreground">Date, time block and due point</p>
      </div>
    </div>

    <div class="space-y-4 p-4">
      <div class="grid gap-4 sm:grid-cols-2">
        <div class="space-y-2">
          <Label :for="`${idPrefix}-planned-date`">Planned date</Label>
          <Input
            :id="`${idPrefix}-planned-date`"
            :model-value="plannedDate"
            type="date"
            @update:model-value="setPlannedDate"
          />
        </div>

        <div class="space-y-2">
          <Label :for="`${idPrefix}-due-at`">Due date/time</Label>
          <Input
            :id="`${idPrefix}-due-at`"
            :model-value="dueAt"
            type="datetime-local"
            @update:model-value="emit('update:dueAt', toInputValue($event))"
          />
        </div>
      </div>

      <div class="rounded-md border bg-muted/40 p-1">
        <div class="grid grid-cols-2 gap-1">
          <Button
            type="button"
            size="sm"
            :variant="isTimeBlock ? 'ghost' : 'secondary'"
            @click="setAllDay"
          >
            All day
          </Button>

          <Button
            type="button"
            size="sm"
            :variant="isTimeBlock ? 'secondary' : 'ghost'"
            @click="setTimeBlock"
          >
            <Clock3 class="size-4" />
            Time block
          </Button>
        </div>
      </div>

      <div v-if="isTimeBlock" class="grid gap-4 sm:grid-cols-2">
        <div class="space-y-2">
          <Label :for="`${idPrefix}-start-time`">Start time</Label>
          <Input
            :id="`${idPrefix}-start-time`"
            :model-value="startTime"
            type="time"
            @update:model-value="setStartTime"
          />
        </div>

        <div class="space-y-2">
          <Label :for="`${idPrefix}-end-time`">End time</Label>
          <Input
            :id="`${idPrefix}-end-time`"
            :model-value="endTime"
            type="time"
            @update:model-value="setEndTime"
          />
        </div>
      </div>
    </div>
  </section>
</template>