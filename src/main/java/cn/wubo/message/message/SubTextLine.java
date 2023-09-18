package cn.wubo.message.message;

public class SubTextLine extends SubLine {

    public SubTextLine(String content) {
        this.content = content;
        this.lineType = SubLineEnum.TEXT;
    }
}
