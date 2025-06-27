package com.movieshop.server.model;

import java.time.OffsetDateTime;

public interface UserWithAddressProjection {
    Integer getId();
    String getEmail();
    String getRole();
    String getFirstName();
    String getLastName();
    AddressProjection getAddress();
    StoreProjection getStore();
    String getPicture();
    boolean isAccountNonExpired();
    boolean isAccountNonLocked();
    boolean isCredentialsNonExpired();
    boolean isEnabled();
    OffsetDateTime getCreateAt();
    OffsetDateTime getLastUpdate();



    interface StoreProjection {
        Integer getId();
    }
}
