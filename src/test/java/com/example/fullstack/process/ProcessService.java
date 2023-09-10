package com.example.fullstack.process;

import com.example.fullstack.task.Task;
import com.example.fullstack.user.UserService;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.security.UnauthorizedException;
import io.smallrye.mutiny.Uni;
import org.hibernate.ObjectNotFoundException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class ProcessService {

    private final UserService userService;

    @Inject
    public ProcessService(UserService userService) {
        this.userService = userService;
    }

    public Uni<Process> findById(long id) {
        return userService.getCurrentUser()
                .chain(user -> Process.<Process>findById(id)
                        .onItem().ifNull().failWith(() -> new ObjectNotFoundException(id, "Project"))
                        .onItem().invoke(project -> {
                            if (!user.equals(project.user)) {
                                throw new UnauthorizedException("You are not allowed to update this project");
                            }
                        }));
    }

    public Uni<List<Process>> listForUser() {
        return userService.getCurrentUser()
                .chain(user -> Process.find("user", user).list());
    }

    @ReactiveTransactional
    public Uni<Process> create(Process process) {
        return userService.getCurrentUser()
                .chain(user -> {
                    process.user = user;
                    return process.persistAndFlush();
                });
    }

    @ReactiveTransactional
    public Uni<Process> update(Process project) {
        return findById(project.id)
                .chain(p -> Process.getSession())
                .chain(s -> s.merge(project));
    }

    @ReactiveTransactional
    public Uni<Void> delete(long id) {
        return findById(id)
                .chain(p -> Task.update("project = null where project = ?1", p)
                        .chain(i -> p.delete()));
    }
}