<script setup lang="ts">
import { RouterLink } from 'vue-router'

const navigationItems = [
  { label: 'Today', to: '/' },
  { label: 'Tasks', to: '/tasks' },
  { label: 'Routines', to: '/routines' },
  { label: 'Settings', to: '/settings' },
]
</script>

<template>
  <div class="min-h-screen bg-background text-foreground">
    <header class="border-b bg-card text-card-foreground">
      <div class="mx-auto flex w-full max-w-6xl items-center justify-between gap-4 px-4 py-4">
        <RouterLink to="/" class="text-sm font-semibold tracking-tight">
          Homebase
        </RouterLink>

        <nav class="hidden items-center gap-1 sm:flex">
          <RouterLink
            v-for="item in navigationItems"
            :key="item.to"
            :to="item.to"
            class="rounded-md px-3 py-2 text-sm text-muted-foreground transition hover:bg-accent hover:text-accent-foreground"
            active-class="bg-accent text-accent-foreground"
          >
            {{ item.label }}
          </RouterLink>
        </nav>
      </div>
    </header>

    <main class="mx-auto w-full max-w-6xl px-4 py-6">
      <slot />
    </main>
  </div>
</template>