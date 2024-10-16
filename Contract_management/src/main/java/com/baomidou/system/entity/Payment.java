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
@Schema(description = "Payment对象")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "收款id")
    @TableId(value = "payment_id", type = IdType.AUTO)
    private Long paymentId;

    @Schema(description = "收款日期")
    private LocalDate paymentDate;

    @Schema(description = "收款金额")
    private BigDecimal paymentAmount;


    @Schema(description = "合同id")
    private Long contractId;

}
