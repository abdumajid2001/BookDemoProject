package services

import connection.DBConnection
import entity.Book
import responce.{AppErrorDto, DataDto}
import utils.Utils

import java.sql.{PreparedStatement, Statement}
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}


class UpdateService {
  def update(req: HttpServletRequest, resp: HttpServletResponse): Unit = {

    var conn = DBConnection.getSingletonConnection
    var cs: PreparedStatement = null
    var es: Statement = null
    var csUpdate: PreparedStatement = null

    try {
      var book = Utils.readValue(req.getReader, classOf[Book])

      if (book.id eq null) {
        var response = new DataDto[AppErrorDto](new AppErrorDto("Not found book id =", "", req.getRequestURL.toString))
        resp.getWriter.write(Utils.json.toJson(response))
        resp.setStatus(400)
        return null
      }

      es = conn.createStatement();
      if (!Utils.isExistBook(es, req, resp, book.id)) {
        return null
      }

      cs = conn.prepareStatement("select * from RND_TEST.BOOKS where IS_DELETED = 'Y' and id = " + book.id)
      var result = cs.execute()
      var updateValueQ = updateValue(book)
      csUpdate = conn.prepareStatement(" update BOOKS set " + updateValueQ.substring(0, updateValueQ.length - 1) + " where id = ?")
      csUpdate.setInt(1, book.id)
      var updateRowcount: Int = csUpdate.executeUpdate()
      var response = new DataDto[String]("update boldi");
      resp.getWriter.write(Utils.json.toJson(response))

    }
    catch {
      case ex: Exception => {
        var response = new DataDto[AppErrorDto](new AppErrorDto("Xatolik yuz berdi", "", req.getRequestURL.toString))
        resp.getWriter.write(Utils.json.toJson(response))
        resp.setStatus(500)
      }
    }
    finally {
      if (cs != null) cs.close()
      conn.close()
      conn = null
    }

  }

  def updateValue(book: Book): String = {
    var query: StringBuilder = new StringBuilder(" ")
    if (book.name.nonEmpty) {
      query ++= ("NAME = '" + book.name + "',")
    }
    if (book.authName ne null) {
      query ++= (" AUTHOR_NAME = '" + book.authName + "',")
    }
    if (book.description ne null)
      query ++= (" DESCRIPTION = '" + book.description + "',")
    if (book.pageCount != 0)
      query ++= (" PAGE_COUNT = " + book.pageCount + ",")
    query.toString()
  }


}
