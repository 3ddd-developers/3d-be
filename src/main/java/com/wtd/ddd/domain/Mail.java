package com.wtd.ddd.domain;

import lombok.Data;

@Data
public class Mail {

    private String receiverMail;
    private String subject;
    private String content;

    public static String convertToMailContent(SideProjectPost post) {
        StringBuilder builder = new StringBuilder();
        builder.append(post.getLeader() + "님이 생성하신 사이드 프로젝트 [" + post.getTitle() + "]");
        builder.append(" 가 생성 완료되었습니다! ");
        builder.append(" 사이드프로젝트 내용은 다음과 같습니다 :) <br> [위치] " + post.getLocation());
        builder.append("<br> [모임방식] " + post.getMeeting());
        builder.append("<br> [내용] " + post.getContents());
        builder.append("<br>" + post.getMemTotalCapa() + "명의 동료를 모아 사이드 프로젝트를 진행해 보세요!");
        return builder.toString();
    }

}
