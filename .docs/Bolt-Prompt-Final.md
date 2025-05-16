# WorkSchd: Employee Scheduling Web App

Create a full-stack employee scheduling application named "WorkSchd" with Vue 3 (frontend) and Spring Boot 3 (backend).

## Core Functionality
Build an app that helps store managers create work schedules for employees with irregular hours, track attendance, and calculate labor costs.

## Technology Stack
- Frontend: Vue 3 (Composition API), Pinia, responsive design
- Backend: Spring Boot 3 (Java 17), MySQL, REST API
- Authentication: OAuth2 (Google/Kakao)

## Key Features

### Authentication System
- Google OAuth2 login
- Role-based permissions (Manager/Employee)
- Secure JWT token handling

### Store Management
- Store registration with basic info (name, address, operating hours)
- Multi-store support for managers

### Employee Directory
- Employee profiles with contact info and hourly rates
- Availability settings (days/hours available to work)
- Max hours per week settings

### Interactive Schedule Builder
- Weekly calendar view with drag-and-drop interface
- Color-coded shifts by employee
- Conflict detection (availability, overlapping shifts)
- Schedule templates for common patterns

### Attendance Tracking
- Mobile-optimized clock-in/out interface
- Attendance status indicators (on time, late, absent)
- Actual hours vs. scheduled hours comparison

### Basic Reporting
- Weekly labor cost calculation
- Hours summary by employee
- Visualization of scheduled vs. actual hours

## Core Pages

1. Login Page - Clean design with OAuth buttons
2. Dashboard - Week overview with schedule summary and quick actions
3. Schedule Editor - Interactive calendar with employee sidebar
4. Employee Manager - Directory with add/edit forms
5. Attendance Tracker - Daily check-in status and manual override
6. Settings - Store configuration and user preferences

## Data Requirements

- Users must have roles (MANAGER or EMPLOYEE)
- Employees must be associated with at least one store
- Shifts must have start/end times and assigned employee
- Attendance records must track actual clock-in/out times
- Schedules should be organized by week

## UI/UX Priorities
- Mobile-first responsive design
- Intuitive drag-and-drop schedule creation
- Clean, minimalist interface
- Accessible color scheme with proper contrast

Focus on creating a functional MVP with clean code and intuitive user experience. 