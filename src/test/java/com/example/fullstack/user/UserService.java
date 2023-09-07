package com.example.fullstack.user;
import java.util.List;
import java.util.Optional;
import io.smallrye.mutiny.Uni;
import com.example.fullstack.process.Process;
import com.example.fullstack.task.Task;
import io.smallrye.mutiny.Uni;
import org.hibernate.ObjectNotFoundException;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserService {
    public Uni<List<User>> list() {
        return User.listAll();
    }
    public Uni<User> findByName(String name) {
        return User.find("name",name).firstResult();
    }
    public Uni<User> findById(Long id) {
        return User.<User>findById(id).onItem().failWith(() -> new ObjectNotFoundException(id, User.class.getName()));
    }
    public Uni<User> findByEmail(String email) {
        return User.find("email",email).firstResult();
    }
    @ReactiveTransactional
    public Uni<User> save(User user) {
        user.password = BcryptUtil.bcryptHash(user.password);
        return user.persistAndFlush().onItem().transformToUni(u -> Uni.createFrom().item(user));
    }
    @ReactiveTransactional
    public Uni<User> update(User user) {
        return User.<User>findById(user.id).onItem().ifNotNull().failWith(() -> new ObjectNotFoundException(user.id, User.class.getName()));
    }
    @ReactiveTransactional
    public Uni<User> delete(Long id) {
        return User.<User>findById(id).onItem().ifNotNull().transformToUni(u -> u.delete().onItem().transformToUni(v -> Uni.createFrom().item(u)));
    }
    public Uni<User> getActiveUser(){
        return User.find("active",true).firstResult();
}


}
