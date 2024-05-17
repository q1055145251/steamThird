package com.example.steamThird.Bo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Objects;


@Schema(description = "压缩进度回调")
@Data
public class ZipBo {

    @Schema(description = "当前压缩文件")
    private String path;

    @Schema(description = "zip压缩路径")
    private String zipPath;

    @Schema(description = "当前文件进度")
    private Integer size;

    @Schema(description = "总进度")
    private Integer totalSize;

    @Schema(description = "总文件数量")
    private Integer fileTotalSize;

    @Schema(description = "当前文件数量")
    private Integer fileSize;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ZipBo zipBo = (ZipBo) o;
        return Objects.equals(path, zipBo.path) && Objects.equals(zipPath, zipBo.zipPath) && Objects.equals(size, zipBo.size) && Objects.equals(totalSize, zipBo.totalSize) && Objects.equals(fileTotalSize, zipBo.fileTotalSize) && Objects.equals(fileSize, zipBo.fileSize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, zipPath, size, totalSize, fileTotalSize, fileSize);
    }
}
