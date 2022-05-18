package fr.pellan.api.openfoodfacts.db.entity;

import fr.pellan.api.openfoodfacts.enumeration.OpenFoodFactsFileStatus;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * The File process entity.
 */
@Data
@Entity
@Table(name = "file_import", schema = "openfoodfacts")
public class OpenFoodFactsFileImportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_file")
    private OpenFoodFactsFileEntity file;

    @Column(name = "import_status")
    private OpenFoodFactsFileStatus fileStatus;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "nb_lines")
    private Integer nbLines;

    @Column(name = "nb_lines_imported")
    private Integer nbLinesImported;

    @Column(name = "nb_articles")
    private Integer nbArticles;

    @Column(name = "nb_ingredients")
    private Integer nbIngredients;

    @Column(name = "nb_nutrients")
    private Integer nbNutrients;
}
