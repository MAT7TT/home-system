<script setup lang="ts">
import { Boxes, Check, Tags } from '@lucide/vue'

import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import { Label } from '@/components/ui/label'
import TaskCategoryField from '@/components/tasks/TaskCategoryField.vue'
import type { Category, Routine, Tag } from '@/types/homebase'

const props = defineProps<{
  idPrefix: string
  categoryId: number | null
  tagIds: number[]
  routineIds: number[]
  categories: Category[]
  tags: Tag[]
  routines: Routine[]
}>()

const emit = defineEmits<{
  'update:categoryId': [value: number | null]
  'update:tagIds': [value: number[]]
  'update:routineIds': [value: number[]]
}>()

function toggleId(values: number[], id: number) {
  return values.includes(id)
    ? values.filter((value) => value !== id)
    : [...values, id]
}

function toggleTag(tagId: number) {
  emit('update:tagIds', toggleId(props.tagIds, tagId))
}

function toggleRoutine(routineId: number) {
  emit('update:routineIds', toggleId(props.routineIds, routineId))
}
</script>

<template>
  <section class="rounded-lg border bg-card text-card-foreground shadow-xs">
    <div class="flex items-center gap-3 border-b px-4 py-3">
      <div class="flex size-9 items-center justify-center rounded-md bg-primary/10 text-primary">
        <Boxes class="size-4" />
      </div>

      <div>
        <h3 class="text-sm font-medium">Organisation</h3>
        <p class="text-xs text-muted-foreground">Category, tags and routine links</p>
      </div>
    </div>

    <div class="space-y-5 p-4">
      <TaskCategoryField
        :id="`${idPrefix}-category`"
        :model-value="categoryId"
        :categories="categories"
        @update:model-value="emit('update:categoryId', $event)"
      />

      <div class="space-y-2">
        <div class="flex items-center justify-between gap-3">
          <Label class="inline-flex items-center gap-2">
            <Tags class="size-4 text-muted-foreground" />
            Tags
          </Label>

          <Badge variant="secondary">{{ tagIds.length }} selected</Badge>
        </div>

        <div
          v-if="tags.length === 0"
          class="rounded-md border border-dashed bg-background p-3 text-sm text-muted-foreground"
        >
          No tags yet.
        </div>

        <div v-else class="flex flex-wrap gap-2">
          <Button
            v-for="tag in tags"
            :key="tag.id"
            type="button"
            size="sm"
            :variant="tagIds.includes(tag.id) ? 'default' : 'outline'"
            class="rounded-full"
            @click="toggleTag(tag.id)"
          >
            <span
              v-if="tag.color"
              class="size-2 rounded-full border border-current/20"
              :style="{ backgroundColor: tag.color }"
            />

            {{ tag.name }}

            <Check v-if="tagIds.includes(tag.id)" class="size-3.5" />
          </Button>
        </div>
      </div>

      <div class="space-y-2">
        <div class="flex items-center justify-between gap-3">
          <Label>Routines</Label>
          <Badge variant="secondary">{{ routineIds.length }} selected</Badge>
        </div>

        <div
          v-if="routines.length === 0"
          class="rounded-md border border-dashed bg-background p-3 text-sm text-muted-foreground"
        >
          No routines yet.
        </div>

        <div v-else class="grid gap-2">
          <button
            v-for="routine in routines"
            :key="routine.id"
            type="button"
            class="flex min-w-0 items-center gap-3 rounded-md border bg-background px-3 py-2 text-left text-sm transition hover:bg-accent hover:text-accent-foreground"
            :class="{
              'border-primary bg-primary/5 text-foreground': routineIds.includes(routine.id),
            }"
            @click="toggleRoutine(routine.id)"
          >
            <span
              class="flex size-5 shrink-0 items-center justify-center rounded-full border"
              :class="{
                'border-primary bg-primary text-primary-foreground': routineIds.includes(routine.id),
                'border-muted-foreground/30': !routineIds.includes(routine.id),
              }"
            >
              <Check v-if="routineIds.includes(routine.id)" class="size-3" />
            </span>

            <span class="min-w-0 flex-1">
              <span class="block truncate font-medium">{{ routine.name }}</span>
              <span class="block truncate text-xs text-muted-foreground">
                {{ routine.targetAmount }} {{ routine.targetUnit }} / {{ routine.period }}
              </span>
            </span>
          </button>
        </div>
      </div>
    </div>
  </section>
</template>