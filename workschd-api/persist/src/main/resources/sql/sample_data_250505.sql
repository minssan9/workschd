-- Account data
INSERT INTO workschd.account (account_id, is_active, email, password, phone, status, username)
VALUES 
(1, 1, 'john.doe@example.com', '$2a$10$xPPwGSHXZAHoJ6FIQZFlvOyqxJQWyH3HvP8P9L5K8K1K1QXZ', '+1234567890', 'ACTIVE', 'johndoe'),
(2, 1, 'jane.smith@example.com', '$2a$10$xPPwGSHXZAHoJ6FIQZFlvOyqxJQWyH3HvP8P9L5K8K1K1QXZ', '+1987654321', 'ACTIVE', 'janesmith'),
(3, 1, 'mike.wilson@example.com', '$2a$10$xPPwGSHXZAHoJ6FIQZFlvOyqxJQWyH3HvP8P9L5K8K1K1QXZ', '+1122334455', 'ACTIVE', 'mikewilson'),
(4, 1, 'sarah.kim@example.com', '$2a$10$xPPwGSHXZAHoJ6FIQZFlvOyqxJQWyH3HvP8P9L5K8K1K1QXZ', '+8210987654', 'ACTIVE', 'sarahkim'),
(5, 1, 'david.lee@example.com', '$2a$10$xPPwGSHXZAHoJ6FIQZFlvOyqxJQWyH3HvP8P9L5K8K1K1QXZ', '+8210123456', 'ACTIVE', 'davidlee'),
(6, 1, 'emma.park@example.com', '$2a$10$xPPwGSHXZAHoJ6FIQZFlvOyqxJQWyH3HvP8P9L5K8K1K1QXZ', '+8210567890', 'ACTIVE', 'emmapark'),
(7, 1, 'james.choi@example.com', '$2a$10$xPPwGSHXZAHoJ6FIQZFlvOyqxJQWyH3HvP8P9L5K8K1K1QXZ', '+8210345678', 'ACTIVE', 'jameschoi'),
(8, 1, 'sophia.jung@example.com', '$2a$10$xPPwGSHXZAHoJ6FIQZFlvOyqxJQWyH3HvP8P9L5K8K1K1QXZ', '+8210789012', 'ACTIVE', 'sophiajung'),
(9, 1, 'daniel.kang@example.com', '$2a$10$xPPwGSHXZAHoJ6FIQZFlvOyqxJQWyH3HvP8P9L5K8K1K1QXZ', '+8210234567', 'ACTIVE', 'danielkang'),
(10, 1, 'olivia.han@example.com', '$2a$10$xPPwGSHXZAHoJ6FIQZFlvOyqxJQWyH3HvP8P9L5K8K1K1QXZ', '+8210890123', 'ACTIVE', 'oliviahan');

-- Account Info
INSERT INTO workschd.account_info (is_active, account_id)
VALUES 
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9), (1, 10);

-- Account Role
INSERT INTO workschd.account_role (is_active, role_type, account_id)
VALUES 
(1, 'OWNER', 1),
(1, 'MANAGER', 2),
(1, 'WORKER', 3),
(1, 'MANAGER', 4),
(1, 'WORKER', 5),
(1, 'WORKER', 6),
(1, 'SCHEDULER', 7),
(1, 'WORKER', 8),
(1, 'MANAGER', 9),
(1, 'WORKER', 10);

-- Account SNS
INSERT INTO workschd.account_sns (is_active, email_verified_yn, provider_type, user_id, account_id)
VALUES 
(1, 'Y', 'GOOGLE', 'google_123', 1),
(1, 'Y', 'KAKAO', 'kakao_456', 2),
(1, 'Y', 'NAVER', 'naver_789', 3),
(1, 'Y', 'KAKAO', 'kakao_101', 4),
(1, 'Y', 'GOOGLE', 'google_102', 5),
(1, 'Y', 'NAVER', 'naver_103', 6),
(1, 'Y', 'FACEBOOK', 'facebook_104', 7),
(1, 'Y', 'KAKAO', 'kakao_105', 8),
(1, 'Y', 'GOOGLE', 'google_106', 9),
(1, 'Y', 'NAVER', 'naver_107', 10);

-- Account Work Hours (Adding more varied schedules)
INSERT INTO workschd.account_work_hour (is_active, date, day, startTime, endTime, preferred, account_id)
VALUES 
(1, '2024-05-01', 'MONDAY', '09:00', '18:00', 1, 1),
(1, '2024-05-01', 'MONDAY', '10:00', '19:00', 1, 2),
(1, '2024-05-01', 'MONDAY', '11:00', '20:00', 1, 3),
(1, '2024-05-01', 'TUESDAY', '08:00', '17:00', 1, 4),
(1, '2024-05-01', 'TUESDAY', '09:00', '18:00', 1, 5),
(1, '2024-05-01', 'WEDNESDAY', '10:00', '19:00', 1, 6),
(1, '2024-05-01', 'WEDNESDAY', '13:00', '22:00', 1, 7),
(1, '2024-05-01', 'THURSDAY', '14:00', '23:00', 1, 8),
(1, '2024-05-01', 'FRIDAY', '07:00', '16:00', 1, 9),
(1, '2024-05-01', 'FRIDAY', '12:00', '21:00', 1, 10);

-- Account Work Off Dates (More varied dates)
INSERT INTO workschd.account_work_off_dates (is_active, off_date, account_id)
VALUES 
(1, '2024-05-15', 1),
(1, '2024-05-16', 2),
(1, '2024-05-17', 3),
(1, '2024-05-20', 4),
(1, '2024-05-21', 5),
(1, '2024-05-22', 6),
(1, '2024-05-23', 7),
(1, '2024-05-24', 8),
(1, '2024-05-27', 9),
(1, '2024-05-28', 10);

-- Team (Adding more teams with varied locations)
INSERT INTO workschd.team (id, is_active, name, region, location, scheduleType)
VALUES 
(1, 1, 'Alpha Team', 'Seoul', 'Gangnam', 'WEEKLY'),
(2, 1, 'Beta Team', 'Busan', 'Haeundae', 'MONTHLY'),
(3, 1, 'Gamma Team', 'Incheon', 'Songdo', 'WEEKLY'),
(4, 1, 'Delta Team', 'Seoul', 'Hongdae', 'WEEKLY'),
(5, 1, 'Epsilon Team', 'Seoul', 'Jamsil', 'MONTHLY'),
(6, 1, 'Zeta Team', 'Daegu', 'Dongseongno', 'WEEKLY'),
(7, 1, 'Eta Team', 'Daejeon', 'Dunsan', 'MONTHLY'),
(8, 1, 'Theta Team', 'Gwangju', 'Chungjangno', 'WEEKLY'),
(9, 1, 'Iota Team', 'Suwon', 'Gwonseon', 'WEEKLY'),
(10, 1, 'Kappa Team', 'Jeju', 'Seogwipo', 'MONTHLY');

-- Shop (More shops with realistic Korean addresses)
INSERT INTO workschd.shop (id, is_active, name, address, district, postal_code, phone, total_rooms, latitude, longitude, team_id)
VALUES 
(1, 1, 'Shop Alpha', '123 Gangnam St', 'Gangnam-gu', '06000', '02-1234-5678', 10, 37.5665, 126.9780, 1),
(2, 1, 'Shop Beta', '456 Haeundae St', 'Haeundae-gu', '48100', '051-987-6543', 8, 35.1796, 129.0756, 2),
(3, 1, 'Shop Gamma', '789 Songdo St', 'Yeonsu-gu', '21984', '032-123-4567', 12, 37.3833, 126.6569, 3),
(4, 1, 'Shop Delta', '234 Hongdae St', 'Mapo-gu', '04036', '02-2345-6789', 15, 37.5558, 126.9234, 4),
(5, 1, 'Shop Epsilon', '567 Jamsil St', 'Songpa-gu', '05545', '02-3456-7890', 20, 37.5130, 127.0982, 5),
(6, 1, 'Shop Zeta', '890 Dongseongno St', 'Jung-gu', '41911', '053-456-7890', 6, 35.8714, 128.5964, 6),
(7, 1, 'Shop Eta', '123 Dunsan St', 'Seo-gu', '35233', '042-567-8901', 9, 36.3504, 127.3845, 7),
(8, 1, 'Shop Theta', '456 Chungjangno St', 'Dong-gu', '61473', '062-678-9012', 11, 35.1496, 126.9156, 8),
(9, 1, 'Shop Iota', '789 Gwonseon St', 'Gwonseon-gu', '16571', '031-789-0123', 7, 37.2579, 127.0292, 9),
(10, 1, 'Shop Kappa', '101 Seogwipo St', 'Seogwipo-si', '63590', '064-890-1234', 14, 33.2496, 126.5628, 10);

-- Task (More varied tasks throughout the day)
INSERT INTO workschd.task (id, is_active, title, description, start_date_time, end_date_time, status, worker_count, shop_id, team_id)
VALUES 
(1, 1, 'Morning Shift', 'Morning cleaning service', '2024-05-01 09:00:00', '2024-05-01 13:00:00', 'SCHEDULED', 2, 1, 1),
(2, 1, 'Afternoon Shift', 'Afternoon maintenance', '2024-05-01 14:00:00', '2024-05-01 18:00:00', 'SCHEDULED', 3, 2, 2),
(3, 1, 'Evening Shift', 'Evening cleaning service', '2024-05-01 18:00:00', '2024-05-01 22:00:00', 'SCHEDULED', 2, 3, 3),
(4, 1, 'Early Bird Shift', 'Early morning setup', '2024-05-01 06:00:00', '2024-05-01 10:00:00', 'SCHEDULED', 2, 4, 4),
(5, 1, 'Mid-Morning Shift', 'Regular maintenance', '2024-05-01 10:00:00', '2024-05-01 14:00:00', 'IN_PROGRESS', 3, 5, 5),
(6, 1, 'Lunch Shift', 'Peak hour service', '2024-05-01 11:00:00', '2024-05-01 15:00:00', 'SCHEDULED', 4, 6, 6),
(7, 1, 'Late Afternoon', 'General cleaning', '2024-05-01 15:00:00', '2024-05-01 19:00:00', 'SCHEDULED', 2, 7, 7),
(8, 1, 'Night Shift', 'Deep cleaning service', '2024-05-01 20:00:00', '2024-05-02 00:00:00', 'SCHEDULED', 3, 8, 8),
(9, 1, 'Weekend Special', 'Premium service', '2024-05-04 10:00:00', '2024-05-04 18:00:00', 'SCHEDULED', 4, 9, 9),
(10, 1, 'Holiday Shift', 'Holiday special service', '2024-05-05 09:00:00', '2024-05-05 17:00:00', 'SCHEDULED', 3, 10, 10);

-- Task Employee (More varied statuses and times)
INSERT INTO workschd.task_employee (id, is_active, status, request_date, approved_at, account_id, task_id)
VALUES 
(1, 1, 'APPROVED', '2024-04-30 10:00:00', '2024-04-30 11:00:00', 1, 1),
(2, 1, 'APPROVED', '2024-04-30 10:00:00', '2024-04-30 11:00:00', 2, 2),
(3, 1, 'PENDING', '2024-04-30 10:00:00', NULL, 3, 3),
(4, 1, 'APPROVED', '2024-04-30 12:00:00', '2024-04-30 13:00:00', 4, 4),
(5, 1, 'REJECTED', '2024-04-30 14:00:00', '2024-04-30 15:00:00', 5, 5),
(6, 1, 'APPROVED', '2024-04-30 16:00:00', '2024-04-30 17:00:00', 6, 6),
(7, 1, 'PENDING', '2024-04-30 18:00:00', NULL, 7, 7),
(8, 1, 'APPROVED', '2024-04-30 20:00:00', '2024-04-30 21:00:00', 8, 8),
(9, 1, 'REJECTED', '2024-04-30 22:00:00', '2024-04-30 23:00:00', 9, 9),
(10, 1, 'PENDING', '2024-05-01 00:00:00', NULL, 10, 10);

-- Team Member (More varied join dates)
INSERT INTO workschd.team_member (id, is_active, status, join_date, account_id, team_id)
VALUES 
(1, 1, 'ACTIVE', '2024-01-01 00:00:00', 1, 1),
(2, 1, 'ACTIVE', '2024-01-15 00:00:00', 2, 2),
(3, 1, 'ACTIVE', '2024-02-01 00:00:00', 3, 3),
(4, 1, 'ACTIVE', '2024-02-15 00:00:00', 4, 4),
(5, 1, 'ACTIVE', '2024-03-01 00:00:00', 5, 5),
(6, 1, 'ACTIVE', '2024-03-15 00:00:00', 6, 6),
(7, 1, 'ACTIVE', '2024-04-01 00:00:00', 7, 7),
(8, 1, 'ACTIVE', '2024-04-15 00:00:00', 8, 8),
(9, 1, 'ACTIVE', '2024-05-01 00:00:00', 9, 9),
(10, 1, 'ACTIVE', '2024-05-15 00:00:00', 10, 10);

-- Translations (More comprehensive translations)
INSERT INTO workschd.translations (id, is_active, txt_key, en, ko, ja, es, fr)
VALUES 
(1, 1, 'WELCOME_MESSAGE', 'Welcome', '환영합니다', 'ようこそ', 'Bienvenido', 'Bienvenue'),
(2, 1, 'GOODBYE_MESSAGE', 'Goodbye', '안녕히 가세요', 'さようなら', 'Adiós', 'Au revoir'),
(3, 1, 'THANK_YOU', 'Thank you', '감사합니다', 'ありがとう', 'Gracias', 'Merci'),
(4, 1, 'SCHEDULE_CONFIRMED', 'Schedule Confirmed', '스케줄 확정', 'スケジュール確認', 'Horario Confirmado', 'Horaire Confirmé'),
(5, 1, 'TASK_COMPLETED', 'Task Completed', '작업 완료', 'タスク完了', 'Tarea Completada', 'Tâche Terminée'),
(6, 1, 'BREAK_TIME', 'Break Time', '휴식 시간', '休憩時間', 'Tiempo de Descanso', 'Temps de Pause'),
(7, 1, 'START_WORK', 'Start Work', '업무 시작', '仕事開始', 'Comenzar Trabajo', 'Commencer le Travail'),
(8, 1, 'END_WORK', 'End Work', '업무 종료', '仕事終了', 'Terminar Trabajo', 'Terminer le Travail'),
(9, 1, 'SHIFT_CHANGE', 'Shift Change', '교대 변경', 'シフト変更', 'Cambio de Turno', 'Changement de Quart'),
(10, 1, 'OVERTIME_REQUEST', 'Overtime Request', '연장 근무 요청', '残業申請', 'Solicitud de Horas Extras', 'Demande d\'Heures Supplémentaires'); 