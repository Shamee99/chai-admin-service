package ${packageName}.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.shamee.common.entity.BaseEntity;

<#if hasDate>
import java.time.LocalDate;
</#if>
<#if hasTime>
import java.time.LocalTime;
</#if>
<#if hasDateTime>
import java.time.LocalDateTime;
</#if>
<#if hasBigDecimal>
import java.math.BigDecimal;
</#if>

/**
 * ${tableComment!}实体
 *
 * @author ${author}
 * @since ${date}
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("${tableName}")
public class ${entityName} extends BaseEntity {

<#list columns as column>
<#if !column.isBaseField>
    /**
     * ${column.columnComment!}
     */
    @TableField("${column.columnName}")
    private ${column.javaType} ${column.javaField};

</#if>
</#list>
}

