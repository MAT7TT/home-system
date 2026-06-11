<script setup lang="ts">
import type { Task } from '@/types/homebase'

defineProps<{
  task: Task
}>()

const emit = defineEmits<{
  select: [task: Task]
}>()
</script>

<template>
  <button
    type="button"
    class="block w-full truncate rounded-sm bg-primary/10 px-1.5 py-0.5 text-left text-xs text-primary transition hover:bg-primary/20"
    @click.stop="emit('select', task)"
  >
    {{ task.title }}
  </button>
</template>