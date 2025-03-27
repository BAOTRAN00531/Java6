package com.example.assigment_java6.service;

import com.example.assigment_java6.domain.User;
import com.example.assigment_java6.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    //Handle account logic
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
   //To create account
    public User handleCreateAccount(User user) {
        return userRepository.save(user);
    }
    //To delete account
    public void handleDeleteAccount(long id ) {
         this.userRepository.deleteById(id);
    }
    //To get account by id ,if not found to return empty
    public User handleGetAccountbyId(long id) {
        Optional<User> account = this.userRepository.findById(id);
        if (account.isPresent()) {
            return account.get();
        }
        return null;

    }
    //To get account by string (name or email)
    public User handleGetAccountbyUsername(String username) {
        return this.userRepository.findByEmail(username);
    }
    //To get all account
    public List<User> handlegetallAccount() {
        return this.userRepository.findAll();
    }
    //First,function will find account by id when actor fill raw data.
    //After that,if function can be found data is valid ,to do update new data
    // Else will be return page null
    public User handleUpateAccount(User user) {
        //TODO:After fix model Account then add more 4 variable about fullname,photo,phone,address and set this variable
        User userUpdate = this.handleGetAccountbyId(user.getId());
       if (userUpdate != null) {
           userUpdate.setFullname(user.getFullname());
           userUpdate.setEmail(user.getEmail());
           userUpdate.setPassword(user.getPassword());
           userUpdate.setAddress(user.getAddress());
           userUpdate.setPhone(user.getPhone());
           userUpdate.setPhoto(user.getPhoto());

           userUpdate = this.userRepository.save(userUpdate);

       }
        return userUpdate;
    }
}
