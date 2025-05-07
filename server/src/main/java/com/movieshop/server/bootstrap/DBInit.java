//package com.movieshop.server.bootstrap;
//
//import com.movieshop.server.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DBInit implements CommandLineRunner {
//    @Autowired
//    private final UserRepository repo;
//
//    public DBInit(UserRepository repo) {
//        this.repo = repo;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        repo.count();
//    }
//}
