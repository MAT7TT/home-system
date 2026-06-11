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
import TaskScheduleFields from '@/components/tasks/TaskScheduleFields.vue'
import {
  createEmptyTaskFormValues,
  createTaskFormSubmitPayload,
  type TaskFormSubmitPayload,
  type TaskFormValues,
} from '@/types/task-form'

const props = defineProps<{
  open: boolean
  defaultPlannedDate: string | null
}>()

const emit = defineEmits<{
  'update:open': [open: boolean]
  'create-task': [payload: TaskFormSubmitPayload]
}>()

const form = ref<TaskFormValues>(createEmptyTaskFormValues())

watch(
  () => props.open,
  (open) => {
    if (open) {
      form.value = createEmptyTaskFormValues({
        plannedDate: props.defaultPlannedDate ?? '',
      })
    }
  },
)

function createTask() {
  const payload = createTaskFormSubmitPayload(form.value)

  if (!payload.title) {
    return
  }

  emit('create-task', payload)
}
</script>

<template>
  <Dialog :open="open" @update:open="emit('update:open', $event)">
    <DialogContent>
      <DialogHeader>
        <DialogTitle>Add task</DialogTitle>
      </DialogHeader>

      <form class="space-y-4" @submit.prevent="createTask">
        <div class="space-y-2">
          <Label for="new-task-title">Title</Label>
          <Input id="new-task-title" v-model="form.title" placeholder="Task title" />
        </div>

        <div class="space-y-2">
          <Label for="new-task-notes">Notes</Label>
          <Textarea id="new-task-notes" v-model="form.notes" rows="4" />
        </div>

        <TaskScheduleFields
          id-prefix="new-task"
          v-model:planned-date="form.plannedDate"
          v-model:scheduled-start="form.scheduledStart"
          v-model:scheduled-end="form.scheduledEnd"
          v-model:due-at="form.dueAt"
        />

        <DialogFooter>
          <Button type="button" variant="outline" @click="emit('update:open', false)">
            Cancel
          </Button>
          <Button type="submit">Add task</Button>
        </DialogFooter>
      </form>
    </DialogContent>
  </Dialog>
</template>