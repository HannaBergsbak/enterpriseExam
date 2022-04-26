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

    // FIXME: 26/04/2022  
    @GetMapping("/byId/{animalId")
    fun getAnimalsById(@RequestParam("animalId") animalId: Long): AnimalEntity{
        return animalService.getAnimalsById(animalId)
    }

    @PostMapping("/create")
    fun createNewAnimal(@RequestBody animal: AnimalEntity): AnimalEntity {
        return animalService.createAnimal(animal)
    }

    // FIXME: 26/04/2022  
    @PutMapping("/update/{animalId}")
    fun updateAnimal(@RequestParam("animalId") animalId: Long?, @RequestBody animal: AnimalEntity?): AnimalEntity?{
        when {
            animalId == null -> throw InvalidParameterException()
            animal == null -> throw InvalidParameterException()
            else -> {
                animalService.updateAnimal(animalId, animal)?.let {
                    return it
                }
            }
        }
        throw AnimalNotFound()
    }

    // FIXME: 26/04/2022  
    @DeleteMapping("delete/{animalId}")
    fun deleteAnimal(@RequestParam("animalId") animalId: Long){
        when{
            animalId == null -> throw InvalidParameterException()
            else -> {
                animalService.deleteAnimal(animalId)
            }
        }
        throw AnimalNotFound()
    }
}
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Animal not found")
class AnimalNotFound: RuntimeException()