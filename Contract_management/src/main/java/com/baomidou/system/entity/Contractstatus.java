package com.baomidou.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
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
@Schema(description = "Contractstatus对象")
public class Contractstatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "合同状态id")
    @TableId(value = "status_id", type = IdType.AUTO)
    private Integer statusId;

    @Schema(description = "状态名")
    private String statusName;

}
