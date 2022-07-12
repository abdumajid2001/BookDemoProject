package services

import connection.DBConnection
import entity.Book
import responce.{AppErrorDto, DataDto}
import utils.Utils

import java.sql.{ResultSet, Statement}
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import scala.collection.mutable.ArrayBuffer

class GetAllService {

  def getAll(req: HttpServletRequest, resp: HttpServletResponse): Unit = {

    var conn = DBConnection.getSingletonConnection
    var cs: Statement = null
    try {
      cs = conn.createStatement()
      var res = cs.executeQuery("select * from RND_TEST.BOOKS where IS_DELETED = 'N'")
      var list: List[Book] = getAllBook(res)
      resp.getWriter.write(Utils.mapper().writeValueAsString(list))
    }
    catch {
      case ex: Exception =>
        var response = new DataDto[AppErrorDto](new AppErrorDto("Xatolik yuz berdi", ex.printStackTrace().toString, req.getRequestURL.toString));
        resp.getWriter.write(Utils.json.toJson(response))
        resp.setStatus(500)
    }
    finally {
      if (cs != null) cs.close()
      conn.close()
      conn = null
    }
  }

  def getAllBook(resultSet: ResultSet): List[Book] = {
    var res = new ArrayBuffer[Book]()
    while (resultSet.next()) {
      res += Book(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getShort(5), resultSet.getString(6))
    }
    res.toList
  }

}
