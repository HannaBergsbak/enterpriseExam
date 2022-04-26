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

    fun updateAnimal(id: Long, animal: AnimalEntity): AnimalEntity?{
        if (animalRepo.existsById(animal.id!!)){
            return animalRepo.save(animal)
        }
        return null
    }

    fun deleteAnimal(id: Long): Boolean{
        if (animalRepo.existsById(id)){
            animalRepo.deleteById(id)
            return true
        }
        return false
    }

    fun getAnimals(): List<AnimalEntity>{
        return animalRepo.findAll()
    }

    fun getAnimalsById(id: Long): AnimalEntity{
        return animalRepo.findById(id).orElse(null)
    }
}