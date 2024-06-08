package com.thangkl2420.server_ducky.service;

import com.thangkl2420.server_ducky.dto.auth.ChangePasswordRequest;
import com.thangkl2420.server_ducky.dto.user.FollowingId;
import com.thangkl2420.server_ducky.dto.user.UpdateProfileRequest;
import com.thangkl2420.server_ducky.entity.rescue.RescueCall;
import com.thangkl2420.server_ducky.entity.user.Following;
import com.thangkl2420.server_ducky.entity.user.User;
import com.thangkl2420.server_ducky.entity.user.UserState;
import com.thangkl2420.server_ducky.repository.FollowingRepository;
import com.thangkl2420.server_ducky.repository.RescueDetailRepository;
import com.thangkl2420.server_ducky.repository.UserRepository;
import com.thangkl2420.server_ducky.repository.UserStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

import static java.util.Objects.isNull;


@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final UserStateRepository userStateRepository;
    private final RescueDetailRepository rescueDetailRepository;
    private final FollowingRepository followingRepository;

    public User changePassword(ChangePasswordRequest request, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        return repository.save(user);
    }

    public List<User> getAllUser(Principal connectedUser){
        List<User> users = repository.findAll();
        return users;
    }

    public User saveDeviceToken(String deviceToken, Principal connectedUser){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        user.setIdDevice(deviceToken);
        return repository.save(user);
    }

    public User updateState(Integer state, Principal connectedUser){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        UserState userState = userStateRepository.findById(state).orElse(null);
        user.setUserState(userState);
        return repository.save(user);
    }

    public User setLocation(double longitude, double latitude, Principal connectedUser){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        user.setLongitude(longitude);
        user.setLatitude(latitude);
        return repository.save(user);
    }

    public User updateProfile(UpdateProfileRequest request, Principal connectedUser){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setBirthday(request.getBirthday());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAvatar(request.getAvatar());
        user.setBackground(request.getBackground());
        user.setDescription(request.getDescription());
        user.setAddress(request.getAddress());
        return repository.save(user);
    }

    public User getById(Integer id, Principal connectedUser){
        User current = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        User user = repository.findById(id).orElse(null);
        FollowingId fli = new FollowingId(id, current.getId());
        Following following = followingRepository.findById(fli).orElse(null);
        if(following == null){
            user.setIsFollow(false);
        } else {
            user.setIsFollow(true);
        }
        return  user;
    }

    public List<User> searchByName(String keyword){
        List<User> users = repository.findByFirstnameOrLastnameContaining(keyword.toLowerCase());
        return  users;
    }

    public List<User> getNearPeople(Principal connectedUser) {
        List<User> users = repository.findAll();
        var u = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        return users.stream()
                .filter(user -> u.getId() != user.getId())
                .filter(user -> !isNull(user.getLatitude()) && !isNull(user.getLongitude()))
                .filter(user -> calculateDistance(user.getLatitude(), user.getLongitude(),
                        u.getLatitude(), u.getLongitude()) < 10)
                .toList();
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

    public List<String> getAllTokenDeviceUserByRescue(Principal connectedUser, RescueCall rescueCall){
        List<String> devices = rescueDetailRepository.findUserByRescueDetail(rescueCall.getRescue().getId());

        return devices;
    }

    public List<User> getAll(){
        return repository.findAll();
    }

    public User followUser(Principal connectedUser, Integer id){
        var u = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        FollowingId fid = new FollowingId(id, u.getId());
        Following f = new Following();
        f.setId(fid);
        followingRepository.save(f);
        return getById(id, connectedUser);
    }

    public User cancelFollowUser(Principal connectedUser, Integer id){
        var u = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        FollowingId fid = new FollowingId(id, u.getId());
        followingRepository.deleteById(fid);
        return getById(id, connectedUser);
    }

    public List<User> getFollowers(Integer id){
        return followingRepository.getAllFollower(id);
    }

    public List<User> getWatchers(Integer id){
        return followingRepository.getAllWatcher(id);
    }
}
