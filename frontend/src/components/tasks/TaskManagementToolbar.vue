<script setup lang="ts">
import { Search } from '@lucide/vue'

import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import type { TaskStatusFilter } from '@/types/task-filters'

const props = defineProps<{
  search: string
  status: TaskStatusFilter
}>()

const emit = defineEmits<{
  'update:search': [value: string]
  'update:status': [value: TaskStatusFilter]
}>()

const statusOptions: Array<{ value: TaskStatusFilter; label: string }> = [
  { value: 'all', label: 'All' },
  { value: 'active', label: 'Active' },
  { value: 'done', label: 'Done' },
  { value: 'skipped', label: 'Skipped' },
]

function updateSearch(value: string | number) {
  emit('update:search', String(value))
}
</script>

<template>
  <section class="rounded-lg border bg-card p-4 text-card-foreground shadow-xs">
    <div class="grid gap-3 lg:grid-cols-[1fr_auto] lg:items-center">
      <div class="relative">
        <Search class="pointer-events-none absolute left-3 top-1/2 size-4 -translate-y-1/2 text-muted-foreground" />

        <Input
          :model-value="props.search"
          class="pl-9"
          placeholder="Search tasks"
          @update:model-value="updateSearch"
        />
      </div>

      <div class="flex flex-wrap gap-2">
        <Button
          v-for="option in statusOptions"
          :key="option.value"
          type="button"
          size="sm"
          :variant="props.status === option.value ? 'default' : 'outline'"
          @click="emit('update:status', option.value)"
        >
          {{ option.label }}
        </Button>
      </div>
    </div>
  </section>
</template>