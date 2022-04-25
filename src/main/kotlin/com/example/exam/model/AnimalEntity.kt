package com.example.exam.model

import javax.persistence.*

enum class AnimalType{
    DOG,
    CAT,
    RABBIT
}

@Entity
@Table(name = "animals")
class AnimalEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "animals_animal_id_seq")
    @SequenceGenerator(
        name = "animals_animal_id_seq",
        allocationSize = 1
    )
    @Column(name = "animal_id")
    val id: Long? = null,

    @Column(name = "animal_name")
    val animalName: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "animal_type")
    val animalType: AnimalType,

    @Column(name = "breed")
    val breed: String,

    @Column(name = "age")
    val age: Int,

    @Column(name = "healthy")
    val healthy: Boolean? = true
) {
    override fun toString(): String {
        return "AnimalEntity(id=$id, animalName='$animalName', animalType=$animalType, breed='$breed', age=$age, healthy=$healthy)"
    }
}