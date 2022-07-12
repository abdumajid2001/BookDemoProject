package route

import services.UpdateService
import utils.Utils

import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

class UpdateRoute extends HttpServlet {

  override def doPost(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
    if (Utils.isValidAuthKey(req, resp, getServletContext)) {
      new UpdateService().update(req, resp)
    }
  }


}
