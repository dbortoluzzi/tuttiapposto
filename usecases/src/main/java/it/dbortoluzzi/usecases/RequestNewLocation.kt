package it.dbortoluzzi.usecases

import it.dbortoluzzi.data.LocationsRepository
import it.dbortoluzzi.domain.Location

class RequestNewLocation(private val locationsRepository: LocationsRepository) {

    operator fun invoke(): List<Location> = locationsRepository.requestNewLocation()

}
