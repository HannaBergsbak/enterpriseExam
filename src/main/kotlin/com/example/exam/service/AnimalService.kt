package com.example.exam.service

import com.example.exam.model.AnimalEntity
import com.example.exam.repo.AnimalRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AnimalService(@Autowired private val animalRepo: AnimalRepo) {

    fun createAnimal(animal: AnimalEntity): AnimalEntity{
        return animalRepo.save(animal)
    }

    fun updateAnimal(animal: AnimalEntity, animalId: Long){
        animalRepo.findById(animalId)
        animalRepo.save(AnimalEntity(id = animalId, animalName = animal.animalName, animalType = animal.animalType, breed = animal.breed, age = animal.age, healthy = animal.healthy))
    }

    fun deleteAnimal(animalId: Long): Boolean{
        if (animalRepo.existsById(animalId)){
            animalRepo.deleteById(animalId)
            return true
        }
        return false
    }

    fun getAnimals(): List<AnimalEntity>{
        return animalRepo.findAll()
    }

    fun getAnimalsById(animalId: Long): AnimalEntity{
        return animalRepo.findById(animalId).orElse(null)
    }
}