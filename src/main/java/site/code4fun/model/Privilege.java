package site.code4fun.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.code4fun.constant.AppConstants;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = AppConstants.TABLE_PREFIX + "Privilege")
public class Privilege {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "privileges")
    private Collection<Role> roles;

    public Privilege(Long id){
        this.id = id;
    }
}