<script setup lang="ts">
import { computed } from 'vue'
import type { AcceptableValue } from 'reka-ui'
import { Folder } from '@lucide/vue'

import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'
import type { Category } from '@/types/homebase'

const props = defineProps<{
  id: string
  modelValue: number | null
  categories: Category[]
}>()

const emit = defineEmits<{
  'update:modelValue': [value: number | null]
}>()

const selectedCategory = computed(() => {
  return props.categories.find((category) => category.id === props.modelValue) ?? null
})

function updateCategory(value: AcceptableValue) {
  if (value === null || value === 'none') {
    emit('update:modelValue', null)
    return
  }

  const categoryId = Number(value)

  if (!Number.isNaN(categoryId)) {
    emit('update:modelValue', categoryId)
  }
}
</script>

<template>
  <div class="space-y-2">
    <div class="flex items-center justify-between gap-3">
      <div class="inline-flex items-center gap-2 text-sm font-medium">
        <Folder class="size-4 text-muted-foreground" />
        Category
      </div>

      <span class="text-xs text-muted-foreground">One main area</span>
    </div>

    <Select
      :model-value="modelValue === null ? 'none' : String(modelValue)"
      @update:model-value="updateCategory"
    >
      <SelectTrigger
        :id="id"
        class="h-auto min-h-14 w-full justify-between rounded-lg border bg-background px-4 py-3 text-left shadow-xs transition hover:bg-accent hover:text-accent-foreground"
      >
        <div class="flex min-w-0 items-center gap-3">
          <span
            class="size-3 rounded-full border"
            :class="{ 'border-muted-foreground/40': !selectedCategory?.colour }"
            :style="{ backgroundColor: selectedCategory?.colour ?? 'transparent' }"
          />

          <div class="min-w-0">
            <SelectValue placeholder="No category" />
            <p class="text-xs text-muted-foreground">
              {{ selectedCategory ? 'Selected category' : 'Task has no category' }}
            </p>
          </div>
        </div>
      </SelectTrigger>

      <SelectContent>
        <SelectItem value="none">
          <div class="flex items-center gap-2">
            <span class="size-2 rounded-full border border-muted-foreground/40" />
            <span>No category</span>
          </div>
        </SelectItem>

        <SelectItem
          v-for="category in categories"
          :key="category.id"
          :value="String(category.id)"
        >
          <div class="flex items-center gap-2">
            <span
              class="size-2 rounded-full border border-current/20"
              :style="{ backgroundColor: category.colour ?? 'transparent' }"
            />
            <span>{{ category.name }}</span>
          </div>
        </SelectItem>
      </SelectContent>
    </Select>
  </div>
</template>