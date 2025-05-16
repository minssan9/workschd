# Build a WorkSchd Employee Scheduling App

Create a full-stack employee scheduling application with the following specifications:

## App Overview
Build a modern web application called "WorkSchd" that helps retail/service managers schedule staff with irregular hours. Focus on creating a clean, intuitive interface with essential MVP features.

## Stack Requirements
- **Frontend**: Vue 3 with Composition API, Pinia for state management
- **Backend**: Spring Boot 3 with Java 17, MySQL database 
- **Authentication**: OAuth2 with Google/Kakao login

## Core Features to Implement

1. **User Authentication**
   - OAuth login with Google
   - Role-based access (Manager/Employee)

2. **Store Management**
   - Register and manage store information
   - Store hours configuration

3. **Employee Management**
   - Add/edit employee profiles with contact info
   - Set employee hourly rates and availability

4. **Interactive Schedule Builder**
   - Weekly calendar view
   - Drag-and-drop shift creation
   - Color-coding by employee or shift type
   - Validate against availability constraints

5. **Attendance Tracking**
   - Mobile-friendly clock-in/out interface
   - Track actual vs. scheduled hours

6. **Basic Reporting**
   - Scheduled vs. actual hours worked
   - Basic labor cost calculations

## Key Pages to Create

1. **Login Page**
   - OAuth login buttons
   - Simple, clean design

2. **Dashboard**
   - Overview of current week's schedule
   - Quick access to main functions
   - Notifications for scheduling conflicts

3. **Schedule Editor**
   - Interactive calendar interface
   - Employee list sidebar
   - Shift creation/editing controls

4. **Employee Directory**
   - List view of all employees
   - Search and filter options
   - Add/edit employee forms

5. **Attendance Tracker**
   - Daily view of clock-ins/outs
   - Manual time entry for managers
   - Status indicators (on time, late, absent)

## Data Models

- **User**: id, name, email, role, storeIds
- **Store**: id, name, address, operatingHours
- **Employee**: id, userId, storeId, hourlyRate, contactInfo, availability
- **Schedule**: id, storeId, weekStartDate
- **Shift**: id, scheduleId, employeeId, startTime, endTime
- **Attendance**: id, employeeId, shiftId, clockInTime, clockOutTime

## UI Requirements
- Responsive design that works on desktop and mobile
- Clean, minimalist interface with intuitive controls
- Color-coded visual elements for schedule clarity
- Accessible design with proper contrast and readable fonts

Please implement this application with a focus on core functionality first, using best practices for both frontend and backend development. 