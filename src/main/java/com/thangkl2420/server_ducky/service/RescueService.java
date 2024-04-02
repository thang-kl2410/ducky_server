package com.thangkl2420.server_ducky.service;

import com.thangkl2420.server_ducky.dto.UserRescueCallId;
import com.thangkl2420.server_ducky.entity.*;
import com.thangkl2420.server_ducky.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RescueService {
    private final RescueRepository rescueRepository;
    private final RescueCallRepository rescueCallRepository;
    private final RescueStateRepository rescueStateRepository;
    private final RescueTypeRepository rescueTypeRepository;
    private final SpecializationUserRepository specializationUserRepository;
    private final UserRescueCallRepository userRescueCallRepository;
    private final RescueDetailRepository rescueDetailRepository;
    private final UserRepository userRepository;

    public void createRescueCall(Principal connectedUser, RescueCall rescueCall){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        RescueCall rc = new RescueCall();
        RescueState rs = rescueStateRepository.getById(0);
        rc.setRescue(rescueCall.getRescue());
        rc.setRescueState(rs);
        rc.setDescription(rescueCall.getDescription());
        rc.setImage(rescueCall.getImage());
        rc.setVideo(rescueCall.getVideo());
        rc.setLongitude(rescueCall.getLongitude());
        rc.setLatitude(rescueCall.getLatitude());
        rc.setTimestamp(rescueCall.getTimestamp());
        RescueCall r = rescueCallRepository.save(rc);

        UserRescueCall urc = new UserRescueCall();
        urc.setId(new UserRescueCallId(user.getId(), r.getId()));
        urc.setCreator(true);
        urc.setRescueCall(r);
        urc.setUser(user);
        userRescueCallRepository.save(urc);
    }
}
