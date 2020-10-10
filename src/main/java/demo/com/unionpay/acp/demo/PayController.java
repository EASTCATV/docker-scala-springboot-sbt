package demo.com.unionpay.acp.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author : yang
 * @Date : 2019/5/20 5:06 PM
 * @Version : 1.2.1
 */
@Controller
@RequestMapping("/pay/")
@CrossOrigin
public class PayController {
    @Resource
    private Form05_6_1_ApplyQrCode faqc;
    @Resource
    private Form05_6_3_ConsumeUndo fq;


//    @RequestMapping(value="notify_url",method= RequestMethod.POST)
//    public String notify_url(HttpServletRequest request){
//        Boolean top = false;
//        try {
//            top = return_url.tops(request);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if(top){
//            return "success" ;
//        }else{
//            return null;
//        }
//    }

    @GetMapping("doget")
    public void doget(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        faqc.doGet(req,resp);
    }
    @GetMapping("stop")
    public void stop(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
     fq.doGet(req,resp);
    }
//    @PostMapping("as")
//    @ResponseBody
//    public String a(){
//        System.out.println("asssss");
//        return "as";
//    }
//
//    @ApiOperation(value="加密单子", notes="")
//    @ApiImplicitParam(name = "content", value = "订单号", required = true, dataType = "String")
//    @RequestMapping(value = "get_sign",method=RequestMethod.POST)
//    @ResponseBody
//    public JsonResult get_sign(String content){
//        String sign = SignUtils.sign(content);
//        return new JsonResult(sign);
//    }

}
