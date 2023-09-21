package com.example.fullstack;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("blocking-endpoint")
    public class BlockingEndpoint {
        @GET
        public String hello() {
            return "Hello World";
        }
    }

