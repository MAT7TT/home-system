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
import { Label } from '@/components/ui/label'
import { Textarea } from '@/components/ui/textarea'
import type { Task } from '@/types/homebase'

const props = defineProps<{
  task: Task | null
  open: boolean
}>()

const emit = defineEmits<{
  'update:open': [open: boolean]
  skip: [
    payload: {
      id: number
      reason: string | null
    },
  ]
}>()

const reason = ref('')

watch(
  () => props.task,
  () => {
    reason.value = ''
  },
)

function skipTask() {
  if (!props.task) {
    return
  }

  emit('skip', {
    id: props.task.id,
    reason: reason.value.trim() || null,
  })
}
</script>

<template>
  <Dialog :open="open" @update:open="emit('update:open', $event)">
    <DialogContent>
      <DialogHeader>
        <DialogTitle>Skip task</DialogTitle>
      </DialogHeader>

      <form class="space-y-4" @submit.prevent="skipTask">
        <div class="space-y-2">
          <Label for="skip-reason">Reason</Label>
          <Textarea
            id="skip-reason"
            v-model="reason"
            placeholder="Optional reason..."
            rows="4"
          />
        </div>

        <DialogFooter>
          <Button type="button" variant="outline" @click="emit('update:open', false)">
            Cancel
          </Button>
          <Button type="submit" variant="secondary">Skip</Button>
        </DialogFooter>
      </form>
    </DialogContent>
  </Dialog>
</template>