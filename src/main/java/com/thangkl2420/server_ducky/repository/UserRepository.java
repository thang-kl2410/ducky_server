package com.thangkl2420.server_ducky.repository;

import java.util.List;
import java.util.Optional;

import com.thangkl2420.server_ducky.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {
  Optional<User> findByEmail(String email);
  Optional<User> findById(Integer id);
  @Query("SELECT u FROM User u WHERE u.firstname LIKE %:keyword% OR u.lastname LIKE %:keyword%")
  List<User> findByFirstnameOrLastnameContaining(@Param("keyword") String keyword);
  @Query("SELECT u.idDevice FROM User u WHERE u.id = :id")
  Optional<String> findIdDeviceById(Integer id);
  @Query("SELECT u.idDevice FROM User u")
  List<String> findAllDeviceTokens();

//  List<User> findByFirstNameContaining(String firstName);
//  List<User> findByLastNameContaining(String lastName);
}
