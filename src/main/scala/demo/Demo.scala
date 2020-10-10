package demo

import demo.com.unionpay.acp.demo.DemoBase
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.{CrossOrigin, GetMapping, RequestMapping, RestController}

/**
  * @Author :
  * @Date : 2020/8/5 12:03 PM 
  * @Version : 1.2.1
  */
@RestController
@CrossOrigin
@RequestMapping( Array( "/demo" ) )
class Demo {

  @Value( "${test.param}" )
  var param:String=_

//  @Value( "${acpsdk.rootCert.path}" )
//  var path:String=_
  @GetMapping(Array("/b"))
  def b(): String ={
    param
  }
  @GetMapping(Array("/v"))
  def a(): String ={
    DemoBase.version
  }



}
