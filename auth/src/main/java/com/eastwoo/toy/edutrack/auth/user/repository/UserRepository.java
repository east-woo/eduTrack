package com.eastwoo.toy.edutrack.auth.user.repository;

import com.eastwoo.toy.edutrack.auth.user.entity.User;
import com.eastwoo.toy.edutrack.auth.user.enumtype.UserRole;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Modifying
    @Query("delete from User u where u.role = :role")
    int deleteByRole(@Param("role") UserRole role);
}