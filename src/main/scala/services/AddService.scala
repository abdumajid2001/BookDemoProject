package services

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import connection.DBConnection
import entity.Book
import oracle.jdbc.{OracleCallableStatement, OraclePreparedStatement}
import setting.Setting
import utils.Utils

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

class AddService {
  def add(setting: Setting, req: HttpServletRequest, resp: HttpServletResponse): Unit = {

    if (!req.getHeader("auth_key").equals(setting.authKey)) {
      throw new RuntimeException("Bad request")
    }

    var conn = DBConnection.getPoolConnection
    var cs: OraclePreparedStatement = null
    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)
    var book = mapper.readValue(req.getReader, classOf[Book])
    println(book)



    //    try {
    //      cs = conn.prepareStatement("insert into BOOKS(name, author_name, description, page_count, file_path) VALUES (?,?,?,?,?)")
    //      cs.setString(1,req.)
    //    }

  }


}
