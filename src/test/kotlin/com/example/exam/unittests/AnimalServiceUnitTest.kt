package com.example.exam.unittests

import com.example.exam.model.AnimalEntity
import com.example.exam.model.AnimalType
import com.example.exam.repo.AnimalRepo
import com.example.exam.service.AnimalService
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import org.springframework.data.repository.findByIdOrNull

class AnimalServiceUnitTest {
    private val animalRepo = mockk<AnimalRepo>()
    private val animalService = AnimalService(animalRepo)
    private val testAnimalWithId: AnimalEntity = AnimalEntity(id = 1, animalName = "Kevin", animalType = AnimalType.RABBIT, breed = "Friend", age = 2, healthy = true)
    private val testAnimalWithoutId: AnimalEntity = AnimalEntity(animalName = "Kevin", animalType = AnimalType.RABBIT, breed = "Friend", age = 2, healthy = true)


    @Test
    fun shouldGetAllAnimals(){
        val animal1 = AnimalEntity(animalName = "Kevin", animalType = AnimalType.RABBIT, age = 2, breed = "Small")
        val animal2 = AnimalEntity(animalName = "Beefy", animalType = AnimalType.RABBIT, age = 3, breed = "Big")

        every { animalRepo.findAll() } answers {
            mutableListOf(animal1, animal2)
        }

        val animals = animalService.getAnimals()
        assert(animals.size == 2)
    }

    @Test
    fun shouldGetAnimalsById(){
        val animal1 = AnimalEntity(id = 1, animalName = "Kevin", animalType = AnimalType.RABBIT, age = 2, breed = "Small")

        every { animalRepo.findByIdOrNull(any()) } answers {
            animal1
        }

        val animal = animalService.getAnimalsById(1)
        assert(animal?.animalName == "Kevin")

    }

    @Test
    fun shouldCreateAnimal(){
        every { animalRepo.save(any()) } answers {
            firstArg()
        }

        val createdAAnimal = animalService.createAnimal(AnimalEntity(1, "Herb", AnimalType.DOG, "Just a good boy", 2, true))
        assert(createdAAnimal!!.animalName == "Herb")
    }

    @Test
    fun shouldUpdateAnimalOrReturnNull(){
        every { animalRepo.save(any()) } answers { firstArg() }
        every { animalRepo.existsById(any()) } answers { true }

        val updatedAnimal = animalService.updateAnimal(testAnimalWithoutId, 1)

        assert(updatedAnimal?.animalName == testAnimalWithoutId.animalName)
        assert(updatedAnimal?.id == 1.toLong())

        every { animalRepo.existsById(any()) } answers { false }

        val shouldBeNull = animalService.updateAnimal(testAnimalWithoutId, 1)
        assert(shouldBeNull == null)
    }

    @Test
    fun shouldDeleteAnimal(){
        every { animalRepo.deleteById(any()) } answers {}
        every { animalRepo.existsById(any()) } answers { true }
        assert(animalService.deleteAnimal(1))
        every { animalRepo.existsById(any()) } answers { false }
        assert(!animalService.deleteAnimal(1))


    }
}