package org.alumni.repository;

import org.alumni.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// Extend MongoRepository and use String for the ID type.
public interface UserRepository extends MongoRepository<User, String> {

    // These derived query methods work exactly the same with MongoDB!
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
}