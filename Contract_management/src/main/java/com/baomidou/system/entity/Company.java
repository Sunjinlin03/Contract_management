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
@Schema(description = "企业实体")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "企业id")
    @TableId(value = "company_id", type = IdType.AUTO)
    private Long companyId;

    @Schema(description = "企业名称")
    private String companyName;

    @Schema(description = "企业账号")
    private String companyAccount;

    @Schema(description = "企业密码")
    private String companyPassword;

}
