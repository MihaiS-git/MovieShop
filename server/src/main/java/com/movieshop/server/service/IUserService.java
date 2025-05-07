package com.movieshop.server.service;

import com.movieshop.server.domain.User;

public interface IUserService {
    User getUserByEmail(String email);


}
