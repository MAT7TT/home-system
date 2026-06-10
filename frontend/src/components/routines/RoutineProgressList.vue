<script setup lang="ts">
import RoutineProgressCircle from '@/components/routines/RoutineProgressCircle.vue'
import type { RoutineProgress } from '@/types/homebase'

defineProps<{
  routines: RoutineProgress[]
}>()
</script>

<template>
  <aside class="rounded-lg border bg-card p-4 text-card-foreground">
    <h2 class="text-lg font-medium">Routine Progress</h2>

    <div v-if="routines.length === 0" class="mt-3 text-sm text-muted-foreground">
      No routines yet.
    </div>

    <div v-else class="mt-3 grid gap-3">
      <RoutineProgressCircle
        v-for="routine in routines"
        :key="routine.routineId"
        :label="routine.name"
        :completed="routine.completedAmount"
        :target="routine.targetAmount"
        :unit="routine.targetUnit"
      />
    </div>
  </aside>
</template>