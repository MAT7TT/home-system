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

const props = defineProps<{
  open: boolean
  defaultPlannedDate: string | null
}>()

const emit = defineEmits<{
  'update:open': [open: boolean]
  create: [
    payload: {
        title: string
        notes?: string | null
        categoryId?: number | null
        plannedDate?: string | null
        scheduledStart?: string | null
        scheduledEnd?: string | null
        dueAt?: string | null
    },
  ]
}>()

const title = ref('')
const notes = ref('')
const plannedDate = ref('')
const scheduledStart = ref('')
const scheduledEnd = ref('')
const dueAt = ref('')
const categoryId = ref<number | null>(null)

watch(
  () => props.open,
  (open) => {
    if (open) {
      title.value = ''
      notes.value = ''
      categoryId.value = null
      plannedDate.value = props.defaultPlannedDate ?? ''
      scheduledStart.value = ''
      scheduledEnd.value = ''
      dueAt.value = ''
    }
  },
)

function createTask() {
  const trimmedTitle = title.value.trim()

  if (!trimmedTitle) {
    return
  }

  emit('create', {
    title: trimmedTitle,
    notes: notes.value.trim() || null,
    categoryId: categoryId.value,
    plannedDate: plannedDate.value || null,
    scheduledStart: scheduledStart.value || null,
    scheduledEnd: scheduledEnd.value || null,
    dueAt: dueAt.value || null,
  })
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
          <Input id="new-task-title" v-model="title" placeholder="Task title" />
        </div>

        <div class="space-y-2">
          <Label for="new-task-notes">Notes</Label>
          <Textarea id="new-task-notes" v-model="notes" rows="4" />
        </div>

        <div class="space-y-2">
          <Label for="new-task-planned-date">Planned date</Label>
          <Input id="new-task-planned-date" v-model="plannedDate" type="date" />
        </div>

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