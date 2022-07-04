package route


import services.AddService
import setting.Setting

import javax.servlet.ServletConfig
import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

class AddRoute extends HttpServlet {
  private var setting: Option[Setting] = null

  override def init(config: ServletConfig): Unit = {
    setting = Option(config.getServletContext.getAttribute("setting").asInstanceOf[Setting])
  }

  def getSetting(): Setting = {
    setting match {
      case Some(s) => s
      case None => throw new RuntimeException("Biruni setting is not found")
    }
  }

  override def doPost(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
    var addService = new AddService
    addService.add(getSetting(), req, resp);
  }

}
