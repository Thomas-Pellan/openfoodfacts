package fr.pellan.api.openfoodfacts.factory;

import fr.pellan.api.openfoodfacts.db.entity.OpenFoodFactsFileEntity;
import fr.pellan.api.openfoodfacts.dto.OpenFoodFactsFileDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory building file dtos from file entities.
 */
@Slf4j
@Service
public class OpenFoodFactsFileDTOFactory {

    public List<OpenFoodFactsFileDTO> buildFileDtos(List<OpenFoodFactsFileEntity> entities){

        if(CollectionUtils.isEmpty(entities)){
            return new ArrayList<>();
        }

        return entities.stream().map(this::buildFileDto).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    private OpenFoodFactsFileDTO buildFileDto(OpenFoodFactsFileEntity entity){

        if(entity == null){
            return  null;
        }

        OpenFoodFactsFileDTO dto = new OpenFoodFactsFileDTO();
        dto.setId(entity.getId());
        dto.setFileName(entity.getFileName());
        dto.setFileQueryTime(entity.getFileQueryTime());
        dto.setLastImportStatus(entity.getFileStatus());

        return dto;
    }
}
