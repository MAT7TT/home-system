<script setup lang="ts">
const props = defineProps<{
  label: string
  completed: number
  target: number
  unit: string
}>()

const radius = 28
const strokeWidth = 6
const circumference = 2 * Math.PI * radius

const percent = Math.min(100, Math.round((props.completed / props.target) * 100))
const strokeDashoffset = circumference - (percent / 100) * circumference
</script>

<template>
  <div class="flex items-center gap-3 rounded-md border px-3 py-3">
    <div class="relative size-16 shrink-0">
      <svg class="size-16 -rotate-90" viewBox="0 0 72 72" aria-hidden="true">
        <circle
          class="stroke-muted"
          cx="36"
          cy="36"
          :r="radius"
          fill="none"
          :stroke-width="strokeWidth"
        />
        <circle
          class="stroke-primary"
          cx="36"
          cy="36"
          :r="radius"
          fill="none"
          stroke-linecap="round"
          :stroke-width="strokeWidth"
          :stroke-dasharray="circumference"
          :stroke-dashoffset="strokeDashoffset"
        />
      </svg>

      <div class="absolute inset-0 flex items-center justify-center text-xs font-medium">
        {{ percent }}%
      </div>
    </div>

    <div class="min-w-0">
      <div class="truncate text-sm font-medium">{{ label }}</div>
      <div class="text-sm text-muted-foreground">
        {{ completed }} / {{ target }} {{ unit }}
      </div>
    </div>
  </div>
</template>