package com.example.projectdropbox.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity(name = "dropboxUsers")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username, password;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> authorities;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private List<Folder> folders;

    public User(String username, String password, List<String> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }
    public User() {}
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        var list = new ArrayList<SimpleGrantedAuthority>();

        for (var authority : this.authorities) {
            list.add(new SimpleGrantedAuthority(authority));
        }
        return list;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        User otherUser = (User) obj;
        return id == otherUser.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
