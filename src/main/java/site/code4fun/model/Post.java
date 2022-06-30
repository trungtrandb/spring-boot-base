package site.code4fun.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.code4fun.constant.AppConstants;
import site.code4fun.constant.Status;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = AppConstants.TABLE_PREFIX + "Post")
public class Post{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String name;

    @Column(unique = true)
    private String slug;

//    @Lob //for large content
    private String content;

    @ManyToOne
    private Category category;

    @CreatedDate
    @Column(updatable = false)
    private Date created;

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @LastModifiedDate
    private Date updated;

    @LastModifiedBy
    @ManyToOne
    @JoinColumn(name = "updated_by")
    private User updatedBy;
}