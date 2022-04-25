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

    fun updateAnimal(animal: AnimalEntity){

    }

    fun deleteAnimal(id: Long){
        TODO()
    }

    fun getAnimals(): List<AnimalEntity>{
        return animalRepo.findAll()
    }

    fun getAnimalsById(id: Long){
        TODO()
    }
}