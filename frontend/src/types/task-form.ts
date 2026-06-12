import type { Task } from '@/types/homebase'

export interface TaskFormValues {
  title: string
  notes: string
  plannedDate: string
  scheduledStart: string
  scheduledEnd: string
  dueAt: string
  categoryId: number | null
  tagIds: number[]
  routineIds: number[]
}

export interface TaskFormSubmitPayload {
  title: string
  notes: string | null
  plannedDate: string | null
  scheduledStart: string | null
  scheduledEnd: string | null
  dueAt: string | null
  categoryId: number | null
  tagIds: number[]
  routineIds: number[]
}

export function createEmptyTaskFormValues(
  overrides: Partial<TaskFormValues> = {},
): TaskFormValues {
  const values = {
    title: '',
    notes: '',
    plannedDate: '',
    scheduledStart: '',
    scheduledEnd: '',
    dueAt: '',
    categoryId: null,
    tagIds: [],
    routineIds: [],
    ...overrides,
  }

  return {
    ...values,
    tagIds: values.tagIds ?? [],
    routineIds: values.routineIds ?? [],
  }
}

export function createTaskFormValuesFromTask(
  task: Task,
  links: { tagIds?: number[]; routineIds?: number[] } = {},
): TaskFormValues {
  return {
    title: task.title,
    notes: task.notes ?? '',
    plannedDate: task.plannedDate ?? '',
    scheduledStart: task.scheduledStart ?? '',
    scheduledEnd: task.scheduledEnd ?? '',
    dueAt: task.dueAt ?? '',
    categoryId: task.categoryId,
    tagIds: links.tagIds ?? [],
    routineIds: links.routineIds ?? [],
  }
}

export function createTaskFormSubmitPayload(
  form: TaskFormValues,
): TaskFormSubmitPayload {
  return {
    title: form.title.trim(),
    notes: form.notes.trim() || null,
    plannedDate: form.plannedDate || null,
    scheduledStart: form.scheduledStart || null,
    scheduledEnd: form.scheduledEnd || null,
    dueAt: form.dueAt || null,
    categoryId: form.categoryId,
    tagIds: form.tagIds,
    routineIds: form.routineIds,
  }
}