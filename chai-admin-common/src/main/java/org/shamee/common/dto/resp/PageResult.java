package org.shamee.common.dto.resp;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.Setter;
import org.dromara.hutool.core.lang.page.PageInfo;
import org.shamee.common.util.copy.BeanCopierUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Getter
@Setter
public class PageResult<T> extends PageInfo {

    private List<?> records;

    /**
     * 构造
     *
     * @param total    总记录数
     * @param pageSize 每页显示记录数
     */
    private PageResult(int total, int pageSize) {
        super(total, pageSize);
    }

    private PageResult(int total, int pageSize, List<?> records) {
        this(total, pageSize);
        this.records = records;
    }

    private PageResult(PageInfo pageInfo, List<?> records) {
        this(pageInfo.getTotal(), pageInfo.getPageSize());
        this.records = records;
    }

    public static <T> PageResult<T> empty(Class<T> clazz) {
        return new PageResult<>(0, 0, new ArrayList<T>());
    }

    public static <T> PageResult<?> of(Page<T> page) {
        return new PageResult<T>((int) page.getTotal(), (int) page.getSize(), page.getRecords());
    }

    public static <T> PageResult<T> of(Page<?> page, Supplier<T> supplier) {
        List<T> objects = BeanCopierUtils.copyListProperties(page.getRecords(), supplier);
        return new PageResult<T>((int) page.getTotal(), (int) page.getSize(), objects);
    }

}
