//package com.smarthealthcare.appointment.smarthealthcare_appointment;
//
//import com.smarthealthcare.appointment.smarthealthcare_appointment.DTOs.responseDTOs.DoctorDTO;
//import com.smarthealthcare.appointment.smarthealthcare_appointment.model.Doctor;
//import com.smarthealthcare.appointment.smarthealthcare_appointment.repository.DoctorRepository;
//import com.smarthealthcare.appointment.smarthealthcare_appointment.service.AuthService;
//import com.smarthealthcare.appointment.smarthealthcare_appointment.service.DoctorService;
//import jakarta.persistence.EntityManagerFactory;
//import org.hibernate.SessionFactory;
//import org.hibernate.stat.Statistics;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//class DoctorCacheTest {
//
//    @Autowired
//    private DoctorRepository doctorRepository;
//
//    @Autowired
//    private DoctorService doctorService; // contains @Cacheable getAllDoctors()
//
//    @Autowired
//    private AuthService authService;
//
//    @Test
//    void testDoctorListCaching() {
//        // First call → hits DB
//        List<DoctorDTO> firstCall = doctorService.getAllDoctors();
//
//        // Second call → should hit cache
//        List<DoctorDTO> secondCall = doctorService.getAllDoctors();
//
//        // Assertions
//        assertEquals(firstCall.size(), secondCall.size());
//        // You can also check that the DB query only ran once via logs
//    }
//
//    @Test
//    void testCacheEvictionAfterAdd() {
//        List<DoctorDTO> beforeAdd = doctorService.getAllDoctors();
//
//        Doctor newDoctor = new Doctor();
//        newDoctor.setName("Dr. Test");
//        newDoctor.setSpeciality("Cardiology");
//        authService.registerDoctor(null, newDoctor); // this should clear the cache
//
//        List<DoctorDTO> afterAdd = doctorService.getAllDoctors();
//
//        // The new list should be bigger
//        assertEquals(beforeAdd.size() + 1, afterAdd.size());
//    }
//
//    @Autowired
//    private EntityManagerFactory emf;
//
//    @Test
//    void testCacheHits() {
//        SessionFactory sessionFactory = emf.unwrap(SessionFactory.class);
//        Statistics stats = sessionFactory.getStatistics();
//        stats.setStatisticsEnabled(true);
//
//        doctorService.getAllDoctors(); // first call
//        doctorService.getAllDoctors(); // second call
//
//        System.out.println("Second-level cache hit count: " + stats.getSecondLevelCacheHitCount());
//        System.out.println("Second-level cache miss count: " + stats.getSecondLevelCacheMissCount());
//    }
//}
