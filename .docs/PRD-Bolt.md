# WorkSchd - Employee Scheduling Application

## Overview
Build a modern scheduling application for retail/service managers who oversee staff with irregular working hours. The app should help managers create schedules, track attendance, and forecast labor costs.

## Target Users
- **Store Managers**: Need to efficiently schedule employees and track attendance
- **Employees**: Need to view their schedules and record clock-in/clock-out times
- **Business Owners**: Need insights on labor costs and scheduling efficiency

## Core Features

### 1. Authentication & Authorization
- Implement Google and Kakao OAuth2 login
- Role-based access control with admin (manager) and employee permissions
- Secure session management and token handling

### 2. Store & Employee Management
- Allow managers to register and manage multiple stores
- Manager manage employees by team
- Employee profile management with:
  - Contact information
  - Role/position
  - Hourly rates
  - Availability preferences
  - Maximum weekly hours

### 3. Interactive Schedule Creation
- Calendar view with weekly/monthly toggle
- Drag-and-drop schedule creation and modification
- Auto-scheduling based on employee availability
- Shift templates for common patterns
- Conflict detection (overlapping shifts, availability violations)
- Validate against availability constraints
#### 3-1. Task Management 
  - Manager create tasks by daily, weekly, monthly work schedule 
  - Employees request to works on each tasks
  - Manager allow to work requests of employees
  - Tasks have fields of stores, start time, end time, worker TO, remarks
 
### 4. Attendance Tracking
- Mobile-friendly clock-in/clock-out interface
- Real-time attendance monitoring
- Late arrival and early departure flagging
- Attendance history and reporting
- Track actual vs. scheduled hours

### 5. Labor Cost Forecasting
- Calculate projected labor costs based on scheduled hours
- Compare scheduled vs. actual hours worked
- Visual reports showing labor cost trends
- Budget threshold alerts

### 6. Task
- 

## Technical Requirements

### Frontend
- Vue 3 with Composition API
- Quasar Frameowrk
- Responsive design optimized for both desktop and mobile
- Interactive calendar component with drag-and-drop
- Chart.js for data visualization
- Pinia for state management
- PWA capabilities for mobile access
- path resolve src/ as @/

### Backend
- Spring Boot 3 with Java 17
- RESTful API architecture
- MySQL database
- Spring Security with OAuth2
- JWT for API authentication

## Data Model

### Key Entities
- User (admin/employee)
- Store
- Schedule
- Shift
- Attendance Record
- Availability Preference

## UI/UX Requirements
- Clean, minimalist interface prioritizing usability
- Color-coded schedule visualization
- Mobile-responsive design
- Intuitive drag-and-drop interactions
- Clear data visualization for reports

## Wireframes
- Login screen
- Dashboard/overview
- Schedule editor
- Employee management
- Attendance tracker
- Reports view

## Future Enhancements
- Automated schedule optimization
- Employee shift swapping
- Push notifications
- Advanced analytics
- Multi-language support
- Integration with payroll systems 
 