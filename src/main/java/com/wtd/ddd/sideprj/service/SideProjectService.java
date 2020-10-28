package com.wtd.ddd.sideprj.service;

import com.wtd.ddd.sideprj.domain.SideProjectApply;
import com.wtd.ddd.sideprj.repository.SideProjectApplyDAO;
import com.wtd.ddd.sideprj.web.SideProjectApplyRequest;
import com.wtd.ddd.sideprj.web.SideProjectPostRequest;
import com.wtd.ddd.sideprj.web.SideProjectPostResponse;
import com.wtd.ddd.sideprj.domain.SideProjectPost;
import com.wtd.ddd.sideprj.domain.SideProjectRecArea;
import com.wtd.ddd.sideprj.repository.SideProjectPostDAO;
import com.wtd.ddd.sideprj.repository.SideProjectRecAreaDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class SideProjectService {

    @Autowired
    SideProjectRecAreaDAO sideProjectRecAreaDAO;

    @Autowired
    SideProjectPostDAO sideProjectPostDAO;

    @Autowired
    SideProjectApplyDAO sideProjectApplyDAO;


    public String writePost(SideProjectPostRequest request) {
        // TODO : 인원수 validation, 코드 정리
        log.error("INPUT:" + request.toString());
        SideProjectPost post = SideProjectPostRequest.convertToPost(request);
        int key = sideProjectPostDAO.insert(post);
        List<SideProjectRecArea> areas = SideProjectPostRequest.convertToRecArea(request, key);
        addRecAreas(areas);
        return "SUCCESS";
    }

    public SideProjectPostResponse get(int seq) {
        SideProjectPost post = sideProjectPostDAO.select(seq).get(0);
        List<SideProjectRecArea> areas = sideProjectRecAreaDAO.selectByPostSeq(seq);
        return SideProjectPostResponse.convert(post, areas);
    }

    @Transactional
    public boolean changeApplyStatus(SideProjectApply apply) {
        int seq = apply.getSeq();
        int recSeq = apply.getRecAreaSeq();
        log.error("seq = " + seq + " , " + " recSeq = " + recSeq);
        sideProjectApplyDAO.update(apply);
        if ("Accept".equals(apply.getApplyStat())) {
            sideProjectRecAreaDAO.updateCapacity(recSeq);
            sideProjectPostDAO.updateCapacity(seq);

            if (reachedMaxCapaOfRecArea(recSeq)) {
                sideProjectRecAreaDAO.updateToFinish(seq);
            }
            if (reachedMaxCapaOfProject(seq)) {
                sideProjectPostDAO.updateToFinish(seq);
            }
        }
        return true;
    }

    public int addApply(SideProjectApplyRequest request) {
        int recAreaSeq = sideProjectRecAreaDAO.findRecAreaSeq(request.getPostSeq(), request.getRecArea());
        SideProjectApply apply = SideProjectApply.convert(request, recAreaSeq);
        return sideProjectApplyDAO.insert(apply);
    }

    private boolean reachedMaxCapaOfRecArea(int seq) {
        List<SideProjectRecArea> areas = sideProjectRecAreaDAO.selectBySeq(seq);
        if (areas.size() != 1) return false;
        return areas.get(0).getMaxCapa() == areas.get(0).getFixedCapa();
    }

    private boolean reachedMaxCapaOfProject(int seq) {
        List<SideProjectPost> posts = sideProjectPostDAO.selectByApplySeq(seq);
        if (posts.size() != 1) return false;
        return posts.get(0).getMemCapa() == posts.get(0).getMemTotalCapa();
    }


    private boolean addRecAreas(List<SideProjectRecArea> areas) {
        for (SideProjectRecArea recArea : areas) {
            sideProjectRecAreaDAO.insert(recArea);
        }
        return true;
    }


}
