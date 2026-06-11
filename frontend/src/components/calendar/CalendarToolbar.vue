<script setup lang="ts">
import { Button } from '@/components/ui/button'

type CalendarMode = 'list' | 'day' | 'week' | 'month'

defineProps<{
  title: string
  mode: CalendarMode
}>()

const emit = defineEmits<{
  previous: []
  next: []
  'mode-change': [mode: CalendarMode]
}>()

const modes: { label: string; value: CalendarMode }[] = [
  { label: 'List', value: 'list' },
  { label: 'Day', value: 'day' },
  { label: 'Week', value: 'week' },
  { label: 'Month', value: 'month' },
]
</script>

<template>
  <div class="flex flex-col gap-3 rounded-lg border bg-card p-4 text-card-foreground sm:flex-row sm:items-center sm:justify-between">
    <div class="flex items-center gap-2">
      <Button type="button" variant="outline" size="sm" @click="emit('previous')">
        Prev
      </Button>

      <h2 class="min-w-0 text-lg font-medium">
        {{ title }}
      </h2>

      <Button type="button" variant="outline" size="sm" @click="emit('next')">
        Next
      </Button>
    </div>

    <div class="flex rounded-md border p-1">
      <button
        v-for="item in modes"
        :key="item.value"
        type="button"
        class="rounded-sm px-3 py-1.5 text-sm transition"
        :class="
          mode === item.value
            ? 'bg-primary text-primary-foreground'
            : 'text-muted-foreground hover:bg-accent hover:text-accent-foreground'
        "
        @click="emit('mode-change', item.value)"
      >
        {{ item.label }}
      </button>
    </div>
  </div>
</template>