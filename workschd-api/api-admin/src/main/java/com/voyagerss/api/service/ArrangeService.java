package com.voyagerss.api.service;

import com.voyagerss.persist.dto.AccountWorkHourDTO;
import com.voyagerss.persist.dto.AccountWorkOffDatesDTO;
import com.voyagerss.persist.dto.TaskDTO;
import com.voyagerss.persist.dto.TaskEmployeeDTO;
import com.voyagerss.persist.dto.TeamMemberDTO;
import com.voyagerss.persist.entity.Account;
import com.voyagerss.persist.EnumMaster;
import com.voyagerss.persist.service.AccountScheduleService;
import com.voyagerss.persist.service.TaskEmployeeService;
import com.voyagerss.persist.service.TaskService;
import com.voyagerss.persist.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class ArrangeService {
    private final TeamService teamService;
    private final AccountScheduleService accountScheduleService;
    private final TaskService taskService;
    private final TaskEmployeeService taskEmpService;

    // 필드 정의
    private Map<String, Integer> minStaffPerDay; // 요일별 최소 근무 인원
    private Map<Integer, Integer> maxOffDaysPerMonth; // 월별 최대 휴무일수
    private List<Account> accounts; // 직원 목록
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * Generate TaskEmployee entities based on the 6 rules:
     * 1. Check available AccountWorkHour
     * 2. Filter off Day from AccountWorkOffDates
     * 3. Filter off Dates from AccountWorkOffDates
     * 4. Check prefer Dates from AccountWorkOffDates
     * 5. Check prefer Day from AccountWorkOffDates
     * 6. Check priority from TeamMember
     */
    @Transactional
    public List<TaskEmployeeDTO> generateTaskEmployees(Long teamId, Long taskId, LocalDate targetDate) {
        log.info("Generating TaskEmployee entities for team: {}, task: {}, date: {}", teamId, taskId, targetDate);
        
        // Get all team members
        List<TeamMemberDTO> teamMembers = teamService.getTeamMembers(teamId);
        if (teamMembers.isEmpty()) {
            log.warn("No team members found for team: {}", teamId);
            return Collections.emptyList();
        }
        
        // Create a list to store qualified candidates
        List<CandidateScore> candidates = new ArrayList<>();
        
        String dayOfWeek = targetDate.getDayOfWeek().toString();
        
        for (TeamMemberDTO teamMember : teamMembers) {
            Integer accountId = teamMember.getAccountId();
            if (accountId == null) {
                log.warn("Team member {} has no account ID", teamMember.getId());
                continue;
            }
            
            try {
                CandidateScore candidate = evaluateCandidate(accountId, targetDate, dayOfWeek, teamMember);
                if (candidate.isEligible()) {
                    candidates.add(candidate);
                }
            } catch (Exception e) {
                log.error("Error evaluating candidate {}: {}", accountId, e.getMessage());
            }
        }
        
        // Sort candidates by score (highest first)
        candidates.sort((a, b) -> Integer.compare(b.getTotalScore(), a.getTotalScore()));
        
        // Create TaskEmployee entities for qualified candidates
        List<TaskEmployeeDTO> taskEmployees = new ArrayList<>();
        for (CandidateScore candidate : candidates) {
            try {
                TaskEmployeeDTO taskEmployeeDTO = createTaskEmployee(taskId, candidate);
                taskEmployees.add(taskEmployeeDTO);
                log.info("Created TaskEmployee for account: {} with score: {}", 
                        candidate.getAccountId(), candidate.getTotalScore());
            } catch (Exception e) {
                log.error("Error creating TaskEmployee for account {}: {}", 
                        candidate.getAccountId(), e.getMessage());
            }
        }
        
        return taskEmployees;
    }
    
    /**
     * Evaluate a candidate based on the 6 rules
     */
    private CandidateScore evaluateCandidate(Integer accountId, LocalDate targetDate, 
                                           String dayOfWeek, TeamMemberDTO teamMember) {
        CandidateScore candidate = new CandidateScore(accountId, teamMember.getPriority() != null ? teamMember.getPriority() : 0);
        
        // Rule 1: Check available AccountWorkHour
        List<AccountWorkHourDTO> workHours = accountScheduleService.getWorkHourByAccountId(accountId);
        boolean hasAvailableWorkHour = checkAvailableWorkHour(workHours, targetDate, dayOfWeek);
        if (!hasAvailableWorkHour) {
            candidate.setEligible(false);
            candidate.setReason("No available work hours");
            return candidate;
        }
        candidate.addScore(10, "Has available work hours");
        
        // Rule 2 & 3: Filter off Day and Dates from AccountWorkOffDates
        List<AccountWorkOffDatesDTO> offDates = accountScheduleService.getOffDatesByAccountId(accountId);
        if (isOffDay(offDates, targetDate, dayOfWeek)) {
            candidate.setEligible(false);
            candidate.setReason("Account has off day/date");
            return candidate;
        }
        candidate.addScore(5, "Not on off day/date");
        
        // Rule 4 & 5: Check prefer Dates and Days (AccountWorkHour preferred field)
        boolean isPreferredTime = checkPreferredTime(workHours, targetDate, dayOfWeek);
        if (isPreferredTime) {
            candidate.addScore(15, "Preferred time slot");
        }
        
        // Rule 6: Priority from TeamMember
        int priority = teamMember.getPriority() != null ? teamMember.getPriority() : 0;
        candidate.addScore(priority * 2, "Team member priority");
        
        return candidate;
    }
    
    /**
     * Rule 1: Check if account has available work hours for the target date/day
     */
    private boolean checkAvailableWorkHour(List<AccountWorkHourDTO> workHours, 
                                         LocalDate targetDate, String dayOfWeek) {
        if (workHours == null || workHours.isEmpty()) {
            return false;
        }
        
        for (AccountWorkHourDTO workHour : workHours) {
            // Check for specific date
            if (workHour.getDateAsLocalDate() != null && 
                workHour.getDateAsLocalDate().equals(targetDate)) {
                return true;
            }
            
            // Check for day of week
            if (workHour.getDay() != null && 
                workHour.getDay().equalsIgnoreCase(dayOfWeek)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Rules 2 & 3: Check if the target date/day is marked as off day
     */
    private boolean isOffDay(List<AccountWorkOffDatesDTO> offDates, 
                           LocalDate targetDate, String dayOfWeek) {
        if (offDates == null || offDates.isEmpty()) {
            return false;
        }
        
        for (AccountWorkOffDatesDTO offDate : offDates) {
            // Rule 3: Check specific off dates
            if (offDate.getOffDateAsLocalDate() != null && 
                offDate.getOffDateAsLocalDate().equals(targetDate)) {
                return true;
            }
        }
        
        // Rule 2: Check off days (this would require additional field in AccountWorkOffDates)
        // For now, we assume specific dates only
        
        return false;
    }
    
    /**
     * Rules 4 & 5: Check if the target date/day is preferred
     */
    private boolean checkPreferredTime(List<AccountWorkHourDTO> workHours, 
                                     LocalDate targetDate, String dayOfWeek) {
        if (workHours == null || workHours.isEmpty()) {
            return false;
        }
        
        for (AccountWorkHourDTO workHour : workHours) {
            if (!workHour.isPreferred()) {
                continue;
            }
            
            // Check for specific preferred date
            if (workHour.getDateAsLocalDate() != null && 
                workHour.getDateAsLocalDate().equals(targetDate)) {
                return true;
            }
            
            // Check for preferred day of week
            if (workHour.getDay() != null && 
                workHour.getDay().equalsIgnoreCase(dayOfWeek)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Create TaskEmployee DTO
     */
    private TaskEmployeeDTO createTaskEmployee(Long taskId, CandidateScore candidate) {
        TaskEmployeeDTO taskEmployeeDTO = new TaskEmployeeDTO();
        taskEmployeeDTO.setTaskId(taskId);
        taskEmployeeDTO.setAccountId(candidate.getAccountId());
        taskEmployeeDTO.setStatus(EnumMaster.TaskEmployeeStatus.PENDING);
        taskEmployeeDTO.setRequestDate(LocalDateTime.now());
        
        // Save the TaskEmployee entity
        return taskEmpService.createRequest(taskId, taskEmployeeDTO);
    }
    
    /**
     * Inner class to track candidate scoring
     */
    private static class CandidateScore {
        private final Integer accountId;
        private final int basePriority;
        private int totalScore;
        private boolean eligible = true;
        private String reason = "";
        private final List<String> scoreReasons = new ArrayList<>();
        
        public CandidateScore(Integer accountId, int basePriority) {
            this.accountId = accountId;
            this.basePriority = basePriority;
            this.totalScore = basePriority;
        }
        
        public void addScore(int points, String reason) {
            this.totalScore += points;
            this.scoreReasons.add(reason + " (+" + points + ")");
        }
        
        // Getters and setters
        public Integer getAccountId() { return accountId; }
        public int getTotalScore() { return totalScore; }
        public boolean isEligible() { return eligible; }
        public void setEligible(boolean eligible) { this.eligible = eligible; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
        public List<String> getScoreReasons() { return scoreReasons; }
    }

    // 배정 프로세스
    public void generateSchedule() {
        // 시작일자부터 종료일자까지 하루 단위로 반복
        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            String dayOfWeek = current.getDayOfWeek().toString();
            int month = current.getMonthValue();

            List<Account> availableAccounts = getAvailableEmployees(current);

            // 요일별 최소 근무 인원 충족 여부 확인
            int requiredStaff = minStaffPerDay.getOrDefault(dayOfWeek, 0);
            assignStaff(current, availableAccounts, requiredStaff);

            // 다음 날로 이동
            current = current.plusDays(1);
        }
    }

    // 근무 가능한 직원 목록을 반환
    private List<Account> getAvailableEmployees(LocalDate date) {
        return accounts.stream()
                .filter(account -> isAvailable(account, date))
                .collect(Collectors.toList());
    }

    // 계정이 주어진 날짜에 근무 가능한지 확인하는 메서드 
    private boolean isAvailable(Account account, LocalDate date) {
        // 실제 구현에서는 account의 근무 불가능 일자 목록을 확인해야 함
        // 이 예시에서는 항상 가능하다고 가정
        return true;
    }

    // 이미 배정된 근무일인지 확인
//    public boolean isAlreadyAssigned(LocalDate date) {
//        return assignedDays.contains(date);
//    }

    // 직원 배정 로직
    private void assignStaff(LocalDate date, List<Account> availableAccounts, int requiredStaff) {
        List<Account> assigned = new ArrayList<>();

        // 조건에 걸리지 않는 직원 우선 배정
        for (Account account : availableAccounts) {
            if (assigned.size() >= requiredStaff) break;
            if (isPreferredDay(account, date.getDayOfWeek().toString())) {
                assigned.add(account);
            }
        }

        // 여전히 최소 인원에 도달하지 못했을 경우, 나머지 직원 랜덤 배정
        Collections.shuffle(availableAccounts);
        for (Account account : availableAccounts) {
            if (assigned.size() >= requiredStaff) break;
            if (!assigned.contains(account)) {
                assigned.add(account);
            }
        }
    }
    
    // 계정의 선호 요일인지 확인하는 메서드
    private boolean isPreferredDay(Account account, String dayOfWeek) {
        // 실제 구현에서는 account의 선호 요일을 확인해야 함
        // 이 예시에서는 항상 false로 가정
        return false;
    }

    // 출근부 생성 메서드
    public void generateMonthlyAttendance(Long teamId, int year, int month) throws SQLException {
        // 해당 지점에 속한 직원 목록 가져오기
        List<TeamMemberDTO> teamMembers = teamService.getTeamMembers(teamId);

        // 해당 월의 시작일과 종료일 설정
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        // 월간 출근부 생성
        for (LocalDate currentDate = startDate; !currentDate.isAfter(endDate); currentDate = currentDate.plusDays(1)) {
            String dayOfWeek = currentDate.getDayOfWeek().toString();

            // 각 직원별로 스케줄 생성
            for (TeamMemberDTO teamMemberDTO : teamMembers) {
                Integer accountId = teamMemberDTO.getAccountId();
                if (accountId != null) {
                    List<AccountWorkOffDatesDTO> accountWorkOffDatesDTOS = accountScheduleService.getOffDatesByAccountId(accountId);
                    accountScheduleService.getWorkHourByAccountId(accountId);

                    // 직원의 출근 불가능 요일 또는 출근 불가능 일자가 있는지 확인
    //                Set<LocalDate> offDates = teamMemberDTO.getOffDates().stream().map(AccountWorkOffDates::getOffDate).collect(Collectors.toSet());
    //                Set<Integer> unavailableDays = teamMemberDTO.getOffDaysOfWeek();

                    // 출근 및 퇴근 시간 랜덤 생성 (8시~10시 사이 출근, 17시~19시 사이 퇴근)
                    LocalDateTime startTime = getRandomStartTime();
                    LocalDateTime endTime = getRandomEndTime(startTime);

                    TaskDTO taskDTO = new TaskDTO();
                    taskDTO.setStartDateTime(startTime);
                    taskDTO.setEndDateTime(endTime);
                    // 출근부에 기록 추가
//                    taskEmpService.save(taskDTO);
                    taskService.save(taskDTO);
                }
            }
        }
    }

    // 랜덤한 출근 시간 생성 (오늘 날짜, 8시 ~ 10시 사이)
    private LocalDateTime getRandomStartTime() {
        Random random = new Random();
        LocalDate today = LocalDate.now();
        int hour = 8 + random.nextInt(3);  // 8, 9, 10시 중 하나
        int minute = random.nextInt(60);   // 0 ~ 59분 중 하나
        return LocalDateTime.of(today, LocalTime.of(hour, minute));
    }

    // 랜덤한 퇴근 시간 생성 (출근 날짜와 동일, 출근 후 최소 8시간 이후 퇴근)
    private LocalDateTime getRandomEndTime(LocalDateTime startTime) {
        Random random = new Random();
        // Add 8 to 9 hours (inclusive of 8, exclusive of 10 -> nextInt(2) gives 0 or 1) + random minutes
        long hoursToAdd = 8 + random.nextInt(2);
        long minutesToAdd = random.nextInt(60);
        return startTime.plusHours(hoursToAdd).plusMinutes(minutesToAdd);
    }

    public void arrangeDefaultSchedules(Long teamId, int year, int month) {
        List<TeamMemberDTO> teamMembers = teamService.getTeamMembers(teamId); 
    }
}