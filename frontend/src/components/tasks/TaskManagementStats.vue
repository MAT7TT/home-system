<script setup lang="ts">
defineProps<{
  total: number
  active: number
  done: number
  skipped: number
}>()
</script>

<template>
  <section class="grid gap-3 sm:grid-cols-2 lg:grid-cols-4">
    <div class="rounded-lg border bg-card p-4 text-card-foreground shadow-xs">
      <p class="text-sm text-muted-foreground">All tasks</p>
      <p class="mt-2 text-2xl font-semibold tracking-tight">{{ total }}</p>
    </div>

    <div class="rounded-lg border bg-card p-4 text-card-foreground shadow-xs">
      <p class="text-sm text-muted-foreground">Active</p>
      <p class="mt-2 text-2xl font-semibold tracking-tight">{{ active }}</p>
    </div>

    <div class="rounded-lg border bg-card p-4 text-card-foreground shadow-xs">
      <p class="text-sm text-muted-foreground">Done</p>
      <p class="mt-2 text-2xl font-semibold tracking-tight">{{ done }}</p>
    </div>

    <div class="rounded-lg border bg-card p-4 text-card-foreground shadow-xs">
      <p class="text-sm text-muted-foreground">Skipped</p>
      <p class="mt-2 text-2xl font-semibold tracking-tight">{{ skipped }}</p>
    </div>
  </section>
</template>