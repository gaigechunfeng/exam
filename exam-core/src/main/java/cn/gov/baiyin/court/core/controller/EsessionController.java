package cn.gov.baiyin.court.core.controller;

import cn.gov.baiyin.court.core.entity.ESession;
import cn.gov.baiyin.court.core.exception.ServiceException;
import cn.gov.baiyin.court.core.service.IEsessionService;
import cn.gov.baiyin.court.core.util.Msg;
import cn.gov.baiyin.court.core.util.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by WK on 2017/3/28.
 */
@Controller
@RequestMapping("/back/esession")
public class EsessionController {

    private IEsessionService esessionService;

    @Autowired
    public void setEsessionService(IEsessionService esessionService) {
        this.esessionService = esessionService;
    }

    @RequestMapping("/list")
    @ResponseBody
    public Msg list(PageInfo pageInfo) {

        return Msg.success(esessionService.listPagination(pageInfo));
    }

    @RequestMapping("/listWith")
    @ResponseBody
    public Msg listWith(PageInfo pageInfo) {

        return Msg.success(esessionService.listPaginationWith(pageInfo));
    }

    @RequestMapping("/add")
    @ResponseBody
    public Msg add(ESession eSession) {

        try {
            esessionService.add(eSession);
        } catch (ServiceException e) {
            return Msg.error(e);
        }
        return Msg.SUCCESS;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Msg delete(String id) {

        try {
            esessionService.deleteById(id);
        } catch (ServiceException e) {
            return Msg.error(e);
        }
        return Msg.SUCCESS;
    }

    @RequestMapping("/delMulti")
    @ResponseBody
    public Msg delMulti(Integer[] ids) {

        try {
            esessionService.delMulti(ids);
        } catch (ServiceException e) {
            return Msg.error(e);
        }
        return Msg.SUCCESS;
    }

    @RequestMapping("/import")
    @ResponseBody
    public Msg importEsession(MultipartFile file) {

        try {
            esessionService.importEsession(file);
        } catch (ServiceException e) {
            return Msg.error(e);
        }
        return Msg.SUCCESS;
    }
}
