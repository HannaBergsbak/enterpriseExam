package com.example.exam.controller

import com.example.exam.model.AnimalEntity
import com.example.exam.service.AnimalService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/shelter")
class AnimalController(@Autowired private val animalService: AnimalService) {

    // FIXME: 25/04/2022
    // Endre disse endpointsene til å bruke ferdig genererte?

    @GetMapping("/all")
    fun getAllAnimals(): ResponseEntity<List<AnimalEntity>>{
        return ResponseEntity.ok().body(animalService.getAnimals())
    }

    @GetMapping("/all")
    fun getAnimalsById(): ResponseEntity<List<AnimalEntity>>{
        TODO()
    }

    @PostMapping("/create")
    fun createNewAnimal(@RequestBody animal: AnimalEntity): AnimalEntity {
        return animalService.createAnimal(animal)
    }

    @PutMapping("/update/{animalId}")
    fun updateAnimal(@RequestParam("animalId") animalId: Long, @RequestBody animal: AnimalEntity){
        return animalService.updateAnimal(animal)
    }

    @DeleteMapping("delete/{animalId}")
    fun deleteAnimal(@RequestParam("animalId") animalId: Long){
        animalService.deleteAnimal(animalId)
    }
}