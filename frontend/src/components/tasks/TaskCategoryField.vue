<script setup lang="ts">
import type { AcceptableValue } from 'reka-ui'

import { Label } from '@/components/ui/label'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'
import type { Category } from '@/types/homebase'

defineProps<{
  id: string
  modelValue: number | null
  categories: Category[]
}>()

const emit = defineEmits<{
  'update:modelValue': [value: number | null]
}>()

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
    <Label :for="id">Category</Label>

    <Select
      :model-value="modelValue === null ? 'none' : String(modelValue)"
      @update:model-value="updateCategory"
    >
      <SelectTrigger :id="id">
        <SelectValue placeholder="Choose category" />
      </SelectTrigger>

      <SelectContent>
        <SelectItem value="none">No category</SelectItem>

        <SelectItem
          v-for="category in categories"
          :key="category.id"
          :value="String(category.id)"
        >
          {{ category.name }}
        </SelectItem>
      </SelectContent>
    </Select>
  </div>
</template>