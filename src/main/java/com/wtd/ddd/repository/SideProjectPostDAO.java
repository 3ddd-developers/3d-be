package com.wtd.ddd.repository;

import com.wtd.ddd.controller.SideProjectMyApplyResponse;
import com.wtd.ddd.domain.SideProjectPost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
@Slf4j
public class SideProjectPostDAO {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    public SideProjectPostDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.
        simpleJdbcInsert
                .withTableName("side_project_post")
                .usingGeneratedKeyColumns("seq")
                .usingColumns("leader","meeting","location","status","mem_total_capa","title","contents");
    }

    public int insert(SideProjectPost post) {
        SqlParameterSource params = new MapSqlParameterSource()
                                    .addValue("leader", post.getLeader())
                                    .addValue("meeting", post.getMeeting())
                                    .addValue("location", post.getLocation())
                                    .addValue("status", "01")
                                    .addValue("mem_total_capa", post.getMemTotalCapa())
                                    .addValue("title", post.getTitle())
                                    .addValue("contents", post.getContents());
        Number number = simpleJdbcInsert.executeAndReturnKey(params);
        return number.intValue();
    }

    public List<SideProjectPost> select(int seq) {
        String query = "SELECT * from side_project_post where seq = " + seq;
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<SideProjectPost>(SideProjectPost.class));
    }

    public List<SideProjectPost> selectAll(String title, Integer limit, Integer offset) {
        String query = "SELECT * from side_project_post";

        if (!StringUtils.isEmpty(title)) {
            query += "  WHERE title like \'%" + title + "%\'";
        }

        if (limit != null && offset != null) {
            query += " LIMIT " + limit + " OFFSET " + offset;
        }

        List<SideProjectPost> posts = jdbcTemplate.query(query, new BeanPropertyRowMapper<SideProjectPost>(SideProjectPost.class));
        log.error(posts.toString());
        return posts;
    }

    public int getTotalCount(String title) {
        String query = "SELECT count(*) FROM side_project_post";
        if (!StringUtils.isEmpty(title)) {
            query += "  WHERE title like \'%" + title + "%\'";
        }
        int totalCount = jdbcTemplate.queryForObject(query, Integer.class);
        return totalCount;
    }

    public List<SideProjectMyApplyResponse> selectByLeaderMemId(String memId) {
        String query = "select a.seq, a.apply_stat AS status, b.mem_capa, b.mem_total_capa,\n" +
                "        b.title, b.create_dt\n" +
                " from side_project_apply a, side_project_post b where a.post_seq = b.seq " +
                " and b.leader = \'" + memId + "\'";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<SideProjectMyApplyResponse>(SideProjectMyApplyResponse.class));
    }

}
