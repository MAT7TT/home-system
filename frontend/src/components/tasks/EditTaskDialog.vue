<script setup lang="ts">
import { ref, watch } from 'vue'

import { Button } from '@/components/ui/button'
import {
  Dialog,
  DialogFooter,
  DialogHeader,
  DialogScrollContent,
  DialogTitle,
} from '@/components/ui/dialog'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Textarea } from '@/components/ui/textarea'
import TaskClassificationFields from '@/components/tasks/TaskClassificationFields.vue'
import TaskScheduleFields from '@/components/tasks/TaskScheduleFields.vue'
import type { Category, Routine, Tag, Task } from '@/types/homebase'
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
  tags: Tag[]
  routines: Routine[]
  selectedTagIds: number[]
  selectedRoutineIds: number[]
}>()

const emit = defineEmits<{
  'update:open': [open: boolean]
  save: [payload: TaskFormSubmitPayload & { id: number }]
  delete: [id: number]
}>()

const form = ref<TaskFormValues>(createEmptyTaskFormValues())

watch(
  () => [
    props.task,
    props.selectedTagIds,
    props.selectedRoutineIds,
  ] as const,
  ([task, selectedTagIds, selectedRoutineIds]) => {
    form.value = task
      ? createTaskFormValuesFromTask(task, {
          tagIds: selectedTagIds,
          routineIds: selectedRoutineIds,
        })
      : createEmptyTaskFormValues()
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

function deleteTask() {
  if (!props.task) {
    return
  }

  emit('delete', props.task.id)
}
</script>

<template>
  <Dialog :open="open" @update:open="emit('update:open', $event)">
    <DialogScrollContent class="max-w-3xl">
      <DialogHeader>
        <DialogTitle>Edit task</DialogTitle>
      </DialogHeader>

      <form class="space-y-5" @submit.prevent="saveTask">
        <section class="rounded-lg border bg-card text-card-foreground shadow-xs">
          <div class="border-b px-4 py-3">
            <h3 class="text-sm font-medium">Task details</h3>
            <p class="text-xs text-muted-foreground">Name and notes for the task</p>
          </div>

          <div class="space-y-4 p-4">
            <div class="space-y-2">
              <Label for="task-title">Title</Label>
              <Input id="task-title" v-model="form.title" />
            </div>

            <div class="space-y-2">
              <Label for="task-notes">Notes</Label>
              <Textarea
                id="task-notes"
                v-model="form.notes"
                rows="4"
                placeholder="Optional context, links or reminders"
              />
            </div>
          </div>
        </section>

        <TaskScheduleFields
          id-prefix="task"
          v-model:planned-date="form.plannedDate"
          v-model:scheduled-start="form.scheduledStart"
          v-model:scheduled-end="form.scheduledEnd"
          v-model:due-at="form.dueAt"
        />

        <TaskClassificationFields
          id-prefix="task"
          v-model:category-id="form.categoryId"
          v-model:tag-ids="form.tagIds"
          v-model:routine-ids="form.routineIds"
          :categories="categories"
          :tags="tags"
          :routines="routines"
        />

        <DialogFooter class="gap-2 border-t pt-4 sm:justify-between">
          <Button type="button" variant="destructive" @click="deleteTask">
            Delete
          </Button>

          <div class="flex gap-2">
            <Button type="button" variant="outline" @click="emit('update:open', false)">
              Cancel
            </Button>
            <Button type="submit">Save</Button>
          </div>
        </DialogFooter>
      </form>
    </DialogScrollContent>
  </Dialog>
</template>