package com.healthcare.system.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.healthcare.system.model.AvailabilitySlot;
import com.healthcare.system.model.Doctor;
import com.healthcare.system.repository.DoctorRepository;
import com.healthcare.system.repository.SlotRepository;

@RestController
@RequestMapping("/slots")
public class SlotController {

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @GetMapping("/{doctorName}/{date}")
    public List<AvailabilitySlot> getSlotsByDate(
            @PathVariable String doctorName,
            @PathVariable String date) {

        Doctor doctor = doctorRepository.findByName(doctorName);

        if (doctor == null) return List.of();

        LocalDate selectedDate = LocalDate.parse(date);

        return slotRepository.findByDoctorOrderByStartTimeAsc(doctor)
                .stream()
                .filter(s -> s.getStartTime().toLocalDate().equals(selectedDate))
                .collect(Collectors.toList());  // ✅ NO TIME FILTER
    }
}