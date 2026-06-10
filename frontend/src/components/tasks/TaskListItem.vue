<script setup lang="ts">
import { Checkbox } from '@/components/ui/checkbox'
import type { Task } from '@/types/homebase'

defineProps<{
  task: Task
}>()

const emit = defineEmits<{
  complete: [taskId: number]
}>()
</script>

<template>
  <li class="flex items-center gap-3 rounded-md border px-3 py-2 text-sm">
    <Checkbox @update:model-value="emit('complete', task.id)" />
    <span>{{ task.title }}</span>
  </li>
</template>