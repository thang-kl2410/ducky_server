package com.thangkl2420.server_ducky.repository;

import java.util.List;
import java.util.Optional;

import com.thangkl2420.server_ducky.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {
  Optional<User> findByEmail(String email);
  Optional<User> findById(Integer id);
  //@Query("SELECT u FROM User u WHERE u.firstname LIKE %:keyword% OR u.lastname LIKE %:keyword%")
  @Query("SELECT u FROM User u WHERE LOWER(CONCAT(u.firstname, ' ', u.lastname)) LIKE %:keyword%")
  List<User> findByFirstnameOrLastnameContaining(@Param("keyword") String keyword);

  @Query("SELECT u.idDevice FROM User u")
  List<String> findAllDeviceTokens();
  @Query("SELECT u.id FROM User u")
  List<Integer> findAllIds();
  @Query("SELECT u.idDevice FROM User u WHERE u.id = :id")
  Optional<String> findDeviceToken(Integer id);
  boolean existsByEmail(String email);
}
