package com.thangkl2420.server_ducky.service;

import com.thangkl2420.server_ducky.dto.ChangePasswordRequest;
import com.thangkl2420.server_ducky.dto.UpdateProfileRequest;
import com.thangkl2420.server_ducky.entity.User;
import com.thangkl2420.server_ducky.entity.UserState;
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
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        repository.save(user);
    }

    public List<User> getAllUser(Principal connectedUser){
        List<User> users = repository.findAll();
//        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
//        return users.stream()
//                .filter(u -> !u.getId().equals(user.getId()))
//                .collect(Collectors.toList());
        return users;
    }

    public void saveDeviceToken(String deviceToken, Principal connectedUser){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        user.setIdDevice(deviceToken);
        repository.save(user);
    }

    public void updateState(Integer state, Principal connectedUser){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        UserState userState = userStateRepository.findById(state).orElse(null);
        user.setUserState(userState);
        repository.save(user);
    }

    public void setLocation(double longitude, double latitude, Principal connectedUser){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        user.setLongitude(longitude);
        user.setLatitude(latitude);
        repository.save(user);
    }

    public void updateProfile(UpdateProfileRequest request, Principal connectedUser){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setBirthday(request.getBirthday());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAvatar(request.getAvatar());
        user.setBackground(request.getBackground());
        user.setDescription(request.getDescription());
        user.setAddress(request.getAddress());
        repository.save(user);
    }

    public User getById(Integer id){
        User user = repository.findById(id).orElse(null);
        return  user;
    }

    public List<User> searchByName(String keyword){
        List<User> users = repository.findByFirstnameOrLastnameContaining(keyword);
        return  users;
    }

    public List<User> getNearPeople(Principal connectedUser) {
        List<User> users = repository.findAll();
        var u = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        return users.stream()
                .filter(user -> !isNull(user.getLatitude()) && !isNull(user.getLongitude()))
                .filter(user -> calculateDistance(user.getLatitude(), user.getLongitude(),
                        u.getLatitude(), u.getLongitude()) < 10)
                .toList();
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Bán kính trái đất trong km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;

        return distance;
    }
}
