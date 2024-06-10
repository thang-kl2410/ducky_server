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
    private final RescueCallRepository rescueCallRepository;
    private final SpecializationUserRepository specializationUserRepository;
    private final UserRescueCallRepository userRescueCallRepository;
    private final RescueDetailRepository rescueDetailRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final UserActionRepository userActionRepository;
    private final ParticipateRepository participateRepository;

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
            UserAction ua = new UserAction(4, null, null);
            user.setUserAction(ua);
            userRepository.save(user);
            return new RescueCallResponseDto(savedRescueCall, temp);
        } else {
            List<User> users = userRepository.findAll();
            return new RescueCallResponseDto(savedRescueCall, users);
        }
    }

    public Boolean confirmParticipate(Integer rescueId, Principal connectedUser, double distance){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        User u = rescueCallRepository.findRescuerById(rescueId).orElse(null);
        RescueCall rc = rescueCallRepository.findById(rescueId).orElse(null);
        if(u != null || rc == null || rc.isFinish() == true){
            return false;
        } else {
            UserAction ua = new UserAction();
            ua.setId(2);
            user.setUserAction(ua);
            userRepository.save(user);
            Participate p = Participate
                    .builder()
                    .id(new ParticipateId(rescueId, user.getId()))
                    .distance(distance)
                    .isFinish(false)
                    .build();

            participateRepository.save(p);
            return true;
        }
    }

    public User finishWaiting(Integer rescueId) {
        User u = participateRepository.findUserWithMinimumDistance(rescueId).orElse(new User());
        if(u.getEmail() != null){
            participateRepository.deleteUserParticipate(rescueId, u.getId());
            participateRepository.finishParticipateUsers(rescueId);
            UserAction ua = new UserAction();
            ua.setId(3);
            u.setUserAction(ua);
            return userRepository.save(u);
        }
        return new User();
    }

//    DELETE FROM USER_NOTIFICATION un WHERE un.user_id IN (SELECT u.id FROM _USER u WHERE u.EMAIL IS NULL);
//    DELETE FROM _USER u WHERE u.EMAIL IS NULL;
    public User completeRescue(Integer rescueId) {
        participateRepository.updateIsFinishToTrueByRescueCallId(rescueId);
        participateRepository.finishRescueCall(rescueId);
        participateRepository.updateUserParticipate(rescueId);
        User u = rescueCallRepository.findCreateUserById(rescueId).orElse(null);
        if(u.getEmail() != null){
            UserAction ua = new UserAction(1, null, null);
            u.setUserAction(ua);
            u = userRepository.save(u);
        }
        return u;
    }

    public User completeRescue2(Integer id){
        User u = rescueCallRepository.findRescuerById(id).orElse(new User());
        if(u.getEmail() != null){
            UserAction ua = new UserAction();
            ua.setId(1);
            u.setUserAction(ua);
            userRepository.save(u);
        }
        return u;
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
        RescueCall call = rescueCallRepository.findById(id).orElse(new RescueCall());
        User user = rescueCallRepository.findCreateUserById(id).orElse(new User());
        User rescuer = rescueCallRepository.findRescuerById(id).orElse(new User());
        return new RescueCallDto(call, user, rescuer);
    }

    public RescueCallDto getCurrentRescueCallDetail(Principal connectedUser){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        if(user.getUserAction().getId() == 3){
            RescueCall rc = participateRepository.findMyCurrentRescueCall(user.getId()).orElse(new RescueCall());
            User userCreated = rescueCallRepository.findCreateUserById(rc.getId()).orElse(new User());
            return new RescueCallDto(rc, userCreated, user);
        } else if(user.getUserAction().getId() == 4 || user.getUserAction().getId() == 2){
            RescueCall rc = participateRepository.findMyCurrentRescueCall2(user.getId()).orElse(new RescueCall());
            User rescuer = rescueCallRepository.findRescuerById(rc.getId()).orElse(new User());
            return new RescueCallDto(rc, user, rescuer);
        } else {
            return new RescueCallDto();
        }
    }

    public List<RescueCall> getMyCurrentRescueCallDetail(Principal connectedUser){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        return rescueCallRepository.findRescueCallById(user.getId());
    }
}
