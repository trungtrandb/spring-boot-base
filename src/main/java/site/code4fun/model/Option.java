package site.code4fun.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.code4fun.constant.AppConstants;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = AppConstants.TABLE_PREFIX + "Options")
@Builder
public class Option{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Option(Long id){
        this.id = id;
    }

    @Override
    public String toString(){
        return name;
    }
}
