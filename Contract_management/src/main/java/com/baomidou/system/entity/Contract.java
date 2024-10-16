package com.baomidou.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author Sun
 * @since 2024-09-26
 */
@Data
@Schema(description = "合同实体")
public class Contract implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "合同id")
    @TableId(value = "contract_id", type = IdType.AUTO)
    private Long contractId;

    @Schema(description = "合同编号")
    private String contractNumber;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "甲方")
    private String partyA;

    @Schema(description = "乙方")
    private String partyB;

    @Schema(description = "签订日期")
    private LocalDate signingDate;

    @Schema(description = "总价")
    private BigDecimal totalPrice;

    @Schema(description = "税额")
    private BigDecimal taxAmount;

    @Schema(description = "合同状态id")
    private Integer statusId = 1;

    @Schema(description = "负责员工id")
    private Long employeeId;

    @Schema(description = "企业id")
    private Long companyId;

}
