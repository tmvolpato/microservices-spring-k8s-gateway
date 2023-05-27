package br.com.tmvolpato.ms.application.hateoas;

import br.com.tmvolpato.ms.application.form.UserFormDetail;
import br.com.tmvolpato.ms.application.dto.UserDto;
import br.com.tmvolpato.ms.interfaces.web.UserResource;
import org.springframework.context.annotation.Lazy;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Lazy
@Component
public class UserRepresentationAssemble implements RepresentationModelAssembler<UserDto, UserDto> {

    private static final String CREATE_USER = "Create user";
    private static final String UPDATE_USER = "Update user";
    private static final String FIND_USER = "Find user";
    private static final String DELETE_USER = "Delete user";


    @Override
    public UserDto toModel(final UserDto userRepresentation) {

        userRepresentation.add(linkTo(UserResource.class).withRel(CREATE_USER));

        userRepresentation.add(linkTo(methodOn(UserResource.class)
                .update(userRepresentation.getUserExternalId(), UserFormDetail.builder().build()))
                .withRel(UPDATE_USER));

        userRepresentation.add(linkTo(methodOn(UserResource.class)
                .findUser(userRepresentation.getUserExternalId()))
                .withRel(FIND_USER));

        userRepresentation.add(linkTo(methodOn(UserResource.class)
                .delete(userRepresentation.getUserExternalId()))
                .withRel(DELETE_USER));

        return userRepresentation;
    }
}
