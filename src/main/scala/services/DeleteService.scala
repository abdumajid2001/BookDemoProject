package services

import connection.DBConnection
import entity.DeleteBook
import responce.{AppErrorDto, DataDto}
import utils.Utils

import java.sql.{PreparedStatement, ResultSet, Statement}
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

class DeleteService {
  def deleted(req: HttpServletRequest, resp: HttpServletResponse): Unit = {

    var conn = DBConnection.getSingletonConnection
    var cs: PreparedStatement = null
    var es: Statement = null
    try {
      var id = Utils.mapper().readValue(req.getReader, classOf[DeleteBook])
      es = conn.createStatement()
      if (!Utils.isExistBook(es, req, resp, id.id)) {
        return null
      }
      cs = conn.prepareStatement("update RND_TEST.BOOKS set IS_DELETED = 'Y' where id = ?")
      cs.setInt(1, id.id)
      cs.execute()
      var response = new DataDto[String]("success")
      resp.getWriter.write(Utils.json.toJson(response))

    }
    catch {
      case ex: Exception =>
        var response = new DataDto[AppErrorDto](new AppErrorDto("Xatolik yuz berdi", ex.printStackTrace().toString, req.getRequestURL.toString));
        resp.getWriter.write(Utils.json.toJson(response))
        resp.setStatus(500)
    }
    finally {
      if (cs != null) cs.close()
      if (es != null) es.close()
      conn.close()
      conn = null
    }
  }

}
