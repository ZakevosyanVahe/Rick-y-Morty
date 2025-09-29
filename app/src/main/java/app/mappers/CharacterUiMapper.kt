package app.mappers

import CharacterModel
import model.CharacterUiModel
import model.RmLocation

class CharacterUiMapper {
    fun toUiModel(domainModel: CharacterModel): CharacterUiModel = CharacterUiModel(
        id = domainModel.id,
        name = domainModel.name,
        status = domainModel.status,
        species = domainModel.species,
        gender = domainModel.gender,
        image = domainModel.image,
        location = RmLocation(domainModel.location.name, domainModel.location.url),
        origin = RmLocation(name = domainModel.origin.name, url = domainModel.origin.url),
        url = domainModel.url,
        created = domainModel.created
    )

    fun toUiModel(domainModels: List<CharacterModel>): List<CharacterUiModel> = domainModels.map { toUiModel(it) }
}