package it.dbortoluzzi.usecases

import it.dbortoluzzi.data.LocationsRepository
import it.dbortoluzzi.domain.Location

class GetLocations(private val locationsRepository: LocationsRepository) {

    init{
        System.out.println("INIT")
    }

    operator fun invoke(): List<Location> = locationsRepository.getSavedLocations()

}
