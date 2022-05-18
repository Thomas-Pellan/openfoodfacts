package fr.pellan.api.openfoodfacts.factory;

import fr.pellan.api.openfoodfacts.db.entity.OpenFoodFactsFileImportEntity;
import fr.pellan.api.openfoodfacts.dto.OpenFoodFactsFileImportDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory building file import data dtos from file import entities.
 */
@Slf4j
@Service
public class OpenFoodFactsFileImportDTOFactory {

    public List<OpenFoodFactsFileImportDTO> buildFileImportDtos(List<OpenFoodFactsFileImportEntity> entities){

        if(CollectionUtils.isEmpty(entities)){
            return new ArrayList<>();
        }

        return entities.stream().map(this::buildFileImportDto).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public OpenFoodFactsFileImportDTO buildFileImportDto(OpenFoodFactsFileImportEntity entity){

        if(entity == null){
            return  null;
        }

        OpenFoodFactsFileImportDTO dto = new OpenFoodFactsFileImportDTO();
        dto.setId(entity.getId());
        dto.setFileStatus(entity.getFileStatus());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setNbLines(entity.getNbLines());
        dto.setNbLinesImported(entity.getNbLinesImported());
        dto.setNbArticles(entity.getNbArticles());
        dto.setNbIngredients(entity.getNbIngredients());
        dto.setNbNutrients(entity.getNbNutrients());

        return dto;
    }
}
