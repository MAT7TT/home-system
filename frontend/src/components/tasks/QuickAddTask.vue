<script setup lang="ts">
import { ref } from 'vue'

import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'

const emit = defineEmits<{
  create: [payload: { title: string; plannedDate: string | null }]
}>()

const title = ref('')
const plannedDate = ref('')

function submitTask() {
  const trimmedTitle = title.value.trim()

  if (!trimmedTitle) {
    return
  }

  emit('create', {
    title: trimmedTitle,
    plannedDate: plannedDate.value || null,
  })

  title.value = ''
  plannedDate.value = ''
}
</script>

<template>
  <form class="rounded-lg border bg-card p-4 text-card-foreground" @submit.prevent="submitTask">
    <h2 class="text-lg font-medium">Quick Add</h2>

    <div class="mt-3 grid gap-3 sm:grid-cols-[1fr_160px_auto]">
      <Input v-model="title" placeholder="Add a task..." />

      <Input v-model="plannedDate" type="date" />

      <Button type="submit">Add</Button>
    </div>
  </form>
</template>