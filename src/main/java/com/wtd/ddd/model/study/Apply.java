package com.wtd.ddd.model.study;

import com.wtd.ddd.model.commons.Id;
import com.wtd.ddd.model.user.User;
import com.wtd.ddd.util.studycode.StatusUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;
import java.util.Objects;

import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

/**
 * Created By mand2 on 2020-10-24.
 * 스터디 지원글
 */
public class Apply {
    private final Long seq;
    private final Long postSeq;
    private final Id<User, Long> applyUser;
    private final String userName;
    private String content;
    private Id<StudyCode, String> applyStatus;
    private LocalDateTime createdAt;
    private ApplyPost applyPost; // 지원한 모집글


    public Apply(Long postSeq, Id<User, Long> applyUser, String content) {
        this(null, postSeq, applyUser, null, content, null, null, null);
    }

    public Apply(Long seq, Long postSeq, Id<User, Long> applyUser,
                 Id<StudyCode, String> applyStatus, LocalDateTime createdAt) {
        this(seq, postSeq, applyUser, null, null, applyStatus, createdAt, null);
    }

    public Apply(Long seq, Long postSeq, Id<User, Long> applyUser, String userName, String content,
                 Id<StudyCode, String> applyStatus, LocalDateTime createdAt, ApplyPost applyPost) {
        this.seq = seq;
        this.postSeq = postSeq;
        this.applyUser = applyUser;
        this.userName = userName;
        this.content = content;
        this.applyStatus = defaultIfNull(applyStatus, Id.of(StudyCode.class, StatusUtils.WAITING.getCodeSeq()));
        this.createdAt = defaultIfNull(createdAt, now());
        this.applyPost = applyPost;
    }

    public void modify(String content) {
        this.content = content;
    }

    public void modify(Id<StudyCode, String> applyStatus) {
        this.applyStatus = applyStatus;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getPostSeq() {
        return postSeq;
    }

    public Id<User, Long> getApplyUser() {
        return applyUser;
    }

    public String getUserName() {
        return userName;
    }

    public String getContent() {
        return content;
    }

    public Id<StudyCode, String> getApplyStatus() {
        return applyStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ApplyPost getApplyPost() {
        return applyPost;
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (null == o || getClass() != o.getClass()) return false;
        Apply apply = (Apply) o;
        return Objects.equals(seq, apply.seq);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("seq", seq)
                .append("postSeq", postSeq)
                .append("applyUser", applyUser)
                .append("userName", userName)
                .append("content", content)
                .append("applyStatus", applyStatus)
                .append("createdAt", createdAt)
                .append("applyPost", applyPost)
                .toString();
    }

    static public class Builder{
        private Long seq;
        private Long postSeq;
        private Id<User, Long> applyUser;
        private String userName;
        private String content;
        private Id<StudyCode, String> applyStatus;
        private LocalDateTime createdAt;
        private ApplyPost applyPost; // 지원한 모집글

        public Builder() {
        }

        public Builder(Apply apply) {
            this.seq = apply.seq;
            this.postSeq = apply.postSeq;
            this.applyUser = apply.applyUser;
            this.userName = apply.userName;
            this.content = apply.content;
            this.applyStatus = apply.applyStatus;
            this.createdAt = apply.createdAt;
            this.applyPost = apply.applyPost;
        }

        public Builder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public Builder postSeq(Long postSeq) {
            this.postSeq = postSeq;
            return this;
        }

        public Builder applyUser(Id<User, Long> applyUser) {
            this.applyUser = applyUser;
            return this;
        }

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder applyStatus(Id<StudyCode, String> applyStatus) {
            this.applyStatus = applyStatus;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder applyPost(ApplyPost applyPost) {
            this.applyPost = applyPost;
            return this;
        }

        public Apply build() {
            return new Apply(seq, postSeq, applyUser, userName, content,
                    applyStatus, createdAt, applyPost);
        }
    }

}
