package com.org.os.service;

import com.org.os.persistance.entity.Users;
import com.org.os.persistance.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {

    private UsersRepository repository;

    public Users getUserByUsername(String username) {
        return repository.findByUsername(username).get();
    }
}
