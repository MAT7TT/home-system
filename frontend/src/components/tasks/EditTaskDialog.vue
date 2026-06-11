<script setup lang="ts">
import { ref, watch } from 'vue'

import { Button } from '@/components/ui/button'
import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Textarea } from '@/components/ui/textarea'
import TaskCategoryField from '@/components/tasks/TaskCategoryField.vue'
import TaskScheduleFields from '@/components/tasks/TaskScheduleFields.vue'
import type { Category, Task } from '@/types/homebase'
import {
  createEmptyTaskFormValues,
  createTaskFormSubmitPayload,
  createTaskFormValuesFromTask,
  type TaskFormSubmitPayload,
  type TaskFormValues,
} from '@/types/task-form'

const props = defineProps<{
  task: Task | null
  open: boolean
  categories: Category[]
}>()

const emit = defineEmits<{
  'update:open': [open: boolean]
  save: [payload: TaskFormSubmitPayload & { id: number }]
}>()

const form = ref<TaskFormValues>(createEmptyTaskFormValues())

watch(
  () => props.task,
  (task) => {
    form.value = task ? createTaskFormValuesFromTask(task) : createEmptyTaskFormValues()
  },
  { immediate: true },
)

function saveTask() {
  if (!props.task) {
    return
  }

  const payload = createTaskFormSubmitPayload(form.value)

  if (!payload.title) {
    return
  }

  emit('save', {
    id: props.task.id,
    ...payload,
  })
}
</script>

<template>
  <Dialog :open="open" @update:open="emit('update:open', $event)">
    <DialogContent>
      <DialogHeader>
        <DialogTitle>Edit task</DialogTitle>
      </DialogHeader>

      <form class="space-y-4" @submit.prevent="saveTask">
        <div class="space-y-2">
          <Label for="task-title">Title</Label>
          <Input id="task-title" v-model="form.title" />
        </div>

        <div class="space-y-2">
          <Label for="task-notes">Notes</Label>
          <Textarea id="task-notes" v-model="form.notes" rows="4" />
        </div>

        <TaskScheduleFields
          id-prefix="task"
          v-model:planned-date="form.plannedDate"
          v-model:scheduled-start="form.scheduledStart"
          v-model:scheduled-end="form.scheduledEnd"
          v-model:due-at="form.dueAt"
        />

        <TaskCategoryField
          id="task-category"
          v-model="form.categoryId"
          :categories="categories"
        />

        <DialogFooter>
          <Button type="button" variant="outline" @click="emit('update:open', false)">
            Cancel
          </Button>
          <Button type="submit">Save</Button>
        </DialogFooter>
      </form>
    </DialogContent>
  </Dialog>
</template>