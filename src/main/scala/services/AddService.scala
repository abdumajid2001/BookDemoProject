package services

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import connection.DBConnection
import entity.Book
import oracle.jdbc.{OracleCallableStatement, OraclePreparedStatement}
import setting.Setting
import utils.Utils

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import scala.io.{Codec, Source}

class AddService {
  def add(setting: Setting, req: HttpServletRequest, resp: HttpServletResponse): Unit = {

    if (!req.getHeader("auth_key").equals(setting.authKey)) {
      throw new RuntimeException("Bad request")
    }

    var conn = DBConnection.getPoolConnection
    var cs: OraclePreparedStatement = null
    var body = StringBuilder
   var book = Utils.mapper().readValue(Source.fromInputStream(req.getInputStream)(Codec.UTF8).mkString, classOf[Book])
    println(book)



    //    try {
    //      cs = conn.prepareStatement("insert into BOOKS(name, author_name, description, page_count, file_path) VALUES (?,?,?,?,?)")
    //      cs.setString(1,req.)
    //    }

  }


}
