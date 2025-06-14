-- Add priority field to team_member table
ALTER TABLE team_member 
ADD COLUMN priority INTEGER DEFAULT 0 COMMENT 'Priority level for task assignment (higher = more important)';

-- Add day_of_week field to account_work_off_dates table for Rule 2 implementation
ALTER TABLE account_work_off_dates 
ADD COLUMN day_of_week VARCHAR(10) COMMENT 'Day of week pattern for off days (MONDAY, TUESDAY, etc.)';

-- Create index for better performance on priority-based queries
CREATE INDEX idx_team_member_priority ON team_member(priority);

-- Create index for better performance on day_of_week queries
CREATE INDEX idx_account_work_off_dates_day_of_week ON account_work_off_dates(day_of_week);

-- Update existing records with default priority
UPDATE team_member SET priority = 0 WHERE priority IS NULL; 