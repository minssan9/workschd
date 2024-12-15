package com.voyagerss.persist.component.abs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.voyagerss.persist.component.constants.AnnotationConst;
import com.voyagerss.persist.component.constants.DateConst;
import com.voyagerss.persist.component.util.StringUtil;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Slf4j
/** @Builder(lombok) : POJO에 선언된 field의 setter 생성 */
@Setter
/** @Builder(lombok) : POJO에 선언된 field의 getter 생성 */
@Getter
/** @JsonDeserialize : builder = Json을 POJO 클래스로 Deserialize할때 사용할 builder클래스를 지정한다.
 * 	Lombok이 생성하는 기본 builder 클래스명은 POJO명 + Builder이다.
 * 	클래스 명을 변경하고자 할 경우 lombok @Builder어노테이션의 builderClassName 속성으로 클래스 명을 설정한다.
 * 	using = 별도로 사용할 Deserialize가있을경우 등록한다. 사용은 builder처럼 해당 class를 등록한다. */
@JsonDeserialize
/** @EqualsAndHashCode(lombok) : POJO의 일치 여부를 비교하는 equals메소드와 hashCode메소드 생성 */
@EqualsAndHashCode
/** @Builder(lombok) : POJO에 선언된 field를 문자열로 반환하는 toString 생성 */
@ToString(callSuper = true)
/** @SuperBuilder: SuperBuilder 사용 */
@SuperBuilder(toBuilder = true)
/** @Builder(lombok) : 아규먼트가 없는 기본 POJO생성자를 생성하고 access modifier(접근 제어자)를 설정한다.
 * 	Mybatis가 기본생성자를 이용해 객체를 생성하고 리플렉션으로 데이터를 매핑하기 때문에 기본생성자를 생성해준다. */
@NoArgsConstructor(access = AccessLevel.PUBLIC)
/** @Validated: Field 유효성 검사 활성 */
@Validated
/** @JsonIgnoreProperties: JSON De/Serialize 무시 설정 */
//@JsonIgnoreProperties(ignoreUnknown = true, value = { "ignoreField01", "ignoreField02" })
/** @MappedSuperclass(jpa): 테이블과 매핑하지 않고 부모 클래스를 상속받는 자식 클래스에게 매핑 정보(엔티티 칼럼)만 제공 */
@MappedSuperclass
public abstract class AbstractDataCommon {

    /***********************************************
     *
     * 1. 사이트 어플리케이션 개별 공통 데이터 컬럼을 선언하여 사용한다.
     * 2. 아래 선언한 필드는 사이트 어플리케이션 상황에 마추어 변경하여 사용하도록 한다.
     *
     * <@MappedSuperclass 엔티티 필드 설정>
     * 1. 필드와 컬럼 매핑(옵션설정) : @Column
     * 2. 데이터베이스와 매핑하지 않음: @Transient
     * 3. MappedSuperclass에는 @Id, @ManyToOne, @JoinColumn은 사용하지 않도록한다.
     *
     ***********************************************/

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name="reg_usr", nullable=false, updatable=false, length = 20)
        private String regUsr;

    @CreationTimestamp
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    //@Temporal(TemporalType.TIMESTAMP)
    //@Convert(converter=DateTimeToStringConverter.class)
    @Column(name="created_at", nullable=false, updatable=false, columnDefinition="DATETIME")
    @JsonFormat(pattern = DateConst.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime createdAt;

//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @Column(name="reg_ip", nullable=false, length = 50)
//    private String regIp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name="upd_usr", nullable=false, length = 20)
        private String updUsr;

    @UpdateTimestamp
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    //@Convert(converter=DateTimeToStringConverter.class)
    @Column(name="updated_at", nullable=false, columnDefinition="DATETIME")
    @JsonFormat(pattern = DateConst.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime updatedAt;

//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @Column(name="upd_ip", nullable=false, length = 50)
//        private String updIp;


//    public AbstractDataCommon(){
//        setDefaultCommonInfo();
//    }

    public void setQueryCommonInfo(){
        this.createdAt = null;
        this.updatedAt = null;
    }

    public void setDefaultCommonInfo(){
        this.regUsr = StringUtil.isEmpty(this.regUsr) ? AnnotationConst.ATTR_ACCNT_ID : "";
        this.updUsr = StringUtil.isEmpty(this.updUsr) ? AnnotationConst.ATTR_ACCNT_ID : "";

//        this.regIp = StringUtil.isEmpty(this.regIp) ? AnnotationConst.ATTR_ACCNT_IP : "";
//        this.updIp = StringUtil.isEmpty(this.updIp) ? AnnotationConst.ATTR_ACCNT_IP : "";

        this.createdAt = this.createdAt != null ? this.createdAt : LocalDateTime.now();
        this.updatedAt = this.updatedAt != null ? this.updatedAt : LocalDateTime.now();
    }

}
