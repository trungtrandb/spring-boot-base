package site.code4fun.entity.dto;

import lombok.*;
import site.code4fun.entity.Checkin;

import lombok.experimental.SuperBuilder;


@EqualsAndHashCode(callSuper = true)
@Data
@Setter
@Getter
@SuperBuilder
public class CheckinDTO extends Checkin {
    protected String studentCode;
    protected String studentName;
    protected String className;
    protected String lessionName;
    protected String createdName;
    protected int totalCheckin;
    protected int totalPresent;
    protected int totalAbsent;
}
