package br.com.tmvolpato.ms.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.UUID;

import static br.com.tmvolpato.ms.domain.constants.ColumnConstant.EMAIL;
import static br.com.tmvolpato.ms.domain.constants.ColumnConstant.EXTERNAL_ID;
import static br.com.tmvolpato.ms.domain.constants.ColumnConstant.ID;
import static br.com.tmvolpato.ms.domain.constants.ColumnConstant.LAST_NAME;
import static br.com.tmvolpato.ms.domain.constants.ColumnConstant.NAME;
import static br.com.tmvolpato.ms.domain.constants.ColumnConstant.PASSWORD;
import static br.com.tmvolpato.ms.domain.constants.ColumnConstant.STATUS;
import static br.com.tmvolpato.ms.domain.constants.TableConstant.USERS;

@Entity
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(exclude = {"password"})
@EqualsAndHashCode(of = {"id", "externalId", "email"})
@Table(name = USERS)
public class UserEntity implements Serializable {

    private static final long serialVersionUID = -1758866310903037468L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID, updatable = false)
    private Long id;

    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(name = EXTERNAL_ID, unique = true, updatable = false, nullable = false, columnDefinition = "bpchar(36)")
    private final UUID externalId = UUID.randomUUID();

    @NotBlank
    @Column(name = NAME, nullable = false)
    private String name;

    @NotBlank
    @Column(name = LAST_NAME, nullable = false)
    private String lastName;

    @NotBlank
    @Email
    @Column(name = EMAIL, nullable = false, unique = true)
    private String email;

    @Setter
    @NotBlank
    @Column(name = PASSWORD, nullable = false)
    private String password;

    @Column(name = STATUS)
    private boolean status;

    public void allowedChanges(final String name, final String lastName, final boolean status) {
        this.name = name;
        this.lastName = lastName;
        this.status = status;
    }

}
