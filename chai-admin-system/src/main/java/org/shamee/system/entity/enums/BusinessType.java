package org.shamee.system.entity.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

@Getter
public enum BusinessType implements IEnum<String> {

    OTHER("其它"),
    INSERT("新增"),
    UPDATE("修改"),
    DELETE("删除"),
    GRANT("授权"),
    EXPORT("导出"),
    IMPORT("导入"),
    FORCE_LOGOUT("强退"),
    GEN_CODE( "生成代码"),
    CLEAN_DATA("清空数据");

    private final String desc;

    BusinessType(String desc) {
        this.desc = desc;
    }

    @Override
    public String getValue() {
        return desc;
    }
}