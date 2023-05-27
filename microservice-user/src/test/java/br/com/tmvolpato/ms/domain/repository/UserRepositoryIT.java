package br.com.tmvolpato.ms.domain.repository;

import br.com.tmvolpato.ms.AbstractPostgreSQLTestContainerIT;
import br.com.tmvolpato.ms.domain.model.UserEntityHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static br.com.tmvolpato.ms.domain.repository.UserSpecification.findUserByUserExternalId;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.data.jpa.domain.Specification.where;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class UserRepositoryIT extends AbstractPostgreSQLTestContainerIT {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    protected void initEach() {
        this.userRepository.deleteAll();
    }

    @Test
    @DisplayName("Persist object successfully")
    void givenUserEntity_whenDataIsValid_thenSavedSuccessfully() {

        final var userEntity = UserEntityHelper.userEntityOneValid();

        final var userEntitySaved = this.userRepository.save(userEntity);

        assertThat(userEntitySaved, notNullValue());
        assertThat(userEntitySaved.getExternalId(), notNullValue());
        assertThat(userEntitySaved.getName(), is("Maria"));
        assertThat(userEntitySaved.getLastName(), is("Silva"));
        assertThat(userEntitySaved.getEmail(), is("maria@email.com"));
        assertThat(userEntitySaved.getPassword(), notNullValue());
    }

    @Test
    @DisplayName("Update object successfully")
    void givenUserEntity_whenDataIsValid_thenUpdatedSuccessfully() {

        final var userEntity = UserEntityHelper.userEntityOneValid();
        final var userEntitySaved = this.userRepository.save(userEntity);

        assertThat(userEntitySaved.getName(), is("Maria"));
        assertThat(userEntitySaved.getLastName(), is("Silva"));

        final var userEntityUpdate = userEntitySaved;

        userEntityUpdate.allowedChanges("NameTest", "LastNameTest", true);

        final var userEntityUpdated = this.userRepository.saveAndFlush(userEntityUpdate);

        assertThat(userEntityUpdated.getName(), is("NameTest"));
        assertThat(userEntityUpdated.getLastName(), is("LastNameTest"));
    }

    @Test
    @DisplayName("Delete object successfully")
    void givenUserEntity_whenDataIsValid_thenDeletedSuccessfully() {

        final var userEntity = UserEntityHelper.userEntityOneValid();
        final var userEntitySaved = this.userRepository.save(userEntity);

        this.userRepository.delete(userEntitySaved);

        final var exists = this.userRepository.existsUserByEmail(userEntitySaved.getEmail());

        assertThat(exists, is(false));
    }

    @Test
    @DisplayName("Find user by user external id successfully")
    void givenUserExternalId_whenFindUser_thenReturnUserSuccessfully() {

        final var userEntity = UserEntityHelper.userEntityOneValid();
        final var userEntitySaved = this.userRepository.save(userEntity);

        final var userEntityFound = this.userRepository
                .findOne(where(findUserByUserExternalId(userEntitySaved.getExternalId())))
                .orElse(null);

        assertThat(userEntityFound, notNullValue());
        assertThat(userEntityFound.getExternalId(), notNullValue());
        assertThat(userEntityFound.getName(), is("Maria"));
        assertThat(userEntityFound.getLastName(), is("Silva"));
        assertThat(userEntityFound.getEmail(), is("maria@email.com"));
        assertThat(userEntityFound.getPassword(), notNullValue());
    }

}
