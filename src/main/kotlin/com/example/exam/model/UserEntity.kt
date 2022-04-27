package com.example.exam.model

import javax.persistence.*

@Entity
@Table(name = "users")
class UserEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_user_id_seq")
    @SequenceGenerator(
        name = "users_user_id_seq",
        allocationSize = 1
    )
    @Column(name = "user_id")
    val id: Long? = null,

    @Column(name = "user_name")
    val userName: String,

    @Column(name = "user_password")
    val userPassword: String,

    @Column(name = "user_enabled")
    val userEnabled: Boolean? = true,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "users_authorities",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "authority_id")]
    )
    val authorities: MutableList<AuthorityEntity> = mutableListOf()
){
    override fun toString(): String {
        return "UserEntity(id=$id, userName='$userName', userPassword='$userPassword', userEnabled=$userEnabled, authorities=$authorities)"
    }
}