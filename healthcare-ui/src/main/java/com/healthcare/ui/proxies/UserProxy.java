package com.healthcare.ui.proxies;

import com.healthcare.ui.model.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// "user-service" est le nom que nous avons donné au service dans Consul
@FeignClient(name = "user-service", url = "http://localhost:8082")
public interface UserProxy {

    // On imite exactement la méthode POST du UserController du microservice
    @PostMapping("/api/users")
    UserDTO createUser(@RequestBody UserDTO user);
}