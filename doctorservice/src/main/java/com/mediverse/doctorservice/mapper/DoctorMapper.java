package com.mediverse.doctorservice.mapper;

import org.springframework.stereotype.Component;

import com.mediverse.doctorservice.dto.DoctorRequest;
import com.mediverse.doctorservice.dto.DoctorResponse;
import com.mediverse.doctorservice.entity.Doctor;

@Component
public class DoctorMapper {

    public Doctor toEntity(DoctorRequest request) {

        return Doctor.builder()
                .fullName(request.getFullName())
                .specialization(request.getSpecialization())
                .qualification(request.getQualification())
                .experienceYears(request.getExperienceYears())
                .consultationFee(request.getConsultationFee())
                .registrationNumber(request.getRegistrationNumber())
                .bio(request.getBio())
                .clinicAddress(request.getClinicAddress())
                .city(request.getCity())
                .state(request.getState())
                .availableForOnlineConsultation(
                        request.getAvailableForOnlineConsultation()
                )
                .build();
    }

    public DoctorResponse toResponse(Doctor doctor) {

        return DoctorResponse.builder()
                .id(doctor.getId())
                .userId(doctor.getUserId())
                .fullName(doctor.getFullName())
                .specialization(doctor.getSpecialization())
                .qualification(doctor.getQualification())
                .experienceYears(doctor.getExperienceYears())
                .consultationFee(doctor.getConsultationFee())
                .registrationNumber(doctor.getRegistrationNumber())
                .bio(doctor.getBio())
                .clinicAddress(doctor.getClinicAddress())
                .city(doctor.getCity())
                .state(doctor.getState())
                .availableForOnlineConsultation(
                        doctor.getAvailableForOnlineConsultation()
                )
                .status(doctor.getStatus())
                .createdAt(doctor.getCreatedAt())
                .updatedAt(doctor.getUpdatedAt())
                .build();
    }

    public void updateEntity(
            Doctor doctor,
            DoctorRequest request
    ) {

        doctor.setFullName(request.getFullName());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setQualification(request.getQualification());
        doctor.setExperienceYears(request.getExperienceYears());
        doctor.setConsultationFee(request.getConsultationFee());
        doctor.setRegistrationNumber(request.getRegistrationNumber());
        doctor.setBio(request.getBio());
        doctor.setClinicAddress(request.getClinicAddress());
        doctor.setCity(request.getCity());
        doctor.setState(request.getState());
        doctor.setAvailableForOnlineConsultation(
                request.getAvailableForOnlineConsultation()
        );
    }
}