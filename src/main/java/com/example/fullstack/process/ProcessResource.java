package com.example.fullstack.process;

import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.ResponseStatus;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/api/v1/processes")
public class ProcessResource {

    private final ProcessService processService;

    @Inject
    public ProcessResource(ProcessService processService) {
        this.processService = processService;
    }

    @GET
    public Uni<List<Process>> get() {
        return processService.listForUser();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ResponseStatus(201)
    public Uni<Process> create(Process project) {
        return processService.create(project);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Uni<Process> update(@PathParam("id") long id, Process project) {
        project.id = id;
        return processService.update(project);
    }

    @DELETE
    @Path("/{id}")
    public Uni<Void> delete(@PathParam("id") long id) {
        return processService.delete(id);
    }

}