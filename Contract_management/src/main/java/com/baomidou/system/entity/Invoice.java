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
@Schema(description = "Invoice对象")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "开票id")
    @TableId(value = "invoice_id", type = IdType.AUTO)
    private Long invoiceId;

    @Schema(description = "开票日期")
    private LocalDate invoiceDate;

    @Schema(description = "发票号")
    private String invoiceNumber;

    @Schema(description = "不含税金额")
    private BigDecimal amountExcludingTax;

    @Schema(description = "价税合计")
    private BigDecimal amountIncludingTax;

    @Schema(description = "收款id")
    private Long paymentId;

    @Schema(description = "合同id")
    private Long contractId;

}
