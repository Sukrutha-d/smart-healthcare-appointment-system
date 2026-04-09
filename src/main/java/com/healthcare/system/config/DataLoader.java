package com.healthcare.system.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.healthcare.system.model.Doctor;
import com.healthcare.system.repository.DoctorRepository;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(DoctorRepository doctorRepository) {
        return args -> {
            // Check if doctors already exist to avoid duplicates
            if (doctorRepository.count() == 0) {
                Doctor drSmith = new Doctor();
                drSmith.setName("Dr. Smith");
                drSmith.setSpecialization("General Physician");
                drSmith.setAvailableFrom("09:00");
                drSmith.setAvailableTo("18:00");

                doctorRepository.save(drSmith);

                Doctor drJones = new Doctor();
                drJones.setName("Dr. Jones");
                drJones.setSpecialization("Cardiologist");
                drJones.setAvailableFrom("10:00");
                drJones.setAvailableTo("16:00");

                doctorRepository.save(drJones);

                System.out.println("✅ Sample doctors added to database!");
            }
        };
    }
}