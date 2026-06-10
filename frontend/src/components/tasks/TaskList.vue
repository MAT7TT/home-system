<script setup lang="ts">
import TaskListItem from '@/components/tasks/TaskListItem.vue'
import type { Task } from '@/types/homebase'

defineProps<{
  title: string
  emptyText: string
  tasks: Task[]
}>()

const emit = defineEmits<{
  complete: [taskId: number]
}>()
</script>

<template>
  <section class="rounded-lg border bg-card p-4 text-card-foreground">
    <h2 class="text-lg font-medium">{{ title }}</h2>

    <div v-if="tasks.length === 0" class="mt-3 text-sm text-muted-foreground">
      {{ emptyText }}
    </div>

    <ul v-else class="mt-3 space-y-2">
      <TaskListItem
        v-for="task in tasks"
        :key="task.id"
        :task="task"
        @complete="emit('complete', $event)"
      />
    </ul>
  </section>
</template>