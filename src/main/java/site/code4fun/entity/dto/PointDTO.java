package site.code4fun.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import site.code4fun.entity.Point;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PointDTO extends Point{
	protected String pointMulti1;
	protected String pointMulti2;
	protected String pointMulti3;
	protected Double pointAvg;
	protected String studentName;
	protected String studentCode;
}
