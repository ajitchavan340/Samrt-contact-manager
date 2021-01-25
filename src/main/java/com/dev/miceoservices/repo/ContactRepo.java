package com.dev.miceoservices.repo;

import com.dev.miceoservices.model.Contact;
import com.dev.miceoservices.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepo extends JpaRepository<Contact , Integer> {
    @Query("from Contact as c where c.user.user_id =:userId")
    public Page<Contact> findContactByUser (@Param("userId") int userId , Pageable pageable);

    public List<Contact> findByNameContainingAndUser(String name, User user);
}
