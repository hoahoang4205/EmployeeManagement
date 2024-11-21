package com.example.testbackend1.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.testbackend1.model.Attendance;
import com.example.testbackend1.model.Employee;
import com.example.testbackend1.repository.AttendanceRepository;
import com.example.testbackend1.repository.EmployeeRepository;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Attendance> getAttendancesByEmployee(String employeeId, int month, int year) {
        LocalDate startDate = LocalDate.of(year, month, 1); // Ngày đầu tháng
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth()); // Ngày cuối tháng
        return attendanceRepository.findByEmployeeEmployeeIdAndDateBetween(employeeId, startDate, endDate);
    }

    public double calculateTotalHoursWorkedByDate(String employeeId, LocalDate date) {
        LocalDate startDate = date; // Ngày bắt đầu
        LocalDate endDate = date;   // Ngày kết thúc (cùng ngày)
    
        List<Attendance> attendances = attendanceRepository.findByEmployeeEmployeeIdAndDateBetween(employeeId, startDate, endDate);
        double totalHours = 0;
    
        // Tính tổng số giờ làm việc từ danh sách chấm công
        for (Attendance attendance : attendances) {
            if (attendance.getCheckIn() != null && attendance.getCheckOut() != null) {
                Duration duration = Duration.between(attendance.getCheckIn(), attendance.getCheckOut());
                totalHours += duration.toMinutes() / 60.0; // Chuyển đổi từ phút sang giờ
            }
        }
    
        return totalHours;
    }

    // Tính tổng số giờ làm việc của nhân viên trong một khoảng thời gian (tháng)
    public double calculateTotalHoursWorked(String employeeId, LocalDate startDate, LocalDate endDate) {
        List<Attendance> attendances = attendanceRepository.findByEmployeeEmployeeIdAndDateBetween(employeeId, startDate, endDate);
        double totalHours = 0;

        // Duyệt qua các bản ghi chấm công và tính tổng số giờ làm việc
        for (Attendance attendance : attendances) {
            if (attendance.getCheckIn() != null && attendance.getCheckOut() != null) {
                Duration duration = Duration.between(attendance.getCheckIn(), attendance.getCheckOut());
                totalHours += duration.toMinutes() / 60.0; // Chuyển đổi từ phút sang giờ
            }
        }

        return totalHours;
    }
    
    public Attendance addAttendance(Attendance attendance) {
        String employeeId = attendance.getEmployee() != null ? attendance.getEmployee().getEmployeeId() : null;
        if (employeeId == null) {
            throw new RuntimeException("Employee cannot be null");
        }

        // Kiểm tra xem nhân viên có tồn tại trong cơ sở dữ liệu không
        Employee existsByEmployee = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

        // Kiểm tra xem bản ghi chấm công đã tồn tại hay chưa
        LocalDate date = attendance.getDate();
        LocalDateTime checkIn = attendance.getCheckIn();
        LocalDateTime checkOut = attendance.getCheckOut();

        List<Attendance> existingAttendances = attendanceRepository.findByEmployeeEmployeeIdAndDate(employeeId, date);

        for (Attendance existingAttendance : existingAttendances) {
            // Kiểm tra xem giờ vào có trùng không
            if (existingAttendance.getCheckIn() != null && existingAttendance.getCheckIn().equals(checkIn)) {
                throw new RuntimeException("Attendance already exists for this date and check-in time.");
            }
        }

        // Nếu cần, bạn có thể gán lại thông tin nhân viên vào attendance ở đây
        attendance.setEmployee(existsByEmployee);

        return attendanceRepository.save(attendance); // Lưu bản ghi vào cơ sở dữ liệu
    }
    
}
