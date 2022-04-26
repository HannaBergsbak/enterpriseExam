package com.example.exam.service

import com.example.exam.model.AnimalEntity
import com.example.exam.repo.AnimalRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class AnimalService(@Autowired private val animalRepo: AnimalRepo) {

    //ANIMALS
    fun getAnimals(): List<AnimalEntity>{
        return animalRepo.findAll()
    }

    fun getAnimalsById(animalId: Long): AnimalEntity?{
        return animalRepo.findByIdOrNull(animalId)
    }

    fun createAnimal(animal: AnimalEntity): AnimalEntity{
        return animalRepo.save(animal)
    }

    fun updateAnimal(animal: AnimalEntity, animalId: Long): AnimalEntity?{
        if (animalRepo.existsById(animalId)){
            val animalToSave = AnimalEntity(id = animalId, animalName = animal.animalName, animalType = animal.animalType, breed = animal.breed, age = animal.age, healthy = animal.healthy)
            return animalRepo.save(animalToSave)
        }
        return null
    }

    fun deleteAnimal(animalId: Long): Boolean{
        if (animalRepo.existsById(animalId)){
            animalRepo.deleteById(animalId)
            return true
        }
        return false
    }


}