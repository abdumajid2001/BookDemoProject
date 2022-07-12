package route

import services.GetAllService
import utils.Utils

import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

class GelAllRoute extends HttpServlet {

  override def doGet(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
    if (Utils.isValidAuthKey(req, resp, getServletContext)) {
      new GetAllService().getAll(req, resp)
    }
  }

}
