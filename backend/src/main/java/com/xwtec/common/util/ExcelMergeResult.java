package com.xwtec.common.util;

public class ExcelMergeResult {
    public boolean merged;
    public int startRow;
    public int endRow;
    public int startCol;
    public int endCol;
    public ExcelMergeResult(boolean merged,int startRow,int endRow
            ,int startCol,int endCol){
        this.merged = merged;
        this.startRow = startRow;
        this.endRow = endRow;
        this.startCol = startCol;
        this.endCol = endCol;
    }
}
