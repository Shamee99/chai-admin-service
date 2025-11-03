# Office文档预览示例文件

本目录包含用于演示Office文档预览功能的示例文件。

## 当前示例文件

- `sample-document.txt` - 纯文本示例文档
- `sample-word.html` - HTML格式的Word示例文档

## 添加真实Office文档

要使用真实的Office文档进行预览，请将以下格式的文件放置在此目录中：

### 支持的文件格式

- **Word文档**: `.doc`, `.docx`
- **Excel表格**: `.xls`, `.xlsx`
- **PowerPoint演示**: `.ppt`, `.pptx`
- **PDF文档**: `.pdf`

### 使用步骤

1. 将Office文档文件复制到 `/public/samples/` 目录
2. 在Office预览页面中选择"本地示例文档"
3. 更新 `OfficeDemo.vue` 中的 `localSampleFiles` 数组，添加新文件的配置
4. 刷新页面即可看到新添加的文档

### 示例配置

```javascript
{
  name: '您的文档名称.docx',
  type: 'docx',
  color: '#2B579A',
  icon: Document,
  url: '/samples/您的文档名称.docx'
}
```

## 注意事项

- 确保文件路径正确（相对于public目录）
- 文件名不要包含中文或特殊字符（建议使用英文和数字）
- 大文件可能需要较长的加载时间
- 某些复杂的Office文档格式可能无法完美预览