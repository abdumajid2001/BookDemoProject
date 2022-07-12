package route

import services.DeleteService
import utils.Utils

import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

class DeleteRoute extends HttpServlet {

  override def doPost(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
    if (Utils.isValidAuthKey(req, resp, getServletContext)) {
      new DeleteService().deleted(req, resp)
    }
  }
}
