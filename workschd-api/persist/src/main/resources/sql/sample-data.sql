-- Insert sample accounts
INSERT INTO workschd.account (username, english_name, email, phone, incentive_percent, password, profile_image_url, is_active)
VALUES 
('john.doe', 'John Doe', 'john.doe@example.com', '1234567890', 1.0, '$2a$10$encrypted', 'https://example.com/profile1.jpg', 1),
('jane.smith', 'Jane Smith', 'jane.smith@example.com', '0987654321', 1.2, '$2a$10$encrypted', 'https://example.com/profile2.jpg', 1),
('mike.wilson', 'Mike Wilson', 'mike.wilson@example.com', '5555555555', 1.1, '$2a$10$encrypted', 'https://example.com/profile3.jpg', 1);

-- Insert account roles (1: ADMIN, 2: MANAGER, 3: EMPLOYEE)
INSERT INTO workschd.account_role (account_id, role_type, is_active)
VALUES 
(1, 1, 1),  -- John Doe as Admin
(2, 2, 1),  -- Jane Smith as Manager
(3, 3, 1);  -- Mike Wilson as Employee

-- Insert teams
INSERT INTO workschd.team (name, region, schedule_type, created_by)
VALUES 
('Downtown Team', 'Central', 'WEEKLY', 'system'),
('Uptown Team', 'North', 'WEEKLY', 'system'),
('Suburban Team', 'East', 'MONTHLY', 'system');

-- Insert account_info
INSERT INTO workschd.account_info (account_id, branch_id, phone_number, email, name, preferred_branch_id, is_manager)
VALUES 
(1, 1, '1234567890', 'john.doe@example.com', 'John Doe', 1, 1),
(2, 1, '0987654321', 'jane.smith@example.com', 'Jane Smith', 1, 1),
(3, 1, '5555555555', 'mike.wilson@example.com', 'Mike Wilson', 1, 0);

-- Insert stores
INSERT INTO workschd.store (name, address, region, branch_id, created_by)
VALUES 
('Downtown Store', '123 Main St', 'Central', 1, 'system'),
('Uptown Store', '456 High St', 'North', 2, 'system'),
('Suburban Store', '789 East St', 'East', 3, 'system');

-- Insert tasks
INSERT INTO workschd.task (branch_id, store_id, title, description, task_datetime, start_time, end_time, daily_wage, created_by)
VALUES 
(1, 1, 'Morning Shift', 'Regular morning operations', '2024-03-20 09:00:00', '09:00:00', '17:00:00', 150.00, 'system'),
(1, 1, 'Evening Shift', 'Regular evening operations', '2024-03-20 17:00:00', '17:00:00', '23:00:00', 180.00, 'system'),
(2, 2, 'Full Day Shift', 'Full day operations', '2024-03-21 08:00:00', '08:00:00', '20:00:00', 250.00, 'system');

-- Insert task assignments
INSERT INTO workschd.task_employee (task_id, employee_id, account_id, created_by)
VALUES 
(1, 1, 1, 'system'),
(2, 2, 2, 'system'),
(3, 3, 3, 'system');

-- Insert attendance records
INSERT INTO workschd.attendance_records (employee_id, branch_id, attendance_date, day_of_week, start_time, end_time)
VALUES 
(1, 1, '2024-03-20', 'Wednesday', '09:00:00', '17:00:00'),
(2, 1, '2024-03-20', 'Wednesday', '17:00:00', '23:00:00'),
(3, 2, '2024-03-21', 'Thursday', '08:00:00', '20:00:00');

-- Insert team members
INSERT INTO workschd.team_member (account_id, team_id, is_active, created_at, created_by)
VALUES 
(1, 1, 1, NOW(), 'system'),
(2, 1, 1, NOW(), 'system'),
(3, 2, 1, NOW(), 'system');

-- Insert some translations
INSERT INTO workschd.translations (txt_key, en, ko, created_by)
VALUES 
('welcome.message', 'Welcome to WorkSchd', '워크스케줄에 오신 것을 환영합니다', 'system'),
('menu.dashboard', 'Dashboard', '대시보드', 'system'),
('menu.schedule', 'Schedule', '스케줄', 'system');

-- Insert some unavailable dates
INSERT INTO workschd.unavailable_dates (account_id, date, off_date, created_by)
VALUES 
(1, '2024-03-25', '2024-03-25', 'system'),
(2, '2024-03-26', '2024-03-26', 'system'),
(3, '2024-03-27', '2024-03-27', 'system');

-- Insert unavailable days
INSERT INTO workschd.unavailable_days (account_id, day_of_week, created_by)
VALUES 
(1, 'Sunday', 'system'),
(2, 'Saturday', 'system'),
(3, 'Monday', 'system'); 