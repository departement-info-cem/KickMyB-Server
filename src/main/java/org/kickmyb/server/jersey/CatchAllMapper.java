package org.kickmyb.server.jersey;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by joris on 15-10-15.
 */
// Jersey based error handling
@Provider
public class CatchAllMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception ex) {
        ex.printStackTrace();
        System.out.println(ex.getClass().getSimpleName());
        return Response.status(Response.Status.BAD_REQUEST).entity(ex.getClass().getSimpleName()).build();
    }

}