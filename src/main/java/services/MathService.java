package services;

import io.swagger.v3.oas.annotations.Operation;
import manager.MathManager;
import manager.MathManagerImpl;
import models.Alumne;
import models.Institut;
import models.Operacio;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/math")
public class MathService {

    private MathManager mm;

    public MathService() {
        this.mm = MathManagerImpl.getInstance();
    }

    @POST
    @Operation(summary = "Afegir un institut")
    @Path("/institut")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response afegirInstitut(Institut institut) {
        try {
            mm.afegirInstitut(institut);
            return Response.status(201).build();
        } catch (IllegalArgumentException e) {
            return Response.status(400).entity(e.getMessage()).build();
        }
    }

    @POST
    @Operation(summary = "Afegir un alumne")
    @Path("/alumne")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response afegirAlumne(Alumne alumne) {
        try {
            mm.afegirAlumne(alumne);
            return Response.status(201).build();
        } catch (IllegalArgumentException e) {
            return Response.status(400).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(404).entity(e.getMessage()).build();
        }
    }

    @POST
    @Operation(summary = "Requerir una operació RPN")
    @Path("/operacio")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response requerirOperacio(Operacio operacio) {
        try {
            mm.requerirOperacio(operacio);
            return Response.status(201).build();
        } catch (IllegalArgumentException e) {
            return Response.status(400).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(404).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Operation(summary = "Processar la primera operació pendent")
    @Path("/operacio/processar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response processarOperacio() {
        try {
            Operacio op = mm.processarOperacio();
            return Response.status(200).entity(op).build();
        } catch (Exception e) {
            return Response.status(404).entity(e.getMessage()).build();
        }
    }

    @GET
    @Operation(summary = "Llistat d'operacions per institut")
    @Path("/operacions/institut/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response operacionsPerInstitut(@PathParam("id") String id) {
        try {
            List<Operacio> ops = mm.llistatOperacionsPerInstitut(id);
            GenericEntity<List<Operacio>> entity =
                    new GenericEntity<>(ops) {};
            return Response.status(200).entity(entity).build();
        } catch (IllegalArgumentException e) {
            return Response.status(400).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(404).entity(e.getMessage()).build();
        }
    }

    @GET
    @Operation(summary = "Llistat d'operacions per alumne")
    @Path("/operacions/alumne/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response operacionsPerAlumne(@PathParam("id") String id) {
        try {
            List<Operacio> ops = mm.llistatOperacionsPerAlumne(id);
            GenericEntity<List<Operacio>> entity =
                    new GenericEntity<>(ops) {};
            return Response.status(200).entity(entity).build();
        } catch (IllegalArgumentException e) {
            return Response.status(400).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(404).entity(e.getMessage()).build();
        }
    }

    @GET
    @Operation(summary = "Ranking d'instituts per operacions")
    @Path("/instituts/ranking")
    @Produces(MediaType.APPLICATION_JSON)
    public Response rankingInstituts() {
        List<Institut> instituts = mm.llistatInstitutsPorOperacions();
        GenericEntity<List<Institut>> entity =
                new GenericEntity<>(instituts) {};
        return Response.status(200).entity(entity).build();
    }
}
