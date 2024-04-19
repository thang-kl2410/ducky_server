package com.thangkl2420.server_ducky.service;

import com.thangkl2420.server_ducky.dto.SpecializationUserId;
import com.thangkl2420.server_ducky.entity.RescueType;
import com.thangkl2420.server_ducky.entity.SpecializationUser;
import com.thangkl2420.server_ducky.entity.User;
import com.thangkl2420.server_ducky.repository.RescueTypeRepository;
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
    //private final SpecializationRepository repository;
    private final RescueTypeRepository repository;
    private final SpecializationUserRepository specializationUserRepository;
    private final UserRepository userRepository;

    public List<RescueType> getSpecialization(){
        List<RescueType> specializations = repository.findAll();
        return specializations;
    }

    public void setUserSpecialization(List<RescueType> specializations, Principal connectedUser){
        for(RescueType specialization:specializations){
            var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
            SpecializationUserId id = new SpecializationUserId(specialization.getId(), user.getId());
            SpecializationUser sp = new SpecializationUser();
            sp.setId(id);
            sp.setUser(user);
            sp.setRescueType(specialization);
            specializationUserRepository.save(sp);
        }
    }

    public void removeUserSpecialization(RescueType specialization, Principal connectedUser){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        SpecializationUserId id = new SpecializationUserId(specialization.getId(), user.getId());
        specializationUserRepository.deleteById(id);
    }


}
