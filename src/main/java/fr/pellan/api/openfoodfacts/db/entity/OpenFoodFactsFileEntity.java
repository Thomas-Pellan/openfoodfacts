package fr.pellan.api.openfoodfacts.db.entity;

import fr.pellan.api.openfoodfacts.enumeration.OpenFoodFactsFileStatus;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "file", schema = "openfoodfacts")
public class OpenFoodFactsFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_query_date")
    private LocalDateTime fileQueryTime;

    @Column(name = "file_status")
    private OpenFoodFactsFileStatus fileStatus;
}
