package services

import connection.DBConnection
import entity.DeleteBook
import file.FileDowloadService
import responce.{AppErrorDto, DataDto}
import utils.Utils

import java.io.File
import java.sql.{PreparedStatement, Statement}
import javax.servlet.ServletContext
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

class DowloadService {

  def dowload(context: ServletContext, req: HttpServletRequest, resp: HttpServletResponse) = {

    var conn = DBConnection.getSingletonConnection
    var cs: PreparedStatement = null
    var es: Statement = null

    try {
      var id = Utils.mapper().readValue(req.getReader, classOf[DeleteBook])
      es = conn.createStatement();
      if (Utils.isExistBook(es, req, resp, id.id)) {
        cs = conn.prepareStatement("select rf.* from RND_TEST.BOOKS rb inner join RND_TEST.files rf on rb.FILE_ID = rf.ID where rb.id = ?")
        cs.setInt(1, id.id)
        var res = cs.executeQuery()
        while (res.next()) {
          new FileDowloadService().dowload(context, context.getAttribute("filesPath") + File.separator + res.getString(2), res.getString(3), resp)
        }
      }
    }
    catch {
      case e: Exception =>
        println(e.printStackTrace())
        var response = new DataDto[AppErrorDto](new AppErrorDto("Xatolik yuz berdi", e.printStackTrace().toString, req.getRequestURL.toString));
        resp.getWriter.write(Utils.json.toJson(response))
        resp.setStatus(500)
    }

    finally {
      if (!cs.isClosed) cs.close()
      if (!es.isClosed) es.close()
      if (!conn.isClosed) conn.close()
      conn = null
    }

  }

}
