package demo

import demo.com.unionpay.acp.demo.Form05_6_1_ApplyQrCode
import javax.annotation.Resource
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.springframework.web.bind.annotation.{CrossOrigin, GetMapping, RequestMapping, RestController}

import scala.beans.BeanProperty

/**
 * @Author : yang_y_1.0.2
 * @Date : 2020/9/17 4:12 下午 
 * @Version : 1.2.1
 */
@RestController
@CrossOrigin
@RequestMapping( Array( "/pay" ) )
class PayDemo {

  @Resource
  @BeanProperty
  var faqc:Form05_6_1_ApplyQrCode=_

  /**
   * 获取二维码
   * @param req
   * @param resp
   */
  @GetMapping(Array("/do"))
  def doget(req: HttpServletRequest,resp: HttpServletResponse): Unit ={

  }

}
