package site.code4fun.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tblNotify")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@NamedStoredProcedureQuery(name = "Notify.countNotifyByStatus", procedureName = "countNotifyByStatus", parameters = {
		  @StoredProcedureParameter(mode = ParameterMode.IN, name = "userId", type = Long.class),
		  @StoredProcedureParameter(mode = ParameterMode.IN, name = "status", type = Boolean.class),
		  @StoredProcedureParameter(mode = ParameterMode.OUT, name = "numNotify", type = Integer.class) })
public class Notify {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	protected String title;
	protected String content;
	protected String status; // ACTIVE/INACTIVE
	
	@Column(name = "school_id")
	protected Long schoolId;
	
	@Column(name = "created_by")
	protected Long createdBy;
	
	@Column(name = "created_date")
	protected Timestamp createdDate;
}
