package com.voyagerss.api.service;

import com.voyagerss.persist.dto.AccountWorkOffDatesDTO;
import com.voyagerss.persist.dto.AttendanceDTO;
import com.voyagerss.persist.dto.TeamMemberDTO;
import com.voyagerss.persist.entity.AccountInfo;
import com.voyagerss.persist.entity.AccountWorkOffDates;
import com.voyagerss.persist.service.AccountScheduleService;
import com.voyagerss.persist.service.AttendanceService;
import com.voyagerss.persist.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ArrangeService {
    private final TeamService teamService;
    private final AccountScheduleService accountScheduleService;
    private final AttendanceService attendanceService;

    // 필드 정의
    private Map<String, Integer> minStaffPerDay; // 요일별 최소 근무 인원
    private Map<Integer, Integer> maxOffDaysPerMonth; // 월별 최대 휴무일수
    private List<AccountInfo> accountInfos; // 직원 목록
    private LocalDate startDate;
    private LocalDate endDate;


    // 배정 프로세스
    public void generateSchedule() {
        // 시작일자부터 종료일자까지 하루 단위로 반복
        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            String dayOfWeek = current.getDayOfWeek().toString();
            int month = current.getMonthValue();

            List<AccountInfo> availableAccountInfos = getAvailableEmployees(current);

            // 요일별 최소 근무 인원 충족 여부 확인
            int requiredStaff = minStaffPerDay.getOrDefault(dayOfWeek, 0);
            assignStaff(current, availableAccountInfos, requiredStaff);

            // 다음 날로 이동
            current = current.plusDays(1);
        }
    }

    // 근무 가능한 직원 목록을 반환
    private List<AccountInfo> getAvailableEmployees(LocalDate date) {
        return accountInfos.stream()
                .filter(accountInfo -> accountInfo.isAvailable(date))
//                .sorted(Comparator.comparingInt(AccountEmployee::getTotalWorkDays)) // 근무일 수가 적은 순서로 정렬
                .collect(Collectors.toList());
    }


    // 이미 배정된 근무일인지 확인
//    public boolean isAlreadyAssigned(LocalDate date) {
//        return assignedDays.contains(date);
//    }

    // 직원 배정 로직
    private void assignStaff(LocalDate date, List<AccountInfo> availableAccountInfos, int requiredStaff) {
        List<AccountInfo> assigned = new ArrayList<>();

        // 조건에 걸리지 않는 직원 우선 배정
        for (AccountInfo accountInfo : availableAccountInfos) {
            if (assigned.size() >= requiredStaff) break;
            if (accountInfo.getPreferredDay().equals(date.getDayOfWeek().toString())
//                    && !accountEmployee.hasExceededOffDays(maxOffDaysPerMonth.getOrDefault(date.getMonthValue(), Integer.MAX_VALUE)) &&
            ) {
                assigned.add(accountInfo);
            }
        }

        // 여전히 최소 인원에 도달하지 못했을 경우, 나머지 직원 랜덤 배정
        Collections.shuffle(availableAccountInfos);
        for (AccountInfo accountInfo : availableAccountInfos) {
            if (assigned.size() >= requiredStaff) break;
            if (!assigned.contains(accountInfo)) {
                assigned.add(accountInfo);
            }
        }
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
                List<AccountWorkOffDatesDTO> accountWorkOffDatesDTOS = accountScheduleService.getOffDatesByAccountId(teamMemberDTO.getAccountId());
                accountScheduleService.getWorkHourByAccountId(teamMemberDTO.getAccountId());

                // 직원의 출근 불가능 요일 또는 출근 불가능 일자가 있는지 확인
//                Set<LocalDate> offDates = teamMemberDTO.getOffDates().stream().map(AccountWorkOffDates::getOffDate).collect(Collectors.toSet());
//                Set<Integer> unavailableDays = teamMemberDTO.getOffDaysOfWeek();

                // 직원이 해당 일에 근무할 수 있는지 확인
//                if (offDates.contains(currentDate) || unavailableDays.contains(dayOfWeek)) {
//                    continue; // 근무 불가능한 요일이나 일자면 건너뜀
//                }

                // 출근 및 퇴근 시간 랜덤 생성 (8시~10시 사이 출근, 17시~19시 사이 퇴근)
                LocalTime startTime = getRandomStartTime();
                LocalTime endTime = getRandomEndTime(startTime);

                AttendanceDTO attendance = new AttendanceDTO();
                attendance.setEmployeeId(teamMemberDTO.getId());
                attendance.setAttendanceDate(currentDate);
                attendance.setStartTime(startTime);
                attendance.setEndTime(endTime);
                // 출근부에 기록 추가
                attendanceService.save(attendance);
            }
        }
    }

    // 랜덤한 출근 시간 생성 (8시 ~ 10시 사이)
    private LocalTime getRandomStartTime() {
        Random random = new Random();
        int hour = 8 + random.nextInt(3);  // 8, 9, 10시 중 하나
        int minute = random.nextInt(60);   // 0 ~ 59분 중 하나
        return LocalTime.of(hour, minute);
    }

    // 랜덤한 퇴근 시간 생성 (출근 후 최소 8시간 이후 퇴근)
    private LocalTime getRandomEndTime(LocalTime startTime) {
        Random random = new Random();
        int hour = startTime.getHour() + 8 + random.nextInt(2);  // 출근 시간에서 8~10시간 후 퇴근
        int minute = random.nextInt(60);   // 0 ~ 59분 중 하나
        return LocalTime.of(hour, minute);
    }
}