<script setup lang="ts">
import { computed } from 'vue'

import { Button } from '@/components/ui/button'
import { Checkbox } from '@/components/ui/checkbox'
import type { Task } from '@/types/homebase'

const props = defineProps<{
  task: Task
}>()

const emit = defineEmits<{
  complete: [taskId: number]
  edit: [task: Task]
  skip: [task: Task]
}>()

const isDone = computed(() => props.task.status === 'done')
const isSkipped = computed(() => props.task.status === 'skipped')
const isActive = computed(() => props.task.status === 'active')
</script>

<template>
  <li
    class="flex items-center gap-3 rounded-md border px-3 py-2 text-sm"
    :class="{
      'bg-muted/40 text-muted-foreground': isDone || isSkipped,
    }"
  >
    <Checkbox
      :model-value="isDone"
      :disabled="isDone || isSkipped"
      @update:model-value="emit('complete', task.id)"
    />

    <div class="min-w-0 flex-1">
      <div
        class="truncate"
        :class="{
          'line-through': isDone,
        }"
      >
        {{ task.title }}
      </div>

      <div v-if="isSkipped" class="mt-1 text-xs text-muted-foreground">
        Skipped<span v-if="task.skipReason">: {{ task.skipReason }}</span>
      </div>
    </div>

    <div class="flex items-center gap-1">
      <Button type="button" variant="ghost" size="sm" @click="emit('edit', task)">
        Edit
      </Button>

      <Button
        v-if="isActive"
        type="button"
        variant="ghost"
        size="sm"
        @click="emit('skip', task)"
      >
        Skip
      </Button>
    </div>
  </li>
</template>