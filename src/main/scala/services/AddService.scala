package services

import connection.DBConnection
import entity.Book
import file.FileUploadService
import responce.{AppErrorDto, DataDto}
import utils.Utils

import java.sql.PreparedStatement
import javax.servlet.ServletContext
import javax.servlet.http.{HttpServletRequest, HttpServletResponse, Part}
import scala.io.Source

class AddService {


  def add(context: ServletContext, req: HttpServletRequest, resp: HttpServletResponse): Unit = {

    var conn = DBConnection.getSingletonConnection
    var cs: PreparedStatement = null

    try {
      if (req.getParts.size() != 2) {
        var response = new DataDto[AppErrorDto](new AppErrorDto("Bad credential", "", req.getRequestURL.toString));
        resp.getWriter.write(Utils.json.toJson(response))
        resp.setStatus(400)
        return null
      }

      val generatedColumns = Array("ID")
      var id: Int = new FileUploadService().upload(context.getAttribute("filesPath").asInstanceOf[String], req.getPart("file"))
      var book = Utils.mapper().readValue(Source.fromInputStream(req.getPart("data").getInputStream)(scala.io.Codec.UTF8).getLines().mkString, classOf[Book])
      cs = conn.prepareStatement("insert into rnd_test.BOOKS(name, author_name, description, page_count, FILE_ID) VALUES (?,?,?,?,?)", generatedColumns)
      cs.setString(1, book.name)
      cs.setString(2, book.authName)
      cs.setString(3, book.description)
      cs.setShort(4, book.pageCount)
      cs.setInt(5, id)
      cs.execute()

      val generatedKeys = cs.getGeneratedKeys
      if (generatedKeys.next) {
        var response = new DataDto[Integer](generatedKeys.getInt(1));
        resp.getWriter.write(Utils.json.toJson(response))
      }
    }

    catch {
      case ex: Exception => {
        var response = new DataDto[AppErrorDto](new AppErrorDto("Xatolik yuz berdi", ex.printStackTrace().toString, req.getRequestURL.toString));
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

  def check(book: Book) = {
    if (book.name.isEmpty) {
      throw new RuntimeException("Not null name")
    }

    if (book.name.isEmpty) {
      throw new RuntimeException("Not null Author name")
    }

  }
}
