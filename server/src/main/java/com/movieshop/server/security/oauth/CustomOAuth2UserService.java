package com.movieshop.server.security.oauth;

import com.movieshop.server.domain.CustomOAuth2User;
import com.movieshop.server.domain.Role;
import com.movieshop.server.domain.User;
import com.movieshop.server.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;


@Component
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        String email = oauth2User.getAttribute("email");

        if(email == null) {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(oauth2User.getAttribute("name"));
            newUser.setPictureUrl(oauth2User.getAttribute("picture"));
            newUser.setRole(Role.CUSTOMER);
            return userRepository.save(newUser);
        });

        return new CustomOAuth2User(oauth2User, user);
    }
}
