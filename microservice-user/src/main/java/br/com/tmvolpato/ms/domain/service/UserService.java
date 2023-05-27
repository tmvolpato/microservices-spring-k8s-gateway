package br.com.tmvolpato.ms.domain.service;

import br.com.tmvolpato.ms.domain.model.UserEntity;
import br.com.tmvolpato.ms.domain.repository.UserRepository;
import br.com.tmvolpato.ms.domain.repository.UserSpecification;
import br.com.tmvolpato.ms.infrastructure.exception.AlreadyExistsException;
import br.com.tmvolpato.ms.infrastructure.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static br.com.tmvolpato.ms.domain.repository.UserSpecification.findUserByLastName;
import static br.com.tmvolpato.ms.domain.repository.UserSpecification.findUserByName;
import static br.com.tmvolpato.ms.infrastructure.constants.UserMessageConstant.USER_ALREADY_EXISTS;
import static br.com.tmvolpato.ms.infrastructure.constants.UserMessageConstant.USER_NOT_FOUND;
import static org.springframework.data.jpa.domain.Specification.where;
import static org.springframework.util.StringUtils.hasLength;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity save(final UserEntity userEntity) {
        final var email = userEntity.getEmail();
        if (this.userRepository.existsUserByEmail(email)) throw new AlreadyExistsException(USER_ALREADY_EXISTS);

        return this.userRepository.save(userEntity);
    }

    public UserEntity update(final UserEntity userEntity) {
       return this.userRepository.saveAndFlush(userEntity);
    }

    public void delete(final UUID userExternalId) {
        final var userEntity = this.userRepository.findOne(where(UserSpecification
                        .findUserByUserExternalId(userExternalId)))
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        this.userRepository.delete(userEntity);
    }

    @Transactional(readOnly = true)
    public UserEntity findUserByUserExternalId(final UUID userExternalId) {
        return this.userRepository.findOne(where(UserSpecification.findUserByUserExternalId(userExternalId)))
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Page<UserEntity> list(final String search, final int page, final int size, final String orderBy, final String sortBy) {
        final Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(orderBy), sortBy);

        return hasLength(search) ? this.userRepository.findAll(where(findUserByName(search))
                .or(findUserByLastName(search))
                .or(UserSpecification.findUserByEmail(search)), pageable) : this.userRepository.findAll(pageable);
    }

}
