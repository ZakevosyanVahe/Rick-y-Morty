package mappers

import CharacterModel
import CharactersResponse
import RmLocation
import SuccessResponse
import dataSource.models.CharacterApiModel

class CharacterMapper {
    fun toDomain(apiModel: CharacterApiModel): CharacterModel {
        return CharacterModel(
            id = apiModel.id,
            name = apiModel.name,
            status = apiModel.status,
            species = apiModel.species,
            type = apiModel.type,
            gender = apiModel.gender,
            origin = RmLocation(apiModel.origin.name,apiModel.origin.url),
            location = RmLocation(apiModel.location.name,apiModel.location.url),
            image = apiModel.image,
            episode = apiModel.episode,
            url = apiModel.url,
            created = apiModel.created
        )
    }

    fun toDomain(nextPageUrl: String?, apiModels: List<CharacterApiModel>): CharactersResponse =
        SuccessResponse(nextPageUrl, apiModels.map {
            toDomain(it)
        })
}