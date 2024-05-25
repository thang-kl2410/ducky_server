package com.thangkl2420.server_ducky.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.thangkl2420.server_ducky.dto.rescue.ParticipateId;
import com.thangkl2420.server_ducky.dto.rescue.RescueCallDto;
import com.thangkl2420.server_ducky.dto.rescue.RescueCallResponseDto;
import com.thangkl2420.server_ducky.dto.user.SpecializationUserId;
import com.thangkl2420.server_ducky.dto.rescue.UserRescueCallId;
import com.thangkl2420.server_ducky.entity.rescue.*;
import com.thangkl2420.server_ducky.entity.user.SpecializationUser;
import com.thangkl2420.server_ducky.entity.user.User;
import com.thangkl2420.server_ducky.entity.user.UserAction;
import com.thangkl2420.server_ducky.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

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
    private final NotificationService notificationService;
    private final UserActionRepository userActionRepository;
    private final ParticipateRepository participateRepository;

//    Map<Integer, List<User>> draft = new HashMap<>();
//    Map<Integer, List<RescueCall>> userWithRescue = new HashMap<>();

    public RescueCallResponseDto createRescueCall(Principal connectedUser, RescueCall rescueCall){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        RescueCall savedRescueCall = rescueCallRepository.save(rescueCall);

        if(rescueCall.getRescuePersonnel().getId() == 1){
            UserRescueCall userRescueCall = new UserRescueCall();
            userRescueCall.setId(new UserRescueCallId(savedRescueCall.getId(), user.getId()));
            userRescueCall.setCreator(true);
            userRescueCallRepository.save(userRescueCall);

            List<User> users = participateRepository.findFreeUsers(rescueCall.getRescue().getId());
            List<User> temp = users.stream()
                    .sorted((u, v) -> Double.compare(
                            calculateDistance(u.getLatitude(), u.getLongitude(), user.getLatitude(), user.getLongitude()),
                            calculateDistance(v.getLatitude(), v.getLongitude(), user.getLatitude(), user.getLongitude())
                    ))
                    .filter(u -> u.getId() != user.getId())
                    .filter(u -> calculateDistance(u.getLatitude(), u.getLongitude(), user.getLatitude(), user.getLongitude()) < 15)
                    .limit(20)
                    .collect(Collectors.toList());

            return new RescueCallResponseDto(savedRescueCall, temp);
        } else {
            List<User> users = userRepository.findAll();
            return new RescueCallResponseDto(savedRescueCall, users);
        }
    }

    public Boolean confirmParticipate(Integer rescueId, Principal connectedUser, double distance){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        UserAction ua = userActionRepository.getById(3);
        user.setUserAction(ua);
        userRepository.save(user);
        Participate p = new Participate();
        p.setId(new ParticipateId(rescueId, user.getId()));
        p.setDistance(distance);
        p.setIsFinish(false);
        participateRepository.save(p);
        return true;
    }

    public User finishWaiting(Integer rescueId) {
        User u = participateRepository.findUserWithMinimumDistance(rescueId).orElse(null);
        return u;
    }

    public Boolean completeRescue(Integer rescueId) {
        participateRepository.updateIsFinishToTrueByRescueCallId(rescueId);
        return true;
    }

    @Transactional
    public void updateUserRescue(Principal connectedUser, List<Integer> ids){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        specializationUserRepository.deleteAllByUser(user);
        if(!ids.isEmpty()){
            List<SpecializationUser> sus = new ArrayList<>();
            for(Integer rt:ids) {
                SpecializationUser su = new SpecializationUser();
                su.setId(new SpecializationUserId(rt, user.getId()));
                sus.add(su);
            }
            specializationUserRepository.saveAll(sus);
        }
    }

    public void confirmRescue(Principal connectedUser, Integer id){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        RescueCall r = rescueCallRepository.findAllById(id).orElse(null);
        List<User> users = rescueCallRepository.findConfirmUsersById(id);

        if(!users.isEmpty()){
            UserRescueCall urc = new UserRescueCall();
            urc.setId(new UserRescueCallId(user.getId(), r.getId()));
            urc.setCreator(false);
            userRescueCallRepository.save(urc);
            User u = rescueCallRepository.findCreateUserById(id).orElse(null);
            try {
                notificationService.sendFCMById(u.getId(), "", "", user.getId());
            } catch (FirebaseMessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<RescueType> getSpecializationByUser(Principal connectedUser){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        return specializationUserRepository.findAllByUser(user);
    }

    public List<RescueDetail> getAllRescueByType(Integer id){
        return  rescueDetailRepository.getAllByType(id);
    }

    public List<User> getAllUserByType(Integer id){
        return  rescueDetailRepository.getAllUserByType(id);
    }


    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;

        return distance;
    }

    public RescueCallDto getRescueCallDetailById(Integer id){
        RescueCall call = rescueCallRepository.findById(id).orElseThrow(null);
        User user = rescueCallRepository.findCreateUserById(id).orElseThrow(null);
        return new RescueCallDto(call, user);
    }

    public RescueCallDto getCurrentRescueCallDetail(Principal connectedUser){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        RescueCall rc = participateRepository.findMyCurrentRescueCall(user.getId()).orElse(new RescueCall());
        User userCreated = rescueCallRepository.findCreateUserById(rc.getId()).orElse(new User());
        return new RescueCallDto(rc, userCreated);
    }

    public List<RescueCall> getMyCurrentRescueCallDetail(Principal connectedUser){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        return rescueCallRepository.findRescueCallById(user.getId());
    }
}
