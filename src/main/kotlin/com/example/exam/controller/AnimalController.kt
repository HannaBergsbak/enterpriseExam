package com.example.exam.controller

import com.example.exam.model.AnimalEntity
import com.example.exam.service.AnimalService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.InvalidParameterException


@RestController
@RequestMapping("/api/shelter")
class AnimalController(@Autowired private val animalService: AnimalService) {

    @GetMapping("/all")
    fun getAllAnimals(): ResponseEntity<List<AnimalEntity>>{
        return ResponseEntity.ok().body(animalService.getAnimals())
    }

    @GetMapping("/{animalId}")
    fun getAnimalsById(@PathVariable("animalId") animalId: Long): ResponseEntity<AnimalEntity>{
        animalId?.let {
            animalService.getAnimalsById(it)?.let { animal ->
                return ResponseEntity.ok(animal)
            }
        }
        return ResponseEntity.badRequest().build()
    }

    @PostMapping("/create")
    fun createNewAnimal(@RequestBody animal: AnimalEntity): AnimalEntity {
        return animalService.createAnimal(animal)
    }

    @PutMapping("/update/{animalId}")
    fun updateAnimal(@PathVariable("animalId") animalId: Long?, @RequestBody animal: AnimalEntity?): ResponseEntity<AnimalEntity>{
        when {
            animalId == null -> throw InvalidParameterException()
            animal == null -> throw InvalidParameterException()
            else -> {
                animalService.updateAnimal(animal, animalId)?.let {
                    return ResponseEntity.ok(it)
                }
            }
        }
        return ResponseEntity.badRequest().build()
    }

    @DeleteMapping("/delete/{animalId}")
    fun deleteAnimal(@PathVariable("animalId") animalId: Long): ResponseEntity<Boolean> {
        if (animalService.deleteAnimal(animalId)){
            return ResponseEntity.ok(true)
        }
        return ResponseEntity.badRequest().body(false)
    }
}