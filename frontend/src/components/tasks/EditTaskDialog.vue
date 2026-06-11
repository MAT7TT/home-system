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
import type { Task } from '@/types/homebase'

const props = defineProps<{
  task: Task | null
  open: boolean
}>()

const emit = defineEmits<{
  'update:open': [open: boolean]
  save: [
    payload: {
      id: number
      title: string
      notes: string | null
      plannedDate: string | null
      scheduledStart: string | null
      scheduledEnd: string | null
      dueAt: string | null
      categoryId: number | null
    },
  ]
}>()

const title = ref('')
const notes = ref('')
const plannedDate = ref('')

watch(
  () => props.task,
  (task) => {
    title.value = task?.title ?? ''
    notes.value = task?.notes ?? ''
    plannedDate.value = task?.plannedDate ?? ''
  },
  { immediate: true },
)

function saveTask() {
  if (!props.task) {
    return
  }

  const trimmedTitle = title.value.trim()

  if (!trimmedTitle) {
    return
  }

  emit('save', {
    id: props.task.id,
    title: trimmedTitle,
    notes: notes.value.trim() || null,
    plannedDate: plannedDate.value || null,
    scheduledStart: props.task.scheduledStart,
    scheduledEnd: props.task.scheduledEnd,
    dueAt: props.task.dueAt,
    categoryId: props.task.categoryId,
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
          <Input id="task-title" v-model="title" />
        </div>

        <div class="space-y-2">
          <Label for="task-notes">Notes</Label>
          <Textarea id="task-notes" v-model="notes" rows="4" />
        </div>

        <div class="space-y-2">
          <Label for="task-planned-date">Planned date</Label>
          <Input id="task-planned-date" v-model="plannedDate" type="date" />
        </div>

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