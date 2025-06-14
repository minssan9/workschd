# TaskEmployee Generation System

## Overview
This system generates `TaskEmployee` entities based on a comprehensive 6-rule evaluation process that considers work hours, availability, preferences, and team member priorities.

## The 6 Rules (in order)

### Rule 1: Check Available AccountWorkHour
- **Purpose**: Ensure the team member has defined work hours for the target date/day
- **Implementation**: Checks `AccountWorkHour` entity for:
  - Specific date matches (`date` field)
  - Day of week matches (`day` field)
- **Scoring**: +10 points if available work hours exist
- **Elimination**: If no work hours found, candidate is eliminated

### Rule 2: Filter Off Day from AccountWorkOffDates
- **Purpose**: Exclude team members who have marked specific days as unavailable
- **Implementation**: Checks `AccountWorkOffDates` for day-of-week patterns
- **Current Status**: Partially implemented (requires additional `dayOfWeek` field in entity)
- **Elimination**: If day is marked as off, candidate is eliminated

### Rule 3: Filter Off Dates from AccountWorkOffDates
- **Purpose**: Exclude team members who have marked specific dates as unavailable
- **Implementation**: Checks `AccountWorkOffDates.offDate` for exact date matches
- **Scoring**: +5 points if not on off date
- **Elimination**: If specific date is marked as off, candidate is eliminated

### Rule 4: Check Prefer Dates from AccountWorkOffDates
- **Purpose**: Prioritize team members who prefer specific dates
- **Implementation**: Uses `AccountWorkHour.preferred` flag for specific dates
- **Scoring**: +15 points if target date is preferred

### Rule 5: Check Prefer Day from AccountWorkOffDates
- **Purpose**: Prioritize team members who prefer specific days of the week
- **Implementation**: Uses `AccountWorkHour.preferred` flag for day patterns
- **Scoring**: +15 points if target day is preferred

### Rule 6: Check Priority from TeamMember
- **Purpose**: Consider team member hierarchy and importance
- **Implementation**: Uses `TeamMember.priority` field
- **Scoring**: +(priority × 2) points
- **Default**: Priority defaults to 0 if not set

## Entity Requirements

### AccountWorkHour
```java
@Entity
public class AccountWorkHour {
    private LocalDate date;        // Specific date
    private String day;            // Day of week (MONDAY, TUESDAY, etc.)
    private LocalTime startTime;   // Work start time
    private LocalTime endTime;     // Work end time
    private boolean preferred;     // Whether this time slot is preferred
}
```

### AccountWorkOffDates
```java
@Entity
public class AccountWorkOffDates {
    private LocalDate offDate;     // Specific off date
    // Future enhancement: Add dayOfWeek field for Rule 2
}
```

### TeamMember
```java
@Entity
public class TeamMember {
    private Integer priority;      // Priority level (higher = more important)
    // ... other fields
}
```

## Scoring System

The system uses a point-based scoring mechanism:

| Criteria | Points | Notes |
|----------|--------|-------|
| Base Priority | priority × 2 | From TeamMember.priority |
| Available Work Hours | +10 | Required for eligibility |
| Not on Off Day/Date | +5 | Required for eligibility |
| Preferred Time Slot | +15 | Bonus for preference match |

**Total Possible Score**: Variable based on priority + 30 points maximum

## API Usage

### Generate TaskEmployees
```http
POST /api/admin/arrange/generate-task-employees
```

**Parameters:**
- `teamId` (Long): The team ID
- `taskId` (Long): The task ID  
- `targetDate` (LocalDate): Target date for assignment (ISO format: YYYY-MM-DD)

**Response:**
```json
[
  {
    "id": 1,
    "taskId": 123,
    "accountId": 456,
    "status": "PENDING",
    "requestDate": "2024-01-15T10:30:00"
  }
]
```

### Example Request
```bash
curl -X POST "http://localhost:8080/api/admin/arrange/generate-task-employees" \
  -d "teamId=1&taskId=5&targetDate=2024-01-15"
```

## Implementation Details

### CandidateScore Class
Internal scoring mechanism that tracks:
- Account ID
- Base priority from team member
- Total accumulated score
- Eligibility status
- Scoring reasons (for debugging)

### Evaluation Process
1. Retrieve all team members for the given team
2. For each team member:
   - Apply the 6 rules in order
   - Eliminate if fails mandatory rules (1, 2, 3)
   - Add bonus points for preferences (4, 5, 6)
3. Sort candidates by total score (highest first)
4. Create TaskEmployee entities for all qualified candidates

### Error Handling
- Logs detailed information about evaluation process
- Continues processing even if individual candidates fail
- Returns partial results if some candidates succeed

## Database Schema Updates

### Required for Full Implementation

#### AccountWorkOffDates Enhancement
```sql
ALTER TABLE account_work_off_dates 
ADD COLUMN day_of_week VARCHAR(10);
```

#### TeamMember Enhancement
```sql
ALTER TABLE team_member 
ADD COLUMN priority INTEGER DEFAULT 0;
```

## Usage Examples

### Scenario 1: Regular Assignment
- Team has 5 members
- Task requires workers for Monday
- 3 members have Monday work hours
- 1 member has Monday marked as preferred
- Result: All 3 qualified members get TaskEmployee records, with preferred member having highest score

### Scenario 2: High Priority Member
- Team member has priority = 5
- Base score = 5 × 2 = 10 points
- Additional criteria add up to 30 points
- Total score = 40 points (very high)

### Scenario 3: Elimination
- Team member has no work hours for target date → Eliminated at Rule 1
- Team member has target date as off day → Eliminated at Rule 3

## Future Enhancements

1. **Capacity Management**: Limit number of TaskEmployees created based on task requirements
2. **Shift Patterns**: Support for recurring shift patterns
3. **Skills Matching**: Add skill requirements to tasks and team members
4. **Workload Balancing**: Consider existing assignments when scoring
5. **Automated Approval**: Auto-approve high-scoring candidates
6. **Conflict Detection**: Detect scheduling conflicts across multiple tasks

## Troubleshooting

### Common Issues
1. **No candidates found**: Check if team members have work hours defined
2. **All candidates eliminated**: Verify off dates and work hour configurations
3. **Unexpected scoring**: Review priority settings and preference flags

### Debugging
The system provides detailed logging at INFO level:
- Candidate evaluation details
- Scoring breakdown
- Elimination reasons
- Final results

Enable DEBUG logging for `com.voyagerss.api.service.ArrangeService` for maximum detail. 