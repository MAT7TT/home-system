export type TaskStatus = 'active' | 'done' | 'skipped' | 'cancelled'

export interface Category {
  id: number
  name: string
  colour: string | null
  sortOrder: number
  active: boolean
  createdAt: string
  updatedAt: string
}

export interface Task {
  id: number
  title: string
  notes: string | null
  status: TaskStatus
  categoryId: number | null
  plannedDate: string | null
  scheduledStart: string | null
  scheduledEnd: string | null
  dueAt: string | null
  completedAt: string | null
  skippedAt: string | null
  skipReason: string | null
  createdAt: string
  updatedAt: string
}

export interface Routine {
  id: number
  name: string
  notes: string | null
  categoryId: number | null
  routineType: string
  targetAmount: number
  targetUnit: string
  period: string
  startDate: string
  endDate: string | null
  active: boolean
  createdAt: string
  updatedAt: string
}

export interface RoutineProgress {
  routineId: number
  name: string
  routineType: string
  targetAmount: number
  completedAmount: number
  targetUnit: string
  period: string
}

export interface CreateTaskRequest {
  title: string
  notes?: string | null
  categoryId?: number | null
  plannedDate?: string | null
  scheduledStart?: string | null
  scheduledEnd?: string | null
  dueAt?: string | null
}

export interface UpdateTaskRequest {
  title: string
  notes?: string | null
  categoryId?: number | null
  plannedDate?: string | null
  scheduledStart?: string | null
  scheduledEnd?: string | null
  dueAt?: string | null
}

export interface SkipTaskRequest {
  reason?: string | null
}