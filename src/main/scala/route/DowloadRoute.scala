package route

import services.DowloadService
import utils.Utils

import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

class DowloadRoute extends HttpServlet {
  override def doPost(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
    if (Utils.isValidAuthKey(req, resp, getServletContext)) {
      new DowloadService().dowload(getServletContext, req, resp)
    }
  }
}
