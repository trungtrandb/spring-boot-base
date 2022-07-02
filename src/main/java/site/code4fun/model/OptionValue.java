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
@Table(name = AppConstants.TABLE_PREFIX + "Option_Value")
@Builder
public class OptionValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "option_id")
    private Option option;

    public OptionValue(Long id){
        this.id = id;
    }

    @Override
    public String toString(){
        return name;
    }
}
