package br.com.tmvolpato.ms.domain.repository;

import br.com.tmvolpato.ms.domain.model.UserEntity;
import br.com.tmvolpato.ms.domain.model.UserEntity_;
import br.com.tmvolpato.ms.infrastructure.exception.NonInstantiableException;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

import static br.com.tmvolpato.ms.infrastructure.constants.MessageErrorConstant.ERROR_CANNOT_BE_INSTANTIATED;

public final class UserSpecification {

    private static final String PERCENTAGE = "%";

    public static Specification<UserEntity> findUserByUserExternalId(final UUID userExternalId) {
        return (root, query, criteriaBuilder) -> {

            final var propUserExternalId = root.get(UserEntity_.EXTERNAL_ID);
            return criteriaBuilder.equal(propUserExternalId, userExternalId);
        };
    }

    public static Specification<UserEntity> findUserByName(final String name) {
        return (root, query, criteriaBuilder) -> {

            final var propName = root.get(UserEntity_.name);
            return criteriaBuilder.like(criteriaBuilder.lower(propName),
                    PERCENTAGE.concat(name.trim().toLowerCase()).concat(PERCENTAGE));
        };
    }

    public static Specification<UserEntity> findUserByLastName(final String lastName) {
        return (root, query, criteriaBuilder) -> {

            final var propLastName = root.get(UserEntity_.lastName);
            return criteriaBuilder.like(criteriaBuilder.lower(propLastName),
                    PERCENTAGE.concat(lastName.trim().toLowerCase()).concat(PERCENTAGE));
        };
    }

    public static Specification<UserEntity> findUserByEmail(final String email) {
        return (root, query, criteriaBuilder) -> {

            final var propEmail = root.get(UserEntity_.email);
            return criteriaBuilder.like(criteriaBuilder.lower(propEmail),
                    PERCENTAGE.concat(email.trim().toLowerCase()).concat(PERCENTAGE));
        };
    }

    private UserSpecification() {
        throw new NonInstantiableException(ERROR_CANNOT_BE_INSTANTIATED);
    }
}
