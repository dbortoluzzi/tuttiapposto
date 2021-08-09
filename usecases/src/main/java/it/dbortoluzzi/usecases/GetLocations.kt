package it.dbortoluzzi.usecases

import it.dbortoluzzi.data.LocationsRepository
import it.dbortoluzzi.domain.Location

class GetLocations(private val locationsRepository: LocationsRepository) {

    operator fun invoke(): List<Location> = locationsRepository.getSavedLocations()

}
