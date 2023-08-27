/**
 * Item.java
 *
 */
package hac.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

//import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "items")
public class Item implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    @NotEmpty(message = "Title should not be empty")
    @Size(max = 32, message = "item title should not be more than 32 characters")
    private String title;

    //    @Pattern(regexp = "(http(s?):)([/|.|\\w|\\s|-])*\\.(?:jpg|gif|png)",
    //            message = "Invalid image URL, should end with .jpg, .gif, .png etc.")
    @NotEmpty(message = "Photo link should not be empty")
    private String photoUrl;
    @NotEmpty(message = "Content should not be empty")
    @Size(max = 600, message = "content should not be more than 300 characters")
    @Size(min = 40, message = "content should not be less than 40 characters")
    private String content;

    @CreationTimestamp
    private LocalDateTime createdOn;

    @UpdateTimestamp
    private LocalDateTime updatedOn;

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    private List<Review> reviews = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public Item(String title, String photoUrl, String content, Category category) {
        this.title = title;
        this.photoUrl = photoUrl;
        this.content = content;
        this.category = category;
    }

}