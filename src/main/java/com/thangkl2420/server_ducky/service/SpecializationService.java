package com.thangkl2420.server_ducky.service;

import com.thangkl2420.server_ducky.dto.SpecializationUserId;
import com.thangkl2420.server_ducky.entity.Specialization;
import com.thangkl2420.server_ducky.entity.SpecializationUser;
import com.thangkl2420.server_ducky.entity.User;
import com.thangkl2420.server_ducky.repository.SpecializationRepository;
import com.thangkl2420.server_ducky.repository.SpecializationUserRepository;
import com.thangkl2420.server_ducky.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecializationService {
    private final SpecializationRepository repository;
    private final SpecializationUserRepository specializationUserRepository;
    private final UserRepository userRepository;

    List<Specialization> getSpecialization(){
        List<Specialization> specializations = repository.findAll();
        return specializations;
    }

    void setUserSpecialization(List<Specialization> specializations, Principal connectedUser){
        for(Specialization specialization:specializations){
            var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
            SpecializationUserId id = new SpecializationUserId(specialization.getId(), user.getId());
            specializationUserRepository.save(new SpecializationUser(id, null, null));
        }
    }

    void removeUserSpecialization(Specialization specialization, Principal connectedUser){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        SpecializationUserId id = new SpecializationUserId(specialization.getId(), user.getId());
        specializationUserRepository.deleteById(id);
    }

//    List<SpecializationUser> getAllByUser(Integer id){
//        return specializationUserRepository.findAllByUser(id);
//    }


}
