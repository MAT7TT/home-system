import type { Category, CreateTaskRequest, Routine, RoutineProgress, SkipTaskRequest, Task, UpdateTaskRequest } from '@/types/homebase'

async function request<T>(url: string, options?: RequestInit): Promise<T> {
  const response = await fetch(url, {
    headers: {
      'Content-Type': 'application/json',
      ...options?.headers,
    },
    ...options,
  })

  if (!response.ok) {
    throw new Error(`Request failed: ${response.status}`)
  }

  return response.json() as Promise<T>
}

export function getCategories(): Promise<Category[]> {
  return request<Category[]>('/api/v1/categories')
}

export function getTodayTasks(): Promise<Task[]> {
  return request<Task[]>('/api/v1/tasks/today')
}

export function getActiveTasks(): Promise<Task[]> {
  return request<Task[]>('/api/v1/tasks/active')
}

export function getTasks(): Promise<Task[]> {
  return request<Task[]>('/api/v1/tasks')
}

export function createTask(task: CreateTaskRequest): Promise<Task> {
  return request<Task>('/api/v1/tasks', {
    method: 'POST',
    body: JSON.stringify(task),
  })
}

export function updateTask(id: number, task: UpdateTaskRequest): Promise<Task> {
  return request<Task>(`/api/v1/tasks/${id}`, {
    method: 'PUT',
    body: JSON.stringify(task),
  })
}

export function skipTask(id: number, payload: SkipTaskRequest): Promise<Task> {
  return request<Task>(`/api/v1/tasks/${id}/skip`, {
    method: 'PATCH',
    body: JSON.stringify(payload),
  })
}

export function completeTask(id: number): Promise<Task> {
  return request<Task>(`/api/v1/tasks/${id}/complete`, {
    method: 'PATCH',
  })
}

export function getRoutines(): Promise<Routine[]> {
  return request<Routine[]>('/api/v1/routines')
}

export function getRoutineProgress(id: number): Promise<RoutineProgress> {
  return request<RoutineProgress>(`/api/v1/routines/${id}/progress`)
}