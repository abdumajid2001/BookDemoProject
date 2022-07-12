package route

import utils.Utils

import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

class EmailRoute extends HttpServlet {

  override def doPost(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
    if (Utils.isValidAuthKey(req, resp, getServletContext)) {
      email.EmailSender.sendMessage(req, resp)
    }
  }

}
